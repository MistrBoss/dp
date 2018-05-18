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