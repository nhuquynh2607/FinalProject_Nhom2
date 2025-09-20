package automation.pageLocator;

import automation.common.commonBase;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FilterLocator_LoNuongDaNang extends commonBase {
    private WebDriver driver;

    //Danh sách load sản phẩm
    @FindBy(xpath = "//div[@class='flex flex-wrap product-list']") List<WebElement> loadListProduct;

    //Danh sách sản phẩm
    @FindBy(xpath = "//div[@class='flex flex-wrap product-list']/a") List<WebElement> listProduct;

    // Giá mỗi sản phẩm
    @FindBy(xpath = "//span[@class='text-base font-bold md:text-lg text-maroon-500 sale-price']") List<WebElement> productPrices;

    //Logo hãng sản xuất mỗi sản phẩm
    @FindBy(xpath = ".//div[@class='md:h-[50px] h-[40px] flex items-center justify-center md:p-[6px] p-[4px]']//img") List<WebElement> logoManufacturer;
    //Xem thêm danh sách Lọc theo hãng sản xuất
    @FindBy(xpath = "(//span[text()='Xem thêm'])[1]") WebElement btnXemThemManufacturer;
    //Danh sach xem thêm tại Lọc theo hãng sản xuất
    @FindBy(xpath = "(//div[@class='category-menus'])[1]") WebElement listMoreManufacturer;

    //Xem thêm danh sách lọc theo xuất xứ
    @FindBy(xpath = "(//span[text()='Xem thêm'])[2]") WebElement btnXemThemOrigin;
    //Danh sách xem thêm tại Lọc theo xuất xứ
    @FindBy(xpath = "(//div[@class='category-menus'])[4]") WebElement listMoreOrigin;
    //Chọn nơi xuất xứ
    @FindBy(xpath = "(//div[@class='text-xs relative text-grey-500 mb-1.5 sm:mb-0'])[2]") WebElement selectOrigin;

    //Xem thêm danh sách lọc theo dung tích
    @FindBy(xpath = "(//span[text()='Xem thêm'])[3]") WebElement btnXemThemCapacity;
    //Danh sách xem thêm tại Lọc theo dung tích
    @FindBy(xpath = "(//div[@class='category-menus'])[5]") WebElement listMoreCapacity;
    //Chọn số lít dung tích
    @FindBy(xpath = "(//span[@class = 'flex-1'])[7]") WebElement selectCapacity;
    //Xem thêm nội dung trong chi tiết
    @FindBy(xpath = "(//span[text()='Xem thêm nội dung'])[2]") WebElement moreContentCapacity;

    //Chọn hãng
    @FindBy(xpath = "(//div[@class='text-xs relative text-grey-500 mb-1.5 sm:mb-0'])[1]") WebElement selectBrand;

    //% giảm giá
    @FindBy(xpath = "//div[@class='absolute top-0 right-0 z-10']") List<WebElement> btnListDiscountPrecent;

    public FilterLocator_LoNuongDaNang(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Lọc theo giá từ khoảng
    public void filterPrice(int min, int max) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));

        for (WebElement product : productPrices) {

            String priceText = product.getText();
            String numericPrice = priceText.replaceAll("[^0-9]", "");
            int price = Integer.parseInt(numericPrice);

            assertTrue(price >= min && price <= max,
                    "Sản phẩm có giá khoảng "+min+" - "+max+": " + priceText);

        }
        closeDriver();
    }

    //Bấm xem thêm danh sách tại hãng sản xuất
    public void moreManufacturer(){
        btnXemThemManufacturer.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(listMoreManufacturer));

    }

    //Lọc theo hãng sản xuất
    @Test
    public void filterManufacturer(String manufacture){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(loadListProduct)));

        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(listProduct)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> logo = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(logoManufacturer)));

        for (WebElement product : logo) {

            String manufacturerText = product.getAttribute("alt");
            assertEquals(manufacturerText,manufacture,
                    "Sản phẩm không thuộc "+manufacture+": " + manufacturerText);

        }
        closeDriver();
    }

    //Bấm xem thêm danh sách tại nơi xuất xứ
    public void moreOrigin(){
        btnXemThemOrigin.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(listMoreOrigin));

    }

    //Lọc theo nơi xuất xứ
    public void filterOrigin(String origin){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));

        for (int i = 0; i < products.size(); i++) {
            products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
            WebElement product = products.get(i);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);

            wait.until(ExpectedConditions.visibilityOfAllElements(selectOrigin));
            String originText = selectOrigin.getText().replace("Xuất xứ", "").trim();
            assertTrue(originText.equalsIgnoreCase(origin), "Sản phẩm không thuộc "+origin+": " + originText);

            driver.navigate().back();

            wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
        }
        closeDriver();
    }

    //Bấm xem thêm danh sách tại dung tích
    public void moreCapacity(){
        btnXemThemCapacity.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(listMoreCapacity));

    }

    //Lọc theo dung tích
    public void filterCapacity(int number){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));

        for (int i = 0; i < products.size(); i++) {
            products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
            WebElement product = products.get(i);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);

            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement xemThemButton = wait.until(
                    ExpectedConditions.elementToBeClickable(moreContentCapacity)
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", xemThemButton);

            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String capacityText = selectCapacity.getText().replace(".", "").trim();
            String capacityNumber = capacityText.replaceAll("[^0-9]", "");
            int capacity = Integer.parseInt(capacityNumber);
            assertTrue(capacity==number, "Sản phẩm không thuộc "+capacity+": " + capacityText);

            driver.navigate().back();

            wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
        }
        closeDriver();
    }

    //Lọc theo hãng
    @Test
    public void filterBrand(String brand){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));

        for (int i = 0; i < products.size(); i++) {
            products = wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
            WebElement product = products.get(i);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);


            wait.until(ExpectedConditions.visibilityOfAllElements(selectBrand));
            String originText = selectBrand.getText().replace("Thương hiệu", "").trim();
            assertTrue(originText.equalsIgnoreCase(brand), "Sản phẩm không thuộc "+brand+": " + originText);

            driver.navigate().back();

            wait.until(ExpectedConditions.visibilityOfAllElements(listProduct));
        }
        closeDriver();
    }

    //Lọc theo Giảm giá nhiều nhất
    public void filterBestSale(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> discountList = wait.until(ExpectedConditions.visibilityOfAllElements(btnListDiscountPrecent));
        Assert.assertTrue(discountList.size() > 0, "Không tìm thấy sản phẩm nào!");

        int[] discounts = new int[discountList.size()];
        for (int i = 0; i < discountList.size(); i++) {
            String text = discountList.get(i).getText().replace("%", "").replace("-", "").trim();
            discounts[i] = Integer.parseInt(text);
            System.out.println("Giảm giá sản phẩm " + (i+1) + ": -" + discounts[i] + "%");
        }

        for (int i = 0; i < discounts.length - 1; i++) {
            Assert.assertTrue(discounts[i] >= discounts[i+1],
                    "Lỗi: giảm giá không sắp xếp giảm dần tại vị trí " + i);
        }
        closeDriver();
    }

    //Lọc theo giá cao nhất
    public boolean priceSortedDesc() {
        List<Integer> prices = new ArrayList<>();
        for(WebElement el : productPrices){
            String price = el.getText().replaceAll("[^0-9]", ""); // lọc chỉ số
            if(!price.isEmpty()){
                prices.add(Integer.parseInt(price));
            }
        }

        List<Integer> sorted = new ArrayList<>(prices);
        Collections.max(sorted);

        return prices.equals(sorted);
    }

    //Lọc theo giá thấp nhất
    public boolean priceSortedAsc() {
        List<Integer> prices = new ArrayList<>();
        for(WebElement el : productPrices){
            String price = el.getText().replaceAll("[^0-9]", "");
            if(!price.isEmpty()){
                prices.add(Integer.parseInt(price));
            }
        }

        List<Integer> sorted = new ArrayList<>(prices);
        Collections.min(sorted);

        return prices.equals(sorted);
    }



}
