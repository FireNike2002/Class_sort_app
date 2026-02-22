package by.matusevich.sort.app.util;

import java.util.Scanner;

public final class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtils() {}

    public static String readString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int readInt(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: Введите целое число.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        return value;
    }

    public static String readValidEmail() {
        while (true) {
            String email = readString("Введите Email: ");
            if (ValidationUtils.isValidEmail(email)) return email;
            System.out.println("Некорректный формат почты!");
        }
    }
}