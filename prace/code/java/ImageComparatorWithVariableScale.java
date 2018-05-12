package cz.sa.dovolena.similarityclient.similarity;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

/**
 * Scale vzorového obrazu na [320×?] nebo [?×240] bez doplnění nulami na
 * [320×240] a scale referenčních obrázků na scalované rozměry vzorového obrázku
 * + doplnění nulama pokud je v jiném rozměru stran.
 *
 * @author Dobroslav Pelc
 */
public class ImageComparatorWithVariableScale extends ImageComparatorWithConstantScale {

    public ImageComparatorWithVariableScale(String originalFilePath) {
        super(originalFilePath);
        //
        matrixXSize = scaledModelImage.getHeight();
        matrixYSize = 2 * scaledModelImage.getWidth();
    }

    @Override
    protected RenderedImage doConstantScale(File modelImageFile) {
        return doVariableScale(modelImageFile);
    }

    private RenderedImage doVariableScale(File modelImageFile) {
        final BufferedImage bufferedImage = readBufferedImage(modelImageFile);
        if (bufferedImage == null) {
            return null;
        }

        if (scaledModelImage == null) {
            return scale(bufferedImage,
                    BASE_SIZE_WIDTH,
                    BASE_SIZE_HEIGHT
            );
        }

        return scale(bufferedImage,
                matrixYSize / 2,
                matrixXSize
        );
    }

    /**
     * Porovná vstupní obrázek se vzorem. Není pravidlem, že výsledek porovnání
     * vzorového obrázku s referenčním je stejný i naopak. Proto je vhodné
     * provádět porovnání každý s každým, kdy záleží na pořadí.
     *
     * @param path absolutní cesta k druhému obrázku
     * @return koeficient podobnosti obrázku vůči vzoru
     */
    @Override
    public double compare(String path) {
        return super.compare(path);
    }

}
