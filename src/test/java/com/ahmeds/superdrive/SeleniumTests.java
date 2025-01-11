package com.ahmeds.superdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void beforeEach() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void afterEach() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Helper methods
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        driver.get("http://localhost:" + port + "/signup");

        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(firstName);

        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(lastName);

        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);

        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);

        WebElement signUpButton = driver.findElement(By.id("buttonSignUp"));
        signUpButton.click();
    }

    private void doLogIn(String userName, String password) {
        driver.get("http://localhost:" + port + "/login");

        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);

        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);

        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
    }

    private void doLogOut() {
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();
    }

    // Test 1: Write a test that verifies that an unauthorized user can only access the login and signup pages
    @Test
    void testUnauthorizedAccess() {
        driver.get("http://localhost:" + port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    // Test 2: Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible
    @Test
    void testSignUpLoginLogout() {
        doMockSignUp("John", "Doe", "jdoe", "password123");
        doLogIn("jdoe", "password123");

        Assertions.assertEquals("Home", driver.getTitle());

        doLogOut();
        driver.get("http://localhost:" + port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    // Test 3: Write a test that creates a note, and verifies it is displayed
    @Test
    void testNoteCreation() {
        doMockSignUp("Jane", "Doe", "janedoe", "password123");
        doLogIn("janedoe", "password123");

        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
        newNoteButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.sendKeys("Test Note");

        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.sendKeys("This is a test note");

        WebElement saveButton = driver.findElement(By.id("noteSubmit"));
        saveButton.click();

        driver.get("http://localhost:" + port + "/home");
        notesTab.click();

        WebElement firstNoteTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[text()='Test Note']")));
        Assertions.assertNotNull(firstNoteTitle);
    }

    // Test 4: Write a test that edits an existing note and verifies that the changes are displayed
    @Test
    void testNoteEdit() {
        doMockSignUp("Bob", "Smith", "bsmith", "password123");
        doLogIn("bsmith", "password123");

        // First create a note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
        newNoteButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.sendKeys("Original Note");

        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.sendKeys("Original description");

        WebElement saveButton = driver.findElement(By.id("noteSubmit"));
        saveButton.click();

        // Now edit the note
        driver.get("http://localhost:" + port + "/home");
        notesTab.click();

        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@onclick, 'editNote')]")));
        editButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.clear();
        noteTitle.sendKeys("Edited Note");

        noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.clear();
        noteDescription.sendKeys("Edited description");

        saveButton = driver.findElement(By.id("noteSubmit"));
        saveButton.click();

        driver.get("http://localhost:" + port + "/home");
        notesTab.click();

        WebElement editedNoteTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[text()='Edited Note']")));
        Assertions.assertNotNull(editedNoteTitle);
    }

    // Test 5: Write a test that deletes a note and verifies that the note is no longer displayed
    @Test
    void testNoteDeletion() {
        doMockSignUp("Alice", "Johnson", "ajohnson", "password123");
        doLogIn("ajohnson", "password123");

        // First create a note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
        newNoteButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.sendKeys("Note to Delete");

        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.sendKeys("This note will be deleted");

        WebElement saveButton = driver.findElement(By.id("noteSubmit"));
        saveButton.click();

        // Now delete the note
        driver.get("http://localhost:" + port + "/home");
        notesTab.click();

        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@onclick, 'deleteNote')]")));
        deleteButton.click();

        driver.get("http://localhost:" + port + "/home");
        notesTab.click();

        Assertions.assertTrue(driver.findElements(By.xpath("//th[text()='Note to Delete']")).isEmpty());
    }
}
