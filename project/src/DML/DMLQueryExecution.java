package DML;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class DMLQueryExecution {

    public void updateTable(String username,String updateOperations,String tableName,String conditions)
    {
        String[] updateOperationsArray = updateOperations.trim().split("=");
        String[] conditionsArray = conditions.trim().split("=");
        try {
            File myObj = new File("Data/" + username + "_" + tableName + ".txt");
            Scanner myReader = new Scanner(myObj);
            String s = "";
            Map<String,String> keyValue = new HashMap<String,String>();
            String nextLine;
            while (myReader.hasNextLine())
            {
                nextLine = myReader.nextLine();

                while( nextLine.trim().length() > 0 ) {
                    String[] row = nextLine.trim().split(" ");
                    keyValue.put(row[0], row[1]);
                    if(myReader.hasNextLine()) {
                        nextLine = myReader.nextLine();
                    } else {
                        nextLine = "";
                    }
                }

                if(nextLine.trim().length() == 0)
                {
                    String value = keyValue.get(conditionsArray[0]);
                    if(value.trim().equalsIgnoreCase(conditionsArray[1].trim()))
                    {
                        keyValue.put(updateOperationsArray[0], updateOperationsArray[1]);
                    }
                    Iterator<String> iterator = keyValue.keySet().iterator();
                    while(iterator.hasNext())
                    {
                        String key = iterator.next();
                        s = s + key + " " + keyValue.get(key) +"\n";
                    }

                    s = s + "\n";
                }
            }
            myReader.close();
            System.out.println(s);
            FileWriter myfile=new FileWriter("Data/" + username + "_" + tableName + ".txt");
            myfile.write(s);
            myfile.flush();
            myfile.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}