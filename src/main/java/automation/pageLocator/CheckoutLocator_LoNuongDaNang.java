package automation.pageLocator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutLocator_LoNuongDaNang {
    private WebDriver driver;

    //Page mua ngay
    @FindBy(xpath = "//input[@placeholder='Nhập họ và tên']") WebElement textPhoneNumber;
    @FindBy(xpath = "//input[@placeholder='Nhập số điện thoại']") WebElement textName;
    @FindBy(xpath = "//input[@placeholder='Nhập số nhà, tên đường, phường/ xã, quận/huyện, tỉnh/ thành phố']") WebElement textAddress;
    @FindBy(xpath = "(//span[text() ='Thanh toán'])") WebElement buttonCheckout;

    //Page mua trả góp
    @FindBy(xpath = "(//input[@placeholder='Nhập họ và tên'])[2]") WebElement txtPhoneNumber;
    @FindBy(xpath = "(//input[@placeholder='Nhập số điện thoại'])[2]") WebElement txtName;
    @FindBy(xpath = "(//input[@placeholder='Nhập địa chỉ'])[2]") WebElement txtAddress;
    @FindBy(xpath = "(//span[text()='Nhận tư vấn'])[2]") WebElement buttonCheckoutInstallment;

    //Tiêu đề popup mua trả góp
    @FindBy(xpath = "//h3[contains(text(),'Đăng kí mua trả góp')]") WebElement popupTitle;

    //Nhập thông tin không đúng Mua ngay
    @FindBy(xpath = "//*[contains(text(),'Số điện thoại không hợp lệ')]") WebElement msgPhoneError;

    //Nhập thông tin không đúng popup Mua trả góp
    @FindBy(xpath = "(//*[contains(text(),'Số điện thoại không hợp lệ')])[2]") WebElement getMsgPhoneError2;

    public CheckoutLocator_LoNuongDaNang(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void checkoutFunction(String name, String number, String address)
    {
        textName.sendKeys(name);
        textPhoneNumber.sendKeys(number);
        textAddress.sendKeys(address);
        buttonCheckout.click();
    }

    public void chekoutInstallmentFunction(String name, String number, String address)
    {
        if (!popupTitle.isDisplayed()) {
            throw new RuntimeException("Popup Đăng kí mua trả góp không hiển thị!");
        }

        txtName.sendKeys(name);
        txtPhoneNumber.sendKeys(number);
        txtAddress.sendKeys(address);
        buttonCheckoutInstallment.click();
    }
}
