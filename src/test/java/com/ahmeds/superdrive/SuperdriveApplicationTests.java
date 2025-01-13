package com.ahmeds.superdrive;

import com.ahmeds.superdrive.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SuperdriveApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
        userService.deleteTestUsers();
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        URI uri = URI.create(Objects.requireNonNull(driver.getCurrentUrl()));
        Assertions.assertEquals(
                "http://localhost:" + this.port + "/login",
                String.format("%s://%s:%s%s", uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath()));
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void testProtectedRoute() {
        // Attempt to visit the home page without logging in
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertFalse(driver.getCurrentUrl().contains("/home"));

        doMockSignUp("Sign Up Test", "Test", "SUT", "123");

        doLogIn("SUT", "123");

        // Verify that the home page is accessible
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"));

        // Log out and verify the home page is no longer accessible
        driver.get("http://localhost:" + this.port + "/logout");
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertFalse(driver.getCurrentUrl().contains("/home"));
    }

    @Test
    public void testCreateNote() {
        // Create a test account and login
        doMockSignUp("Note", "Test", "NT", "123");
        doLogIn("NT", "123");

        // Click on Notes tab
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        driver.findElement(By.id("nav-notes-tab")).click();

        // Create a new note
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add a New Note')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add a New Note')]")).click();

        // Fill in note details
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleField = driver.findElement(By.id("note-title"));
        noteTitleField.sendKeys("Test Note");

        WebElement noteDescriptionField = driver.findElement(By.id("note-description"));
        noteDescriptionField.sendKeys("This is a test note description");

        // Submit the note by clicking the Save changes button
        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close and page to refresh
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));

        // Verify the note appears in the list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement notesTable = driver.findElement(By.id("userTable"));
        Assertions.assertTrue(notesTable.getText().contains("Test Note"));
        Assertions.assertTrue(notesTable.getText().contains("This is a test note description"));
    }

    @Test
    public void testEditNote() {
        // Create a test account, login, and create a note first
        doMockSignUp("Note", "Test", "NT2", "123");
        doLogIn("NT2", "123");

        // Create initial note
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        driver.findElement(By.id("nav-notes-tab")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add a New Note')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add a New Note')]")).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        driver.findElement(By.id("note-title")).sendKeys("Original Note");
        driver.findElement(By.id("note-description")).sendKeys("Original description");

        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close and page to refresh
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));

        // Edit the note
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Edit')]")));
        driver.findElement(By.xpath("//button[contains(text(), 'Edit')]")).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement titleField = driver.findElement(By.id("note-title"));
        titleField.clear();
        titleField.sendKeys("Updated Note");

        WebElement descriptionField = driver.findElement(By.id("note-description"));
        descriptionField.clear();
        descriptionField.sendKeys("Updated description");

        saveButton = driver.findElement(By.xpath("//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));

        // Verify the changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement notesTable = driver.findElement(By.id("userTable"));
        Assertions.assertTrue(notesTable.getText().contains("Updated Note"));
        Assertions.assertTrue(notesTable.getText().contains("Updated description"));
        Assertions.assertFalse(notesTable.getText().contains("Original Note"));
        Assertions.assertFalse(notesTable.getText().contains("Original description"));
    }

    @Test
    public void testDeleteNote() {
        // Create a test account, login, and create a note first
        doMockSignUp("Note", "Test", "NT3", "123");
        doLogIn("NT3", "123");

        // Create a note to delete
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        driver.findElement(By.id("nav-notes-tab")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add a New Note')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add a New Note')]")).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        driver.findElement(By.id("note-title")).sendKeys("Note to Delete");
        driver.findElement(By.id("note-description")).sendKeys("This note will be deleted");

        WebElement saveButton = driver.findElement(By.xpath("//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));

        // Delete the note
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Delete')]")));
        driver.findElement(By.xpath("//a[contains(text(), 'Delete')]")).click();

        // Verify the note is deleted
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement notesTable = driver.findElement(By.id("userTable"));
        Assertions.assertFalse(notesTable.getText().contains("Note to Delete"));
        Assertions.assertFalse(notesTable.getText().contains("This note will be deleted"));
    }

    @Test
    public void testCreateCredential() {
        // Create a test account and login
        doMockSignUp("Credential", "Test", "CT", "123");
        doLogIn("CT", "123");

        // Click on Credentials tab
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        driver.findElement(By.id("nav-credentials-tab")).click();

        // Create a new credential
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add New Credential')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add New Credential')]")).click();

        // Wait for modal to be visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        // Fill in credential details
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement urlField = driver.findElement(By.id("credential-url"));
        urlField.sendKeys("http://example.com");

        WebElement usernameField = driver.findElement(By.id("credential-username"));
        usernameField.sendKeys("testuser");

        WebElement passwordField = driver.findElement(By.id("credential-password"));
        passwordField.sendKeys("testpass");

        // Wait for Save changes button to be clickable
        webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]")));
        WebElement saveButton = driver.findElement(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));

        // Verify the credential appears in the list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement credentialTable = driver.findElement(By.id("credentialTable"));
        Assertions.assertTrue(credentialTable.getText().contains("http://example.com"));
        Assertions.assertTrue(credentialTable.getText().contains("testuser"));
    }

    @Test
    public void testEditCredential() {
        // Create a test account, login, and create a credential first
        doMockSignUp("Credential", "Test", "CT2", "123");
        doLogIn("CT2", "123");

        // Create initial credential
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        driver.findElement(By.id("nav-credentials-tab")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add New Credential')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add New Credential')]")).click();

        // Wait for modal to be visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        driver.findElement(By.id("credential-url")).sendKeys("http://original.com");
        driver.findElement(By.id("credential-username")).sendKeys("originaluser");
        driver.findElement(By.id("credential-password")).sendKeys("originalpass");

        // Wait for Save changes button to be clickable
        webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]")));
        WebElement saveButton = driver.findElement(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));

        // Edit the credential
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Edit')]")));
        driver.findElement(By.xpath("//button[contains(text(), 'Edit')]")).click();

        // Wait for modal to be visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement urlField = driver.findElement(By.id("credential-url"));
        urlField.clear();
        urlField.sendKeys("http://updated.com");

        WebElement usernameField = driver.findElement(By.id("credential-username"));
        usernameField.clear();
        usernameField.sendKeys("updateduser");

        WebElement passwordField = driver.findElement(By.id("credential-password"));
        passwordField.clear();
        passwordField.sendKeys("updatedpass");

        // Wait for Save changes button to be clickable
        webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]")));
        saveButton = driver.findElement(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));

        // Verify the changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement credentialTable = driver.findElement(By.id("credentialTable"));
        Assertions.assertTrue(credentialTable.getText().contains("http://updated.com"));
        Assertions.assertTrue(credentialTable.getText().contains("updateduser"));
        Assertions.assertFalse(credentialTable.getText().contains("http://original.com"));
        Assertions.assertFalse(credentialTable.getText().contains("originaluser"));
    }

    @Test
    public void testDeleteCredential() {
        // Create a test account, login, and create a credential first
        doMockSignUp("Credential", "Test", "CT3", "123");
        doLogIn("CT3", "123");

        // Create a credential to delete
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        driver.findElement(By.id("nav-credentials-tab")).click();

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Add New Credential')]")));
        driver.findElement(By.xpath("//button[contains(text(), '+ Add New Credential')]")).click();

        // Wait for modal to be visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        driver.findElement(By.id("credential-url")).sendKeys("http://todelete.com");
        driver.findElement(By.id("credential-username")).sendKeys("deleteuser");
        driver.findElement(By.id("credential-password")).sendKeys("deletepass");

        // Wait for Save changes button to be clickable
        webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]")));
        WebElement saveButton = driver.findElement(
                By.xpath("//div[@id='credentialModal']//button[contains(text(), 'Save changes')]"));
        saveButton.click();

        // Wait for the modal to close
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));

        // Delete the credential
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Delete')]")));
        driver.findElement(By.xpath("//a[contains(text(), 'Delete')]")).click();

        // Accept the confirmation dialog
        driver.switchTo().alert().accept();

        // Verify the credential is deleted
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement credentialTable = driver.findElement(By.id("credentialTable"));
        Assertions.assertFalse(credentialTable.getText().contains("http://todelete.com"));
        Assertions.assertFalse(credentialTable.getText().contains("deleteuser"));
    }
}
