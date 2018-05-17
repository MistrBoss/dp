package cz.sa.dovolena.similarityclient.similarity;

...

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_2D;
import javax.imageio.ImageIO;

...

/**
 * Pracuje s konvolucnim jadrem 3x3 typu horni propust, symetrickym, ne idealnim
 * => propousti vysoke prostorove frekvence (nizke zahazuje)
 *
 * @author Dobroslav Pelc
 */
public class SharpnessCounter {

	...

    /**
     * Urci pomerny koeficient ostrosti
     *
     * @return koeficient ostrosti
     */
    public double countSharpness() {
        log.debug("Sharpess: " + filePath);
        File modelImageFile = new File(filePath);
        Preconditions.checkArgument(modelImageFile.exists(), "[" + filePath + "] --> obrazek musi existovat.");
        Preconditions.checkArgument(modelImageFile.canRead(), "[" + filePath + "] --> nad obrazkem musi byt pravo ke cteni.");
        /* Konstantne scalovany vzorovy obrazek */
        RenderedImage image = readBufferedImage(modelImageFile);
        matrixXSize = image.getHeight();
        matrixYSize = image.getWidth() * 2;
        Preconditions.checkArgument(matrixXSize >= KERNEL_SIZE && matrixYSize / 2 >= KERNEL_SIZE, "[" + filePath + "] --> obrazek musi byt > = nez jadro konvolucni matice tj. " + KERNEL_SIZE + "×" + KERNEL_SIZE + " px");

        fFTransformer = new DoubleFFT_2D(matrixXSize, matrixYSize / 2);
        /* Obrazek preveden do matice, px = cross corel value */
        double[][] fTMatrix = buildBrightnessMatrix(image);
        /* Puvodni pole prebere novou hodnotu z fft transformeru */
        fFTransformer.realForwardFull(fTMatrix);
        /* Konvolucni jadro ve vlnovem spektru pres horni propust */
        double[][] ftHighKernelMatrix = buildFTHighKernelMatrix();
        /* Konvolucni jadro ve vlnovem spektru pres dolni propust */
        double[][] ftLowKernelMatrix = buildFTLowKernelMatrix();
        /* konvoluce ve vlnovem spektru s jadrem horni propust */
        double[][] convolutionHighMatirx = buildConvolutionMatrix(fTMatrix, ftHighKernelMatrix);
        /* 
         * Odfiltruje sum ve vysokych frekvencich:
         * konvoluce ve vlnovem spektru s jadrem horni propust a nasledne i s 
         * jadrem dolni propust
         */
        double[][] convolutionHighLowMatirx = buildConvolutionMatrix(convolutionHighMatirx, ftLowKernelMatrix);
        /* zpetna (inverzni) FT */
        /* konvoluce v puvodnim souradnicovem systemu */
        fFTransformer.complexInverse(convolutionHighMatirx, true);
        fFTransformer.complexInverse(convolutionHighLowMatirx, true);

        sharpness = sumSharpnessPointOfBorder(convolutionHighMatirx);
        overSharpness = sumOverSharpnessPointOfBorder(convolutionHighMatirx, convolutionHighLowMatirx);

        return (double) sharpness / Math.max(OVER_SHARPNESS_ERROR_BOUND, overSharpness - OVER_SHARPNESS_ERROR_BOUND);
    }
	
	...
	
}