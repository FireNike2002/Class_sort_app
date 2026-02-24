package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.service.UserInputService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataStrategyTest {

    @TempDir
    Path tempDir;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Nested
    @DisplayName("FileInputStrategy Tests")
    class FileInputStrategyTest {

        private UserInputService inputService;
        private FileInputStrategy strategy;

        @BeforeEach
        void setUp() {
            inputService = mock(UserInputService.class);
            strategy = new FileInputStrategy(inputService);
        }

        @Test
        @DisplayName("Should load users from valid file")
        void testGetUsers_ValidFile() throws IOException {
            Path testFile = tempDir.resolve("users.txt");
            String content = "John;password123;john@mail.com\n" +
                    "Jane;pass456;jane@gmail.com\n";
            Files.writeString(testFile, content);

            when(inputService.readString("Введите путь к файлу: ")).thenReturn(testFile.toString());

            List<User> users = strategy.getUsers();

            assertEquals(2, users.size());
            assertEquals("John", users.get(0).getName());
            assertEquals("password123", users.get(0).getPassword());
            assertEquals("john@mail.com", users.get(0).getEmail());
            assertEquals("Jane", users.get(1).getName());

            String output = outContent.toString();
            assertTrue(output.contains("✓ Загружено пользователей: 2"));
        }

        @Test
        @DisplayName("Should filter out invalid records")
        void testGetUsers_FileWithInvalidData() throws IOException {
            Path testFile = tempDir.resolve("users.txt");
            String content = "John;pass;john@mail.com\n" +
                    "Jane;pass456;jane@gmail.com\n" +
                    "Bob;pass123;invalid-email\n";
            Files.writeString(testFile, content);

            when(inputService.readString("Введите путь к файлу: ")).thenReturn(testFile.toString());

            List<User> users = strategy.getUsers();

            assertEquals(1, users.size());
            assertEquals("Jane", users.get(0).getName());

            String output = outContent.toString();
            assertTrue(output.contains("✓ Загружено пользователей: 1"));
            assertTrue(output.contains("⚠ Пропущено некорректных записей: 2"));
        }

        @Test
        @DisplayName("Should handle empty file")
        void testGetUsers_EmptyFile() throws IOException {
            Path testFile = tempDir.resolve("empty.txt");
            Files.writeString(testFile, "");

            when(inputService.readString("Введите путь к файлу: ")).thenReturn(testFile.toString());

            List<User> users = strategy.getUsers();

            assertTrue(users.isEmpty());
            assertTrue(outContent.toString().contains("✗ Файл не содержит валидных данных"));
        }

        @Test
        @DisplayName("Should handle non-existent file")
        void testGetUsers_FileNotFound() {
            when(inputService.readString("Введите путь к файлу: ")).thenReturn("nonexistent.txt");

            List<User> users = strategy.getUsers();

            assertTrue(users.isEmpty());
            assertTrue(outContent.toString().contains("✗ Ошибка при чтении файла"));
        }
    }

    @Nested
    @DisplayName("ManualInputStrategy Tests")
    class ManualInputStrategyTest {

        private UserInputService inputService;
        private ManualInputStrategy strategy;

        @BeforeEach
        void setUp() {
            inputService = mock(UserInputService.class);
            strategy = new ManualInputStrategy(inputService);
        }

        @Test
        @DisplayName("Should create users with valid input")
        void testGetUsers_ValidInput() {
            when(inputService.readInt("Сколько пользователей ввести? ")).thenReturn(2);

            when(inputService.readString("Введите имя (2-50 символов, только буквы): "))
                    .thenReturn("John", "Jane");
            when(inputService.readString("Введите пароль (мин. 6 символов, заглавная буква и цифра): "))
                    .thenReturn("Password123", "JanePass456");
            when(inputService.readString("Введите email: "))
                    .thenReturn("john@mail.com", "jane@gmail.com");

            List<User> users = strategy.getUsers();

            assertEquals(2, users.size());
            assertEquals("John", users.get(0).getName());
            assertEquals("jane@gmail.com", users.get(1).getEmail());

            String output = outContent.toString();
            assertTrue(output.contains("✓ Пользователь добавлен: John"));
            assertTrue(output.contains("✓ Пользователь добавлен: Jane"));
        }

        @Test
        @DisplayName("Should retry on invalid input")
        void testGetUsers_InvalidInputRetry() {
            when(inputService.readInt("Сколько пользователей ввести? ")).thenReturn(1);

            when(inputService.readString("Введите имя (2-50 символов, только буквы): "))
                    .thenReturn("", "John");
            when(inputService.readString("Введите пароль (мин. 6 символов, заглавная буква и цифра): "))
                    .thenReturn("pass", "Password123");
            when(inputService.readString("Введите email: "))
                    .thenReturn("invalid", "john@mail.com");

            List<User> users = strategy.getUsers();

            assertEquals(1, users.size());
            assertEquals("John", users.get(0).getName());

            String output = outContent.toString();
            assertTrue(output.contains("Ошибка: недопустимое имя!"));
            assertTrue(output.contains("Ошибка: недопустимый пароль!"));
            assertTrue(output.contains("Ошибка: недопустимый email!"));
        }
    }

    @Nested
    @DisplayName("RandomInputStrategy Tests")
    class RandomInputStrategyTest {

        private UserDataGenerator generator;
        private RandomInputStrategy strategy;

        @BeforeEach
        void setUp() {
            generator = new UserDataGenerator();
            strategy = new RandomInputStrategy(generator);
        }

        @Test
        @DisplayName("Constructor should accept generator")
        void testConstructor() {
            assertNotNull(strategy);
        }
    }

    @Nested
    @DisplayName("UserDataGenerator Tests")
    class UserDataGeneratorTest {

        private UserDataGenerator generator;

        @BeforeEach
        void setUp() {
            generator = new UserDataGenerator();
        }

        @Test
        @DisplayName("Should generate valid names")
        void testGenerateName() {
            for (int i = 0; i < 100; i++) {
                String name = generator.generateName();
                assertNotNull(name);
                assertFalse(name.isEmpty());
            }
        }

        @Test
        @DisplayName("Should generate valid emails")
        void testGenerateEmail() {
            for (int i = 0; i < 100; i++) {
                String email = generator.generateEmail();
                assertNotNull(email);
                assertTrue(email.contains("@"));
                assertTrue(email.matches("^[^@]+@[^@]+\\.[^@]+$"));
            }
        }

        @Test
        @DisplayName("Should generate valid passwords")
        void testGeneratePassword() {
            for (int i = 0; i < 100; i++) {
                String password = generator.generatePassword();
                assertNotNull(password);
                assertTrue(password.length() >= 6);
                assertTrue(password.matches(".*[A-Z].*"));
                assertTrue(password.matches(".*[0-9].*"));
            }
        }

        @Test
        @DisplayName("Should generate strong passwords")
        void testGenerateStrongPassword() {
            for (int i = 0; i < 100; i++) {
                String password = generator.generateStrongPassword();
                assertNotNull(password);
                assertTrue(password.length() >= 8);
                assertTrue(password.matches(".*[A-Z].*"));
                assertTrue(password.matches(".*[0-9].*"));
            }
        }

        @Test
        @DisplayName("Should generate multiple users")
        void testGenerateUsers() {
            List<User> users = generator.generateUsers(5);

            assertEquals(5, users.size());
            for (User user : users) {
                assertNotNull(user.getName());
                assertNotNull(user.getPassword());
                assertNotNull(user.getEmail());
                assertTrue(user.getPassword().length() >= 6);
            }
        }

        @Test
        @DisplayName("Should generate user with prefix and domain")
        void testGenerateUser() {
            User user = generator.generateUser("testuser", "example.com");

            assertNotNull(user);
            assertTrue(user.getName().startsWith("testuser_"));
            assertEquals("example.com", user.getEmail().split("@")[1]);
        }

        @Test
        @DisplayName("Should generate users with variety")
        void testGenerateUsersWithVariety() {
            List<User> users = generator.generateUsersWithVariety(10);

            assertEquals(10, users.size());
            for (User user : users) {
                assertNotNull(user.getName());
                assertNotNull(user.getPassword());
                assertNotNull(user.getEmail());
            }
        }

        @Test
        @DisplayName("Should handle zero count in generateUsers")
        void testGenerateUsersZero() {
            List<User> users = generator.generateUsers(0);
            assertTrue(users.isEmpty());
        }
    }
}