package DML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class DMLQueryExecution {

    public void insert(Matcher insert, String username, FileWriter eventfile, FileWriter generalfile) throws IOException {
        String TableName, first, second;
        TableName = insert.group(2);
        File file = new File("Data/" + username + "_" + TableName + ".txt");
        boolean file_exist = file.exists();
        if (file_exist) {

            long StartTime = System.currentTimeMillis();
            first = insert.group(3);
            second = insert.group(4);

            String[] Column = first.split("\\s*,\\s*");
            String[] Value = second.split("\\s*,\\s*");

            List<String> ListOfColums = Arrays.asList(Column);
            List<String> ListOfValues = Arrays.asList(Value);


            FileWriter fileWriter = new FileWriter(file, true);

            int length = ListOfColums.size();
            for (int i = 0; i < length; i++) {
                fileWriter.write(ListOfColums.get(i) + " " + ListOfValues.get(i) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.close();
            System.out.println("Inserted Successfully");
            long EndTime = System.currentTimeMillis();
            long TotalTime = EndTime - StartTime;
            generalfile.append("[").append(username).append("] [Total time] [Insert Query]").append(String.valueOf(TotalTime)).append(" milliseconds\n");
            eventfile.append("[").append(username).append("] Inserted successfully").append("\n");
        } else {
            System.out.println("Table does not exist");
            eventfile.append("[").append(username).append("] Table does not exist").append("\n");
        }
        eventfile.close();
        generalfile.close();

    }

}