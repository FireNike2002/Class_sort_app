package by.matusevich.sort.app.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import by.matusevich.sort.app.model.User;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Nested
public class StrategyTest {
    private User user1 = User.builder().setName("Alex").setEmail("Aboba@gmail.com").setPassword("A12345").build();
    private User user2 = User.builder().setName("Calvin").setEmail("calvin111@gmail.com").setPassword("qq12345").build();
    private User user3 = User.builder().setName("Boris").setEmail("borisbritva@mail.ru").setPassword("qq12345Q").build();

    @Nested
    @DisplayName("Email Test")
    class EmailStrategyTest {
        @Test
        @DisplayName("Email by Ascend")
        public void EmailByAscStrategyTest() {
            EmailByAscStrategy strategy = new EmailByAscStrategy();
            assertTrue(strategy.compare(user1, user2) < 0, "Alex должен быть перед Calvin (Aboba@gmail.com < calvin111@gmail.com)");
            assertTrue(strategy.compare(user1, user3) < 0, "Alex должен быть перед Boris (Aboba@gmail.com < borisbritva@mail.ru)");
            assertTrue(strategy.compare(user2, user3) > 0, "Calvin должен быть перед Boris (calvin111@gmail.com > borisbritva@mail.ru)");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
        @Test
        @DisplayName("Email by Descend")
        public void EmailByDescStrategyTest() {
            EmailByDescStrategy strategy = new EmailByDescStrategy();
            assertTrue(strategy.compare(user1, user2) > 0, "Calvin должен быть перед Alex (calvin111@gmail.com > Aboba@gmail.com)");
            assertTrue(strategy.compare(user1, user3) > 0, "Boris должен быть перед Alex (borisbritva@mail.ru > Aboba@gmail.com)");
            assertTrue(strategy.compare(user2, user3) < 0, "Boris должен быть перед Calvin (borisbritva@mail.ru < calvin111@gmail.com)");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
    }


    @Nested
    @DisplayName("First name test")
    class FirstNameTest {
        @Test
        @DisplayName("First name by Ascend")
        public void FirstNameByAscStrategyTest() {
            FirstNameByAscStrategy strategy = new FirstNameByAscStrategy();
            assertTrue(strategy.compare(user1, user2) < 0, "Alex должен быть перед Calvin (Alex < Calvin)");
            assertTrue(strategy.compare(user1, user3) < 0, "Alex должен быть перед Boris (Alex < Boris)");
            assertTrue(strategy.compare(user2, user3) > 0, "Calvin должен быть перед Boris (Calvin > Boris)");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
        @Test
        @DisplayName("First name by Descend")
        public void FirstNameByDescStrategyTest() {
            FirstNameByDescStrategy strategy = new FirstNameByDescStrategy();
            assertTrue(strategy.compare(user1, user2) > 0, "Calvin должен быть перед Alex (Calvin > Alex)");
            assertTrue(strategy.compare(user1, user3) > 0, "Boris должен быть перед Alex (Boris > Alex)");
            assertTrue(strategy.compare(user2, user3) < 0, "Boris должен быть перед Calvin (Boris < Calvin)");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
    }


    @Nested
    @DisplayName("Password test")
    class PasswordStrategyTest {
        @Test
        @DisplayName("Password by Ascend")
        public void PasswordByAscStrategyTest() {
            PasswordByAscStrategy strategy = new PasswordByAscStrategy();
            assertTrue(strategy.compare(user1, user2) < 0, "Пароль A12345 должен быть перед qq12345");
            assertTrue(strategy.compare(user1, user3) < 0, "Пароль A12345 должен быть перед qq12345Q");
            assertTrue(strategy.compare(user2, user3) < 0, "Пароль qq12345 должен быть перед qq12345Q");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
        @Test
        @DisplayName("Password by Descend")
        public void PasswordByDescStrategyTest() {
            PasswordByDescStrategy strategy = new PasswordByDescStrategy();
            assertTrue(strategy.compare(user1, user2) > 0, "Пароль qq12345 должен быть перед A12345");
            assertTrue(strategy.compare(user1, user3) > 0, "Пароль qq12345Q должен быть перед A12345");
            assertTrue(strategy.compare(user2, user3) > 0, "Пароль qq12345Q должен быть перед qq12345");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
    }


    @Nested
    @DisplayName("Password Length test")
    class PasswordLengthStrategyTest {
        @Test
        @DisplayName("Pass length by Ascend")
        public void PasswordLengthByAscStrategyTest() {
            PasswordLengthByAscStrategy strategy = new PasswordLengthByAscStrategy();
            assertTrue(strategy.compare(user1, user2) < 0, "Пароль длиной 6 должен быть перед паролем длиной 7");
            assertTrue(strategy.compare(user1, user3) < 0, "Пароль длиной 6 должен быть перед паролем длиной 8");
            assertTrue(strategy.compare(user2, user3) < 0, "Пароль длиной 7 должен быть перед паролем длиной 8");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }

        @Test
        @DisplayName("Pass length by Descend")
        public void PasswordLengthByDescStrategyTest() {
            PasswordLengthByDescStrategy strategy = new PasswordLengthByDescStrategy();
            assertTrue(strategy.compare(user1, user2) > 0, "Пароль длиной 7 должен быть перед паролем длиной 6");
            assertTrue(strategy.compare(user1, user3) > 0, "Пароль длиной 8 должен быть перед паролем длиной 6");
            assertTrue(strategy.compare(user2, user3) > 0, "Пароль длиной 8 должен быть перед паролем длиной 7");
            assertEquals(0, strategy.compare(user1, user1), "Сравнение одинаковых пользователей должно вернуть 0");
        }
    }
}
