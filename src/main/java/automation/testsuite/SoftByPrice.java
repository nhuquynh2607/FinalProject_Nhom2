package automation.testsuite;

import org.testng.annotations.*;
import automation.common.commonBase;
import automation.pageLocator.ProductPage;

public class SoftByPrice extends commonBase {
    ProductPage productPage;

    @BeforeMethod
    public void openBrowser() {
        driver = initChromeDriver("https://bepantoan.vn/danh-muc/chau-voi-rua-chen-bat");
        productPage = new ProductPage(driver);
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
        productPage.selectPriceFilter(productPage.getTren15Trieu());
        productPage.checkAllPricesAbove(15000000);
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


    @AfterMethod
    public void closeBrowser() {
        quitDriver(driver);
    }
}
