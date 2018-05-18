package cz.sa.dovolena.similarityclient.similarity;

import com.google.common.base.Preconditions;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.apache.log4j.Logger;

/**
 *Scalevzoroveho a refencnich obrazku na [320 x ?] nebo [? x 240] + doplneni
 * vzoroveho i referencniho nulami na [320 x 240]. Dale provede krizovou
 * korelaci ve vlnovem spektru. <B>Operace je komutativni.</B>
 *
 * @author Dobroslav Pelc, 05.12.2014 9:42
 */
public class ImageComparatorWithConstantScale {

    /* Pozadovana sirka obrazku */
    protected static final int BASE_SIZE_WIDTH = 320;
    /* Pozadovana vyska obrazku */
    protected static final int BASE_SIZE_HEIGHT = 240;

    /* Pocet barev pro zvoleny graficky rezim */
    private static final int RGB = 3;

    protected int matrixXSize = BASE_SIZE_HEIGHT;
    protected int matrixYSize = 2 * BASE_SIZE_WIDTH;

    protected DoubleFFT_2D fFTransformer;
    protected double[][] normalizedFourierMatrix;

    protected final RenderedImage scaledModelImage;
    
    private final Logger log = Logger.getLogger(getClass());

    /**
     * @param originalFilePath cesta k vzorovemu obrazku
     */
    public ImageComparatorWithConstantScale(String originalFilePath) {
        Preconditions.checkNotNull(originalFilePath, "Cesta ke zdrojovemu souboru musi byt vyplnena.");
        File modelImageFile = new File(originalFilePath);
        Preconditions.checkArgument(modelImageFile.exists(), "[" + originalFilePath + "] --> vzorovy obrazek musi existovat.");
        Preconditions.checkArgument(modelImageFile.canRead(), "[" + originalFilePath + "] --> nad vzorovym obrazkem musi byt pravo ke cteni.");
        /* Konstantne scalovany vzorovy obrazek */
        scaledModelImage = doConstantScale(modelImageFile);
    }

    public void bindModel() {
        fFTransformer = new DoubleFFT_2D(matrixXSize, matrixYSize / 2);
        double[][] matrixImage = buildNormalizedFTMatrix(scaledModelImage);
        /* Vysledna matice */
        normalizedFourierMatrix = matrixImage;
    }

    protected RenderedImage doConstantScale(File modelImageFile) {
        RenderedImage scaledModelImage = scale(
                readBufferedImage(modelImageFile),
                BASE_SIZE_WIDTH,
                BASE_SIZE_HEIGHT
        );

        return scaledModelImage;
    }

    protected BufferedImage readBufferedImage(File modelImageFile) {
        try {
            return ImageIO.read(modelImageFile);
        } catch (IOException ex) {
            log.fatal("Nepodarilo se nacist obrazek '" + modelImageFile.getAbsolutePath() + "'", ex);
        }
        return null;
    }

    protected double[][] buildNormalizedFTMatrix(RenderedImage scaledModelImage) {
        /* Obrazek prevedeny do matice, px = cross corel value */
        double[][] matrixImage = buildBrightnessMatrix(scaledModelImage);
        /* Obrazek je jiz prevedeny furierovou transformaci */
        /* Puvodni pole prebere novou hodnotu */
        fFTransformer.realForwardFull(matrixImage);
        /* Normovani Fourierovi transformace obrazku */
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                /* bereme sude y => realne cislo */
                final int realPartOfMatrixFieldY = 2 * y;
                final double realPart = matrixImage[x][realPartOfMatrixFieldY];

                /* bereme liche y => imaginarni cislo */
                final int imagPartOfMatrixFieldY = 2 * y + 1;
                final double imagPart = matrixImage[x][imagPartOfMatrixFieldY];

                /* absolutni hodnota komplexniho cisla */
                double absoluteMatrixFieldValue = Math.sqrt(imagPart * imagPart + realPart * realPart);

                /* 
                 * Pokud vyjde nula, tak obecne doslo k tomu, ze pro nejakou
                 * mocninu dvou 2^k je v nejakym radku nebo sloupci kazdej 
                 * 2^k-ty pixel stejnej 
                 */
                if (absoluteMatrixFieldValue != 0) {
                    Preconditions.checkArgument(absoluteMatrixFieldValue != 0, "Nulou nelze delit.");
                    matrixImage[x][realPartOfMatrixFieldY] /= absoluteMatrixFieldValue;
                    matrixImage[x][imagPartOfMatrixFieldY] /= absoluteMatrixFieldValue;
                }
            }
        }
        return matrixImage;
    }

    /**
     * Vrati vyrenderovany obrazek v novych rozmerech. Kontroluje vetsi stranu,
     * podle ktere urci pomer. NEDEFORMUJE pomer stran => Vysledny obrazek je
     * jeste potreba doplnit nulama, protoze nemusi odpovidat presne zadanym
     * rozmerum v obouch osasch.scaledIimage@param image originalni obrazek
     *
     * @param scaleToWidth pozadovany sirka, do ktere se ma obrazek scalovat
     * @param scaleToHeight pozadovana vyska, do ktere se ma obrazek scalovat
     *
     * @return obrazek v novych rozmerech
     */
    protected RenderedImage scale(RenderedImage image, int scaleToWidth, int scaleToHeight) {
        RenderedOp rescale = null;
        final int originalWidth = image.getWidth();
        final int originalHeight = image.getHeight();

        float widthRatio = (float) scaleToWidth / (float) originalWidth;
        float heightRatio = (float) scaleToHeight / (float) originalHeight;

        float ratio = Math.min(widthRatio, heightRatio);

        // Scales the original image
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(ratio);
        pb.add(ratio);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());

        // Creates a new, scaled image and uses it on the DisplayJAI component
        rescale = JAI.create("scale", pb);

        return rescale;
    }

    /**
     * Vytvori matici, ktera ma pro kazdy px obrazku vypocita prumernou svetlost
     * a ulozi do vystupniho pole. Pokud obrazek neni v pomeru 4x3, prazdne
     * mista se doplni nulami.
     *
     * @param image vyrenderovany obrazek
     * @return pole krizove korelace vsech px obrazku.
     */
    private double[][] buildBrightnessMatrix(RenderedImage image) {

        Preconditions.checkNotNull(image, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, nesmi byt null.");
        final int imageWidth = image.getWidth();
        Preconditions.checkArgument(imageWidth <= matrixYSize / 2, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, musi byt scalovany na 320x240px.");
        final int imageHeight = image.getHeight();
        Preconditions.checkArgument(imageHeight <= matrixXSize, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, musi byt scalovany na 320x240px.");

        final double[][] brightnessMatrix = new double[matrixXSize][matrixYSize];
        final RandomIter iterator = RandomIterFactory.create(image, null);

        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                final double[] pixel = new double[RGB];
                /* Pro vhodnejsi praci s matici v FFT jsme otocili vyznam indexu, z obrazku ale musim nacitat porad stejne */
                iterator.getPixel(y, x, pixel);

                /* Vypocet jasu prumerneho jasu pixelu */
                double averageBrightness = (pixel[0] + pixel[1] + pixel[2]) / 3;
                brightnessMatrix[x][y] = averageBrightness;
            }
        }
        return brightnessMatrix;
    }

    /**
     * Porovna vstupni obrazek se vzorem. Je pravidlem, ze vysledek porovnani
     * vzoroveho obrazku s referencnim je stejny i naopak. Proto je vhodne
     * provadet porovnani kazdy s kazdym, kdy NEzalezi na poradi.
     *
     * @param path absolutni cesta k druhemu obrazku
     * @return koeficient podobnosti obrazku vuci vzoru
     */
    public double compare(String path) {
        Preconditions.checkNotNull(path, "Cesta ke zdrojovemu souboru musi byt vyplnena.");
        File reference = new File(path);
        Preconditions.checkArgument(reference.exists(), "Vzorovy obrazek musi existovat.");
        Preconditions.checkArgument(reference.canRead(), "Nad vzorovym obrazkem musi byt pravo ke cteni.");

        try {
            /* Konstantne scalovany referencni obrazek */
            final RenderedImage comparedImage = doConstantScale(reference);
            /* Vysledni matice druheho obrazku */
            double[][] matrixImage = buildNormalizedFTMatrix(comparedImage);

            /* krizova korelace ve vlnovem spektru*/
            double[][] crossCorelMatirx = crossCorrelation(normalizedFourierMatrix, matrixImage);

            /* zpetna (inverzni) FT */
            /* krizova korelace v puvodnim souradnicovem systemu */
            fFTransformer.complexInverse(crossCorelMatirx, true);

            return maxCorrelationCoeficient(crossCorelMatirx);
        } catch (Throwable e) {
            log.fatal("Pri vypoctu podobnosti '" + path + "' doslo k chybe.", e);
            return -1D;
        }

    }

    public double maxCorrelationCoeficient(double[][] matrix) {
        double max = 0;
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                max = Math.max(
                        Math.abs(matrix[x][y * 2]),
                        max);
            }
        }

        return max;
    }

    /**
     * Krizova korelace ve vlnovem spektru.
     *
     * @param a normovana matice FT vzoru
     * @param b normovana matice FT obrazu
     * @return a .* b' (vynasobim prvek po prvku a a b, pricemz b je komplexne
     * zrduzena)
     */
    protected double[][] crossCorrelation(double[][] a, double[][] b) {
        double[][] result = new double[matrixXSize][matrixYSize];
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                final double realPartA = a[x][2 * y];
                final double realPartB = b[x][2 * y];
                final double imagPartA = a[x][2 * y + 1];
                final double imagPartB = b[x][2 * y + 1];
                /* realna cast vysledne matice */
                result[x][2 * y] = realPartA * realPartB + imagPartA * imagPartB;
                /* imaginarni cast vysledne matice */
                result[x][2 * y + 1] = realPartA * imagPartB - imagPartA * realPartB;
            }
        }
        return result;
    }
}
