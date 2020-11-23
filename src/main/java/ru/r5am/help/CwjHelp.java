package ru.r5am.help;

import oracle.help.Help;
import oracle.help.library.helpset.HelpSet;
import oracle.help.library.helpset.HelpSetParseException;


public class CwjHelp {

    private static Help cwjHelp;

    public CwjHelp() {}

    /**
     * Показать Help
     */
    public static void show() throws HelpSetParseException {

        cwjHelp = new Help();

        Help.setHelpEncoding("UTF-8");
        String helpSetFileName = "/help/cwj.hs";
        String firstHelpTopic = "topic_about";

        HelpSet helpSet = new HelpSet(CwjHelp.class.getResource(helpSetFileName));
        cwjHelp.addBook(helpSet);
        cwjHelp.showNavigatorWindow();
        cwjHelp.showTopic(helpSet, firstHelpTopic);

    }

    /**
     * Уничтожить Help
     */
    public static void destroy() {
        cwjHelp.dispose();
    }

}
