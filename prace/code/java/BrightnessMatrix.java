    /**
     * Vytvori matici, ktera ma pro kazdy px obrazku vypocita prumernou svetlost
     * a ulozi do vystupniho pole. Pokud obrazek neni v pomeru 4×3, prazdne
     * mista se doplni nulami.
     *
     * @param image vyrenderovany obrazek
     * @return pole krizove korelace vsech px obrazku.
     */
    private double[][] buildBrightnessMatrix(RenderedImage image) {

        Preconditions.checkNotNull(image, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, nesmi byt null.");
        final int imageWidth = image.getWidth();
        Preconditions.checkArgument(imageWidth <= matrixYSize / 2, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, musi byt scalovany na 320×24px.");
        final int imageHeight = image.getHeight();
        Preconditions.checkArgument(imageHeight <= matrixXSize, "Obrazek, nad kterych chcete vypocitat prumernou svetlost, musi byt scalovany na 320×24px.");

        final double[][] brightnessMatrix = new double[matrixXSize][matrixYSize];
        final RandomIter iterator = RandomIterFactory.create(image, null);

        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                final double[] pixel = new double[RGB];
                /* Pro vhodnejsi praci s matici v FFT jsme otocili vyznam indexu, z obrazku ale musim nacitat porad stejne */
                iterator.getPixel(y, x, pixel);

                /* Vypocet jasu prumerneho jasu pixelu */
                double averageBrightness = (pixel[0] + pixel[1] + pixel[2]) / 3;
                brightnessMatrix[x][y] = averageBrightness;
            }
        }
        return brightnessMatrix;
    }