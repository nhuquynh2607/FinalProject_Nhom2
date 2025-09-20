package automation.testsuite;

import automation.common.commonBase;
import automation.pageLocator.ChauVoi_ProductPage;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChauVoi_SoftByOrigin extends commonBase {
    ChauVoi_ProductPage productPage;
    WebDriverWait wait;

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://bepantoan.vn/danh-muc/chau-voi-rua-chen-bat");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        productPage = new ChauVoi_ProductPage(driver);
    }

    @Test
    public void locTheoXuatXu_ThuySy() {
        productPage.clickOrigin("thuy-sy7g6X-1254");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//a[contains(@class,'product-card')]")
        ));
        List<String> srcs = productPage.getProductBrandSrcs();
        for (String src : srcs) {
            assertTrue(src.contains("Franke"),
                "Sản phẩm không thuộc xuất xứ Thụy Sỹ (logo không đúng): " + src);
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
