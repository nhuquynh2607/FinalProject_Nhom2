package automation.pageLocator;

import java.time.Duration;
import java.util.ArrayList;
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

	// ================== LOCATORS ==================
	@FindBy(xpath = "//span[normalize-space(text())='< 3.000.000']")
	WebElement duoi3Trieu;

	@FindBy(xpath = "//span[normalize-space(text())='3.000.000 > 5.000.000']")
	WebElement tu3Den5Trieu;

	@FindBy(xpath = "//span[normalize-space(text())='5.000.000 > 10.000.000']")
	WebElement tu5Den10Trieu;

	@FindBy(xpath = "//span[normalize-space(text())='10.000.000 > 15.000.000']")
	WebElement tu10Den15Trieu;

	// ✅ Locator cho > 15tr
	@FindBy(xpath = "//span[normalize-space(text())='> 15.000.000']")
	WebElement tren15Trieu;

	@FindBy(xpath = "//span[contains(@class,'sale-price')]")
	List<WebElement> productPrices;

	// ================== ACTIONS ==================
	public void selectPriceFilter(WebElement priceElement) {
	    clickElement(priceElement);
	    wait.until(ExpectedConditions.visibilityOfAllElements(productPrices));
	}

	// ✅ Hàm riêng để chọn filter ≥ 15tr + assert
	public void selectPriceOver15Trieu() {
	    clickElement(tren15Trieu);
	    wait.until(ExpectedConditions.visibilityOfAllElements(productPrices));
	    checkAllPricesAboveOrEqual(15000000); // Kiểm tra luôn sau khi bấm
	}

	// ================== CHECKS ==================
	public void checkAllPricesInRange(int min, int max) {
	    for (WebElement priceElement : productPrices) {
	        int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	        assert (price >= min && price <= max) :
	                "❌ Có sản phẩm ngoài khoảng [" + min + " - " + max + "]: " + price;
	        System.out.println("✅ Giá hợp lệ: " + price);
	    }
	}

	public void checkAllPricesUnder(int max) {
	    for (WebElement priceElement : productPrices) {
	        int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	        assert (price <= max) :
	                "❌ Có sản phẩm vượt quá " + max + ": " + price;
	        System.out.println("✅ Giá hợp lệ: " + price);
	    }
	}

	// ✅ Sửa lại điều kiện thành ≥ (greater or equal)
	public void checkAllPricesAboveOrEqual(int min) {
	    for (WebElement priceElement : productPrices) {
	        int price = Integer.parseInt(normalizePriceText(priceElement.getText()));
	        assert (price >= min) :
	                "❌ Có sản phẩm nhỏ hơn " + min + ": " + price;
	        System.out.println("✅ Giá hợp lệ (≥ " + min + "): " + price);
	    }
	}

	// ================== GETTERS ==================
	public WebElement getDuoi3Trieu() { return duoi3Trieu; }
	public WebElement getTu3Den5Trieu() { return tu3Den5Trieu; }
	public WebElement getTu5Den10Trieu() { return tu5Den10Trieu; }
	public WebElement getTu10Den15Trieu() { return tu10Den15Trieu; }
	public WebElement getTren15Trieu() { return tren15Trieu; }

	// ================== SORT TABS ==================
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
					".//span[contains(@class,'sale-price') or contains(@class,'price') or contains(@class,'text-maroon-500')]"));

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
			int oldPrice = Integer.parseInt(
					normalizePriceText(card.findElement(By.xpath(".//span[contains(@class,'old-price')]")).getText()));
			int salePrice = Integer.parseInt(
					normalizePriceText(card.findElement(By.xpath(".//span[contains(@class,'sale-price')]")).getText()));
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
			if (price == -1)
				continue;

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
			if (price == -1)
				continue;

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
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'product-card')]")));
	}

	public void clickVoi() {
		click(By.xpath("//a[contains(text(),'Vòi Rửa Chén Bát')]"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'product-card')]")));
	}

	public List<String> getProductNames() {
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class,'product-card')]//h4"));
		return elements.stream().map(e -> e.getText().toLowerCase().trim()).collect(Collectors.toList());
	}

	public void clickBrandKainer() {
		WebElement kainer = driver.findElement(By.xpath("//a[contains(@href,'/chau-voi-rua-chen-bat/kainer')]"));
		kainer.click();
	}

	public List<String> getProductBrands() {
		List<WebElement> brandLogos = driver.findElements(By.xpath("//a[contains(@class,'product-card')]//img[@alt]"));

		List<String> brands = new ArrayList<>();
		for (WebElement logo : brandLogos) {
			brands.add(logo.getAttribute("alt").toLowerCase());
		}
		return brands;
	}

	public void clickOrigin(String originId) {
		WebElement originCheckbox = driver.findElement(By.id(originId));
		if (!originCheckbox.isSelected()) {
			originCheckbox.click();
		}
	}

	public List<String> getProductBrandSrcs() {
		List<WebElement> brandLogos = driver
				.findElements(By.xpath("//a[contains(@class,'product-card')]//div[contains(@class,'flex')]/img"));

		List<String> srcs = new ArrayList<>();
		for (WebElement logo : brandLogos) {
			srcs.add(logo.getAttribute("src"));
		}
		return srcs;
	}

	public void clickMaterial(String materialId) {
		WebElement checkbox = driver.findElement(By.id(materialId));
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
	}

	public void clickFirstProduct() {
		WebElement firstProduct = driver.findElement(By.xpath("(//a[contains(@class,'product-card')])[1]"));
		firstProduct.click();
	}

	public boolean checkMaterialDetail(String materialText) {
		WebElement detail = driver.findElement(By.xpath("//*[contains(text(),'" + materialText + "')]"));
		return detail.isDisplayed();
	}

	// ================== CHECK KẾT HỢP 2 ĐIỀU KIỆN ==================
	// Giá + Hãng
	public void checkPriceAndBrand(int min, int max, String expectedBrand) {
		for (WebElement card : productCards) {
			int price = getProductPrice(card);
			String brand = card.findElement(By.xpath(".//img[@alt]")).getAttribute("alt").toLowerCase();

			if (price != -1) {
				assert (price >= min && price <= max)
						: "❌ Sản phẩm có giá ngoài khoảng [" + min + " - " + max + "]: " + price;
			}
			assert brand.contains(expectedBrand.toLowerCase()) : "❌ Sản phẩm có brand khác: " + brand;
		}
	}

	// Giá + Xuất xứ
	public void checkPriceAndOrigin(int min, int max, String expectedOrigin) {
		for (WebElement card : productCards) {
			int price = getProductPrice(card);
			String origin = card.getText().toLowerCase();

			if (price != -1) {
				assert (price >= min && price <= max)
						: "❌ Sản phẩm có giá ngoài khoảng [" + min + " - " + max + "]: " + price;
			}
			assert origin.contains(expectedOrigin.toLowerCase()) : "❌ Sản phẩm không thuộc xuất xứ mong đợi: " + origin;
		}
	}

	// Giá + Chất liệu
	public void checkPriceAndMaterial(int min, int max, String expectedMaterial) {
		for (WebElement card : productCards) {
			int price = getProductPrice(card);
			String material = card.getText().toLowerCase();

			if (price != -1) {
				assert (price >= min && price <= max)
						: "❌ Sản phẩm có giá ngoài khoảng [" + min + " - " + max + "]: " + price;
			}
			assert material.contains(expectedMaterial.toLowerCase())
					: "❌ Sản phẩm không có chất liệu mong đợi: " + material;
		}
	}

	// Xuất xứ + Chất liệu
	public void checkOriginAndMaterial(String expectedOrigin, String expectedMaterial) {
		for (WebElement card : productCards) {
			String text = card.getText().toLowerCase();
			assert text.contains(expectedOrigin.toLowerCase()) : "❌ Sản phẩm không thuộc xuất xứ mong đợi: " + text;
			assert text.contains(expectedMaterial.toLowerCase()) : "❌ Sản phẩm không có chất liệu mong đợi: " + text;
		}
	}
	// Sản phẩm
    @FindBy(xpath = "//a[contains(@href, '/voi-rua-bat-hai-duong-nuoc-korea-at2-304')]")
    WebElement productLink;

    // Nút mua ngay
    @FindBy(xpath = "//a[contains(@class,'btn-linear-red')]//span[text()='Mua ngay']")
    WebElement buyNowBtn;

    // Form thông tin khách hàng
    @FindBy(xpath = "//input[@placeholder='Nhập họ và tên']")
    WebElement fullNameInput;

    @FindBy(xpath = "//input[@placeholder='Nhập số điện thoại']")
    WebElement phoneInput;

    @FindBy(xpath = "//input[@placeholder='Nhập số nhà, tên đường, phường/ xã, quận/huyện, tỉnh/ thành phố']")
    WebElement addressInput;

    // Thanh toán khi nhận hàng
    @FindBy(id = "checkout-type-0")
    WebElement codOption;

    // Nút Thanh toán
    @FindBy(xpath = "//button[span[text()='Thanh toán']]")
    WebElement checkoutBtn;

    // ==== Action Methods ====
    public void selectProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
    }

    public void clickBuyNow() {
        wait.until(ExpectedConditions.elementToBeClickable(buyNowBtn)).click();
    }

    public void fillCustomerInfo(String name, String phone, String address) {
        wait.until(ExpectedConditions.visibilityOf(fullNameInput)).sendKeys(name);
        phoneInput.sendKeys(phone);
        addressInput.sendKeys(address);
    }

    public void chooseCODPayment() {
        codOption.click();
    }

    public void clickCheckout() {
        checkoutBtn.click();
    }

}
