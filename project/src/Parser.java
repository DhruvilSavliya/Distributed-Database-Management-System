import DDL.DDLQueryExecution;
import DML.DMLQueryExecution;
import DQL.DQLQueryExecution;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {



    final String selectRegex = "(SELECT)\\s+(\\*|[\\w{1,}]+)\\s+FROM\\s+([\\w]+)\\s*( WHERE\\s+([\\S]+))?;";
    final String insertRegex = "(?i)(INSERT\\sINTO\\s+(\\w+)\\s+\\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);)";
    final String deleteRegex = "^DELETE\\sFROM\\s\\w{1,}\\sWHERE\\s.*";
    final String createTableRegex = "(?i)(CREATE)\\sTABLE\\s\\w{1,}\\s([\\s\\S]+)+;";
    final String dropTableRegex = "^(DROP)\\sTABLE\\s(\\w{1,}+);";
    final String updateTableRegex ="(UPDATE)\\s+([\\w]+)\\s+SET\\s+([\\S]+)\\s*( WHERE\\s+([\\S]+))?;";

    public void userInput(String username) throws Exception {
        final Pattern selectPattern = Pattern.compile(selectRegex);
        final Pattern insertPattern = Pattern.compile(insertRegex);
        final Pattern deletePattern = Pattern.compile(deleteRegex);
        final Pattern createTablePattern = Pattern.compile(createTableRegex);
        final Pattern dropTablePattern = Pattern.compile(dropTableRegex);
        final Pattern updateTablePattern = Pattern.compile(updateTableRegex);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query");
        String queryInput=scanner.nextLine();
        final Matcher selectMatcher = selectPattern.matcher(queryInput);
        final Matcher insertMatcher = insertPattern.matcher(queryInput);
        final Matcher deleteMatcher = deletePattern.matcher(queryInput);
        final Matcher createTableMatcher = createTablePattern.matcher(queryInput);
        final Matcher dropTableMatcher = dropTablePattern.matcher(queryInput);
        final Matcher updateTableMatcher = updateTablePattern.matcher(queryInput);

        DDLQueryExecution ddlQueryExecution = new DDLQueryExecution();
        DQLQueryExecution dqlQueryExecution=new DQLQueryExecution();
        DMLQueryExecution dmlQueryExecution=new DMLQueryExecution();

        FileWriter myFile = new FileWriter("queryParsing.txt");
        FileWriter eventfile = new FileWriter("EventLogs.txt",true);
        FileWriter generalfile = new FileWriter("GeneralLogs.txt",true);

        if(selectMatcher.find())
        {
            myFile.append("[Select query] ").append(queryInput).append("\n");
            myFile.flush();
            String type = selectMatcher.group(1);
            String columns = selectMatcher.group(2);
            String tableName = selectMatcher.group(3);
            String conditions = selectMatcher.group(5);
//            dqlQueryExecution.selectTable(username,type,columns,tableName,conditions);
            // Select methode code goes here
        }else if(insertMatcher.find())
        {
            myFile.append("[Insert query]").append(queryInput).append("\n");
            myFile.flush();
            dmlQueryExecution.insert(insertMatcher, username, eventfile, generalfile);
            // insert methode code goes here
        }else if(deleteMatcher.find())
        {
            myFile.append("[Delete query] ").append(queryInput).append("\n");
            myFile.flush();
            // delete methode code goes here
            dmlQueryExecution.delete(deleteMatcher, username, eventfile, generalfile);

        }else if(createTableMatcher.find())
        {
            myFile.append("[Create table query] ").append(queryInput).append("\n");
            myFile.flush();
            // Create table methode code goes here
            ddlQueryExecution.createTable(createTableMatcher, username);
        }else if(dropTableMatcher.find())
        {
            myFile.append("[Drop table query] ").append(queryInput).append("\n");
            myFile.flush();
            // Drop table methode code goes here
            ddlQueryExecution.dropTable(dropTableMatcher, username);

        }else if(updateTableMatcher.find())
        {
            myFile.append("[Update table query] ").append(queryInput).append("\n");
            myFile.flush();
            String operation = updateTableMatcher.group(1);
            String tableName = updateTableMatcher.group(2);
            String updateOperations = updateTableMatcher.group(3);
            String conditions = updateTableMatcher.group(5);
            // Drop table methode code goes here
            dmlQueryExecution.updateTable(username,updateOperations,tableName,conditions);
        }else if (queryInput.equalsIgnoreCase("dump"))
        {
            myFile.append("[Dump][").append(username).append("] ").append(queryInput).append("\n");
            dqlQueryExecution.dump(username);
        }else
        {
            System.out.println("Invalid SQL syntax .Please check your query ");
        }
        myFile.close();
    }
}