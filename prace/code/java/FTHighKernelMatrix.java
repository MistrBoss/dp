   /* POZN: soucet vsech zapornych a kladnych koeficientu musi byt = 0. */
   /* Rozmer konvolucniho jadra - zpravidla ctvercove. */
   public static final int KERNEL_SIZE = 3;
   /* Negativni koeficient konvolucniho jadra pro horni propust */
   private static final double HIGH_KERNEL_COEF_NEGATIV = -0.0625; // -(1/16)
   /* Kladny koeficient konvolucniho jadra pro horni propust */
   private static final double HIGH_KERNEL_COEF_POSITIVE = 0.5;
   
   
   private double[][] buildFTHighKernelMatrix() {
        /* Druha matice z konvolucniho jadra */
        double[][] kernelMatrix = new double[matrixXSize][matrixYSize];
        /* 
		 * do prvnich 3×3 poli dame konvolucni jadro:
		 *
		 * -1/16	-1/16	-1/16
		 * -1/16	 1/2 	-1/16
		 * -1/16	-1/16	-1/16
		 */
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