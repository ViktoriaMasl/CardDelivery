package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    String setMeetingDate(int daysFromToday) {
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar dataToday = Calendar.getInstance();
        dataToday.add(Calendar.DAY_OF_YEAR, daysFromToday);
        return dateFormat.format(dataToday.getTime());
    }


    @BeforeEach
            void openLocalhost() {
        open("http://localhost:9999");
    }


    @Test
    void shouldSendValidData() {
        $("[data-test-id=\"city\"] input").setValue("Челябинск");
        $("[data-test-id=\"date\"] input").setValue(setMeetingDate(3));
        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldSelectCityFromTheList() {
        $("[data-test-id=\"city\"] input").setValue("Че");
        $$(".menu-item").find(exactText("Челябинск")).click();
        $("[data-test-id=\"date\"] input").setValue(setMeetingDate(3));
        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldSelectDataFromTheCalendar() {
        $("[data-test-id=\"city\"] input").setValue("Челябинск");
        $("[placeholder=\"Дата встречи\"]").setValue(setMeetingDate(0));     //удаляем дату из строки
        $("[placeholder=\"Дата встречи\"]").click();                         //открываем календарь
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.DOWN, Keys.DOWN);  //опускаемся на 1 ячейку вниз (на неделю позже)
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.ENTER);
        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }
}


