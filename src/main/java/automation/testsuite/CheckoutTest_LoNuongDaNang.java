package automation.testsuite;

import automation.common.commonBase;
import automation.constant.CT_PageURL;
import automation.pageLocator.CheckoutLocator_LoNuongDaNang;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class CheckoutTest_LoNuongDaNang extends commonBase {
    WebDriverWait wait;
    CheckoutLocator_LoNuongDaNang checkout;

    @BeforeMethod
    @Parameters("browser")
    public void openBrowser(String browser)
    {
        driver = setupDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://bepantoan.vn/lo-nuong-eurosun-eov65me");
        checkout = new CheckoutLocator_LoNuongDaNang(driver);
    }

    //Mua ngay nhập thông tin để trống số điện thoại
    @Test
    public void checkoutWithoutNumberPhone(){
        WebElement btnMuaNgay = driver.findElement(By.xpath("(//span[text()='Mua ngay'])[1]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnMuaNgay);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMuaNgay);

        checkout.checkoutFunction("Quỳnh", " ","Đường Đông Bắc");
        WebElement msgPhoneError = driver.findElement(By.xpath("//*[contains(text(),'Số điện thoại không hợp lệ')]"));

        Assert.assertTrue(msgPhoneError.isDisplayed(),
                "Không hiển thị thông báo 'Số điện thoại không hợp lệ'");
        closeDriver();
    }

    //Mua ngay nhập thông tin sai format số điện thoại
    @Test
    public void checkoutErrorNumberPhone(){
        WebElement btnMuaNgay = driver.findElement(By.xpath("(//span[text()='Mua ngay'])[1]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnMuaNgay);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMuaNgay);

        checkout.checkoutFunction("Quỳnh", "123","Đường Đông Bắc");
        WebElement msgPhoneError = driver.findElement(By.xpath("//*[contains(text(),'Số điện thoại không hợp lệ')]"));

        Assert.assertTrue(msgPhoneError.isDisplayed(),
                "Không hiển thị thông báo 'Số điện thoại không hợp lệ'");
        closeDriver();
    }

    //Mua trả góp nhập thông tin để trống số điện thoại
    @Test
    public void chekoutInstallmentWithoutNumberPhone(){
        WebElement btnMuaTraGop = driver.findElement(By.xpath("(//span[text()='Mua trả góp'])[1]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnMuaTraGop);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMuaTraGop);

        checkout.chekoutInstallmentFunction("Quỳnh", " ","Đường Đông Bắc");
        WebElement msgPhoneError = driver.findElement(By.xpath("(//*[contains(text(),'Số điện thoại không hợp lệ')])[2]"));

        Assert.assertTrue(msgPhoneError.isDisplayed(),
                "Không hiển thị thông báo 'Số điện thoại không hợp lệ'");
        closeDriver();
    }

    //Mua trả góp nhập thông tin sai format số điện thoại
    @Test
    public void checkoutInstallmentErrorNumberPhone(){
        WebElement btnMuaTraGop = driver.findElement(By.xpath("(//span[text()='Mua trả góp'])[1]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnMuaTraGop);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMuaTraGop);

        checkout.chekoutInstallmentFunction("Quỳnh", "123","Đường Đông Bắc");
        WebElement msgPhoneError = driver.findElement(By.xpath("(//*[contains(text(),'Số điện thoại không hợp lệ')])[2]"));

        Assert.assertTrue(msgPhoneError.isDisplayed(),
                "Không hiển thị thông báo 'Số điện thoại không hợp lệ'");
        closeDriver();
    }
}
