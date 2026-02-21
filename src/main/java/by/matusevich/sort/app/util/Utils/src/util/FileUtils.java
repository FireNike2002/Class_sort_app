package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
    private FileUtils() {}

    public static List<String[]> readAndValidateFile(String path) {
        List<String[]> validData = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                String[] parts = line.split(";"); // Формат: Имя;Пароль;Почта
                if (parts.length == 3 &&
                        ValidationUtils.isValidName(parts[0]) &&
                        ValidationUtils.isValidPassword(parts[1]) &&
                        ValidationUtils.isValidEmail(parts[2])) {
                    validData.add(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return validData;
    }
}
