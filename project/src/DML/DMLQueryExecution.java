package DML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class DMLQueryExecution {

    public void insert(Matcher insert, String username, FileWriter eventfile, FileWriter generalfile) throws IOException {
        String TableName = insert.group(2);
        File file = new File("project/Data/"+username +"_"+TableName+".txt");
        boolean does_not_exist = !file.exists();
        if (does_not_exist) {
            System.out.println("Table does not exist");
        } else {
            String Columns = insert.group(3);
            String Values = insert.group(4);
            String[] Column = Columns.split("\\s*,\\s*");
            String[] Value = Values.split("\\s*,\\s*");
            List<String> ListOfColums = Arrays.asList(Column);
            List<String> ListOfValues = Arrays.asList(Value);
            long StartTime = System.currentTimeMillis();

            File tablefile = new File(TableName + ".txt");
            FileWriter fileWriter = new FileWriter(tablefile, true);
            File GDD = new File("Data_Dictionary.txt");
            FileReader readGDD = new FileReader(GDD);
            BufferedReader brGDD = new BufferedReader(readGDD);
            String input;
            List<String> temp = new ArrayList<>();
            input = brGDD.readLine();

            while (input != null) {

                while (!input.isBlank()) {
                    String[] tempcolumn = input.split(" ");

                    if (tempcolumn.length > 2) {
                        if (tempcolumn[2].equalsIgnoreCase("pk")) {

                            File ftn = new File(TableName + ".txt");
                            FileReader tnfr = new FileReader(ftn);
                            BufferedReader tnbr = new BufferedReader(tnfr);
                            List<String> column1 = new ArrayList<>();
                            String execute = tnbr.readLine();
                            while (execute != null) {
                                if (!execute.isBlank()) {
                                    String[] e = execute.split(" ");
                                    String tablecolumn = e[0];
                                    if (tablecolumn.equalsIgnoreCase(tempcolumn[0])) {
                                        column1.add(e[1]);
                                    }
                                }
                                execute = tnbr.readLine();
                            }

                            if (ListOfColums.contains(tempcolumn[0])) {
                                int index = ListOfColums.indexOf(tempcolumn[0]);

                                for (String s : column1) {
                                    if (ListOfValues.get(index).equalsIgnoreCase(s)) {
                                        System.out.println("Primary key column should contain unique value");
                                    }
                                }
                            }

                            tnbr.close();
                            tnfr.close();
                        }
                    }
                    temp.add(tempcolumn[0]);
                    input = brGDD.readLine();
                }
                int count = 0;
                int novalue = 0;
                for (int i = 0; i < temp.size(); i++) {
                    for (int j = 0; j < ListOfColums.size(); j++) {
                        if (temp.get(i).equalsIgnoreCase(ListOfColums.get(j))) {
                            count = j;
                            novalue = 1;
                        }
                    }
                    fileWriter.append(temp.get(i));
                    fileWriter.write(" ");
                    if (novalue == 1) {
                        fileWriter.append(ListOfValues.get(count));
                    } else {
                        fileWriter.write("");
                    }
                    fileWriter.write("\n");
                    count = 0;
                    novalue = 0;
                }
                fileWriter.write("\n");
                input = brGDD.readLine();
            }
            brGDD.close();
            fileWriter.close();
            long EndTime = System.currentTimeMillis();
            long TotalTime = EndTime - StartTime;
            generalfile.append("[").append(username).append("] [Total time]").append(String.valueOf(TotalTime)).append(" milliseconds\n");
            eventfile.append("[").append(username).append("] Inserted successfully").append("\n");
        }

    }
}
