package by.matusevich.sort.app;
import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.UserInputService;
import by.matusevich.sort.app.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        UserInputService input = new UserInputService();
        int operation;
        while (true) {
            operation = input.readInt("Выберите операцию\n1-Заполнение из файла\n2-Заполнение рандом\n3-Заполнение вручную\n4-список пользователей\n5-Сортировка\n0-Выход из программы\n");
            if (operation == 0)
                break;
            switch (operation) {
                case 1:
                    List<String[]> lines = FileUtils.readAndValidateFile("Указать путь файла");
                    for(String[] parts: lines){
                        User user = User.builder()
                                .setName(parts[0])
                                .setPassword(parts[1])
                                .setEmail(parts[2])
                                .build();
                        userList.add(user);
                        System.out.println("Пользователь " + user + " добавлен");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    String name = input.readString("Введите имя пользователя ");
                    String password = input.readString("Введите пароль ");
                    String email = input.readString("Введите почту ");
                    User user = User.builder()
                            .setName(name)
                            .setPassword(password)
                            .setEmail(email)
                            .build();
                    if ((UserValidator.validateName(name)) && (UserValidator.validatePassword(password)) && (UserValidator.validateEmail(email))) {
                        userList.add(user);
                        System.out.println(user + " создан");
                    } else {
                        UserValidator.validateUser(user);
                    }
                    break;
                case 4:
                    if (userList.isEmpty())
                        System.out.println("Список пользователей пуст");
                    else
                        System.out.println(userList);
                    break;
                case 5:
                    if (userList.isEmpty())
                        System.out.println("Сортировать нечего");
                    else {
                        int typeOfSort = input.readInt("Выберите способ сортировки\n1-По имени \n2-По паролю \n3-По почте \n");
                        switch (typeOfSort) {
                            case 1, 2, 3:
                                break;
                        }
                    }
                    break;
            }
        }
    }
}
