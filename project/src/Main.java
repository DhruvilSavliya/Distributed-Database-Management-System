import Login.*;

import java.util.Scanner;

public class Main {

    public static void main(String [] args)
    {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Welcome to Group 18's DBMS Project. Please select an option");
//        System.out.println("1. Login" +"\n"
//                +"2. Register");
//        Integer option = scanner.nextInt();
//        if (option == 1) {
//            Login login = new Login();
//            if (login.login()) {
//                System.out.println("Login successful"); //Main flow to app goes here
//            } else {
//                System.out.println("Login failed. Please try again.");
//            }
//        }
//        else if (option == 2) {
//            Registration registration = new Registration();
//            if (registration.register()) {
//                System.out.println("User successfully registered!");
//            } else {
//                System.out.println("Registration failed. Please try again.");
//            }
//        }

        Parser p=new Parser();
        try {
            p.userInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
