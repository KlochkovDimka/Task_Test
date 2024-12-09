package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.BaseSeleniumPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Страница редакций
 */
@Slf4j
public class EditsPage extends BaseSeleniumPage {
    //Список элементов с изменениями не вступивших в силу
    @FindBy(xpath = "//tbody//div[contains(text(), 'с изменениями, не вступившими в силу')]" +
            "/parent::*/parent::*/following-sibling::*//div[@class='redDate']")
    private List<WebElement> listChangesEdits;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    public EditsPage() {
        PageFactory.initElements(BaseSeleniumPage.driver, this);
    }

    //Метод для проверки наличия изменений не вступивших в силу
    public boolean isChangesEditsNotJoined() {
        log.debug("Ожидаем пока список элементов не станет видимым");
        wait.until(ExpectedConditions.visibilityOfAllElements(listChangesEdits));

        log.debug("Возвращаем булевое значение пустой список или нет");
        return !listChangesEdits.isEmpty();
    }

    //Метод возвращающий список дат
    public List<LocalDate> getListData() {
        List<LocalDate> dateList = listChangesEdits.stream()
                .map(WebElement::getText)
                .map(s -> s.substring(s.length() - 11))
                .map(s -> LocalDate.parse(s, formatter))
                .toList();

        log.debug("Возвращаем список дат :" + dateList);

        return dateList;
    }
}
