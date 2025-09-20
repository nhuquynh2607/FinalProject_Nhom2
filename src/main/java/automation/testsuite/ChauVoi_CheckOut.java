package automation.testsuite;

import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import automation.common.commonBase;
import org.testng.annotations.Test;
import automation.pageLocator.ChauVoi_ProductPage;

public class ChauVoi_CheckOut extends commonBase {
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
    public void checkOut() {
    	productPage.selectProduct();
    	productPage.clickBuyNow();
    	productPage.fillCustomerInfo("Nguyen Van A", "123abc", "123 Khâm Thiên , Hà Nội");
    	productPage.chooseCODPayment();
    	productPage.clickCheckout();
    }
}
