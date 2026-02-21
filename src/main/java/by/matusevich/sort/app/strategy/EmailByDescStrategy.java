package by.matusevich.sort.app.strategy;

import by.matusevich.sort.app.model.*;

public class EmailByDescStrategy implements ComparingStrategy {
    @Override
    public int compare(User user1, User user2) {
        return user2.getEmail().compareTo(user1.getEmail());
    }
}
