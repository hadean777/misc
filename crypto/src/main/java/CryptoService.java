abstract public class CryptoService {

    protected final static String KEY_PATH = "/home/crypto/data/";

    protected final static String CHARSET = "UTF-8";

    protected static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
