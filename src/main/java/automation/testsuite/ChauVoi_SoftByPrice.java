package automation.testsuite;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import automation.common.commonBase;
import automation.pageLocator.ChauVoi_ProductPage;

public class ChauVoi_SoftByPrice extends commonBase {
    ChauVoi_ProductPage productPage;
    WebDriverWait wait;

    @BeforeMethod
    public void openBrowser() {
        driver = initChromeDriver("https://bepantoan.vn/danh-muc/chau-voi-rua-chen-bat");
        productPage = new ChauVoi_ProductPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testDuoi3Trieu() {
        productPage.selectPriceFilter(productPage.getDuoi3Trieu());
        productPage.checkAllPricesUnder(3000000);
    }

    @Test
    public void test3To5Trieu() {
        productPage.selectPriceFilter(productPage.getTu3Den5Trieu());
        productPage.checkAllPricesInRange(3000000, 5000000);
    }

    @Test
    public void test5To10Trieu() {
        productPage.selectPriceFilter(productPage.getTu5Den10Trieu());
        productPage.checkAllPricesInRange(5000000, 10000000);
    }

    @Test
    public void test10To15Trieu() {
        productPage.selectPriceFilter(productPage.getTu10Den15Trieu());
        productPage.checkAllPricesInRange(10000000, 15000000);
    }

    @Test
    public void testTren15Trieu() {
        productPage.selectPriceOver15Trieu();
    }

    @Test
    public void testSapXepGiamGiaNhieu() {
        productPage.clickGiamGiaNhieu();
        productPage.checkDiscountSortedDescending();
    }

    @Test
    public void testSapXepGiaThap() {
        productPage.clickGiaThap();
        productPage.checkPriceSortedAscending();
    }

    @Test
    public void testSapXepGiaCao() {
        productPage.clickGiaCao();
        productPage.checkPriceSortedDescending();
    }

    @Test
    public void testPriceAndBrand() {
        productPage.clickBrandKainer();
        productPage.selectPriceFilter(productPage.getTu3Den5Trieu());
        wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//a[contains(@class,'product-card')]")
                )
        ));
        List<String> products = productPage.getProductNames();
        for (String p : products) {
            assertTrue(p.toLowerCase().contains("kainer"),
                    "❌ Sản phẩm không thuộc thương hiệu Kainer: " + p);
        }
        productPage.checkAllPricesInRange(3000000, 5000000);
    }

    @Test
    public void testPriceAndOrigin() {
        productPage.selectPriceOver15Trieu();
        productPage.clickOrigin("thuy-sy7g6X-1254");
        List<WebElement> products = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//a[contains(@class,'product-card')]")
                )
        ));

        if (products.isEmpty()) {
            throw new AssertionError("❌ Không tìm thấy sản phẩm nào có xuất xứ Thụy Sỹ với giá ≥ 15 triệu!");
        }
        productPage.checkAllPricesAboveOrEqual(15000000);
        productPage.clickFirstProduct();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Thụy Sỹ')]")
        ));
        assertTrue(productPage.checkMaterialDetail("Thụy Sỹ"),
                "❌ Chi tiết sản phẩm không hiển thị xuất xứ Thụy Sỹ!");
    }

    @Test
    public void testPriceAndMaterial() {
        productPage.selectPriceFilter(productPage.getTu3Den5Trieu());
        productPage.clickMaterial("inox-304mDvo-1224");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@class,'product-card')]")
        ));
        productPage.checkAllPricesInRange(3000000, 5000000);
        productPage.clickFirstProduct();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Inox 304')]")
        ));
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
