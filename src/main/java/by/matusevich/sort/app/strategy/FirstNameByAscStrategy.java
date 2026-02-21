package by.matusevich.sort.app.strategy;

import by.matusevich.sort.app.model.*;

public class FirstNameByAscStrategy implements ComparingStrategy {
    @Override
    public int compare(User user1, User user2) {
        return user1.getEmail().compareTo(user2.getEmail());
    }
}
