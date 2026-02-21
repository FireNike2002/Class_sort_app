package by.matusevich.sort.app.service;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.strategy.ComparingStrategy;

import java.util.ArrayList;
import java.util.List;

public class SortingService {
    private ComparingStrategy strategy;

    public void setStrategy(ComparingStrategy strategy) {
        this.strategy = strategy;
    }

    public List<User> insertionSort(List<User> users) {
        List<User> sortedList = new ArrayList<>(users);

        for (int i = 1; i < sortedList.size(); i++) {
            User key = sortedList.get(i);
            int j = i - 1;
            while (j >= 0 && strategy.compare(sortedList.get(j), key) > 0) {
                sortedList.set(j + 1, sortedList.get(j));
                j--;
            }
            sortedList.set(j + 1, key);
        }

        return sortedList;
    }
}
