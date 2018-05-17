	/* Prah preostrenych hran */
	private static final int BORDER_SHARPNESS_LIMIT = 20;
	
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