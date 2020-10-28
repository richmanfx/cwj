package ru.r5am.help;

import oracle.help.Help;
import oracle.help.library.helpset.HelpSet;
import oracle.help.library.helpset.HelpSetParseException;


public class CwjHelp {

    private static Help cwjHelp;

    public CwjHelp() {}

    public static void show() throws HelpSetParseException {

        Help.setHelpEncoding("UTF-8");
        String helpSetFileName = "/help/cwj.hs";
        String firstHelpTopic = "topic1";

        HelpSet helpSet = new HelpSet(CwjHelp.class.getResource(helpSetFileName));
        cwjHelp.addBook(helpSet);
        cwjHelp.showNavigatorWindow();
        cwjHelp.showTopic(helpSet, firstHelpTopic);

    }

}
