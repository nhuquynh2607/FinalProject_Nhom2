package automation.testsuite;

import automation.common.commonBase;
import automation.pageLocator.ChauVoi_ProductPage;
import org.testng.annotations.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChauVoi_SoftByBrand extends commonBase {
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
    public void locTheoHangKainer() throws InterruptedException {
        productPage.clickBrandKainer();
        Thread.sleep(5000); // có thể thay bằng WebDriverWait

        List<String> brands = productPage.getProductBrands();

        for (String b : brands) {
            assertTrue(b.contains("kainer"),
                    "Sản phẩm không thuộc hãng Kainer: " + b);
        }
    }

    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
