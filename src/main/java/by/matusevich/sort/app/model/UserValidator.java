package by.matusevich.sort.app.model;

public class UserValidator {
    public static boolean validateName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 50
                && name.matches("^[a-zA-Zа-яА-ЯёЁ\\s-]+$");
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 6
                && password.matches(".*[A-Z].*")
                && password.matches(".*\\d.*")
                && !password.contains(" ");
    }

    public static boolean validateEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static void validateUser(User user) throws IllegalArgumentException {
        if (!validateName(user.getName())){
            throw new IllegalArgumentException(" Недопустимое имя ");
        }
        if (!validatePassword(user.getPassword())){
            throw new IllegalArgumentException(" Неверный пароль ");
        }
        if (!validateEmail(user.getEmail())){
            throw new IllegalArgumentException(" Неверный адрес электронной почты ");
        }
    }
    public static void validateBeforeBuild(String name, String password, String email) throws IllegalArgumentException {
        if (!validateName(name)){
            throw new IllegalArgumentException(" Недопустимое имя: должно содержать только буквы и быть от 2 до 50 символов ");
        }
        if (!validatePassword(password)){
            throw new IllegalArgumentException(" Неверный пароль: минимум 6 символов, хотя бы одна заглавная буква и одна цифра, без пробелов ");
        }
        if (!validateEmail(email)){
            throw new IllegalArgumentException(" Неверный адрес электронной почты ");
        }
    }
}
