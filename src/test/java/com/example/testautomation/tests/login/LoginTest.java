package com.example.testautomation.tests.login;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.example.testautomation.elements.Elements;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private static String loginUrl = "https://jira-auto.codecool.metastage.net/login.jsp";

    @BeforeAll
    public static void setUpAll() {
        Configuration.startMaximized = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        Selenide.open(loginUrl);
    }

    @Test
    @Order(1)
    public void pageIsOpen() { // PASS 3/3
        SelenideElement login = Elements.LOGIN_BUTTON.getElement();
        login.scrollIntoView(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(login.isDisplayed());
    }

    @Test
    @Order(2)
    public void successfulLogin() { // PASS 3/3
        fillFormWithValidData();
        Elements.LOGIN_BUTTON.getElement().click();
        validateLogin();
    }

    @Test
    @Order(2)
    public void loginWithSecondaryForm() { // PASS 2/4
        String secondaryFormUrl = "https://jira-auto.codecool.metastage.net/secure/Dashboard.jspa";
        Selenide.open(secondaryFormUrl);
        SelenideElement login = Elements.OTHER_LOGIN_BUTTON.getElement();
        assertTrue(login.isDisplayed());

        fillFormWithValidData();
        login.click();
        validateLogin();
    }

    @Test
    @Order(3)
    public void emptyPassword() { // PASS 3/3
        fillFormWithValidData();
        Elements.LOGIN_PASSWORD.getElement().clear();
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        Elements.LOGIN_BUTTON.getElement().click();
        validateUnsuccessfulLogin(currentUrl);
    }

    @Test
    @Order(3)
    public void wrongPassword() { // PASS 2/4
        String wrongPassword = "1";
        fillFormWithValidData();
        Elements.LOGIN_PASSWORD.getElement().clear();
        Elements.LOGIN_PASSWORD.getElement().sendKeys(wrongPassword);
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        Elements.LOGIN_BUTTON.getElement().click();
        validateUnsuccessfulLogin(currentUrl);
    }

    private void fillFormWithValidData() {
        String validUsername = System.getenv("USERNAME");
        String validPassword = System.getenv("PASSWORD");
        Elements.LOGIN_USERNAME.getElement().sendKeys(validUsername);
        Elements.LOGIN_PASSWORD.getElement().sendKeys(validPassword);
    }

    private void validateUnsuccessfulLogin(String loginPage) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(Elements.PROFILE_BUTTON.getElement().isDisplayed());
        String profilePage = "https://jira-auto.codecool.metastage.net/secure/Tests.jspa#/testCase/MTP-T417";
        Selenide.open(profilePage);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertTrue(currentUrl.startsWith(loginPage));
        fillFormWithValidData();
        Elements.LOGIN_BUTTON.getElement().click();
        logOut();
    }

    private void validateLogin() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(Elements.PROFILE_BUTTON.getElement().isDisplayed());
        Elements.PROFILE_BUTTON.getElement().click();
        assertTrue(Elements.LOGOUT_OPTION.getElement().isDisplayed());

        Elements.PROFILE_OPTION.getElement().click();
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        String expectedUrl = "https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa";
        assertEquals(expectedUrl, currentUrl);

        String currentUserName = Elements.PROFILE_USERNAME.getElement().getText();
        assertEquals(System.getenv("USERNAME"), currentUserName);
        logOut();
    }

    private void logOut() {
        Elements.PROFILE_BUTTON.getElement().click();
        Elements.LOGOUT_OPTION.getElement().click();
        openPage();
    }
}
