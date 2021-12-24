package ru.netology.rest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDelivery {
    int delay = 15;

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
        open("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void sendIfDataSuccessful() {
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__title").shouldHave(Condition.text("Успешно!"),
                Duration.ofSeconds(delay));
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + inputDate));
    }

}
