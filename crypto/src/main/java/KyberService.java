import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jcajce.spec.KEMExtractSpec;
import org.bouncycastle.jcajce.spec.KEMGenerateSpec;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec;
import org.bouncycastle.util.Arrays;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class KyberService extends CryptoService {

    private final static String ALGORITHM = "Kyber";
    private final static String PROVIDER = "BCPQC";
    private final static String SYMMETRIC_ALGORITHM = "AES";


    private final static String PUBLIC_FILENAME = KEY_PATH + "kyber_public.key";
    private final static String PRIVATE_FILENAME = KEY_PATH + "kyber_private.key";



    public static void run() throws Exception {
        System.out.println("KyberService started");
        // Add BouncyCastle PQC provider
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }

        // Step 1: Generate Kyber-1024 key pair
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Step 2 & 3: Save keys to files
        saveKeyToFile(publicKey.getEncoded(), PUBLIC_FILENAME);
        saveKeyToFile(privateKey.getEncoded(), PRIVATE_FILENAME);

        // Step 4 & 5: Load keys from files
        PublicKey loadedPublicKey = loadPublicKeyFromFile(PUBLIC_FILENAME);
        PrivateKey loadedPrivateKey = loadPrivateKeyFromFile(PRIVATE_FILENAME);

        // Step 6: Encrypt a sample message
        String sampleMessage = "Hello, this is a test message!11";
        byte[] messageBytes = sampleMessage.getBytes();
        SecretKeyWithEncapsulation senderSecret = generateSharedSecret(loadedPublicKey);
        byte[] cipherText = senderSecret.getEncapsulation(); // Encapsulated secret (ciphertext)
        byte[] sharedSecret = senderSecret.getEncoded(); // Shared secret for encryption

        // Simulate encryption using shared secret (e.g., as a key for AES or XOR for simplicity)
        byte[] encryptedMessage = encryptMessage(messageBytes, sharedSecret);
        String encryptedMessageHex = bytesToHex(encryptedMessage);

        // Step 7: Decrypt the message
        byte[] receiverSecret = extractSharedSecret(loadedPrivateKey, cipherText);
        byte[] decryptedMessage = decryptMessage(encryptedMessage, receiverSecret);
        String decryptedMessageStr = new String(decryptedMessage, CHARSET);

        // Verify results
        System.out.println("Original message: " + sampleMessage);
        System.out.println("Original message length: " + sampleMessage.length());
        System.out.println("Encrypted Message (hex): " + encryptedMessageHex);
        System.out.println("Encrypted Message (hex) length: " + encryptedMessageHex.length());
        System.out.println("Decrypted message: " + decryptedMessageStr);
        System.out.println("Shared secrets match: " + Arrays.areEqual(sharedSecret, receiverSecret));
        System.out.println("KyberService completed");
    }

    // Generate Kyber-1024 key pair
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KyberParameterSpec.kyber1024, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    // Save key to file
    private static void saveKeyToFile(byte[] keyBytes, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(keyBytes);
        }
    }

    // Load public key from file
    private static PublicKey loadPublicKeyFromFile(String fileName) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] keyBytes = readFile(fileName);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return keyFactory.generatePublic(spec);
    }

    // Load private key from file
    private static PrivateKey loadPrivateKeyFromFile(String fileName) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] keyBytes = readFile(fileName);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return keyFactory.generatePrivate(spec);
    }

    // Read file into byte array
    private static byte[] readFile(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        }
    }

    // Generate shared secret and encapsulation (ciphertext) using public key
    private static SecretKeyWithEncapsulation generateSharedSecret(PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, PROVIDER);
        KEMGenerateSpec kemGenerateSpec = new KEMGenerateSpec(publicKey, SYMMETRIC_ALGORITHM);
        keyGenerator.init(kemGenerateSpec);
        return (SecretKeyWithEncapsulation) keyGenerator.generateKey();
    }

    // Extract shared secret using private key and encapsulation (ciphertext)
    private static byte[] extractSharedSecret(PrivateKey privateKey, byte[] encapsulation) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, PROVIDER);
        KEMExtractSpec kemExtractSpec = new KEMExtractSpec(privateKey, encapsulation, SYMMETRIC_ALGORITHM);
        keyGenerator.init(kemExtractSpec);
        SecretKeyWithEncapsulation receiverSecret = (SecretKeyWithEncapsulation) keyGenerator.generateKey();
        return receiverSecret.getEncoded();
    }

    // Encrypt with AES-GCM
    private static byte[] encryptMessage(byte[] message, byte[] sharedSecret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(sharedSecret, SYMMETRIC_ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // 12-byte IV for GCM
        new SecureRandom().nextBytes(iv); // Random IV
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128-bit tag
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        byte[] encrypted = cipher.doFinal(message);
        // Concatenate IV and ciphertext for decryption
        return Arrays.concatenate(iv, encrypted);
    }

    // Decrypt with AES-GCM
    private static byte[] decryptMessage(byte[] encryptedMessage, byte[] sharedSecret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(sharedSecret, SYMMETRIC_ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = Arrays.copyOfRange(encryptedMessage, 0, 12); // Extract IV
        byte[] cipherText = Arrays.copyOfRange(encryptedMessage, 12, encryptedMessage.length);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        return cipher.doFinal(cipherText);
    }

//    // Simple encryption using shared secret (XOR for demo purposes)
//    private static byte[] encryptMessage(byte[] message, byte[] key) {
//        byte[] encrypted = new byte[message.length];
//        for (int i = 0; i < message.length; i++) {
//            encrypted[i] = (byte) (message[i] ^ key[i % key.length]);
//        }
//        return encrypted;
//    }
//
//    // Simple decryption using shared secret (XOR for demo purposes)
//    private static byte[] decryptMessage(byte[] encryptedMessage, byte[] key) {
//        byte[] decrypted = new byte[encryptedMessage.length];
//        for (int i = 0; i < encryptedMessage.length; i++) {
//            decrypted[i] = (byte) (encryptedMessage[i] ^ key[i % key.length]);
//        }
//        return decrypted;
//    }

}
