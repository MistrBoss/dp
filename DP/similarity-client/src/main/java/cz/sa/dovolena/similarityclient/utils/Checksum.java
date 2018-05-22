package cz.sa.dovolena.similarityclient.utils;

import com.google.common.base.Preconditions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
 *
 * @author Dobroslav Pelc, 18.9.2014 9:50
 */
public class Checksum {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * Digest a file.
     *
     * @param filename to digest
     * @return file MD5 in byte array form
     *
     * @throws FileNotFoundException if file is not present
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws IOException on error while reading
     */
    public static byte[] createFileChecksum(String filename) throws FileNotFoundException, NoSuchAlgorithmException, IOException {

        InputStream fis = new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance(ChecksumAlghorithm.MD5.name());
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    /**
     * Digest a file and return a String MD5.
     *
     *
     * @param filename to digest
     * @return stringified hex coded MD5
     *
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws IOException on error while reading
     */
    public static String getMD5Checksum(String filename) throws NoSuchAlgorithmException, IOException {

        byte[] b = createFileChecksum(filename);
        return bytesToHexString(b);
    }

    /**
     * Digest a String (use default charser utf-8).
     *
     * @param data String to digest
     * @return MD5 of the String
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws java.lang.NullPointerException if the String is null
     */
    public static String getMD5SumOfString(String data) throws NoSuchAlgorithmException {

        Preconditions.checkNotNull(data, "Data for MD5 cannot be null!");
        return getMD5SumOfString(data, DEFAULT_CHARSET);
    }

    /**
     * Digest a String.
     *
     * @param data String to digest
     * @param charset Charser of input data
     * @return MD5 of the String
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws java.lang.NullPointerException if the String is null
     */
    public static String getMD5SumOfString(String data, Charset charset) throws NoSuchAlgorithmException {

        Preconditions.checkNotNull(data, "Data for MD5 cannot be null!");
        Preconditions.checkNotNull(charset, "Charser of input data cannot be null!");

        return bytesToHexString(createStringChecksum(
                data.getBytes(charset),
                ChecksumAlghorithm.MD5
        ));
    }

    /**
     * Digest a Byte Array.
     *
     * @param data Byte Array to digest
     * @return MD5 of the Byte Array
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws java.lang.NullPointerException if the Byte Array is null
     */
    public static String getMD5SumOfByteArray(byte[] data) throws NoSuchAlgorithmException {

        Preconditions.checkNotNull(data, "Data for MD5 cannot be null!");
        return bytesToHexString(createStringChecksum(data, ChecksumAlghorithm.MD5));
    }

    /**
     * Digest a String.
     *
     * @param data String to digest
     * @return SHA1 of the String
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     * @throws java.lang.NullPointerException if the String is null
     */
    public static String getSHA1SumOfString(String data) throws NoSuchAlgorithmException {

        Preconditions.checkNotNull(data, "Data for SHA1 cannot be null!");
        return bytesToHexString(createStringChecksum(data.getBytes(), ChecksumAlghorithm.SHA1));
    }

    /**
     * Convert a byte array into String.
     *
     * see this How-to for a faster way to convert a byte array to a HEX string
     *
     * @param bytes to convert
     * @return converted hex-coded String
     */
    public static String bytesToHexString(byte[] bytes) {

        if (bytes == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Digest a String and return a String MD5.
     *
     * @param data
     * @return stringified hex coded MD5
     *
     * @throws NoSuchAlgorithmException if selected algorithm is not suppported
     */
    private static byte[] createStringChecksum(byte[] data, ChecksumAlghorithm algorithm) throws NoSuchAlgorithmException {

        MessageDigest stringDigest = MessageDigest.getInstance(algorithm.name());
        return stringDigest.digest(data);
    }
}
