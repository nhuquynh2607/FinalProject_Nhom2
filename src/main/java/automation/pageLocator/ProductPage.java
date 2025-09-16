package automation.pageLocator;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class ProductPage {
    WebDriver driver;
    WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Filter mức giá
    @FindBy(xpath = "//span[contains(text(),'< 3.000.000')]")
    WebElement duoi3Trieu;

    @FindBy(xpath = "//span[contains(text(),'3.000.000 > 5.000.000')]")
    WebElement tu3Den5Trieu;

    @FindBy(xpath = "//span[contains(text(),'5.000.000 > 10.000.000')]")
    WebElement tu5Den10Trieu;

    @FindBy(xpath = "//span[contains(text(),'10.000.000 > 15.000.000')]")
    WebElement tu10Den15Trieu;

    @FindBy(xpath = "//span[contains(text(),'> 15.000.000')]")
    WebElement tren15Trieu;

    // Giá sản phẩm hiển thị
    @FindBy(xpath = "//span[contains(@class,'sale-price')]")
    List<WebElement> productPrices;

    // Click chọn filter
    public void selectPriceFilter(WebElement priceElement) {
        wait.until(ExpectedConditions.elementToBeClickable(priceElement)).click();
        wait.until(ExpectedConditions.visibilityOfAllElements(productPrices));
    }

    // Hàm kiểm tra tất cả giá trong khoảng
    public void checkAllPricesInRange(int min, int max) {
        for (WebElement priceElement : productPrices) {
            String priceText = priceElement.getText()
                    .replace(".", "")
                    .replace("₫", "")
                    .replace("\u00A0", "")
                    .trim();
            int price = Integer.parseInt(priceText);
            assert (price >= min && price <= max) :
                    "Có sản phẩm ngoài khoảng [" + min + " - " + max + "]: " + price;
            System.out.println("✅ Giá hợp lệ: " + price);
        }
    }

    // Hàm kiểm tra giá nhỏ hơn max
    public void checkAllPricesUnder(int max) {
        for (WebElement priceElement : productPrices) {
            String priceText = priceElement.getText()
                    .replace(".", "")
                    .replace("₫", "")
                    .replace("\u00A0", "")
                    .trim();
            int price = Integer.parseInt(priceText);
            assert (price <= max) :
                    "Có sản phẩm vượt quá " + max + ": " + price;
            System.out.println("✅ Giá hợp lệ: " + price);
        }
    }

    // Hàm kiểm tra giá lớn hơn min
    public void checkAllPricesAbove(int min) {
        for (WebElement priceElement : productPrices) {
            String priceText = priceElement.getText()
                    .replace(".", "")
                    .replace("₫", "")
                    .replace("\u00A0", "")
                    .trim();
            int price = Integer.parseInt(priceText);
            assert (price >= min) :
                    "Có sản phẩm nhỏ hơn " + min + ": " + price;
            System.out.println("✅ Giá hợp lệ: " + price);
        }
    }

    public WebElement getDuoi3Trieu() { return duoi3Trieu; }
    public WebElement getTu3Den5Trieu() { return tu3Den5Trieu; }
    public WebElement getTu5Den10Trieu() { return tu5Den10Trieu; }
    public WebElement getTu10Den15Trieu() { return tu10Den15Trieu; }
    public WebElement getTren15Trieu() { return tren15Trieu; }
    
    // ============ Locators ============
    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giảm giá nhiều']")
    WebElement tabGiamGiaNhieu;

    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giá thấp']")
    WebElement tabGiaThap;

    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giá cao']")
    WebElement tabGiaCao;

    @FindBy(xpath = "//a[contains(@class,'product-card')]")
    List<WebElement> productCards;

    // ============ Actions ============

    public void clickGiamGiaNhieu() {
        wait.until(ExpectedConditions.elementToBeClickable(tabGiamGiaNhieu)).click();
        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
    }

    public void clickGiaThap() {
        wait.until(ExpectedConditions.elementToBeClickable(tabGiaThap)).click();
        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
    }

    public void clickGiaCao() {
        wait.until(ExpectedConditions.elementToBeClickable(tabGiaCao)).click();
        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
    }

    // ====== Xử lý giá ======
    public int getProductPrice(WebElement card) {
        String priceText = "";

        try {
            WebElement priceElement = card.findElement(By.xpath(
                ".//span[contains(@class,'sale-price') or contains(@class,'price') or contains(@class,'text-maroon-500')]"
            ));
            priceText = priceElement.getText().trim();

            if (priceText.equalsIgnoreCase("Liên hệ")) {
                System.out.println("⚠️ Sản phẩm '" + card.getText() + "' không có giá (Liên hệ). Bỏ qua.");
                return -1; 
            }

            priceText = priceText.replace(".", "")
                                 .replace("₫", "")
                                 .replace("\u00A0", "")
                                 .trim();

        } catch (Exception e) {
            System.out.println("❌ Không tìm thấy giá cho sản phẩm: " + card.getText());
            return -1;
        }

        return Integer.parseInt(priceText);
    }

    public double getDiscountPercent(WebElement card) {
        try {
            String oldPriceText = card.findElement(By.xpath(".//span[contains(@class,'old-price')]"))
                    .getText().replace(".", "").replace("₫", "").replace("\u00A0", "").trim();
            String salePriceText = card.findElement(By.xpath(".//span[contains(@class,'sale-price')]"))
                    .getText().replace(".", "").replace("₫", "").replace("\u00A0", "").trim();

            int oldPrice = Integer.parseInt(oldPriceText);
            int salePrice = Integer.parseInt(salePriceText);

            return ((double) (oldPrice - salePrice) / oldPrice) * 100;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void checkDiscountSortedDescending() {
        double prev = Double.MAX_VALUE;
        for (WebElement card : productCards) {
            String name = card.findElement(By.xpath(".//h4")).getText();
            double discount = getDiscountPercent(card);
            System.out.println(name + " | Giảm giá: " + discount + "%");

            assert (discount <= prev) : "❌ Sản phẩm " + name + " không đúng thứ tự giảm giá.";
            prev = discount;
        }
    }

    public void checkPriceSortedAscending() {
        int prev = Integer.MIN_VALUE;
        for (WebElement card : productCards) {
            String name = card.findElement(By.xpath(".//h4")).getText();
            int price = getProductPrice(card);

            if (price == -1) continue; // bỏ qua "Liên hệ"

            System.out.println(name + " | Giá: " + price);
            assert (price >= prev) : "❌ Sản phẩm " + name + " có giá nhỏ hơn sản phẩm trước đó.";
            prev = price;
        }
    }

    public void checkPriceSortedDescending() {
        int prev = Integer.MAX_VALUE;
        for (WebElement card : productCards) {
            String name = card.findElement(By.xpath(".//h4")).getText();
            int price = getProductPrice(card);

            if (price == -1) continue; // bỏ qua "Liên hệ"

            System.out.println(name + " | Giá: " + price);
            assert (price <= prev) : "❌ Sản phẩm " + name + " có giá lớn hơn sản phẩm trước đó.";
            prev = price;
        }
    }
}
