package by.matusevich.sort.app.service;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

@Nested
public class SortingServiceTest {
    private User user1 = User.builder().setName("George").setPassword("123Qq12rsa").setEmail("GeorgeFloyd@icantbreathe.com").build();
    private User user2 = User.builder().setName("John").setPassword("123Qq1a2rsa").setEmail("JohnCena@gmail.com").build();
    private User user3 = User.builder().setName("Arcady").setPassword("Ookoopneek123").setEmail("arcady@mail.ru").build();
    private List<User> users = new ArrayList<>();
    private SortingService sortingService = new SortingService();

    @BeforeEach
    void setup() {
        users.clear();
        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    @Nested
    class EmailTests {
        @Test
        @DisplayName("Email by Ascend")
        public void EmailByAscSortingTest() {
            sortingService.setStrategy(new EmailByAscStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("arcady@mail.ru", sorted.get(0).getEmail());
            assertEquals("GeorgeFloyd@icantbreathe.com", sorted.get(1).getEmail());
            assertEquals("JohnCena@gmail.com", sorted.get(2).getEmail());
        }

        @Test
        @DisplayName("Email by Descend")
        public void EmailByDescSortingTest() {
            sortingService.setStrategy(new EmailByDescStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("JohnCena@gmail.com", sorted.get(0).getEmail());
            assertEquals("GeorgeFloyd@icantbreathe.com", sorted.get(1).getEmail());
            assertEquals("arcady@mail.ru", sorted.get(2).getEmail());
        }
    }

    @Nested
    class FistNameTests {
        @Test
        @DisplayName("First name by Ascend")
        public void FirstNameByAscSortingTest() {
            sortingService.setStrategy(new FirstNameByAscStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("Arcady", sorted.get(0).getName());
            assertEquals("George", sorted.get(1).getName());
            assertEquals("John", sorted.get(2).getName());
        }

        @Test
        @DisplayName("First name by Descend")
        public void FirstNameByDescSortingTest() {
            sortingService.setStrategy(new FirstNameByDescStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("John", sorted.get(0).getName());
            assertEquals("George", sorted.get(1).getName());
            assertEquals("Arcady", sorted.get(2).getName());
        }
    }

    @Nested
    class PasswordTests {
        @Test
        @DisplayName("Password by Ascend")
        public void PasswordByAscSortingTest() {
            sortingService.setStrategy(new PasswordByAscStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("123Qq12rsa", sorted.get(0).getPassword());
            assertEquals("123Qq1a2rsa", sorted.get(1).getPassword());
            assertEquals("Ookoopneek123", sorted.get(2).getPassword());
        }

        @Test
        @DisplayName("Password by Descend")
        public void PasswordByDescSortingTest() {
            sortingService.setStrategy(new PasswordByDescStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals("Ookoopneek123", sorted.get(0).getPassword());
            assertEquals("123Qq1a2rsa", sorted.get(1).getPassword());
            assertEquals("123Qq12rsa", sorted.get(2).getPassword());
        }
    }

    @Nested
    class PasswordLengthTests {
        @Test
        @DisplayName("Password length by Ascend")
        public void PasswordLengthByAscSortingTest() {
            sortingService.setStrategy(new PasswordLengthByAscStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals(10, sorted.get(0).getPassword().length());
            assertEquals(11, sorted.get(1).getPassword().length());
            assertEquals(13, sorted.get(2).getPassword().length());
        }

        @Test
        @DisplayName("Password length by Descend")
        public void PasswordLengthByDescSortingTest() {
            sortingService.setStrategy(new PasswordLengthByDescStrategy());
            List<User> sorted = sortingService.insertionSort(users);
            assertEquals(13, sorted.get(0).getPassword().length());
            assertEquals(11, sorted.get(1).getPassword().length());
            assertEquals(10, sorted.get(2).getPassword().length());
        }
    }
}
