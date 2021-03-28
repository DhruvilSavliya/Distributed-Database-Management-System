import java.io.*;
import java.util.*;
import java.util.regex.*;

public class parser {
    private static final Pattern CREATE_TABLE = Pattern.compile("(?i)(CREATE\\sTABLE\\s(\\w+)\\s?\\(((?:\\s?\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\)\\s?;)");
    private static final Pattern DROP_TABLE = Pattern.compile("(?i)(DROP\\sTABLE\\s(\\w+);)");
    private static final Pattern SELECT = Pattern.compile("(?i)(SELECT\\s([\\s\\S]+)\\sFROM\\s(\\w+)+\\s?(WHERE\\s([\\s\\S]+))?;)");
    private static final Pattern INSERT = Pattern.compile("(?i)(INSERT\\sINTO\\s+(\\w+)\\s+\\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);)");
    private static final Pattern DELETE = Pattern.compile("(?i)(DELETE\\sFROM\\s+(\\w+)\\s+WHERE\\s+([\\s\\S]+);)");
    private static final Pattern UPDATE = Pattern.compile("(?i)(UPDATE\\s(\\w+)\\sSET\\s([\\s\\S]+)\\sWHERE\\s([\\s\\S]+);)");
    private static final Pattern PRIMARY_KEY = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sPRIMARY\\sKEY\\s\\(([\\s\\S]+)\\);)");
    private static final Pattern FOREIGN_KEY = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sFOREIGN\\sKEY\\s\\(([\\s\\S]+)\\)\\sREFERENCES\\s(\\w+)\\s\\(([\\s\\S]+)\\);)");

    public void generatelog() throws IOException {
        File eventlogfile = new File("EventLogs.txt");
        File generallogfile = new File("GeneralLogs.txt");
        if (eventlogfile.createNewFile())
        {
            System.out.println("Event Logs generated.");
        }
        if (generallogfile.createNewFile())
        {
            System.out.println("General Logs generated.");
        }
        FileWriter fwevent = new FileWriter(eventlogfile, true);
        FileWriter fwgeneral = new FileWriter(generallogfile, true);
    }
    public static void parse(String username) throws IOException {
        System.out.println("enter your query here, or type 'exit' to 'quit'.");
        Scanner scanner = new Scanner(System.in);
        String input;
        parser p = new parser();
        p.generatelog();
    }
}