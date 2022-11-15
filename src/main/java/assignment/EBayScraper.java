package assignment;
import assignment.interfaces.*;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;


public class EBayScraper {
    public EBayScraper(){}
    protected TypeInput typeInput;
    protected EBaySite EBaySite;
    protected WebDriver DriverStatus;
    protected API apiSystem;
    protected WebDriverWait timer;
    protected List<WebElement> items;
    protected List<AlertProperties> alertProperties = new LinkedList<>();
    protected org.openqa.selenium.WebDriver driver;
    // Initialise XPaths here
    protected String itemsOnPageXPath = "//div[contains(@class, 'srp-river-results') and contains(@class, 'clearfix')]//ul[contains(@class, 'srp-results') and contains(@class, 'srp-list') and contains(@class, 'clearfix')]//li[contains(@class, 's-item__pl-on-bottom') and contains(@class, 's-item--watch-at-corner') and contains(@class, 's-item')]//div[contains(@class, 's-item__wrapper') and contains(@class, 'clearfix')]//div[contains(@class, 's-item__info') and contains(@class, 'clearfix')]//a"; //path for search page
    protected String headingXPath = "//div[contains(@class, 'vi-swc-lsp')]//div[contains(@class, 'vim') and contains(@class, 'x-item-title')]//h1[contains(@class, 'x-item-title__mainTitle')]//span[contains(@class, 'ux-textspans') and contains(@class, 'ux-textspans--BOLD')]"; //path of title product
    protected String descriptionXPath = "//div[contains(@class, 'ux-layout-section__item')]//div[contains(@class, 'ux-layout-section__row')]//div[contains(@class, 'ux-layout-section__textual-display--sellerLegalText')]//span"; //description of product page
    protected String imageUrlXPath = "//div[contains(@class, 'ux-image-filmstrip-carousel')]//button[contains(@class, 'image')]//img"; //img path in page
    protected String priceXPath = "//div[contains(@class, 'x-price-primary')]//span[contains(@itemdrop, 'price')]//span[contains(@class, 'ux-textspans')]"; //price in product page
    //note: had issue with this class, e-bay path sends to login screen after selecting first item
    public int parsePrice(String price){
        int dp = 100;
        String correctPrice = "";

        if (price.contains(".")){
            dp = 1;
        }

        for (int i = 0; i < price.length(); i ++){
            char currentChar = price.charAt(i);
            if (currentChar == '0' || currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4' || currentChar == '5' || currentChar == '6' || currentChar == '7' || currentChar == '8' || currentChar == '9'){
                correctPrice += currentChar;
            }
        }

        return (Integer.parseInt(correctPrice) * dp); //ref: https://www.tutorialspoint.com/java/number_parseint.htm
    }
    public void setTypeInput(TypeInput typeInput){
        this.typeInput = typeInput;
    }
    public void setEbayWebsite(EBaySite EBaySite){
        this.EBaySite = EBaySite;
    }
    public void setDriverStatus(WebDriver driverStatus){ this.DriverStatus = driverStatus;
    }
    public void setapiSystem(API apiSystem) {
        this.apiSystem = apiSystem;
    }

    protected boolean setupDriver(){
        if (DriverStatus == null){
            return false;
        }
        if (DriverStatus.getDriverStatus() == WebDriver.DRIVER_NOT_INITIALISED){ //if driver not initialised set false for testing
            return false;
        } else {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
            driver = new ChromeDriver();
            timer = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
        return true;
    }
    public boolean scrapeFiveAlertsAndUploadToSite() throws IOException, InterruptedException {
        // Check that the input provider is initialised
        if (typeInput == null){
            return false;
        }
        String searchText = "";
        int input = typeInput.InsertInput();;
        switch (input) {
            case 1 -> searchText = "Car";
            case 2 -> searchText = "Boat";
            case 3 -> searchText = "Property to rent";
            case 4 -> searchText = "Property for sale";
            case 5 -> searchText = "Toys";
            case 6 -> searchText = "Electronics";
        }

        boolean setup = setupDriver();
        if (!setup){
            return false;
        }

        if (EBaySite == null){
            driver.quit();
            return false;
        }

        if (EBaySite.getEbayWebsite() == EBaySite.SITE_NOT_AVAILABLE){ //if site not available then quit otherwise enter ebay
            driver.quit();
            return false;
        } else {
            driver.get("https://www.ebay.com/");
            driver.manage().window().maximize();

    }
    WebElement searchField = driver.findElement(By.id("gh-ac")); //gh-ac is id of search field, after it is found the text (one of 1-6 types) is entered
        searchField.sendKeys(searchText);
        searchField.sendKeys(Keys.ENTER);
        int scraped = 0;
        try {
            timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(itemsOnPageXPath)));
            items = driver.findElements(By.xpath(itemsOnPageXPath));
        } catch (Exception e){
            System.out.println("Exception while scraping:" + e);
            driver.quit();
            return false;
        }

        for (int i = 0; i < 15; i++){
            try {
                timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(itemsOnPageXPath)));
                items = driver.findElements(By.xpath(itemsOnPageXPath));
            } catch (Exception e){ //Loops to Look to check that elements have all the properties. (i.e if price is missing, it is skipped)
                System.out.println("Exception while scraping items from page: " + e);
                driver.quit();
                return false;
            }
            if (items.size() == 0){
                driver.quit();
                return false;
            }
            driver.navigate().to(items.get(i).getAttribute("href"));
            boolean itemissue = false; //checks for an issue with the item
            String heading = "";
            String description = "";
            String url = "";
            String imageUrl = "";
            int priceInCents = 0;
            try {
                timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(headingXPath)));
                heading = driver.findElement(By.xpath(headingXPath)).getText(); //get heading text
            } catch (Exception e){
                itemissue = true;
            }
            try {
                timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(descriptionXPath)));
                description = driver.findElement(By.xpath(descriptionXPath)).getText(); //get description text
            } catch (Exception e){
                itemissue = true;
            }
            url = driver.getCurrentUrl(); //get the url
            try {
                timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(imageUrlXPath)));
                imageUrl = driver.findElement(By.xpath(imageUrlXPath)).getAttribute("src"); //get image url, src specifies img url
            } catch (Exception e){
                itemissue = true;
            }
            try {
                timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(priceXPath)));
                priceInCents = parsePrice(driver.findElement(By.xpath(priceXPath)).getText()); //get price in text
            } catch (Exception e){
                itemissue = true;
            }
            if (!itemissue){ //sees if image added has an issue like as previously mentioned (i.e no image or price)
                // Add scraped item count
                scraped++;
                //
                // Add alert
                alertProperties.add(new AlertProperties(input, heading, description, url, imageUrl, priceInCents));
            }
            if (scraped == 5){
                break; //end after scraping 5
            }
            driver.navigate().back();
        }
        ApiRequest apiRequest = new ApiRequest();
        if (apiSystem == null){
            driver.quit();
            return false;
        }
        apiRequest.setapiSystem(apiSystem); //api setup
        boolean requests = apiRequest.sendFiveAlerts(alertProperties);
        if (!requests){
            driver.quit();
            return false;
        }
        driver.quit();
        return true;
    }
}
