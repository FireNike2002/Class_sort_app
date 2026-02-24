package by.matusevich.sort.app;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.SortingService;
import by.matusevich.sort.app.service.UserInputService;
import by.matusevich.sort.app.strategy.*;
import by.matusevich.sort.app.util.FileUtils;
import by.matusevich.sort.app.util.RandomGeneratorUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        UserInputService input = new UserInputService();
        int operation;
        while (true) {
            operation = input.readInt("Выберите операцию\n" +
                    "1-Заполнение из файла\n" +
                    "2-Заполнение рандомом\n" +
                    "3-Заполнение вручную\n" +
                    "4-Список пользователей\n" +
                    "5-Сортировка\n" +
                    "0-Выход из программы\n");

            if (operation == 0) {
                System.out.println("Программа завершена.");
                break;
            }

            switch (operation) {
                case 1:
                    String path = input.readString("Укажите путь к файлу ");
                    List<String[]> lines = FileUtils.readAndValidateFile(path);
                    int addedCount = 0;
                    for (String[] parts : lines) {
                        if (parts.length >= 3) {
                            User user = User.builder()
                                    .setName(parts[0])
                                    .setPassword(parts[1])
                                    .setEmail(parts[2])
                                    .build();
                            userList.add(user);
                            addedCount++;
                            System.out.println("Пользователь " + user.getName() + " добавлен");
                        }
                        System.out.println("Загружено " + addedCount + " пользователей из файла");
                    break;

                case 2:
                    String nameRand = RandomGeneratorUtils.generateName();
                    String passwordRand = RandomGeneratorUtils.generatePassword();
                    String emailRand = RandomGeneratorUtils.generateEmail();
                    User userRand = User.builder()
                            .setName(nameRand)
                            .setPassword(passwordRand)
                            .setEmail(emailRand)
                            .build();
                    userList.add(userRand);
                    System.out.println("Сгенерирован пользователь: " + userRand.getName());
                    break;

                case 3:
                    String name;
                    do {
                        name = input.readString("Введите имя пользователя (2-50 символов, буквы): ");
                        if (!UserValidator.validateName(name)) {
                            System.out.println("✗ Недопустимое имя! Попробуйте снова.");
                        }
                    } while (!UserValidator.validateName(name));

                    String password;
                    do {
                        password = input.readString("Введите пароль (мин. 6 символов, заглавная буква и цифра): ");
                        if (!UserValidator.validatePassword(password)) {
                            System.out.println("✗ Недопустимый пароль! Попробуйте снова.");
                        }
                    } while (!UserValidator.validatePassword(password));

                    String email;
                    do {
                        email = input.readString("Введите почту: ");
                        if (!UserValidator.validateEmail(email)) {
                            System.out.println("✗ Недопустимый email! Попробуйте снова.");
                        }
                    } while (!UserValidator.validateEmail(email));

                    User user = User.builder()
                            .setName(name)
                            .setPassword(password)
                            .setEmail(email)
                            .build();
                    userList.add(user);
                    System.out.println("Пользователь " + user.getName() + " создан");
                    break;

                case 4:
                    if (userList.isEmpty()) {
                        System.out.println("Список пользователей пуст");
                    } else {
                        System.out.println("\n=== СПИСОК ПОЛЬЗОВАТЕЛЕЙ (" + userList.size() + ") ===");
                        for (int i = 0; i < userList.size(); i++) {
                            User u = userList.get(i);
                            System.out.printf("%d. Имя: %-15s | Email: %-25s | Пароль: %s%n",
                                    i + 1,
                                    u.getName(),
                                    u.getEmail(),
                                    "*".repeat(u.getPassword().length()));
                        }
                    }
                    break;

                case 5:
                    if (userList.isEmpty()) {
                        System.out.println("Сортировать нечего");
                        break;
                    }

                    int typeOfSort = input.readInt("Выберите способ сортировки\n1-По имени\n2-По паролю\n3-По почте\n");
                    SortingService service = new SortingService();
                    boolean strategySet = true;

                    switch (typeOfSort) {
                        case 1:
                            int nameOrder = input.readInt("1-По возрастанию\n2-По убыванию\n");
                            if (nameOrder == 1) {
                                service.setStrategy(new FirstNameByAscStrategy());
                            } else if (nameOrder == 2) {
                                service.setStrategy(new FirstNameByDescStrategy());
                            } else {
                                System.out.println("Неверный выбор направления");
                                strategySet = false;
                            }
                            break;

                        case 2:
                            int passOrder = input.readInt("1-По возрастанию\n2-По убыванию\n");
                            if (passOrder == 1) {
                                service.setStrategy(new PasswordByAscStrategy());
                            } else if (passOrder == 2) {
                                service.setStrategy(new PasswordByDescStrategy());
                            } else {
                                System.out.println("Неверный выбор направления");
                                strategySet = false;
                            }
                            break;

                        case 3:
                            int emailOrder = input.readInt("1-По возрастанию\n2-По убыванию\n");
                            if (emailOrder == 1) {
                                service.setStrategy(new EmailByAscStrategy());
                            } else if (emailOrder == 2) {
                                service.setStrategy(new EmailByDescStrategy());
                            } else {
                                System.out.println("Неверный выбор направления");
                                strategySet = false;
                            }
                            break;

                        default:
                            System.out.println("Неверный выбор способа сортировки");
                            strategySet = false;
                    }

                    if (strategySet) {
                        userList = service.insertionSort(userList); // сохраняем результат!
                        System.out.println("Список отсортирован.");
                    }
                    break;

                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
        input.close();
    }
}