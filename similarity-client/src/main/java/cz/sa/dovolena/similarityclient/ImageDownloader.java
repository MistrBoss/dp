package cz.sa.dovolena.similarityclient;

import com.google.common.base.Preconditions;
import cz.sa.dovolena.server.ws.SFileSource;
import static cz.sa.dovolena.similarityclient.Settings.IMG_DIR;
import cz.sa.dovolena.similarityclient.utils.Checksum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author dobroslav.pelc
 */
public class ImageDownloader {

    public static final String SERVLET;

    private static final String COOKIE;
    private static final String JPG;
    private static final String PNG;

    private static final int CONNECT_TIMEOUT;
    private static final int REQUEST_TIMEOUT;

    static {
        SERVLET = "/cz.sa.dovolena.Application/image";
        COOKIE = "Cookie";
        JPG = "jpg";
        PNG = "png";
        CONNECT_TIMEOUT = 10000;
        REQUEST_TIMEOUT = 30000;
    }

    public static String downloadImage(
            final SFileSource fileSource,
            final String cookieName
    ) {

        final String relativePath = fileSource.getRelativePath();
        final String md5 = fileSource.getHash();

        int loop = 0;

        while (loop < 5) {

            try {
                final File image = download(relativePath, cookieName);
                final String absolutePath = image.getAbsolutePath();
                final String computedMd5 = Checksum.getMD5Checksum(absolutePath);
                if (!md5.equals(computedMd5)) {
                    throw new IllegalStateException("Downloaded image has incorrect md5 (expected " + md5 + ", was " + computedMd5 + ")");
                }

                return absolutePath;

            } catch (final Throwable e) {
                /* Pokud mi stahování na cokoliv spadne, musím obrázek smazat, pokud existuje */
                final String imgFileName = IMG_DIR + File.separator + relativePath;

                final File imgFile = new File(imgFileName);
                if (imgFile.exists()) {
                    imgFile.delete();
                }

            } finally {
                loop++;
            }
        }

        return null;
    }

    private static File download(
            final String relativePath,
            final String cookieName
    ) throws NoSuchAlgorithmException, IOException {

        Preconditions.checkNotNull(relativePath, "Image url cannot be null!");
        Preconditions.checkNotNull(cookieName, "CookieName cannot be null!");

        /* Kontrola adresáře na fotky */
        final File imgDir = new File(IMG_DIR);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        final String url
                = Settings.getConfiguration().getDomainUrl()
                + SERVLET
                + "?name=" + relativePath;

        final URL sourceUrl = new URL(url);
        final URLConnection con = sourceUrl.openConnection();

        con.setConnectTimeout(CONNECT_TIMEOUT);
        con.setReadTimeout(REQUEST_TIMEOUT);

        final HttpURLConnection httpCon = (HttpURLConnection) con;
        httpCon.addRequestProperty(COOKIE, cookieName);

        httpCon.connect();

        if (httpCon.getResponseCode() != 200) {
            throw new FileNotFoundException(
                    "Nelze stahnout soubor z adresy '" + url + "', "
                    + "navratovy kod " + httpCon.getResponseCode()
            );
        }

        final String contentType = httpCon.getContentType();
        final String extension;
        if (contentType.contains("image/jp")) {
            extension = JPG;

        } else if (contentType.contains("image/png")) {
            extension = PNG;

        } else {
            throw new FileNotFoundException(
                    "Nelze stahnout soubor z adresy '"
                    + url + "', chybny mime type '"
                    + contentType + "' "
            );
        }

        final String relativeFileName
                = Checksum.getMD5SumOfString(relativePath)
                + "."
                + extension;

        final String imgFileName = IMG_DIR + File.separator + relativeFileName;
        final File imgFile = new File(imgFileName);
        /*
         * URL obrázků jsou unikátní, názvy obrázků jsou podle MD5 z URL => jestli obrázek je v home dir, vrátím ho a
         * nemusím znova stahovat
         */
        if (imgFile.exists()) {
            return imgFile;
        }

        /*
         * Obrázek nemám, stáhnu ho
         */
        final InputStream is = con.getInputStream();
        final OutputStream os = new FileOutputStream(imgFile);

        try {

            byte[] imgBuf = new byte[4096];
            int nbytes;
            while ((nbytes = is.read(imgBuf)) != -1) {
                os.write(imgBuf, 0, nbytes);
            }

        } finally {
            is.close();
            os.close();
        }

        return imgFile;
    }
}
