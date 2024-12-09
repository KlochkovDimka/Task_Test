package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.BaseSeleniumPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Страница оглавлений
 */
@Slf4j
public class ContentsPage extends BaseSeleniumPage {
    //Элемент поиска по оглавлению
    @FindBy(xpath = "//input[@class='x-input__field x-input__field x-search-box__input']")
    private WebElement inputSearch;
    //Элемент 163 статьи
    @FindBy(xpath = "//span[contains(text(), 163)]")
    private WebElement article;

    public ContentsPage() {
        PageFactory.initElements(BaseSeleniumPage.driver, this);
    }

    //Метод получения найденной статьи
    public ArticlePage searchArticle(String valueArticle) {
        log.debug("Ожидаем когда строка поиска станет кликабельной и вводим " + valueArticle
                + " и нажимаем ENTER");
        wait.until(ExpectedConditions.elementToBeClickable(inputSearch))
                .sendKeys(valueArticle, Keys.ENTER);

        log.debug("Ожидаем пока статья не станет кликабельной и нажимаем на статью");
        wait.until(ExpectedConditions.visibilityOf(article)).click();

        log.debug("Возвращаем найденную статью");
        return new ArticlePage();
    }


}
