import javax.media.jai.JAI;
...
	/* Pozadovana sirka obrazku */
    protected static final int BASE_SIZE_WIDTH = 320;
    /* Pozadovana vyska obrazku */
    protected static final int BASE_SIZE_HEIGHT = 240;
...
	/**
     * Vrati vyrenderovany obrazek v novych rozmerech. Kontroluje vetsi stranu,
     * podle ktere urci pomer. NEDEFORMUJE pomer stran => Vysledny obrazek je
     * jeste potreba doplnit nulama, protoze nemusi odpovidat presne zadanym
     * rozmerum v obouch osasch.scaledIimage@param image originalni obrazek
     *
     * @param scaleToWidth pozadovany sirka, do ktere se ma obrazek scalovat
     * @param scaleToHeight pozadovana vyska, do ktere se ma obrazek scalovat
     *
     * @return obrazek v novych rozmerech
     */
    protected RenderedImage scale(RenderedImage image, int scaleToWidth, int scaleToHeight) {
        RenderedOp rescale = null;
        final int originalWidth = image.getWidth();
        final int originalHeight = image.getHeight();

        float widthRatio = (float) scaleToWidth / (float) originalWidth;
        float heightRatio = (float) scaleToHeight / (float) originalHeight;

        float ratio = Math.min(widthRatio, heightRatio);

        // Scales the original image
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(ratio);
        pb.add(ratio);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());

        // Creates a new, scaled image and uses it on the DisplayJAI component
        rescale = JAI.create("scale", pb);

        return rescale;
    }