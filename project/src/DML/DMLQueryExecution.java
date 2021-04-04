package DML;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
            System.out.println("Table does not exist to perform insert.");
            eventfile.append("[").append(username).append("] Table does not exist to perform insert.").append("\n");
        }
        eventfile.close();
        generalfile.close();

    }

    public void delete(Matcher delete, String username, FileWriter eventfile, FileWriter generalfile) throws IOException {
        String query = delete.group(0);
        String[] querywords = query.split(" ");
        String TableName = querywords[2];
        File file = new File("Data/" + username + "_" + TableName + ".txt");
        File fold = new File("Data/updated.txt");
        File fnew = new File("Data/" + username + "_" + TableName + ".txt");
        boolean file_exist = file.exists();
        if (file_exist) {
            String condition = querywords[4];
            String[] columnandvalue = condition.split("=|;");
            String col = columnandvalue[0];
            String value = columnandvalue[1];

            FileReader fileReader = new FileReader(file);
            Scanner scanner = new Scanner(file);
            File tempfile = new File("Data/temp.txt");
            tempfile.createNewFile();
            FileWriter fileWriter = new FileWriter(tempfile);
            File updatedtable = new File("Data/updated.txt");
            updatedtable.createNewFile();
            FileWriter fw = new FileWriter(updatedtable);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == "") {
                    continue;
                } else {
                    String[] s = line.split(" ");
                    if (s[0].equalsIgnoreCase(col) && s[1].equalsIgnoreCase(value)) {
                        while (scanner.hasNextLine()) {
                            fileWriter.write(s[0] + " " + s[1]);
                            if (scanner.nextLine() == "") {
                                break;
                            }
                        }
                    } else if (s[0] != col && s[1] != value) {
                        fw.write(s[0] + " " + s[1]);
                        fw.append("\n");
                    } else {
                        System.out.println("record not found");
                    }

                }

            }
            fileWriter.close();
            fw.close();

            if (tempfile.delete()) {
                eventfile.append("[").append(username).append("] Temp file deleted.").append("\n");
            }

        } else {
            System.out.println("Table does not exist to perform delete.");
            eventfile.append("[").append(username).append("] Table does not exist to perform delete.").append("\n");

        }
        if (file.delete()) {
            System.out.println("deleted");
        }

        if (fold.renameTo(fnew)) {
            System.out.println("renamed");
        }

    }


}