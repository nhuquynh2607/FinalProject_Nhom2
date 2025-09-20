package automation.testsuite;

import automation.common.commonBase;
import automation.constant.CT_PageURL;
import automation.pageLocator.FilterLocator_LoNuongDaNang;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class FilterTest_LoNuongDaNang extends commonBase {
    WebDriverWait wait;
    FilterLocator_LoNuongDaNang filter;

    @BeforeMethod
    @Parameters("browser")
    public void openBrowser(String browser)
    {
        driver = setupDriver(browser);
        driver.get(CT_PageURL.BEPANTOAN_URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://bepantoan.vn/danh-muc/lo-nuong-da-nang");
        filter = new FilterLocator_LoNuongDaNang(driver);
    }

    //Lọc theo giá từ 3.000.000 đến 5.000.000
    @Test
    public void filterPriceTu3trTo5tr() {
        click(By.xpath("//span[contains(text(),'3.000.000 > 5.000.000')]"));
        filter.filterPrice(3000000,5000000);
    }

    //Lọc theo giá từ 5.000.000 đến 10.000.000
    @Test
    public void filterPriceTu5trTo10tr() {
        click(By.xpath("//span[contains(text(),'5.000.000 > 10.000.000')]"));
        filter.filterPrice(5000000,10000000);
    }

    //Lọc theo hãng sản xuất Dudoff
    @Test
    public void filterManufacturerByDudoff(){
        filter.moreManufacturer();
        click(By.xpath("//div[@class='grid grid-cols-3 gap-1 md:grid-cols-2']//a[last()-9]"));
        filter.filterManufacturer("Dudoff");

    }

    //Lọc theo nơi xuất xứ tại Japan
    @Test
    public void filterOriginByJapan(){

        filter.moreOrigin();
        WebElement originLabel = driver.findElement(By.id("japan-1018"));
        boolean isOriginSeleted = originLabel.isSelected();
        if(isOriginSeleted == false)
        {
            originLabel.click();
        }
        filter.filterOrigin("Japan");
    }

    //Lọc theo nơi xuất xứ tại Malaysia
    @Test
    public void filterOriginByMalaysia(){

        filter.moreOrigin();
        WebElement originLabel = driver.findElement(By.id("malaysia-1023"));
        boolean isOriginSeleted = originLabel.isSelected();
        if(isOriginSeleted == false)
        {
            originLabel.click();
        }
        filter.filterOrigin("Malaysia");
    }

    //Lọc dữ liệu Dung tích là 34 lít
    @Test
    public void filterCapacityBy34Lit(){

        filter.moreCapacity();
        WebElement capacityLabel = driver.findElement(By.id("34-litI037-1214"));
        boolean isCapacitySeleted = capacityLabel.isSelected();
        if(isCapacitySeleted == false)
        {
            capacityLabel.click();
        }
        filter.filterCapacity(34);
    }

    //Lọc theo hãng Bosch
    @Test
    public void filterBrandByBosch(){
        WebElement brandLabel = driver.findElement(By.xpath("//span[text()='Bosch']"));
        boolean isBrandSeleted = brandLabel.isSelected();
        if(isBrandSeleted == false)
        {
            brandLabel.click();
        }
        filter.filterBrand("Bosch");
    }

    //Lọc theo tab giảm giá nhiều nhất đến thấp nhất
    @Test
    public void bestDiscountPrecent(){
        driver.get("https://bepantoan.vn/danh-muc/lo-nuong-da-nang?sort=discount&page=1");
        filter.filterBestSale();
    }

    //Lọc theo tab giá cao nhất đến thấp nhât
    @Test
    public void priceSortedDesc(){
        driver.get("https://bepantoan.vn/danh-muc/lo-nuong-da-nang?sort=price_desc&page=1");
        Assert.assertTrue(filter.priceSortedDesc(),
                "Giá sản phẩm KHÔNG được sắp xếp từ cao đến thấp!");
        closeDriver();
    }

    //Lọc theo tab giá thấp nhất đến cao nhất
    @Test
    public void priceSortedAsc(){
        driver.get("https://bepantoan.vn/danh-muc/lo-nuong-da-nang?sort=price_asc&page=1");
        Assert.assertTrue(filter.priceSortedAsc(),
                " Giá sản phẩm KHÔNG được sắp xếp từ thấp đến cao!");
        closeDriver();
    }

}

