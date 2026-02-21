package by.matusevich.sort.app.strategy;

import by.matusevich.sort.app.model.*;

public class PasswordLengthByDescStrategy implements ComparingStrategy {
    @Override
    public int compare(User user1, User user2) {
        return Integer.compare(user2.getPassword().length(), user1.getPassword().length());
    }
}
