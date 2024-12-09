import lombok.extern.slf4j.Slf4j;
import org.example.page.ArticlePage;
import org.example.page.EditsPage;
import org.example.page.MainPage;
import org.example.utils.BaseSeleniumTest;
import org.example.utils.Listener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(Listener.class)
public class MainTest extends BaseSeleniumTest {

    private final String URL = System.getProperty("URL");

    @Test
    public void searchDocTest() {
        log.debug("Тест поиска документа");

        ArticlePage articlePage = getArticlePage();

        String list = articlePage.getNameArticle();
        log.debug("Получение заголовка документа: " + list);

        log.debug("Проверка наличия слов \"налоговый кодекс\" в заголовке в тексте открытого документа");
        assertTrue(list.contains("налоговый кодекс"));

        log.debug("Проверка наличия слов \"часть вторая\" в заголовке в тексте открытого документа");
        assertTrue(list.contains("часть вторая"));

        String title = articlePage.getTitleValue();
        log.debug("Получение названия вкладки браузера: " + list);

        log.debug("Проверка наличия слов \"налоговый кодекс\" в названии вкладки браузера");
        assertTrue(title.contains("налоговый кодекс"));

        log.debug("Проверка наличия слов \"часть вторая\" в названии вкладки браузера");
        assertTrue(title.contains("часть вторая"));
    }

    @Test
    public void checkedPanelSearchInTextTest() {
        log.debug("Тест: проверка, что в панели «Поиск в тексте» находится поисковая фраза \"нк ч2\"");

        String valueSearch = getArticlePage().getValueInSearchInText();
        log.debug("Получение слова в панели поиска: " + valueSearch);

        log.debug("Сравнение слова " + valueSearch + "с \"нк ч2\"");
        assertEquals(valueSearch, "нк ч2");
    }

    @Test
    public void printArticleTest() throws IOException, UnsupportedFlavorException {
        log.debug("Тест: проверка, что страница печати содержит только тот текст, что был выделен в документе.");

        String text = getArticlePage()
                .openContents()
                .searchArticle("статья 163")
                .choiceText();

    }

    @Test
    public void checkedChangesEditsTest() {
        log.debug("Тест: проверка наличия редакций с изменениями и дат");

        EditsPage editsPage = getArticlePage().switchEdits();
        boolean result = editsPage.isChangesEditsNotJoined();

        assertTrue(result);

        editsPage
                .getListData()
                .forEach(localDate -> assertTrue(localDate.isAfter(LocalDate.now())));
    }

    @Test
    public void timeOpenArticleTest() {
        log.debug("Проверка времени загрузки документа");
        MainPage mainPage = new MainPage();
        long resultTime = mainPage.inputSearchDocuments("нк ч2").getOne().getTimeOpenArticle();
        log.debug("Время загрузки документа: " + resultTime);
        assertTrue(resultTime < 10000);
    }

    private ArticlePage getArticlePage() {
        MainPage mainPage = new MainPage();
        log.debug("Поиск и получение первого документа по фразе «нк ч2»");
        return mainPage.inputSearchDocuments("нк ч2").getOne();
    }
}
