package DDL;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

public class DDLQueryExecution {

    private static String SEMICOLON = ";";
    private static String [] possibleDatatypes = new String[]{"int", "varchar", "boolean"};

    public void createTable(Matcher createMatcher, String username)
    {
        try {
            FileWriter eventLogsWriter = new FileWriter("EventLogs.txt", true);
            String query = createMatcher.group(0);
            query = query.substring(0, query.length()-1);
            String [] queryWords = query.split(" ");
            Boolean tableNameFound = false;
            String tableName="";
            String columns = "";
//            connectToGcp();
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
            columns = columns.substring(1, columns.length()-2);
            String [] columnsArray = columns.split(",");
            Map<String, String> columnMap = new HashMap<>();
            for (int k=0; k<columnsArray.length; k++) {
                String [] column = columnsArray[k].split(" ");
                Boolean validDatatype = false;
                for (int l=0; l< possibleDatatypes.length; l++) {
                    column[1] = column[1].replace("\\s","");
                    if (possibleDatatypes[l].equalsIgnoreCase(column[1])) {
                        validDatatype = true;
                        break;
                    }
                }
                if (validDatatype) {
                    columnMap.put(column[0], column[1]);
                } else {
                    System.out.println("Invalid Datatype! Supported datatypes : int, varchar, boolean");
                    return;
                }
            }
            if (updateDictionary(tableName, username, columnMap)) {
                File tableFile = new File("Data/"+username+"_"+tableName+".txt");
                if (tableFile.createNewFile()) {
                    eventLogsWriter.append("[Table created] User: "+username+", Table: "+tableName+"\n");
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
        File userTables = new File("Data/UserTableDictionary.txt");
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
            tableName = tableName.substring(0, tableName.length()-1);
            if (removeFromDictionary(tableName, userName)) {
                eventFile.write("[Table Dropped] Table : "+tableName+" User : "+userName+"\n");
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
            File userTables = new File("Data/UserTableDictionary.txt");
            File tempFile = new File("Data/tempFile.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userTables));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));

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
            }
            bufferedReader.close();
            bufferedWriter.close();
            File tableFile = new File("Data/"+username+"_"+tableName+".txt");
            tableFile.delete();
            userTables.delete();
            tempFile.renameTo(userTables);
        } catch (Exception e) {
            System.out.println("Exception in dropping table : "+e.getMessage());
            return false;
        }
        return returnFlag;
    }

    private void connectToGcp()
    {
        try {
            FileReader fileReader = new FileReader("Data/GcpConf.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String ip = bufferedReader.readLine();
            String username = bufferedReader.readLine();
            String password = bufferedReader.readLine();
            bufferedReader.close();
            FileReader privateKeyFile = new FileReader("Data/PrivateKey.txt");
            BufferedReader privateBufferedReader = new BufferedReader(privateKeyFile);
            String privateKey = "";
            String privateKeyReader = privateBufferedReader.readLine();
            while (privateKeyReader != null)
            {
                privateKey += privateKeyReader;
                privateKeyReader = privateBufferedReader.readLine();
            }
            JSch jSch = new JSch();
            jSch.addIdentity("C:/5408Project/csci-5408-group-18/project/Data/id_rsa_private");
            Session session = jSch.getSession(username, ip);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
//            properties.put("PreferredAuthentications", password);
            session.setConfig(properties);
            session.setPassword(password);

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            InputStream inputStream = channelSftp.get("/project/Data/Hello.txt");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = "";
            while ((readLine = inputReader.readLine()) != null)
            {
                System.out.println(readLine);
            }
            inputReader.close();
            privateBufferedReader.close();
            fileReader.close();
            channelSftp.exit();
            session.disconnect();
        } catch (Exception e)
        {
//            System.out.println("Exception : "+e.getMessage());
            e.printStackTrace();
        }
    }
}
