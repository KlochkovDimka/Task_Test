package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.BaseSeleniumPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Страница найденных документов
 */
@Slf4j
public class FoundPage extends BaseSeleniumPage {
    // Элемент первой статьи
    @FindBy(xpath = "//div[@data-index='0']")
    private WebElement article;

    public FoundPage() {
        PageFactory.initElements(BaseSeleniumPage.driver, this);
    }

    //Метод получения статьи
    public ArticlePage getOne() {
        log.debug("Ожидаем пока первая статья не станет кликабельной и кликаем на нее");
        wait.until(ExpectedConditions.elementToBeClickable(article)).click();

        log.debug("Переходим на страницу с статьей");
        return new ArticlePage();
    }

}
