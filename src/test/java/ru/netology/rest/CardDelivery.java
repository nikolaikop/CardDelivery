package ru.netology.rest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDelivery {
    int delay = 15;

    @NotNull
    private String when(boolean chose) {
        Calendar cl = new GregorianCalendar();
        cl.add(Calendar.DATE, 7);
        if (chose) {
            return new SimpleDateFormat("d").format(cl.getTime());
        } else {
            return new SimpleDateFormat("dd.MM.yyyy").format(cl.getTime());
        }
    }

    @BeforeEach
    void setUp() {

        open("http://localhost:9999/");
    }

    @Test
    void sendIfDataSuccessful (){
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

    @Test
    void shouldntSendWithoutCity() {
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldntSendWithForeignSymbols() {
        $("[placeholder=Город]").setValue("Saint-Petersburg");
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldntSendWithoutName() {
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldntSendWithoutPhone (){
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldntSendWithoutCheckBox (){
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        String inputDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        data.$("[placeholder]").setValue(inputDate);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldntSendWithoutDate() {
        $("[placeholder=Город]").setValue("Санкт-Петербург");
        SelenideElement data = $("[data-test-id=date]");
        data.$("[value]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name].input_type_text .input__control").setValue("Николай Николаевич");
        $("[data-test-id=phone]").$("[name=phone]").setValue("+71234567890");
        $("[class=checkbox__box]").click();
        $$("[class=button__text]").find(exactText("Забронировать")).click();
        $("[data-test-id=date] .input__sub").shouldHave
                (exactTextCaseSensitive("Неверно введена дата"));
    }

    @Test
    void shouldSubmitTask2() {
        $("[data-test-id=city] .input__control").setValue("Ха");
        $$(".menu-item__control").findBy(text("Хабаровск")).click();
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, when(false), Keys.CONTROL + "A", Keys.DELETE);
        $$(".calendar__day").findBy(text(when(true))).click();
        $("[data-test-id=name] [name=name]").setValue("Николай Николаевич");
        $("[data-test-id=phone] [name=phone]").setValue("+71234567890");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(delay)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + when(false)));
    }

}
