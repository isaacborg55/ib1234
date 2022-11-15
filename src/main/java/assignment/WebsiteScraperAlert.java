package assignment;
import com.google.gson.Gson;
import assignment.interfaces.*;
import org.openqa.selenium.By;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
public class WebsiteScraperAlert {
    protected WebDriver driver;
    protected WebDriverWait timer;
    protected List<AlertProperties> alertProperties = new LinkedList<>();
    protected ApiRequest apiRequest;
    protected String jString;

    public boolean result;
    public int iconResult;
    public int headingResult;
    public int descResult;
    public int imgResult;
    public int priceResult;
    public int linkResult;
    public int resultAlertCount;
    public String resultIcon;

    String userId = "12c87e79-dede-4c7d-a47c-2b07b7a7a009"; //given ID for MarketAlert

    String Xpathloggingin = "//div[@class='navbar-collapse collapse d-sm-inline-flex justify-content-between']//li[2]//a";
    String IDuser = "UserId";
    String Xpathcheckloggingin = "//main[@class='pb-3']//h1";
    String Xpathicon = "//table//tbody//`tr[1]//td//h4//img";
    String headingXPath = "//table//tr[1]//h4";
    String linkXPath = "//table//tr[5]//a";
    String descriptionXPath = "//table//tr[3]//td";
    String priceXPath = "//table//tr[4]//td";
    String imageXPath = "//table//tr[2]//td//img";
    String alertsXPath = "//table";
    //setting xpaths for use Website Scraper


    //Test Alerts to see if scraper works, using given format in assignment (AlertProperties)
    AlertProperties carAlertProperties = new AlertProperties(
            1,
            "1991 Mercedes-Benz 300E E 4MATIC",
            "991 Mercedes-Benz 300E Sedan Brown AWD Automatic E 4MATIC",
            "https://www.ebay.com/itm/374352988409?hash=item57292d48f9:g:GmgAAOSwYdxjcRBZ&amdata=enc%3AAQAHAAAAoKV%2FIDDBQMQGgcus8vbO%2FCs5EBP4pCyE5rVolU%2Bly6E4j%2Fh5j9POdIjpKpgCS905v%2BTJ7pzh6x37WHl5k58kDrAgU60lRRjSZ5J4UYpeji1KrPz%2BKp0f0awa8uvjS8io%2FUFEQ52R3DcWE3pZqCcmFpMMLzdx53TkpQTm7QQfRIPZtFHLnMocfH3OPqvUKyLZuTvq9uSoDxeg0G7GmzXCoKU%3D%7Ctkp%3ABk9SR_qcr7KOYQ",
            "https://i.ebayimg.com/images/g/GmgAAOSwYdxjcRBZ/s-l500.jpg",
            250000
    );
    AlertProperties boatAlertProperties = new AlertProperties(
            2,
            "Storm Damaged 2008 Chaparral 270 Signature Project Boat 496 Mercruiser Bravo 3",
            "Used storm damaged boat AS IS",
            "https://www.ebay.com/itm/165770994021?hash=item2698b8a165:g:ZxsAAOSwdkhjWt5r&amdata=enc%3AAQAHAAAAkJYfPsaJu4jgiCFSrITyRaNuArbU4%2BjbDlMVl9a%2BhOO8Vuh21haHcyKoqb2GIbWCkOuzq2cOHc%2BkxoKJP2NtxJAHNFjTw3X7s%2F79O21cnr%2BTST%2BUHBPrIRNtyaHUS48xRLA%2FiEhAEjQT4ZGeXbrHlalwOFGE%2BpH%2BNWU9wOlB4%2B6oGNX2lULqmC9wXOGl1iP3Dw%3D%3D%7Ctkp%3ABk9SR8Ce2LKOYQ",
            "https://i.ebayimg.com/images/g/ZxsAAOSwdkhjWt5r/s-l300.png",
            520000
    );
    AlertProperties rentAlertProperties = new AlertProperties(
            3,
            "Spain Costa Blanca Torrevieja Apartments for Rent Sept to May -only £325-425 pw",
            "Well equipped fully furnished holiday apartments",
            "https://www.ebay.com/itm/155242287588?hash=item24252951e4:g:HYQAAOSw8Sdd2Zh2",
            "https://i.ebayimg.com/images/g/HYQAAOSw8Sdd2Zh2/s-l500.png",
            47697
    );
    AlertProperties saleAlertProperties = new AlertProperties(
            4,
            "Villa for sale in Spain",
            "Immaculately presented complete with all furniture and car",
            "https://www.ebay.com/itm/275529975116?hash=item4026ddc54c:g:StAAAOSwNY5jaCTF",
            "https://i.ebayimg.com/images/g/StAAAOSwNY5jaCTF/s-l500.png",
            19078740
    );
    AlertProperties toysAlertProperties = new AlertProperties(
            5,
            "Transformers G1 Optimus Prime Reissue Brand New MISB Free Shipping\n",
            "A brand-new, unused, unopened, undamaged item (including handmade items). ",
            "https://www.ebay.com/itm/394278843701?hash=item5bccd9b535:g:hM0AAOSw2ili8h8n&amdata=enc%3AAQAHAAAA0Ig0uTIUngdcZpWq7k7HOxCpP3k62i24VyF49FHcVQlGwQxwBmAoeAgnpBxl%2Bq5tAB%2Fl0oKDT9G%2FrHB%2FCXbaRCSbQ1Tw1cO3fWJ%2FMfdr6Pb%2BMh%2BSnIceORCU30Uu9wuzOVej1vv42veUY%2FB9CANKx1V0G7u3OjOgZMfknovDH00OjKzHu9ObDTTVOGLBFKtz%2FuLP8tfwPb1QYUt47Dv50WtkgD47seumqPS%2B0Vj2SP4DbQatycZb2iwVTPgfwB7BtyqKRzosEZ7%2B0ecf1IbPL4w%3D%7Ctkp%3ABFBMyrnSs45h",
            "https://i.ebayimg.com/images/g/hM0AAOSw2ili8h8n/s-l500.jpg",
            4999
    );
    AlertProperties electronicsAlertProperties = new AlertProperties(
            6,
            "Sony Playstation 4 Console Selection PS4 PRO, PS4 Slim, PS4 & 2 Free Games!!-\n",
            "1 YEAR WARRANTY",
            "https://www.ebay.com/itm/165607507134?hash=item268efa04be:g:VsoAAOSwZoZifsVL&amdata=enc%3AAQAHAAAA8Mwh9VGm5GXTRr6RtqjejjRkitfYWSCVn4QiUkCVQpQuA9f0WlAqFvrcwg6O8kuFfe62sZjDFJw%2Brj%2BmqZMxy4KFbX4TZrUnEHMW5RtGWLS45ekiHC4OsC71isZFeBs%2BskY%2Fb7k7xcwleZViJQxlGC61bvusfcnyq0eOvxiGvlTnS3u0KZPSF%2BxZg3gTmIM5QbdjPaeMmAwAcXDOmWQa4XGzW4CaowCMi4sJWm20vllVBY%2Bnmig7%2BaKaoPR60RVguGoCE6oAy9lIy2dts3llliihC9X96Kc6tFwq3i99yH%2FRvH%2FtK2eGNOAvOzzVbVjzNw%3D%3D%7Ctkp%3ABFBM-pHfs45h",
            "https://i.ebayimg.com/images/g/VsoAAOSwZoZifsVL/s-l500.png",
            19610
    );


    public void setupWebDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe"); //web driver setup
        driver = new ChromeDriver();
        timer = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public void validLogin() { //validate login
        driver.get("https://www.marketalertum.com/");
        driver.manage().window().maximize(); //ref: https://www.browserstack.com/guide/maximize-chrome-window-in-selenium

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpathloggingin))); //making use of the previously defined variables
        driver.findElement(By.xpath(Xpathloggingin)).click(); //ref: https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/ui/ExpectedConditions.html#:~:text=visibilityOfElementLocated,-public%20static%20ExpectedCondition&text=%E2%80%8B(By%20locator)-,An%20expectation%20for%20checking%20that%20an%20element%20is%20present%20on,that%20is%20greater%20than%200.

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.id(IDuser))); //checking that element is on the page and Visible
        WebElement element = driver.findElement(By.id(IDuser));
        element.sendKeys(userId); //types given userid
        element.sendKeys(Keys.ENTER);

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpathcheckloggingin)));
        if (driver.findElement(By.xpath(Xpathcheckloggingin)).getText().equals("Latest alerts for Isaac Borg")) {
            result = true;//driver waits until the text appears to continue
        } else {
            result = false;
        }
        driver.quit();
    }

    public void invalidLogin() { //checks for invalid login
        driver.get("https://www.marketalertum.com/");
        driver.manage().window().maximize();

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpathloggingin)));
        driver.findElement(By.xpath(Xpathloggingin)).click();

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.id(IDuser)));
        WebElement element = driver.findElement(By.id(IDuser)); //same method as before
        element.sendKeys("Invalid Login"); //types the following text instead of ID for an invalid ID
        element.sendKeys(Keys.ENTER);

        if (driver.getCurrentUrl().equals("https://www.marketalertum.com/Alerts/Login")) {
            result = true;
        } else {
            result = false;
        }
        driver.quit();
    }

    public void uploadNAlerts(int n, API apiService) throws IOException, InterruptedException {
        switch (n) { //using Switch statement to add properties
            case 1 -> alertProperties.add(carAlertProperties);
            case 2 -> {
                alertProperties.add(carAlertProperties);
                alertProperties.add(boatAlertProperties);
            }
            case 3 -> {
                alertProperties.add(carAlertProperties);
                alertProperties.add(boatAlertProperties);
                alertProperties.add(rentAlertProperties);
            }
            case 4 -> {
                alertProperties.add(carAlertProperties);
                alertProperties.add(boatAlertProperties);
                alertProperties.add(rentAlertProperties);
                alertProperties.add(saleAlertProperties);
            }
            case 5 -> {
                alertProperties.add(carAlertProperties);
                alertProperties.add(boatAlertProperties);
                alertProperties.add(rentAlertProperties);
                alertProperties.add(saleAlertProperties);
                alertProperties.add(toysAlertProperties);
            }
            case 6 -> {
                alertProperties.add(carAlertProperties);
                alertProperties.add(boatAlertProperties);
                alertProperties.add(rentAlertProperties);
                alertProperties.add(saleAlertProperties);
                alertProperties.add(toysAlertProperties);
                alertProperties.add(electronicsAlertProperties);
            }
        }
        //calls methods from ApiRequest
        apiRequest = new ApiRequest();
        apiRequest.setapiSystem(apiService); //sets service for api
        apiRequest.sendDeleteRequest(); //Deletes previously added alerts
        apiRequest.sendFiveAlerts(alertProperties); // Upload the alerts to the website
    }

    public void uploadAlertType(String type, API apiService) throws IOException, InterruptedException {
        apiRequest = new ApiRequest(); //method takes type of alert
        apiRequest.setapiSystem(apiService);
        apiRequest.sendDeleteRequest();

        //ref: https://www.geeksforgeeks.org/convert-java-object-to-json-string-using-gson/
        //converting to Json using Gson
        switch (type) {
            case "1" -> {
                jString = new Gson().toJson(carAlertProperties);
            }
            case "2" -> {
                jString = new Gson().toJson(boatAlertProperties);
            }
            case "3" -> {
                jString = new Gson().toJson(rentAlertProperties);
            }
            case "4" -> {
                jString = new Gson().toJson(saleAlertProperties);
            }
            case "5" -> {
                jString = new Gson().toJson(toysAlertProperties);
            }
            case "6" -> {
                jString = new Gson().toJson(electronicsAlertProperties);
            }
        }
        apiRequest.sendPostRequest(jString); //POST Request sent to the API
    }

    public void viewListOfAlerts() { //views list of alerts
        driver.get("https://www.marketalertum.com/");
        driver.manage().window().maximize();

        timer.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpathloggingin)));
        driver.findElement(By.xpath(Xpathloggingin)).click();
        timer.until(ExpectedConditions.visibilityOfElementLocated(By.id(IDuser)));
        WebElement element = driver.findElement(By.id(IDuser));
        element.sendKeys(userId);
        element.sendKeys(Keys.ENTER); //like previous methods up to this point.

        timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(Xpathicon)));
        List<WebElement> iconElements = driver.findElements(By.xpath(Xpathicon)); //creating list of iconElements
        //Function for the Headings
        timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(headingXPath)));
        List<WebElement> headingElements = driver.findElements(By.xpath(headingXPath)); //creating list of heading elements
        List<String> headingList = new ArrayList<>();
        for (WebElement headingElement : headingElements) { //for each headingElement in headingElements
            String heading = headingElement.getText();
            if (!heading.equals("")) { //if the heading is not null add it to the list of headings
                headingList.add(heading);
            }
        }
        //Function for the Descriptions
        timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(descriptionXPath)));
        List<WebElement> descriptionElements = driver.findElements(By.xpath(descriptionXPath)); //creating list of description elements
        List<String> descriptionList = new ArrayList<>(); //creating list of descriptions
        for (WebElement descriptionElement : descriptionElements) { //for each descriptionElement in descriptionElements
            { //for each descriptionElement in descriptionElements
                String description = descriptionElement.getText();
                if (!description.equals("")) { //if the description is not null add it to the list of descriptions
                    descriptionList.add(description);
                }
            }
            //Function for the Images
            timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(imageXPath)));
            List<WebElement> imageElements = driver.findElements(By.xpath(imageXPath)); //creating list of image elements
            //Function for the Price
            timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(priceXPath)));
            List<WebElement> priceElements = driver.findElements(By.xpath(priceXPath)); //creating list of price elements
            List<String> priceList = new ArrayList<>(); //creating list of price
            for (WebElement priceElement : priceElements) {
                String price = priceElement.getText().replace("Price: ", ""); //replacing
                if (!price.equals("")) { //if the price is not null add it to the list of price
                    priceList.add(price);
                }
            }
            //Function for the Link
            timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(linkXPath)));
            List<WebElement> linkElements = driver.findElements(By.xpath(linkXPath)); //creating list of link elements
            //Function for the Alert list
            timer.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(alertsXPath)));
            List<WebElement> alertsElements = driver.findElements(By.xpath(alertsXPath)); //creating list of link elements


            iconResult = iconElements.size();
            headingResult = headingList.size();
            descResult = descriptionList.size();
            imgResult = imageElements.size();
            priceResult = priceList.size();
            linkResult = linkElements.size(); // Check if all lists are of size 3 Scenario 3

            resultAlertCount = alertsElements.size(); // Check result alert count - for the Scenario 4/5
            resultIcon = iconElements.get(0).getAttribute("src").split("/")[4]; // Result Icon - for Scenario 5
            driver.quit(); //exit driver
        }
    }
}
