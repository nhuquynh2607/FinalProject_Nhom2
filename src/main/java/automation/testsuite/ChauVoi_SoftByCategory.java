package automation.testsuite;

import static org.testng.Assert.assertTrue;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import automation.common.commonBase;
import automation.pageLocator.ChauVoi_ProductPage;

import java.time.Duration;

public class ChauVoi_SoftByCategory extends commonBase {
    WebDriverWait wait;
    ChauVoi_ProductPage productPage;

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://bepantoan.vn/danh-muc/chau-voi-rua-chen-bat");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        productPage = new ChauVoi_ProductPage(driver);
    }

    @Test
    public void testCategoryChau() {
        WebElement linkChau = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href='/danh-muc/chau-rua-chen-bat']")));
        linkChau.click();
        wait.until(ExpectedConditions.urlContains("/danh-muc/chau-rua-chen-bat"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("a.product-card h4")));

        List<WebElement> productNames = driver.findElements(
                By.cssSelector("a.product-card h4"));
        for (WebElement name : productNames) {
            String text = name.getText().toLowerCase();
            assertTrue(text.contains("chậu"),
                    "Tên sản phẩm không chứa 'chậu': " + text);
        }
    }

    @Test
    public void testCategoryVoi() {
        WebElement linkVoi = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href='/danh-muc/voi-rua-chen-bat']")));
        linkVoi.click();
        wait.until(ExpectedConditions.urlContains("/danh-muc/voi-rua-chen-bat"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("a.product-card h4")));

        List<WebElement> productNames = driver.findElements(
                By.cssSelector("a.product-card h4"));
        for (WebElement name : productNames) {
            String text = name.getText().toLowerCase();
            assertTrue(text.contains("vòi"),
                    "Tên sản phẩm không chứa 'vòi': " + text);
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
