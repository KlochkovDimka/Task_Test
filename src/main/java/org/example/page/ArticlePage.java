package org.example.page;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.BaseSeleniumPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Страница найденной статьи
 */
@Slf4j
public class ArticlePage extends BaseSeleniumPage {
    //Список элементов названия статьи
    @FindBy(xpath = "//span[@class='f']")
    private List<WebElement> listSpanNameArticle;

    //Элемент для переключения на фрейм
    @FindBy(xpath = "//div[@id='mainContent']//iframe")
    private WebElement iframeDocument;

    //Строка поиска
    @FindBy(xpath = "//div[@tooltipclass='autofill']")
    private WebElement inputSearch;

    //Кнопка недавних поисковых запросов
    @FindBy(xpath = "//button[@class='pushable showVariants flat']")
    private WebElement buttonPushable;

    //Кнопка оглавления
    @FindBy(xpath = "//button[@class='contents']")
    private WebElement buttonContents;

    //Элемент для начала выделения
    @FindBy(xpath = "//div[@id='p1882']")
    private WebElement beginningOfArticle;

    //Элемент для конца выделения
    @FindBy(xpath = "//div[@id='p1888']")
    private WebElement endOfArticle;

    //Кнопка печати
    @FindBy(xpath = "//button[@class='print']")
    private WebElement buttonPrint;

    //Кнопка редакций
    @FindBy(xpath = "//button[@class='editions']")
    private WebElement buttonEdits;

    //Последний элемент статьи
    @FindBy(xpath = "//div[@id='z155']")
    WebElement endDivArticle;

    public ArticlePage() {
        PageFactory.initElements(BaseSeleniumPage.driver, this);
    }

    // Метод для получения названия статьи
    public String getNameArticle() {
        log.debug("Переключаемся на вкладку со статьей");
        switchWindow();

        log.debug("Переключаемся на фрейм с документом");
        switchIframe(iframeDocument);

        log.debug("Ожидаем пока не появится название статьи");
        wait.until(ExpectedConditions.visibilityOfAllElements(listSpanNameArticle));

        String nameArticle = listSpanNameArticle.stream()
                .map(WebElement::getText)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
        log.debug("Возвращаем название статьи: " + nameArticle);
        return nameArticle;
    }

    //Метод для получения названия вкладки браузера
    public String getTitleValue() {
        String titleName = driver.getTitle().toLowerCase();
        log.debug("Возвращаем название вкладки браузера: " + titleName);
        return titleName;
    }

    //Метод для получения значения из поисковой строки
    public String getValueInSearchInText() {
        switchWindow();

        log.debug("Ожидаем пока кнопка недавних не будет видимой");
        wait.until(ExpectedConditions.visibilityOf(buttonPushable));
        log.debug("Наводим курсором на кнопку недавних запросов и нажимаем на нее");
        actions.moveToElement(buttonPushable).click().perform();
        log.debug("Возвращаем значение из поисковой строки");
        return inputSearch.getText();
    }

    //Метод для перехода на страницу оглавлений
    public ContentsPage openContents() {
        switchWindow();

        log.debug("Ожидаем пока кнопка оглавлений не будет кликабельной и кликаем на нее");
        wait.until(ExpectedConditions.elementToBeClickable(buttonContents)).click();

        log.debug("Переходим на страницу с оглавлением");
        return new ContentsPage();
    }

    //Метод для выделения текста для печати
    public String choiceText() throws IOException, UnsupportedFlavorException {
        switchIframe(iframeDocument);

        log.debug("Ожидаем пока элементы для выделения не станут видимыми");
        wait.until(ExpectedConditions.visibilityOf(beginningOfArticle));
        wait.until(ExpectedConditions.visibilityOf(endOfArticle));

        int OneX = beginningOfArticle.getLocation().getX();
        int OneY = beginningOfArticle.getLocation().getY();
        log.debug("Находим начальные координаты выделения х=" + OneX + " у=" + OneY);

        int endX = endOfArticle.getLocation().getX() + endOfArticle.getSize().getWidth();
        int endY = endOfArticle.getLocation().getY() + endOfArticle.getSize().getHeight();
        log.debug("Находим конечные координаты выделения х=" + endX + " у=" + endY);

        log.debug("Наводим мышку в начало; кликаем и зажимаем; выделяем по заданным координатам; отпускам");
        actions.moveToElement(beginningOfArticle)
                .clickAndHold()
                .moveByOffset(endX - OneX, endY - OneY)
                .release()
                .perform();
        // Копируем выделенный текст
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
        //Получаем доступ к буферу
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // получаем текст из буфера
        String selectedText = (String) clipboard.getData(DataFlavor.stringFlavor);

        log.debug("Переключаемся на дефолтный фрейм");
        driver.switchTo().defaultContent();

        log.debug("Кликаем на кнопку печати");
        buttonPrint.click();

        switchWindow();
        log.debug("Возвращаем текст из буфера " + selectedText);
        return selectedText;
    }

    //Метод для получения страницы редакций
    public EditsPage switchEdits() {
        switchWindow();

        log.debug("Ожидаем пока кнопка редакций не будет кликабельной и кликаем на нее");
        wait.until(ExpectedConditions.elementToBeClickable(buttonEdits)).click();

        log.debug("Переходим на страницу редакций");
        return new EditsPage();
    }

    //Метод получения времени полной загрузки документы
    public Long getTimeOpenArticle() {
        switchWindow();
        //Определяем начальной время
        long startTime = System.currentTimeMillis();
        //Ожидаем последний элемент
        switchIframe(iframeDocument);

        wait.until(ExpectedConditions.elementToBeClickable(endDivArticle));
        //Определяем конечное время загрузки
        long endTime = System.currentTimeMillis();
        //возвращаем результат
        return endTime - startTime;
    }

    //Метод для переключения между вкладками
    private void switchWindow() {
        //Получаю все открытые вкладки
        Set<String> stringSet = driver.getWindowHandles();

        //Получаю вкладку на которой нахожусь
        String origWindow = driver.getWindowHandle();

        //Цикл для переключения на вторую вкладку
        for (String newWindows : stringSet) {
            if (!origWindow.equals(newWindows)) {
                log.debug("Переключаемся на вторую вкладку");
                driver.switchTo().window(newWindows);
                break;
            }
        }
    }

    //Метод для переключения на фрейм
    private void switchIframe(WebElement iframe) {
        log.debug("Ожидаем пока фрейм не появится и переключаемся на него");
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
    }
}
