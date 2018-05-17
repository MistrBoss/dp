	/* Koeficient okolnich bodu konvolucniho jadra pro dolni propust */
    private static final double LOW_KERNEL_NEIGHBOUR_COEF = 0.125; // 1/8
    /* Stredovy koeficient konvolucniho jadra pro dolni propust */
    private static final double LOW_KERNEL_MIDDLE_COEF = 0.5;
	
	private double[][] buildFTLowKernelMatrix() {
        /* 
		 * do prvnich 3x3 poli dame konvolucni jadro
		 *
		 *  1/8		 1/8	 1/8
		 *  1/8		 1/2 	 1/8
		 *  1/8		 1/8	 1/8
		 */
        double[][] kernelMatrix = new double[matrixXSize][matrixYSize];

        kernelMatrix[0][0] = LOW_KERNEL_MIDDLE_COEF;
        kernelMatrix[0][1] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[1][0] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[matrixXSize - 1][0] = LOW_KERNEL_NEIGHBOUR_COEF;
        kernelMatrix[0][(matrixYSize / 2) - 1] = LOW_KERNEL_NEIGHBOUR_COEF;

        fFTransformer.realForwardFull(kernelMatrix);
        return kernelMatrix;
    }