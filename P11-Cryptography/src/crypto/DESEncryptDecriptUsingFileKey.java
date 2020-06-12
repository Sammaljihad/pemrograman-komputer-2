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
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author Risnandahm
 */
public class DESEncryptDecriptUsingFileKey {
      public static void generateKey(String keyFile){
        FileOutputStream fos;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            Key key = keyGen.generateKey();
            fos = new FileOutputStream(keyFile);
            fos.write(key.getEncoded());
            fos.close();
        } catch (IOException | NoSuchAlgorithmException e) {
        }
    }
    public static byte[] readKeyFile(String keyFile){
        byte[] fileContent = null;
        FileInputStream fis;
        try {
            fis = new FileInputStream(keyFile);
            int len = fis.available();
            fileContent = new byte[len];
            fis.read(fileContent, 0, len);
            fis.close();
        } catch (IOException e) {
        } return fileContent;
    }
    public static byte[] Encript(String data, byte[] keyEncoded){
        byte[] encrypted = null;
        try {
            byte[] input = data.getBytes("UTF8");
            SecretKeySpec key = new SecretKeySpec(keyEncoded, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(input);
        } catch (UnsupportedEncodingException | InvalidKeyException |
                NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | NoSuchPaddingException e) {
        } return encrypted;
    }
    public static byte[] Decrypt(byte[] encrypted, byte[] keyEncoded){
        byte[] decrypted = null;
        try {
            SecretKeySpec key = new SecretKeySpec(keyEncoded, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(encrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                BadPaddingException | IllegalBlockSizeException | 
                NoSuchPaddingException e) {
        } return decrypted;
    }
    public static void main(String[] args){
        String file = System.getProperty("user.dir")+File.separator+"KEY.key";
        generateKey(file);
        String message = "Symetric Cryptography dengan file sebagai key";
        byte[] key = readKeyFile(file);
        byte[] encrypted = Encript(message, key);
        byte[] decrypted = Decrypt(encrypted, key);
        System.out.println("PlainText: "+message);
        System.out.println("Encrypted: "+new String(encrypted));
        System.out.println("Encrypted Hex: "+MyStringUtils.getHexString(encrypted));
        System.out.println("Decrypted: "+new String(decrypted));
    }
}
