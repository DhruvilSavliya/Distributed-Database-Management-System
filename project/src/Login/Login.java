package Login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Login {

    public Boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = scanner.next();
        System.out.println("Enter your password");
        String password = scanner.next();
        try {
            File users = new File("project/Data/Users.txt");
            FileReader fileReader = new FileReader(users);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String user = bufferedReader.readLine();
            while (user != null) {
                String [] userDetails = user.split(";");
                if (username.equals(userDetails[0])) {
                    byte[] decodedBytes = Base64.getDecoder().decode(userDetails[1]);
                    String decodedPassword = new String(decodedBytes, StandardCharsets.UTF_8);
                    if (password.equals(decodedPassword)) {
                        return true;
                    }
                    else {
                        System.out.println("Wrong password. Please try again");
                        return false;
                    }
                }
                user = bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Exception in application : "+e.getMessage());
            return false;
        }
        return false;
    }
}
