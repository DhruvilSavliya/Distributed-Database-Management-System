import DDL.DDLQueryExecution;
import DML.DMLQueryExecution;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {

    final String selectRegex = "^SELECT\\s.\\sFROM\\s\\w{1,}\\sWHERE\\s\\w{1,}..\\w{1,}..*";
    final String insertRegex = "(?i)(INSERT\\sINTO\\s+(\\w+)\\s+\\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);)";
    final String deleteRegex = "^DELETE\\sFROM\\s\\w{1,}\\sWHERE\\s.*";
    final String createTableRegex = "^CREATE\\sTABLE\\s\\w{1,}\\s.*.";


    public void userInput(String username) throws Exception {
        final Pattern selectPattern = Pattern.compile(selectRegex);
        final Pattern insertPattern = Pattern.compile(insertRegex);
        final Pattern deletePattern = Pattern.compile(deleteRegex);
        final Pattern createTablePattern = Pattern.compile(createTableRegex);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query");
        String queryInput=scanner.nextLine();
        final Matcher selectMatcher = selectPattern.matcher(queryInput);
        final Matcher insertMatcher = insertPattern.matcher(queryInput);
        final Matcher deleteMatcher = deletePattern.matcher(queryInput);
        final Matcher createTableMatcher = createTablePattern.matcher(queryInput);
        DDLQueryExecution ddlQueryExecution = new DDLQueryExecution();

        FileWriter myFile = new FileWriter("queryParsing.txt");
        FileWriter eventfile = new FileWriter("EventLogs.txt",true);
        FileWriter generalfile = new FileWriter("GeneralLogs.txt",true);

        if(selectMatcher.find())
        {
            myFile.append("[Select query]").append(queryInput).append("\n");
            myFile.flush();

            // Select methode code goes here
        }else if(insertMatcher.find())
        {
//            String tablename = insertMatcher.group(2);
//
//            System.out.println(tablename);
//            File file = new File("Data/"+tablename+".txt");
//            if (file.exists()){
//                System.out.println("exist");
//            }
//            myFile.append("[Insert query]").append(queryInput).append("\n");
//            myFile.flush();
            DMLQueryExecution dmlQueryExecution = new DMLQueryExecution();
            dmlQueryExecution.insert(insertMatcher, username, eventfile, generalfile);


            // insert methode code goes here
        }else if(deleteMatcher.find())
        {
            myFile.append("[Delete query]").append(queryInput).append("\n");
            myFile.flush();

            // delete methode code goes here
        }else if(createTableMatcher.find())
        {
            myFile.append("[Create table query]").append(queryInput).append("\n");
            myFile.flush();
            // Create table methode code goes here
            ddlQueryExecution.createTable(createTableMatcher, username);
        }else
        {
            System.out.println("Invalid SQL syntax .Please check your query ");
        }
        myFile.close();
    }








}