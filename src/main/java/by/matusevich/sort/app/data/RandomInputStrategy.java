package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;
import java.util.List;

public class RandomInputStrategy {
    private final UserDataGenerator generator;

    public RandomInputStrategy(UserDataGenerator generator) {
        this.generator = generator;
    }
}