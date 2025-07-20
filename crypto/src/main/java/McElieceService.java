import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.legacy.crypto.mceliece.*;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.legacy.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.legacy.math.linearalgebra.PolynomialGF2mSmallM;


import java.io.*;
import java.math.BigInteger;
import java.security.*;

public class McElieceService extends CryptoService {

    private final static String PUBLIC_FILENAME = KEY_PATH + "mceliece_public.key";
    private final static String PRIVATE_FILENAME = KEY_PATH + "mceliece_private.key";

    public static void run() throws Exception {
        System.out.println("McElieceService started");
        long startTime = System.currentTimeMillis();
        Security.addProvider(new BouncyCastleProvider());

        final int m = 12;
        final int t = 128;

        final String publicFileName = PUBLIC_FILENAME + m;
        final String privateFileName = PRIVATE_FILENAME + m;

        System.out.println("Starting generating key for m=" + m + " t=" + t);

        // Step 1: Generate McEliece key pair using legacy API
        McElieceKeyPairGenerator keyGen = new McElieceKeyPairGenerator();
        McElieceParameters params = new McElieceParameters(m, t);
        McElieceKeyGenerationParameters genParams = new McElieceKeyGenerationParameters(new SecureRandom(),params);
        keyGen.init(genParams);
        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();

        McEliecePublicKeyParameters publicKeyParams = (McEliecePublicKeyParameters) keyPair.getPublic();
        McEliecePrivateKeyParameters privateKeyParams = (McEliecePrivateKeyParameters) keyPair.getPrivate();

        long keyGeneratedTime = System.currentTimeMillis();
        long generationTook = keyGeneratedTime - startTime;
        System.out.println("Key generation took: " + generationTook + " millis");

        savePublicKeyToFile(publicKeyParams, publicFileName);
        savePrivateKeyToFile(privateKeyParams, privateFileName);

        McEliecePublicKeyParameters loadedPublicKey = readPublicKeyFromFile(publicFileName);
        McEliecePrivateKeyParameters loadedPrivateKey = readPrivateKeyFromFile(privateFileName);


        // Step 2: Test encryption/decryption
        String originalMessage = "Hello, McEliece!11";

        encryptAndDecrypt(originalMessage, loadedPublicKey, loadedPrivateKey);
        System.out.println("Key generation took: " + generationTook + " millis");

        System.out.println("McElieceService completed");
    }

    private static boolean encryptAndDecrypt(String originalMessage, McEliecePublicKeyParameters publicKeyParams, McEliecePrivateKeyParameters privateKeyParams) {
        try {
            long encDecTime = System.currentTimeMillis();
            byte[] messageBytes = originalMessage.getBytes(CHARSET);

            McElieceCipher cipher = new McElieceCipher();
            cipher.init(true, publicKeyParams); // Use raw key parameter
            byte[] encryptedMessage = cipher.messageEncrypt(messageBytes);
            String encryptedMessageHex = bytesToHex(encryptedMessage);

            cipher.init(false, privateKeyParams); // Use raw key parameter
            byte[] decryptedMessage = cipher.messageDecrypt(encryptedMessage);
            String decryptedMessageStr = new String(decryptedMessage, CHARSET);

            long encDecTook = System.currentTimeMillis() - encDecTime;
            System.out.println("Encryption and decryption took: " + encDecTook + " millis");

            // Step 3: Output results
            System.out.println("Original Message: " + originalMessage);
            System.out.println("Original Message Length: " + originalMessage.length());
            System.out.println("Encrypted Message (hex): " + encryptedMessageHex);
            System.out.println("Encrypted Message (hex) length: " + encryptedMessageHex.length());
            System.out.println("Decrypted Message: " + decryptedMessageStr);
        } catch (Exception e) {
            System.out.println("Failed with Original Message Length: " + originalMessage.length());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void savePublicKeyToFile(McEliecePublicKeyParameters keyParam, String filename) throws IOException {
        // Extract parameters
        int n = keyParam.getN();
        int k = keyParam.getK();
        int t = keyParam.getT();
        GF2Matrix g = keyParam.getG();

        // Encode the components
        byte[] nBytes = BigInteger.valueOf(n).toByteArray();
        byte[] kBytes = BigInteger.valueOf(k).toByteArray();
        byte[] tBytes = BigInteger.valueOf(t).toByteArray();
        byte[] gBytes = g.getEncoded(); // GF2Matrix encoding

        // Create ASN.1 structure for the public key
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new DEROctetString(nBytes));
        v.add(new DEROctetString(kBytes));
        v.add(new DEROctetString(tBytes));
        v.add(new DEROctetString(gBytes));

        DERSequence keySequence = new DERSequence(v);

        // Create SubjectPublicKeyInfo with McEliece OID
        AlgorithmIdentifier algId = new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.1"));
        SubjectPublicKeyInfo keyInfo = new SubjectPublicKeyInfo(algId, keySequence);

        // Write the encoded key to a file
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(keyInfo.getEncoded("DER"));
        }

        System.out.println("McEliece public key saved to: " + filename);
    }

    private static McEliecePublicKeyParameters readPublicKeyFromFile(String filename) throws IOException {
        try {
            // Read the DER-encoded key from the file
            byte[] encodedKey;
            try (FileInputStream fis = new FileInputStream(filename);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                encodedKey = baos.toByteArray();
            }

            // Parse the SubjectPublicKeyInfo
            ASN1InputStream asn1Input = new ASN1InputStream(encodedKey);
            SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(asn1Input.readObject());
            asn1Input.close();

            // Extract the key components from the sequence
            ASN1Sequence keySequence = ASN1Sequence.getInstance(keyInfo.getPublicKeyData().getBytes());
            DEROctetString nOctet = (DEROctetString) keySequence.getObjectAt(0);
            DEROctetString kOctet = (DEROctetString) keySequence.getObjectAt(1);
            DEROctetString tOctet = (DEROctetString) keySequence.getObjectAt(2);
            DEROctetString gOctet = (DEROctetString) keySequence.getObjectAt(3);

            // Decode the components
            int n = new BigInteger(nOctet.getOctets()).intValue();
            int k = new BigInteger(kOctet.getOctets()).intValue();
            int t = new BigInteger(tOctet.getOctets()).intValue();

            // Calculate expected dimensions
            int expectedRows = k; // k rows
            int expectedCols = n; // n columns

            // Reconstruct GF2Matrix
            byte[] gBytes = gOctet.getOctets();
            GF2Matrix g;
            try {
                g = new GF2Matrix(gBytes);
            } catch (ArithmeticException e) {
                throw new IOException("Failed to reconstruct GF2Matrix: " + e.getMessage(), e);
            }

            // Validate matrix dimensions
            if (g.getNumRows() != expectedRows || g.getNumColumns() != expectedCols) {
                // Check if the matrix is transposed
                if (g.getNumRows() == expectedCols && g.getNumColumns() == expectedRows) {
                    // Transpose the matrix to correct the orientation
                    g = (GF2Matrix) g.computeTranspose();
                    System.out.println("Transposed GF2Matrix to correct dimensions: " + g.getNumRows() + "x" + g.getNumColumns());
                } else {
                    throw new IOException("Invalid GF2Matrix dimensions: expected " + expectedRows + "x" + expectedCols +
                            ", got " + g.getNumRows() + "x" + g.getNumColumns());
                }
            }

            // Reconstruct and return McEliecePublicKeyParameters
            return new McEliecePublicKeyParameters(n, t, g);
        } catch (IOException | ClassCastException e) {
            throw new IOException("Error reading McEliece public key from file: " + e.getMessage(), e);
        }
    }

    public static void savePrivateKeyToFile(McEliecePrivateKeyParameters privateKey, String filePath) throws IOException {

        try {
            // Encode the McEliece private key components
            byte[] nBytes = BigInteger.valueOf(privateKey.getN()).toByteArray();
            byte[] kBytes = BigInteger.valueOf(privateKey.getK()).toByteArray();
            byte[] fieldBytes = privateKey.getField().getEncoded();
            byte[] goppaPolyBytes = privateKey.getGoppaPoly().getEncoded();
            byte[] sInvBytes = privateKey.getSInv().getEncoded();
            byte[] p1Bytes = privateKey.getP1().getEncoded();
            byte[] p2Bytes = privateKey.getP2().getEncoded();
            byte[] hBytes = privateKey.getH().getEncoded();

            // Create ASN.1 structure for the private key
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DEROctetString(nBytes));
            v.add(new DEROctetString(kBytes));
            v.add(new DEROctetString(fieldBytes));
            v.add(new DEROctetString(goppaPolyBytes));
            v.add(new DEROctetString(sInvBytes));
            v.add(new DEROctetString(p1Bytes));
            v.add(new DEROctetString(p2Bytes));
            v.add(new DEROctetString(hBytes));

            DERSequence keySequence = new DERSequence(v);

            // Create AlgorithmIdentifier with McEliece OID
            AlgorithmIdentifier algId = new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.1"));

            // Create PrivateKeyInfo
            PrivateKeyInfo keyInfo = new PrivateKeyInfo(algId, keySequence);

            // Get the DER-encoded byte array
            byte[] encodedKey = keyInfo.getEncoded();

            // Write the encoded key to a file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(encodedKey);
            }

            System.out.println("McEliece private key saved to: " + filePath);
        } catch (IOException e) {
            throw new IOException("Error saving McEliece private key to file: " + e.getMessage(), e);
        }
    }

    public static McEliecePrivateKeyParameters readPrivateKeyFromFile(String filePath) throws IOException {
        try {
            // Read the DER-encoded key from the file
            byte[] encodedKey;
            try (FileInputStream fis = new FileInputStream(filePath);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                encodedKey = baos.toByteArray();
            }

            // Parse the PrivateKeyInfo
            ASN1InputStream asn1Input = new ASN1InputStream(encodedKey);
            PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(asn1Input.readObject());
            asn1Input.close();

            // Extract the private key OCTET STRING and parse it as an ASN1Sequence
            ASN1OctetString privateKeyOctet = (ASN1OctetString) keyInfo.getPrivateKey();
            ASN1InputStream octetInput = new ASN1InputStream(privateKeyOctet.getOctets());
            ASN1Sequence keySequence = ASN1Sequence.getInstance(octetInput.readObject());
            octetInput.close();

            // Extract the key components from the sequence
            DEROctetString nOctet = (DEROctetString) keySequence.getObjectAt(0);
            DEROctetString kOctet = (DEROctetString) keySequence.getObjectAt(1);
            DEROctetString fieldOctet = (DEROctetString) keySequence.getObjectAt(2);
            DEROctetString goppaPolyOctet = (DEROctetString) keySequence.getObjectAt(3);
            DEROctetString sInvOctet = (DEROctetString) keySequence.getObjectAt(4);
            DEROctetString p1Octet = (DEROctetString) keySequence.getObjectAt(5);
            DEROctetString p2Octet = (DEROctetString) keySequence.getObjectAt(6);
            DEROctetString hOctet = (DEROctetString) keySequence.getObjectAt(7);

            // Decode the components
            int n = new BigInteger(nOctet.getOctets()).intValue();
            int k = new BigInteger(kOctet.getOctets()).intValue();
            GF2mField field = new GF2mField(fieldOctet.getOctets());
            PolynomialGF2mSmallM goppaPoly = new PolynomialGF2mSmallM(field, goppaPolyOctet.getOctets());
            GF2Matrix sInv = new GF2Matrix(sInvOctet.getOctets());
            Permutation p1 = new Permutation(p1Octet.getOctets());
            Permutation p2 = new Permutation(p2Octet.getOctets());
            GF2Matrix h = new GF2Matrix(hOctet.getOctets()); // Note: h is deserialized but not used in the constructor

            // Reconstruct the McEliecePrivateKeyParameters
            return new McEliecePrivateKeyParameters(n, k, field, goppaPoly, p1, p2, sInv);
        } catch (IOException | ClassCastException e) {
            throw new IOException("Error reading McEliece private key from file: " + e.getMessage(), e);
        }
    }
}
