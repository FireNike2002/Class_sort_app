package by.matusevich.sort.app.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @TempDir
    Path tempDir;

    private Path testFile;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test.txt");
        originalIn = System.in;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Nested
    @DisplayName("FileUtils Tests")
    class FileUtilsTest {

        @Test
        @DisplayName("Should read and validate correct data from file")
        void testReadAndValidateFile_ValidData() throws IOException {
            String content = "John;password123;john@mail.com\n" +
                    "Jane;pass456;jane@gmail.com\n";
            Files.writeString(testFile, content);

            List<String[]> result = FileUtils.readAndValidateFile(testFile.toString());

            assertEquals(2, result.size());
            assertArrayEquals(new String[]{"John", "password123", "john@mail.com"}, result.get(0));
            assertArrayEquals(new String[]{"Jane", "pass456", "jane@gmail.com"}, result.get(1));
        }

        @Test
        @DisplayName("Should filter out invalid data")
        void testReadAndValidateFile_InvalidData() throws IOException {
            String content = "John;pass;john@mail.com\n" +
                    ";pass123;jane@gmail.com\n" +
                    "Bob;pass123;invalid-email\n" +
                    "Valid;validpass123;valid@email.com\n";
            Files.writeString(testFile, content);

            List<String[]> result = FileUtils.readAndValidateFile(testFile.toString());

            assertEquals(1, result.size());
            assertArrayEquals(new String[]{"Valid", "validpass123", "valid@email.com"}, result.get(0));
        }

        @Test
        @DisplayName("Should handle non-existent file")
        void testReadAndValidateFile_FileNotFound() {
            List<String[]> result = FileUtils.readAndValidateFile("nonexistent.txt");

            assertTrue(result.isEmpty());
            assertTrue(errContent.toString().contains("Ошибка чтения файла"));
        }

        @Test
        @DisplayName("Should handle empty file")
        void testReadAndValidateFile_EmptyFile() throws IOException {
            Files.writeString(testFile, "");

            List<String[]> result = FileUtils.readAndValidateFile(testFile.toString());

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("ValidationUtils Tests")
    class ValidationUtilsTest {

        @Test
        @DisplayName("Name validation should work correctly")
        void testIsValidName() {
            assertTrue(ValidationUtils.isValidName("John"));
            assertTrue(ValidationUtils.isValidName(" John "));
            assertTrue(ValidationUtils.isValidName("John Doe"));

            assertFalse(ValidationUtils.isValidName(null));
            assertFalse(ValidationUtils.isValidName(""));
            assertFalse(ValidationUtils.isValidName("   "));
            assertFalse(ValidationUtils.isValidName("\t\n"));
        }

        @Test
        @DisplayName("Password validation should work correctly")
        void testIsValidPassword() {
            assertTrue(ValidationUtils.isValidPassword("pass12"));
            assertTrue(ValidationUtils.isValidPassword("123456"));
            assertTrue(ValidationUtils.isValidPassword("verylongpassword"));

            assertFalse(false);
            assertFalse(ValidationUtils.isValidPassword("pass1"));
            assertFalse(ValidationUtils.isValidPassword(""));
            assertFalse(ValidationUtils.isValidPassword("   "));
        }

        @Test
        @DisplayName("Email validation should work correctly")
        void testIsValidEmail() {
            assertTrue(ValidationUtils.isValidEmail("user@mail.com"));
            assertTrue(ValidationUtils.isValidEmail("user.name@domain.com"));
            assertTrue(ValidationUtils.isValidEmail("user+tag@mail.co.uk"));
            assertTrue(ValidationUtils.isValidEmail("123@domain.com"));
            assertTrue(ValidationUtils.isValidEmail("USER@MAIL.COM"));

            assertFalse(false);
            assertFalse(ValidationUtils.isValidEmail(""));
            assertFalse(ValidationUtils.isValidEmail("user@"));
            assertFalse(ValidationUtils.isValidEmail("@domain.com"));
            assertFalse(ValidationUtils.isValidEmail("user@domain"));
            assertFalse(ValidationUtils.isValidEmail("user domain.com"));
            assertFalse(ValidationUtils.isValidEmail("user@.com"));
            assertFalse(ValidationUtils.isValidEmail("@."));
        }
    }

    @Nested
    @DisplayName("RandomGeneratorUtils Tests")
    class RandomGeneratorUtilsTest {

        @Test
        @DisplayName("Should generate name from predefined list")
        void testGenerateName() {
            String[] validNames = {"User1", "User2", "User3", "User4", "User5"};

            for (int i = 0; i < 100; i++) {
                String name = RandomGeneratorUtils.generateName();
                assertTrue(isInArray(validNames, name));
            }
        }

        @Test
        @DisplayName("Should generate valid email format")
        void testGenerateEmail() {
            for (int i = 0; i < 100; i++) {
                String email = RandomGeneratorUtils.generateEmail();
                assertTrue(ValidationUtils.isValidEmail(email));
                assertTrue(email.contains("@mail.com"));
            }
        }

        @Test
        @DisplayName("Should generate password with valid format")
        void testGeneratePassword() {
            for (int i = 0; i < 100; i++) {
                String password = RandomGeneratorUtils.generatePassword();
                assertTrue(ValidationUtils.isValidPassword(password));
                assertTrue(password.startsWith("pass"));
                assertTrue(password.length() >= 8);
            }
        }

        private boolean isInArray(String[] array, String value) {
            for (String item : array) {
                if (item.equals(value)) return true;
            }
            return false;
        }
    }

    @Nested
    @DisplayName("InputUtils Tests")
    class InputUtilsTest {

        @Test
        @DisplayName("readValidEmail should return valid email when correct input provided")
        void testReadValidEmail() {
            String input = "valid@email.com\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            String result = InputUtils.readValidEmail();

            assertEquals("valid@email.com", result);
        }

        @Test
        @DisplayName("readValidEmail should retry on invalid email")
        void testReadValidEmailWithRetry() {
            String input = "invalid-email\ninvalid@\nvalid@email.com\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            String result = InputUtils.readValidEmail();

            assertEquals("valid@email.com", result);
            assertTrue(outContent.toString().contains("Некорректный формат почты!"));
        }

        @Test
        @DisplayName("readString should return entered string")
        void testReadString() {
            String expected = "test input";
            System.setIn(new ByteArrayInputStream((expected + "\n").getBytes()));

            String result = InputUtils.readString("Enter text: ");

            assertEquals(expected, result);
            assertTrue(outContent.toString().contains("Enter text: "));
        }

        @Test
        @DisplayName("readInt should return integer when valid input provided")
        void testReadInt() {
            String input = "123\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            int result = InputUtils.readInt("Enter number: ");

            assertEquals(123, result);
        }

        @Test
        @DisplayName("readInt should retry on invalid input")
        void testReadIntWithRetry() {
            String input = "abc\n123\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            int result = InputUtils.readInt("Enter number: ");

            assertEquals(123, result);
            assertTrue(outContent.toString().contains("Ошибка: Введите целое число"));
        }

        @Test
        @DisplayName("readInt should handle multiple invalid inputs")
        void testReadIntWithMultipleInvalidInputs() {
            String input = "abc\ndef\nghi\n123\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            int result = InputUtils.readInt("Enter number: ");

            assertEquals(123, result);
            String output = outContent.toString();
            assertEquals(3, output.split("Ошибка: Введите целое число").length - 1);
        }
    }
}