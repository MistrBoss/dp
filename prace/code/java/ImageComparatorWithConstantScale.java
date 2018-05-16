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
 *Scalevzorového a refenčních obrázků na [320 × ?] nebo [? × 240] + doplnění
 * vzorového i referenčního nulami na [320 × 240]. Dále provede křížovou
 * korelaci ve vlnovém spektru. <B>Operace je komutativní.</B>
 *
 * @author Dobroslav Pelc, 05.12.2014 9:42
 */
public class ImageComparatorWithConstantScale {

    /* Požadovaná šířka obrázku */
    protected static final int BASE_SIZE_WIDTH = 320;
    /* Požadovaná výška obrázku */
    protected static final int BASE_SIZE_HEIGHT = 240;

    /* Počet barev pro zvolený grafický režim */
    private static final int RGB = 3;

    protected int matrixXSize = BASE_SIZE_HEIGHT;
    protected int matrixYSize = 2 * BASE_SIZE_WIDTH;

    protected DoubleFFT_2D fFTransformer;
    protected double[][] normalizedFourierMatrix;

    protected final RenderedImage scaledModelImage;
    
    private final Logger log = Logger.getLogger(getClass());

    /**
     * @param originalFilePath cesta k vzorovému obrázku
     */
    public ImageComparatorWithConstantScale(String originalFilePath) {
        Preconditions.checkNotNull(originalFilePath, "Cesta ke zdrojovému souboru musí být vyplněná.");
        File modelImageFile = new File(originalFilePath);
        Preconditions.checkArgument(modelImageFile.exists(), "[" + originalFilePath + "] --> vzorový obrázek musí existovat.");
        Preconditions.checkArgument(modelImageFile.canRead(), "[" + originalFilePath + "] --> nad vzorovým obrázkem musí být právo ke čtení.");
        /* Konstantně scalovaný vzorový obrázek */
        scaledModelImage = doConstantScale(modelImageFile);
    }

    public void bindModel() {
        fFTransformer = new DoubleFFT_2D(matrixXSize, matrixYSize / 2);
        double[][] matrixImage = buildNormalizedFTMatrix(scaledModelImage);
        /* Výsledná matice */
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
            log.fatal("Nepodařilo se načíst obrázek '" + modelImageFile.getAbsolutePath() + "'", ex);
        }
        return null;
    }

    protected double[][] buildNormalizedFTMatrix(RenderedImage scaledModelImage) {
        /* Obrázek převedený do matice, px = cross corel value */
        double[][] matrixImage = buildBrightnessMatrix(scaledModelImage);
        /* Obrázek je již převedený furierovou transformací */
        /* Původní pole přebere novou hodnotu */
        fFTransformer.realForwardFull(matrixImage);
        /* Normování Fourierovi transformace obrázku */
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                /* bereme sudé y => reálné číslo */
                final int realPartOfMatrixFieldY = 2 * y;
                final double realPart = matrixImage[x][realPartOfMatrixFieldY];

                /* bereme liché y => imaginární číslo */
                final int imagPartOfMatrixFieldY = 2 * y + 1;
                final double imagPart = matrixImage[x][imagPartOfMatrixFieldY];

                /* absolutní hodnota komplexního čísla */
                double absoluteMatrixFieldValue = Math.sqrt(imagPart * imagPart + realPart * realPart);

                /* 
                 * Pokud výjde nula, tak obecne doslo k tomu, ze pro nejakou
                 * mocninu dvou 2^k je v nejakym radku nebo sloupci kazdej 
                 * 2^k-tý pixel stejnej 
                 */
                if (absoluteMatrixFieldValue != 0) {
                    Preconditions.checkArgument(absoluteMatrixFieldValue != 0, "Nulou nelze dělit.");
                    matrixImage[x][realPartOfMatrixFieldY] /= absoluteMatrixFieldValue;
                    matrixImage[x][imagPartOfMatrixFieldY] /= absoluteMatrixFieldValue;
                }
            }
        }
        return matrixImage;
    }

    /**
     * Vrátí vyrenderovaný obrázek v nových rozměrech. Kontroluje větší stranu,
     * podle které určí poměr. NEDEFORMUJE poměr stran => Výsledný obrázek je
     * ještě potřeba doplnit nulama, protože nemusí odpovídat přesně zadaným
     * rozměrům v obouch osásch.scaledIimage@param image originální obrázek
     *
     * @param scaleToWidth požadovaný šířka, do které se má obrázek scalovat
     * @param scaleToHeight požadovaná výška, do které se má obrázek scalovat
     *
     * @return obrázek v nových rozměrech
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
     * Vytvoří matici, která má pro každý px obrázku vypočítá průměrnou světlost
     * a uloží do výstupního pole. Pokud obrázek není v poměru 4×3, prázdné
     * místa se doplní nulami.
     *
     * @param image vyrenderovaný obrázek
     * @return pole křížové korelace všech px obrázku.
     */
    private double[][] buildBrightnessMatrix(RenderedImage image) {

        Preconditions.checkNotNull(image, "Obrázek, nad kterých chcete vypočítat průměrnou světlost, nesmí být null.");
        final int imageWidth = image.getWidth();
        Preconditions.checkArgument(imageWidth <= matrixYSize / 2, "Obrázek, nad kterých chcete vypočítat průměrnou světlost, musí být scalovaný na 320×24px.");
        final int imageHeight = image.getHeight();
        Preconditions.checkArgument(imageHeight <= matrixXSize, "Obrázek, nad kterých chcete vypočítat průměrnou světlost, musí být scalovaný na 320×24px.");

        final double[][] brightnessMatrix = new double[matrixXSize][matrixYSize];
        final RandomIter iterator = RandomIterFactory.create(image, null);

        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                final double[] pixel = new double[RGB];
                /* Pro vhodnější práci s maticí v FFT jsme otočili význam indexů, z obrázku ale musím načítat pořád stejně */
                iterator.getPixel(y, x, pixel);

                /* Výpočet jasu průměrného jasu pixelu */
                double averageBrightness = (pixel[0] + pixel[1] + pixel[2]) / 3;
                brightnessMatrix[x][y] = averageBrightness;
            }
        }
        return brightnessMatrix;
    }

    /**
     * Porovná vstupní obrázek se vzorem. Je pravidlem, že výsledek porovnání
     * vzorového obrázku s referenčním je stejný i naopak. Proto je vhodné
     * provádět porovnání každý s každým, kdy NEzáleží na pořadí.
     *
     * @param path absolutní cesta k druhému obrázku
     * @return koeficient podobnosti obrázku vůči vzoru
     */
    public double compare(String path) {
        Preconditions.checkNotNull(path, "Cesta ke zdrojovému souboru musí být vyplněná.");
        File reference = new File(path);
        Preconditions.checkArgument(reference.exists(), "Vzorový obrázek musí existovat.");
        Preconditions.checkArgument(reference.canRead(), "Nad vzorovým obrázkem musí být právo ke čtení.");

        try {
            /* Konstantně scalovaný referenční obrázek */
            final RenderedImage comparedImage = doConstantScale(reference);
            /* Výslední matice druhého obrázku */
            double[][] matrixImage = buildNormalizedFTMatrix(comparedImage);

            /* křížová korelace ve vlnovém spektru*/
            double[][] crossCorelMatirx = crossCorrelation(normalizedFourierMatrix, matrixImage);

            /* zpětná (inverzní) FT */
            /* křížová korelace v původním souřadnicovém systému */
            fFTransformer.complexInverse(crossCorelMatirx, true);

            return maxCorrelationCoeficient(crossCorelMatirx);
        } catch (Throwable e) {
            log.fatal("Při výpočtu podobnosti '" + path + "' došlo k chybě.", e);
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
     * Křížová korelace ve vlnovém spektru.
     *
     * @param a normovaná matice FT vzoru
     * @param b normovaná matice FT obrazu
     * @return a .* b' (vynásobím prvek po prvku a a b, přičemž b je komplexně
     * zrdužená)
     */
    protected double[][] crossCorrelation(double[][] a, double[][] b) {
        double[][] result = new double[matrixXSize][matrixYSize];
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                final double realPartA = a[x][2 * y];
                final double realPartB = b[x][2 * y];
                final double imagPartA = a[x][2 * y + 1];
                final double imagPartB = b[x][2 * y + 1];
                /* reálná část výsledné matice */
                result[x][2 * y] = realPartA * realPartB + imagPartA * imagPartB;
                /* imaginární část výsledné matice */
                result[x][2 * y + 1] = realPartA * imagPartB - imagPartA * realPartB;
            }
        }
        return result;
    }
}
