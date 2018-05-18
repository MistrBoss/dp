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