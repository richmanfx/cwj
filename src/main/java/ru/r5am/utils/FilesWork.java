package ru.r5am.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FilesWork {

    static final Logger log = LogManager.getLogger();

    public static void readSymbolToCw() {
        System.out.println("Symbol file read");
        log.info("Читаем файл");
    }

}
