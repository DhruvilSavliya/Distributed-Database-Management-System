package DDL;

import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.regex.Matcher;

public class DDLQueryExecution {

    private static String SEMICOLON = ";";

    public void createTable(Matcher createMatcher, String username) {
        try {
            FileWriter eventLogsWriter = new FileWriter("EventLogs.txt", true);
            String tableName = createMatcher.group(0);
            String columns = createMatcher.group(1);
            System.out.println("Table names : "+tableName+" Columns : "+columns);
            /*String [] columnsArray = columns.split(",");
            Map<String, String> columnMap = new HashMap<>();
            for (int i=0; i<columnsArray.length; i++) {
                String [] column = columnsArray[i].split(" ");
                columnMap.put(column[0], column[1]);
            }*/
            /*if (updateDictionary(tableName, username, columnMap)) {
                File tableFile = new File("project/Data/"+username+"_"+tableName+".txt");
                if (tableFile.createNewFile()) {
                    eventLogsWriter.append("[Table created] User: "+username+", Table: "+tableName);
                    System.out.println("Table created!");
                } else {
                    System.out.println("Error in table creation! Please try again!");
                }
            }*/
            eventLogsWriter.close();
        } catch (Exception e) {
            System.out.println("[DDLQueryExecution] Exception in application : "+e.getMessage());
        }
    }

    private Boolean updateDictionary(String tableName, String username, Map<String, String> columnMap) throws IOException{
        File userTables = new File("project/Data/UserTableDictionary.txt");
        if (userTables.createNewFile()) {
            FileWriter userTableWriter = new FileWriter(userTables, true);
            String userRow = username;
            userTableWriter.append(userRow);
            userTableWriter.append('\n');
            userTableWriter.append(tableName);
            userTableWriter.append('\n');
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                userTableWriter.append(entry.getKey()+SEMICOLON+entry.getValue());
                userTableWriter.append('\n');
            }
            userTableWriter.append('\n');
            userTableWriter.close();
        } else {
            FileReader userTableReader = new FileReader(userTables);
            BufferedReader bufferedReader = new BufferedReader(userTableReader);
            String userRow = bufferedReader.readLine();
            Boolean tableExists = false;
            while (userRow != null) {
                if (userRow.equals(username)) {
                    userRow = bufferedReader.readLine();
                    if (userRow.equals(tableName)) {
                        tableExists = true;
                        break;
                    }
                }
                userRow = bufferedReader.readLine();
            }
            if (tableExists) {
                System.out.println("Table already exists! Please enter another query");
                return false;
            }
            bufferedReader.close();
            FileWriter userTableWriter = new FileWriter(userTables, true);
            String input = username;
            userTableWriter.append(input);
            userTableWriter.append('\n');
            userTableWriter.append(tableName);
            userTableWriter.append('\n');
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {
                userTableWriter.append(entry.getKey()+SEMICOLON+entry.getValue());
                userTableWriter.append('\n');
            }
            userTableWriter.append('\n');
            userTableWriter.close();
        }
        return true;
    }


}
