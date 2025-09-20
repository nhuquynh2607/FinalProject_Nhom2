package automation.testsuite;

import automation.common.commonBase;
import automation.pageLocator.ChauVoi_ProductPage;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class ChauVoi_SoftByMaterial extends commonBase {
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
    public void locTheoChatLieu_Inox304() {
        productPage.clickMaterial("inox-304mDvo-1224");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-card')]")
        ));
        productPage.clickFirstProduct();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Inox 304')]")
        ));
        assertTrue(productPage.checkMaterialDetail("Inox 304"),
                "Chi tiết sản phẩm không hiển thị chất liệu Inox 304!");
    }
    @Test
    public void testOriginAndMaterial() {

        productPage.clickOrigin("thuy-sy7g6X-1254");
        productPage.clickMaterial("inox-304mDvo-1224");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-card')]")
        ));
        productPage.clickFirstProduct();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Inox 304')]")
        ));
        assertTrue(productPage.checkMaterialDetail("Thụy Sỹ"),
                "❌ Chi tiết sản phẩm không hiển thị xuất xứ Thụy Sỹ!");
        assertTrue(productPage.checkMaterialDetail("Inox 304"),
                "❌ Chi tiết sản phẩm không hiển thị chất liệu Inox 304!");
    }
    @AfterMethod
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}