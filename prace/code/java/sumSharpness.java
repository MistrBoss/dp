	/**
     * Secteme body na hranach, ktere jsou dostatecne ostre. Za miru ostrosti
     * povazujeme ostrost obrazu, ktera je zavisla na jeho velikosti.
     *
     * @param matrix matice hran
     * @return "mira ostrosti"
     */
    private int sumSharpnessPointOfBorder(double[][] matrix) {
        return sumOverSharpnessPointOfBorder(matrix, new double[matrixXSize][matrixYSize]);
    }