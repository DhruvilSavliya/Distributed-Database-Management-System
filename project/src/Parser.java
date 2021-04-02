import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {

    final String selectRegex = "^SELECT\\s.\\sFROM\\s\\w{1,}\\sWHERE\\s\\w{1,}..\\w{1,}..*";
    final String insertRegex = "^INSERT\\sINTO\\s\\w{1,}\\sVALUES.\\w{1,}.*";
    final String deleteRegex = "^DELETE\\sFROM\\s\\w{1,}\\sWHERE\\s.*";
    final String createTableRegex = "^CREATE\\sTABLE\\s\\w{1,}\\s.*.";


    public void userInput() throws Exception {
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

        FileWriter myFile = new FileWriter("queryParsing.txt");

        if(selectMatcher.find())
        {
            myFile.append("[Select query]").append(queryInput).append("\n");
            myFile.flush();
            myFile.close();
            // Select methode code goes here
        }else if(insertMatcher.find())
        {
            myFile.append("[Insert query]").append(queryInput).append("\n");
            myFile.flush();
            myFile.close();
            // insert methode code goes here
        }else if(deleteMatcher.find())
        {
            myFile.append("[Delete query]").append(queryInput).append("\n");
            myFile.flush();
            myFile.close();
            // delete methode code goes here
        }else if(createTableMatcher.find())
        {
            myFile.append("[Create table query]").append(queryInput).append("\n");
            myFile.flush();
            myFile.close();
            // Create table methode code goes here
        }


    }








}