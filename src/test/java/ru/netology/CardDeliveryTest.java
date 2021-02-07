package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @Test
    void shouldSendValidData() {
        open("http://localhost:9999");
        $("[data-test-id=\"city\"] input").setValue("Челябинск");
        LocalDate dateDelivery = LocalDate.now().plusDays(3);
        String inputDate = dateDelivery.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=\"date\"] input").setValue(inputDate);
        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldSelectCityFromTheList() {
        open("http://localhost:9999");
        $("[data-test-id=\"city\"] input").setValue("Че");
        $$(".menu-item").find(exactText("Челябинск")).click();
        LocalDate dateDelivery = LocalDate.now().plusDays(3);
        String inputDate = dateDelivery.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=\"date\"] input").setValue(inputDate);
        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldSelectDataFromTheCalendar() {
        open("http://localhost:9999");

        $("[data-test-id=\"city\"] input").setValue("Челябинск");

        SelenideElement calendar_button = $(".input__icon");
        calendar_button.click();

        ElementsCollection calendar_rows = $$(".calendar__row");
        int total_rows = calendar_rows.size();

        // на какой строке выбранная дата находится
        int current_date_row = -1;
        int current_date_column = -1;

        for (int i = 0; i < calendar_rows.size(); i++) {
            ElementsCollection cells = calendar_rows.get(i).findAll("td");
            for (int j = 0; j < cells.size(); j++) {
                if (cells.get(j).has(Condition.cssClass("calendar__day_state_current"))) {
                    current_date_row = i;
                    current_date_column = j;
                    break;
                }
            }
        }
        ElementsCollection cells = calendar_rows.get(current_date_row + 1).findAll("td");

        if (current_date_row + 1 < total_rows) {
            if (!cells.get(current_date_column).getOwnText().equals("")) {
                cells.get(current_date_column).click();
            } else {
                $("[data-step='1']").click();
                $$(".calendar__row").get(1).findAll("td").get(current_date_column).click();
            }

        } else {
            $("[data-step='1']").click();
            cells = $$(".calendar__row").get(1).findAll("td");
            if (!cells.get(current_date_column).getOwnText().equals("")) {
                cells.get(current_date_column).click();
            } else {
                $$(".calendar__row").get(2).findAll("td").get(current_date_column).click();
            }
        }

        $("[data-test-id=\"name\"] input").setValue("Виктория");
        $("[data-test-id=\"phone\"] input").setValue("+77777777777");
        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofMillis(15000));
    }
}


