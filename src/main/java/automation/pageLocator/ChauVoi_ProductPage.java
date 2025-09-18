package automation.pageLocator;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import automation.common.commonBase;

public class ChauVoi_ProductPage extends commonBase {
	 WebDriver driver;
	    WebDriverWait wait;

	    public ChauVoi_ProductPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    }

	    private void clickElement(WebElement element) {
	        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	    }

	    private String normalizePriceText(String text) {
	        return text.replace(".", "")
	                   .replace("₫", "")
	                   .replace("\u00A0", "")
	                   .trim();
	    }

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

	    @FindBy(xpath = "//span[contains(@class,'sale-price')]")
	    List<WebElement> productPrices;

	    public void selectPriceFilter(WebElement priceElement) {
	        clickElement(priceElement);
	        wait.until(ExpectedConditions.visibilityOfAllElements(productPrices));
	    }

	    public void checkAllPricesInRange(int min, int max) {
	        for (WebElement priceElement : productPrices) {
	            int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	            assert (price >= min && price <= max) :
	                "Có sản phẩm ngoài khoảng [" + min + " - " + max + "]: " + price;
	            System.out.println("✅ Giá hợp lệ: " + price);
	        }
	    }

	    public void checkAllPricesUnder(int max) {
	        for (WebElement priceElement : productPrices) {
	            int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	            assert (price <= max) : "Có sản phẩm vượt quá " + max + ": " + price;
	            System.out.println("✅ Giá hợp lệ: " + price);
	        }
	    }

	    public void checkAllPricesAbove(int min) {
	        for (WebElement priceElement : productPrices) {
	            int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	            assert (price >= min) : "Có sản phẩm nhỏ hơn " + min + ": " + price;
	            System.out.println("✅ Giá hợp lệ: " + price);
	        }
	    }

	    public WebElement getDuoi3Trieu() { return duoi3Trieu; }
	    public WebElement getTu3Den5Trieu() { return tu3Den5Trieu; }
	    public WebElement getTu5Den10Trieu() { return tu5Den10Trieu; }
	    public WebElement getTu10Den15Trieu() { return tu10Den15Trieu; }
	    public WebElement getTren15Trieu() { return tren15Trieu; }

	    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giảm giá nhiều']")
	    WebElement tabGiamGiaNhieu;

	    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giá thấp']")
	    WebElement tabGiaThap;

	    @FindBy(xpath = "//a[contains(@class,'menu-tab') and text()='Giá cao']")
	    WebElement tabGiaCao;

	    @FindBy(xpath = "//a[contains(@class,'product-card')]")
	    List<WebElement> productCards;

	    public void clickGiamGiaNhieu() {
	        clickElement(tabGiamGiaNhieu);
	        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
	    }

	    public void clickGiaThap() {
	        clickElement(tabGiaThap);
	        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
	    }

	    public void clickGiaCao() {
	        clickElement(tabGiaCao);
	        wait.until(ExpectedConditions.visibilityOfAllElements(productCards));
	    }

	    public int getProductPrice(WebElement card) {
	        try {
	            WebElement priceElement = card.findElement(By.xpath(
	                ".//span[contains(@class,'sale-price') or contains(@class,'price') or contains(@class,'text-maroon-500')]"
	            ));

	            String priceText = priceElement.getText().trim();
	            if (priceText.equalsIgnoreCase("Liên hệ")) {
	                System.out.println("⚠️ Sản phẩm '" + card.getText() + "' không có giá (Liên hệ). Bỏ qua.");
	                return -1;
	            }

	            return Integer.parseInt(normalizePriceText(priceText));
	        } catch (Exception e) {
	            System.out.println("❌ Không tìm thấy giá cho sản phẩm: " + card.getText());
	            return -1;
	        }
	    }

	    public double getDiscountPercent(WebElement card) {
	        try {
	            int oldPrice = Integer.parseInt(normalizePriceText(
	                card.findElement(By.xpath(".//span[contains(@class,'old-price')]")).getText()
	            ));
	            int salePrice = Integer.parseInt(normalizePriceText(
	                card.findElement(By.xpath(".//span[contains(@class,'sale-price')]")).getText()
	            ));
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
	            if (price == -1) continue;

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
	            if (price == -1) continue;

	            System.out.println(name + " | Giá: " + price);
	            assert (price <= prev) : "❌ Sản phẩm " + name + " có giá lớn hơn sản phẩm trước đó.";
	            prev = price;
	        }
	    }

	    @FindBy(xpath = "//a[contains(text(),'Chậu Rửa Chén Bát')]")
	    WebElement linkChau;
	    @FindBy(xpath = "//a[contains(text(),'Vòi Rửa Chén Bát')]")
	    WebElement linkVoi;
	    @FindBy(xpath = "//a[contains(@class,'product-card')]//h4")
	    List<WebElement> productNames;
	    public void clickChau() {
	        click(By.xpath("//a[contains(text(),'Chậu Rửa Chén Bát')]"));
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//div[contains(@class,'product-card')]")
	        ));
	    }

	    public void clickVoi() {
	        click(By.xpath("//a[contains(text(),'Vòi Rửa Chén Bát')]"));
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//div[contains(@class,'product-card')]")
	        ));
	    }
	    public List<String> getProductNames() {
	        List<WebElement> elements = driver.findElements(
	            By.xpath("//a[contains(@class,'product-card')]//h4")
	        );
	        return elements.stream()
	                       .map(e -> e.getText().toLowerCase().trim()) 
	                       .collect(Collectors.toList());
	    }
}
