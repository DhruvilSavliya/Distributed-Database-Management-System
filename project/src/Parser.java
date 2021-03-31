import DDL.DDLQueryExecution;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
    private final Pattern CREATE_TABLE = Pattern.compile("(?i)(CREATE\\sTABLE\\s(\\w+)\\s?\\(((?:\\s?\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\)\\s?;)");
    private final Pattern INSERT = Pattern.compile("(?i)(INSERT\\sINTO\\s+(\\w+)\\s+\\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);)");
    private final Pattern SELECT = Pattern.compile("(?i)(SELECT\\s([\\s\\S]+)\\sFROM\\s(\\w+)+\\s?(WHERE\\s([\\s\\S]+))?;)");
    private final Pattern UPDATE = Pattern.compile("(?i)(UPDATE\\s(\\w+)\\sSET\\s([\\s\\S]+)\\sWHERE\\s([\\s\\S]+);)");
    private final Pattern DELETE = Pattern.compile("(?i)(DELETE\\sFROM\\s+(\\w+)\\s+WHERE\\s+([\\s\\S]+);)");
    private final Pattern PRIMARY_KEY = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sPRIMARY\\sKEY\\s\\(([\\s\\S]+)\\);)");
    private final Pattern FOREIGN_KEY = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sFOREIGN\\sKEY\\s\\(([\\s\\S]+)\\)\\sREFERENCES\\s(\\w+)\\s\\(([\\s\\S]+)\\);)");
    private final Pattern DROP_TABLE = Pattern.compile("(?i)(DROP\\sTABLE\\s(\\w+);)");


    public void parse(String username) throws IOException {
        System.out.println("enter your query here, or type 'exit' to 'quit'.");
        Scanner scanner = new Scanner(System.in);
        String input;
        input = scanner.nextLine();
        LogfileGeneration logfileGeneration = new LogfileGeneration();
        logfileGeneration.generatelog();
        boolean not_exit = !input.equalsIgnoreCase("exit");

        FileWriter eventfile = new FileWriter("EventLogs.txt",true);
        FileWriter generalfile = new FileWriter("GeneralLogs.txt",true);

        while (scanner.hasNext() && not_exit){
            Matcher createTable = CREATE_TABLE.matcher(input);
            Matcher insert = INSERT.matcher(input);
            Matcher select = SELECT.matcher(input);
            Matcher update = UPDATE.matcher(input);
            Matcher delete = DELETE.matcher(input);
            Matcher primaryKey = PRIMARY_KEY.matcher(input);
            Matcher foreignKey = FOREIGN_KEY.matcher(input);
            Matcher dropTable = DROP_TABLE.matcher(input);

            if (createTable.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(createTable, username, eventfile, generalfile);
            } else if (insert.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                DDLQueryExecution ddlQueryExecution = new DDLQueryExecution();
                ddlQueryExecution.insert(insert, username, eventfile, generalfile);
            } else if (select.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(select, username, eventfile, generalfile);
            } else if (update.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(update, username, eventfile, generalfile);
            } else if (delete.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(delete, username, eventfile, generalfile);
            } else if (primaryKey.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(primaryKey, username, eventfile, generalfile);
            } else if (foreignKey.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(foreignKey, username, eventfile, generalfile);
            } else if (dropTable.find()) {
                eventfile.append("[").append(username).append("] ").append("[query type User] ").append(input).append("\n");
                create(dropTable, username, eventfile, generalfile);
            } else if (input.equalsIgnoreCase("dump")){
                eventfile.append("[").append(username).append("] ").append("[Dump File Created]").append(input).append("\n");
                long dumpFileCreationStart = System.currentTimeMillis();
                int dumpCreationFlag = dump.dumpGeneration(username);
                long dumpFileCreationEnd = System.currentTimeMillis();  // Get the end Time
                long totalTime = dumpFileCreationEnd - dumpFileCreationStart;  // Calculate the execution Time
                generalfile.append("[").append(username).append("]").append("[Time Taken]").append(" DUMP FILE CREATED in ").append(String.valueOf(totalTime)).append(" milliseconds\n");
                if (dumpCreationFlag == 0) {
                    eventfile.append("[").append(username).append("] ").append("[Error during dump generation]").append("[Dump file creation failed]").append("\n");
                    System.out.println("Failed to create dump file.");
                } else {
                    eventfile.append("Dump file created by ").append(username).append("\n");
                    System.out.println("Dump file created by " + username);
                }
            } else {
                // Nothing matches with Regex patterns
                eventfile.append("[").append(username).append("] ").append("[Query error]").append(input).append("Invalid SQL syntax").append("\n");
                System.out.println("Invalid syntax!!! Please enter valid SQL Query");
            }
            System.out.println("Enter your SQL query here or type 'exit' to quit.");
        }
        eventfile.close();
        generalfile.close();
    }
}