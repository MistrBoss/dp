package cz.sa.dovolena.similarityclient.similarity;

import com.google.common.base.Preconditions;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.apache.log4j.Logger;

/**
 * Pracuje s konvolučním jádrem 3×3 typu horní propusť, symetrickým, ne ideálním
 * => propouští vysoké prostorové frekvence (nízké zahazuje)
 *
 * @author Dobroslav Pelc, 16.12.2014
 */
public class SharpnessCounter {

    /* POZN: součet všech záporných a kladných koeficientů musí být = 0. */
    /* Rozměr konvolučního jádra - zpravidla čtvercové. */
    public static final int KERNEL_SIZE = 3;
    /* Negativní koeficient konvolučního jádra pro horní propusť */
    private static final double HIGH_KERNEL_COEF_NEGATIV = -0.0625; // -(1/16)
    /* Kladný koeficient konvolučního jádra pro horní propusť */
    private static final double HIGH_KERNEL_COEF_POSITIVE = 0.5;
    /* Koeficient okolních bodů konvolučního jádra pro dolní propusť*/
    private static final double LOW_KERNEL_NEIGHBOUR_COEF = 0.125; // 1/8
    /* Středový koeficient konvolučního jádra pro dolní propusť */
    private static final double LOW_KERNEL_MIDDLE_COEF = 0.5;
    /* Práh dostatečné ostrosti hran */
    private static final int BORDER_SHARPNESS_LIMIT = 20;
    private static final int OVER_SHARPNESS_ERROR_BOUND = 250;

    private DoubleFFT_2D fFTransformer;
    private int matrixXSize;
    private int matrixYSize;

    /* Počet barev pro zvolený grafický režim */
    private static final int RGB = 3;

    /* Absolutní cesta k obrázku */
    private final String filePath;

    private final Logger log = Logger.getLogger(SharpnessCounter.class);

    int sharpness;
    int overSharpness;

    /**
     * @param filePath absolutní cesta k vzorovému obrázku
     */
    public SharpnessCounter(String filePath) {
        Preconditions.checkNotNull(filePath, "Cesta ke zdrojovému souboru musí být vyplněná.");
        this.filePath = filePath;
    }

    /**
     * Určí poměrný koeficient ostrosti
     *
     * @return koeficient ostrosti
     */
    public double countSharpness() {
        log.debug("Sharpess: " + filePath);
        File modelImageFile = new File(filePath);
        Preconditions.checkArgument(modelImageFile.exists(), "[" + filePath + "] --> obrázek musí existovat.");
        Preconditions.checkArgument(modelImageFile.canRead(), "[" + filePath + "] --> nad obrázkem musí být právo ke čtení.");
        /* Konstantně scalovaný vzorový obrázek */
        RenderedImage image = readBufferedImage(modelImageFile);
        matrixXSize = image.getHeight();
        matrixYSize = image.getWidth() * 2;
        Preconditions.checkArgument(matrixXSize >= KERNEL_SIZE && matrixYSize / 2 >= KERNEL_SIZE, "[" + filePath + "] --> obrázek musí být > = než jádro konvoluční matice tj. " + KERNEL_SIZE + "×" + KERNEL_SIZE + " px");

        fFTransformer = new DoubleFFT_2D(matrixXSize, matrixYSize / 2);
        /* Obrázek převedený do matice, px = cross corel value */
        double[][] fTMatrix = buildBrightnessMatrix(image);
        /* Původní pole přebere novou hodnotu z fft transformeru */
        fFTransformer.realForwardFull(fTMatrix);
        /* Konvoluční jádro ve vlnovém spektru přes horní propusť */
        double[][] ftHighKernelMatrix = buildFTHighKernelMatrix();
        /* Konvoluční jádro ve vlnovém spektru přes dolní propusť */
        double[][] ftLowKernelMatrix = buildFTLowKernelMatrix();
        /* konvoluce ve vlnovém spektru s jádrem horní propusť */
        double[][] convolutionHighMatirx = buildConvolutionMatrix(fTMatrix, ftHighKernelMatrix);
        /* 
         * Odfiltruje šum ve vysokých frekvencích:
         * konvoluce ve vlnovém spektru s jádrem horní propusť a následně i s 
         * jádrem dolní propusť
         */
        double[][] convolutionHighLowMatirx = buildConvolutionMatrix(convolutionHighMatirx, ftLowKernelMatrix);
        /* zpětná (inverzní) FT */
        /* konvoluce v původním souřadnicovém systému */
        fFTransformer.complexInverse(convolutionHighMatirx, true);
        fFTransformer.complexInverse(convolutionHighLowMatirx, true);

        sharpness = sumSharpnessPointOfBorder(convolutionHighMatirx);
        overSharpness = sumOverSharpnessPointOfBorder(convolutionHighMatirx, convolutionHighLowMatirx);

        return (double) sharpness / Math.max(OVER_SHARPNESS_ERROR_BOUND, overSharpness - OVER_SHARPNESS_ERROR_BOUND);
    }

    private double[][] buildFTHighKernelMatrix() {
        /* Druhá matice z konvolučního jádra */
        double[][] kernelMatrix = new double[matrixXSize][matrixYSize];
        /* do prvních 3×3 polí dáme konvoluční jádro */
        kernelMatrix[0][0] = HIGH_KERNEL_COEF_POSITIVE;
        kernelMatrix[0][1] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[1][0] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[1][1] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[matrixXSize - 1][0] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[matrixXSize - 1][1] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[0][(matrixYSize / 2) - 1] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[1][(matrixYSize / 2) - 1] = HIGH_KERNEL_COEF_NEGATIV;
        kernelMatrix[matrixXSize - 1][(matrixYSize / 2) - 1] = HIGH_KERNEL_COEF_NEGATIV;

        fFTransformer.realForwardFull(kernelMatrix);
        return kernelMatrix;
    }

    private double[][] buildFTLowKernelMatrix() {
        /* do prvních 3×3 polí dáme konvoluční jádro */
        double[][] kernelMatrix = new double[matrixXSize][matrixYSize];

        kernelMatrix[0][0] = LOW_KERNEL_MIDDLE_COEF;
        kernelMatrix[0][1] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[1][0] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[matrixXSize - 1][0] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[0][(matrixYSize / 2) - 1] = LOW_KERNEL_NEIGHBOUR_COEF;

        fFTransformer.realForwardFull(kernelMatrix);
        return kernelMatrix;
    }

    protected BufferedImage readBufferedImage(File modelImageFile) {
        try {
            return ImageIO.read(modelImageFile);
        } catch (IOException ex) {
            log.fatal("Při výpočtu míry ostrosti došlo k problému při čtení z obrázku " + modelImageFile.getAbsolutePath(), ex);
        }
        return null;
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
     * Sečteme body na hranách, které jsou dostatečně ostré. Za míru ostrosti
     * považujeme ostrost obrazu, která je závislá na jeho velikosti.
     *
     * @param matrix matice hran
     * @return "míra ostrosti"
     */
    private int sumSharpnessPointOfBorder(double[][] matrix) {
        return sumOverSharpnessPointOfBorder(matrix, new double[matrixXSize][matrixYSize]);
    }

    /**
     * Sečteme body na hranách, které jsou dostatečně přeostřené.
     *
     * @param matrixA matice hran
     * @param matrixB matice hran bez šumu (projetá dolní propustí)
     * @return míra přeostřenosti
     */
    private int sumOverSharpnessPointOfBorder(double[][] matrixA, double[][] matrixB) {
        int sum = 0;
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                if (matrixA[x][y * 2] - matrixB[x][y * 2] > BORDER_SHARPNESS_LIMIT) {
                    sum++;
                }
            }
        }
        /* Míra přeostřenosti */
        return sum;
    }

    /**
     * Konvoluce ve vlnovém spektru.
     *
     * @param a matice FT z obrázku
     * @param b matice FT konvolučního jádra
     * @return a .* b' (vynásobím prvek po prvku a a b)
     */
    protected double[][] buildConvolutionMatrix(double[][] a, double[][] b) {
        double[][] result = new double[matrixXSize][matrixYSize];
        for (int x = 0; x < matrixXSize; x++) {
            for (int y = 0; y < matrixYSize / 2; y++) {
                final double realPartA = a[x][2 * y];
                final double realPartB = b[x][2 * y];
                final double imagPartA = a[x][2 * y + 1];
                final double imagPartB = b[x][2 * y + 1];
                /* reálná část výsledné matice */
                result[x][2 * y] = realPartA * realPartB - imagPartA * imagPartB;
                /* imaginární část výsledné matice */
                result[x][2 * y + 1] = realPartA * imagPartB + imagPartA * realPartB;
            }
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="GET / SET">
    public int getSharpness() {
        return sharpness;
    }

    public int getOverSharpness() {
        return overSharpness;
    }
    //</editor-fold>
}
