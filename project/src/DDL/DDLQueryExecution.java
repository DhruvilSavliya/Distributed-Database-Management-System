package DDL;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class DDLQueryExecution {

    private static String SEMICOLON = ";";

    public void createTable(Matcher createMatcher, String username)
    {
        try {
            FileWriter eventLogsWriter = new FileWriter("EventLogs.txt", true);
            String query = createMatcher.group(0);
            String [] queryWords = query.split(" ");
            Boolean tableNameFound = false;
            String tableName="";
            String columns = "";
            int i=0;
            for (i=0; i<queryWords.length; i++)
            {
                if (queryWords[i].equalsIgnoreCase("TABLE"))
                {
                    tableNameFound = true;
                    continue;
                }

                if (tableNameFound)
                {
                    tableName = queryWords[i];
                    break;
                }
            }

            for (int j=i+1; j< queryWords.length; j++)
            {
                columns = columns+queryWords[j]+" ";
            }
            String [] columnsArray = columns.split(",");
            Map<String, String> columnMap = new HashMap<>();
            for (int k=0; k<columnsArray.length; k++) {
                String [] column = columnsArray[k].split(" ");
                columnMap.put(column[0], column[1]);
            }
            if (updateDictionary(tableName, username, columnMap)) {
                File tableFile = new File("project/Data/"+username+"_"+tableName+".txt");
                if (tableFile.createNewFile()) {
                    eventLogsWriter.append("[Table created] User: "+username+", Table: "+tableName);
                    System.out.println("Table created!");
                } else {
                    System.out.println("Error in table creation! Please try again!");
                }
            }
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

    public void insertTable(Matcher inserttable, String username){

    }

    public void dropTable(Matcher dropMatcher, String userName)
    {
        try {
            FileWriter eventFile = new FileWriter("EventLogs.txt", true);
            String query = dropMatcher.group(0);
            String [] queryWords = query.split(" ");
            String tableName = "";
            Boolean tableNameFound = false;
            for (int i = 0; i<queryWords.length; i++)
            {
                if (queryWords[i].equalsIgnoreCase("TABLE"))
                {
                    tableNameFound = true;
                    continue;
                }
                if (tableNameFound)
                {
                    tableName = queryWords[i];
                }
            }
            if (removeFromDictionary(tableName, userName)) {
                eventFile.write("[Table Dropped] Table : "+tableName+" User : "+userName);
                System.out.println("Table dropped!");
            } else {
                System.out.println("Failed to drop table.");
            }
        } catch (Exception e)
        {
            System.out.println("Exception in dropping table : "+e.getMessage());
        }
    }

    private Boolean removeFromDictionary(String tableName, String username)
    {
        Boolean returnFlag = false;
        try {
            File userTables = new File("project/Data/UserTableDictionary.txt");
            File tempFile = new File("project/Data/tempFile.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userTables));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

            if (!tempFile.createNewFile()) {
                System.out.println("Unable to create temp file.");
                return false;
            }

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.equals(username)) {
                    currentLine = bufferedReader.readLine();
                    if (currentLine.equals(tableName)) {
                        currentLine = bufferedReader.readLine();
                        while (!currentLine.isBlank()) {
                            returnFlag = true;
                            currentLine = bufferedReader.readLine();
                        }
                    }
                    else {
                        bufferedWriter.write(username);
                        bufferedWriter.write("\n");
                        bufferedWriter.write(currentLine);
                        bufferedWriter.write("\n");
                    }
                }
                else {
                    bufferedWriter.write(currentLine);
                    bufferedWriter.write("\n");
                }
                currentLine = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedWriter.close();
            File tableFile = new File("project/Data/"+username+"_"+tableName+".txt");
            tableFile.delete();
            userTables.delete();
            tempFile.renameTo(userTables);
        } catch (Exception e) {
            System.out.println("Exception in dropping table : "+e.getMessage());
            return false;
        }
        return returnFlag;
    }
}
