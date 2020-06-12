/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 *
 * @author Risnandahm
 */
public class AsymetricRSA {
    public static void generateKey(String privateKeyFile, String publicKeyFile){
        ObjectOutputStream oosPublic, oosPrivate;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            File publicKey = new File(publicKeyFile);
            File privateKey = new File(privateKeyFile);
            publicKey.createNewFile();
            privateKey.createNewFile();
            FileOutputStream fPublic = new FileOutputStream(publicKeyFile);
            oosPublic = new ObjectOutputStream(fPublic);
            oosPublic.writeObject(key.getPublic());
            oosPublic.close();
            FileOutputStream fPrivate = new FileOutputStream(privateKeyFile);
            oosPrivate = new ObjectOutputStream(fPrivate);
            oosPrivate.writeObject(key.getPrivate());
            oosPrivate.close();
        } catch (IOException | NoSuchAlgorithmException e) {
        }
    }
    public static PublicKey getPublicKey(String keyFile){
        PublicKey pbKey = null;
        try {
            FileInputStream fis = new FileInputStream(keyFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            pbKey = (PublicKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        } return pbKey;
    }
    public static PrivateKey getPrivateKey(String keyFile){
        PrivateKey pvKey = null;
        try {
            FileInputStream fis = new FileInputStream(keyFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            pvKey = (PrivateKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        } return pvKey;
    }
    public static byte[] encrypt(String message, PublicKey key){
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | 
                BadPaddingException e) {
        } return cipherText;
    }
    public static String decrypt(byte[] encrypted, PrivateKey key){
        byte[] decrypted = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | 
                BadPaddingException e) {
        } return new String(decrypted);
    }
    public static void main(String[] args){
        String path = System.getProperty("user.dir")+File.separator;
        String privateFile = path+"PRIVATE.cer";
        String publicFile = path+"PUBLIC.cer";
        generateKey(privateFile, publicFile);
        String message = "Asymetric Cryptography dengan algoritma RSA";
        PublicKey pubKey = getPublicKey(publicFile);
        byte[] encrypted = encrypt(message, pubKey);
        PrivateKey privateKey = getPrivateKey(privateFile);
        String plainText = decrypt(encrypted, privateKey);
        System.out.println("Message: "+message);
        System.out.println("Encrypted: "+new String(encrypted));
        System.out.println("Encrypted: "+MyStringUtils.getHexString(encrypted));
        System.out.println("Decrypted: "+plainText);
    }
}
