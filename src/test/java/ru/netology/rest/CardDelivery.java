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

    String getFormattedDate(int add7Days, String patternOfDate) {
        return LocalDate.now().plusDays(add7Days).format(DateTimeFormatter.ofPattern(patternOfDate));
    }

    int yearOffset(int add7Days) {
        return LocalDate.now().plusDays(add7Days).getYear() - LocalDate.now().plusDays(3).getYear();
    }

    int monthOffset(int add7Days) {
        return LocalDate.now().plusDays(add7Days).getMonthValue() - LocalDate.now().plusDays(3).getMonthValue();
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
        int add7Days = 7;
        $("[data-test-id=city] .input__control").setValue("Ха");
        $$(".menu-item__control").findBy(text("Хабаровск")).click();
        $(".icon-button__text>.icon_name_calendar").click();
        if (yearOffset(add7Days = 7) > 0) {
            for (int i = 0; i < yearOffset(add7Days = 7); i++) {
                $(".calendar__arrow_direction_right[data-step='12']").click();
            }
            if (monthOffset(add7Days = 7) > 0) {
                for (int i = 0; i < monthOffset(add7Days = 7); i++) {
                    $(".calendar__arrow_direction_right[data-step='1']").click();
                }
            } else {
                for (int i = 0; i > monthOffset(add7Days = 7); i--) {
                    $(".calendar__arrow_direction_left[data-step='-1']").click();
                }
            }
        } else {
            for (int i = 0; i < monthOffset(add7Days = 7); i++) {
                $(".calendar__arrow_direction_right[data-step='1']").click();
            }
        }
        $$(".calendar__day").findBy(text(LocalDate.now().plusDays(add7Days = 7).format(DateTimeFormatter.ofPattern("d")))).click();
        $("[data-test-id=name] [name=name]").setValue("Николай Николаевич");
        $("[data-test-id=phone] [name=phone]").setValue("+71234567890");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(delay)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + getFormattedDate(add7Days, "dd.MM.yyyy")));
    }

}
