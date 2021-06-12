package ru.r5am.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Файл CW слов
 */
@ToString
public class CwWordFile {

    @Getter @Setter public String Name;     // Имя файла

    @Getter @Setter public List<String> cwWords;       // CW слова в файле

}
