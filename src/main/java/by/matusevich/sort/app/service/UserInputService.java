package by.matusevich.sort.app.service;

import java.util.Scanner;

public class UserInputService {
    private Scanner userScan;
    public UserInputService(){
        this.userScan = new Scanner(System.in);
    }
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (userScan.hasNextInt()) {
                int value = userScan.nextInt();
                userScan.nextLine();
                return value;
            } else {
                System.out.println("Ошибка: введите целое число.");
                userScan.next();
            }
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return userScan.nextLine();
    }

    public String readLineTrim(String prompt) {
        System.out.print(prompt);
        return userScan.nextLine().trim();
    }

    public void close() {
        userScan.close();
    }
}
