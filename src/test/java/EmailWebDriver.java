import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class EmailWebDriver extends BaseTest {

    @Test
    public void loginToEmail() {

        driver.get("https://mail.ru");
//      new WebDriverWait(driver,10).until(PageCondition.jQueryAJAXCompleted());

        WebElement inputEmailField = waitForElementLocatedBy(driver, By.xpath("//*[@id='mailbox:login']"));
        inputEmailField.sendKeys("aliaksandr.yarkiy@mail.ru");

        WebElement inputPasswordButton = driver.findElement(By.cssSelector("input.o-control"));
        inputPasswordButton.click();

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
        WebElement inputPasswordField = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.xpath("//*[@id='mailbox:password']"));
            }
        });

        inputPasswordField.sendKeys("obuchenie2015");
        inputPasswordButton.click();

        WebElement exitButton = waitForElementLocatedBy(driver, By.xpath("//*[@id='PH_logoutLink']"));
        Assert.assertFalse(exitButton == null,"Page was not loaded");

        WebElement composeButton = waitForElementLocatedBy(driver, By.xpath("//*[@class='compose-button compose-button_white compose-button_short js-shortcut']"));
        composeButton.click();

        WebElement toField = waitForElementLocatedBy(driver, By.xpath("//div[@class='container--ItIg4 size_s--2eBQT size_s--3_M-_']//input[@class='container--H9L5q size_s--3_M-_']"));
        toField.sendKeys("aliaksandr.yarkiy@mail.ru");

        WebElement subjectField = driver.findElement(By.xpath("//div[@class='container--3QXHv']//input[@class='container--H9L5q size_s--3_M-_']"));
        subjectField.sendKeys("WebDriver");

        WebElement bodyField = driver.findElement(By.xpath("//div[contains (@class, 'cke_editable cke_editable_inline cke_contents_true cke_show_borders')]/div[1]"));
        bodyField.sendKeys("Text for test");

        WebElement saveButton = driver.findElement(By.xpath("//span[@class='button2 button2_base button2_hover-support js-shortcut']"));
        saveButton.click();

        WebElement closeButton = waitForElementLocatedBy(driver, By.xpath("//button[@title='Закрыть' and @class='container--2lPGK type_base--rkphf color_base--hO-yz']"));
        closeButton.click();

        WebElement draftsButton = waitForElementLocatedBy(driver, By.xpath("//a[@href='/drafts/']"));
        draftsButton.click();

        WebElement draftName = waitForElementLocatedBy(driver, By.xpath("//span[@class='ll-sj__normal' and text()='WebDriver']"));
        Assert.assertFalse(draftName == null,"Email was not saved in drafts folder");

        WebElement draftLink = waitForElementLocatedBy(driver, By.cssSelector("span[title='aliaksandr.yarkiy@mail.ru'"));
        draftLink.click();

        WebElement toFieldDraft = waitForElementLocatedBy(driver, By.xpath("//div[contains (@class, 'container--3B3Lm status_base--wsRcM')]//span[contains (@class,'text')]"));
        Assert.assertEquals(toFieldDraft.getText(), "aliaksandr.yarkiy@mail.ru", "Incorrect Email");

        WebElement subjectFieldDraft = driver.findElement(By.xpath("//div[@class='container--3QXHv']//input[@class='container--H9L5q size_s--3_M-_']"));
        Assert.assertEquals(subjectFieldDraft.getAttribute("value"), "WebDriver", "Incorrect Subject in the Draft");

        WebElement bodyFieldDraft = driver.findElement(By.xpath("//div[contains (@class,'class')]/div[1]"));
        Assert.assertEquals(bodyFieldDraft.getText(), "Text for test", "Incorrect Text in Body");

        WebElement sendButton = waitForElementLocatedBy(driver, By.xpath("//span[text()='Отправить']"));
        sendButton.click();

        WebElement closeDraftsButton = waitForElementLocatedBy(driver, By.xpath("//span[@class='button2 button2_has-ico button2_close button2_pure button2_clean button2_short button2_hover-support']"));
        closeDraftsButton.click();

        WebElement draftPageLoading = waitForElementLocatedBy(driver, By.xpath("//div[@title='Черновики']"));
        List<WebElement> draftNameAbsence = driver.findElements(By.xpath("//span[@class='octopus__title' and text()='У вас нет незаконченных']"));
        Assert.assertTrue(draftNameAbsence.isEmpty(),"Draft was not sent");

        WebElement sentFolderButton = driver.findElement(By.xpath("//a[@href='/sent/']"));
        sentFolderButton.click();

        WebElement sentEmailTitle = waitForElementLocatedBy(driver, By.xpath("//span[@class='ll-sj__normal' and text()='Self: WebDriver']"));
        Assert.assertEquals(sentEmailTitle.getText(), "Self: WebDriver","Email is not in sent folder");

        WebElement logOutButton = driver.findElement(By.xpath("//a[@title='выход']"));
        logOutButton.click();

    }


    private static WebElement waitForElementLocatedBy(WebDriver driver, By by) {
        return new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated((by)));
    }

}
