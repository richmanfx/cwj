package ru.r5am.utils;

import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesWork {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);
    static String resourcePath = "src/main/resources/";

    /**
     * Прочитать файл соответствия символов посылкам
     * @return Словарь "Символ": "посылки"
     */
    public static Map<String, String> readSymbolToCw() throws IOException {

        Map<String, String> symbolToCw = new HashMap<>();
        String symbolCwFileName = config.symbolCwFileName();
        Path fullPath = Paths.get(resourcePath + symbolCwFileName);
        List<String> symbolCwLines = Files.readAllLines(fullPath);

        for (String oneLine: symbolCwLines) {
            if (oneLine.contains(" ") && oneLine.length() > 2) {    // Не обрабатывать пустые строки
                symbolToCw.put(oneLine.split(" ")[0], oneLine.split(" ")[1]);
            }
        }

        return symbolToCw;
    }

}
