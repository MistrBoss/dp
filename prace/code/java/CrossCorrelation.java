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