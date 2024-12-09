package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.BaseSeleniumPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Главная страница
 */
@Slf4j
public class MainPage extends BaseSeleniumPage {
    // Элемент строка поиска
    @FindBy(xpath = "//input[@class='x-input__field x-input__field x-search-box__input']")
    private WebElement inputSearch;

    public MainPage() {
        PageFactory.initElements(BaseSeleniumPage.driver, this);
        BaseSeleniumPage.driver.get("http://base.consultant.ru/cons/");
    }

    //Метод для перехода на найденную страницу
    public FoundPage inputSearchDocuments(String value) {
        log.debug("Ожидаем пока строка поиска будет кликабельной и кликаем на нее");
        wait.until(ExpectedConditions.elementToBeClickable(inputSearch)).click();
        //Ввод значения для поиска
        log.debug("Вводим значение " + value + " в поисковую строку и нажимаем ENTER");
        inputSearch.sendKeys(value, Keys.ENTER);

        log.debug("Получаем найденную страницу");
        return new FoundPage();
    }
}
