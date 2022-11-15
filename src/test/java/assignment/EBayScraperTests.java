package assignment;
import assignment.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class EBayScraperTests {
    EBayScraper EBayScraper;
    TypeInput typeInput;
    EBaySite EBaySite;
    WebDriver webDriverStatus;
    API apiSystem;
    ApiRequest apiRequest;

    @BeforeEach
    public void setup(){
        EBayScraper = new EBayScraper();
        apiRequest = new ApiRequest();
    }

    @AfterEach
    public void teardown(){
        EBayScraper = null;
    }

    @Test
    public void testPriceCommaAndDot(){
        // Exercise
        int result = EBayScraper.parsePrice("£10,000.00");

        // Verify
        Assertions.assertEquals(1000000, result);
    }

    @Test
    public void testPriceWithComma(){
        // Exercise
        int result = EBayScraper.parsePrice("$9,000");

        // Verify
        Assertions.assertEquals(900000, result);
    }

    @Test
    public void testPriceDot(){
        // Exercise
        int result = EBayScraper.parsePrice("10000.00");

        // Verify
        Assertions.assertEquals(1000000, result);
    }

    @Test
    public void testPriceNumbers(){
        // Exercise
        int result = EBayScraper.parsePrice("600");

        // Verify
        Assertions.assertEquals(60000, result);
    }

    @Test
    public void testSetupDriverIfNoDriverExists(){
        // Exercise
        boolean result = EBayScraper.setupDriver();

        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testSetupDriverIfNoDriverInitialised(){
        // Setup
        webDriverStatus = Mockito.mock(WebDriver.class);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_NOT_INITIALISED);
        EBayScraper.setDriverStatus(webDriverStatus);

        // Exercise
        boolean result =  EBayScraper.setupDriver();

        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testSetupDriverInitialised(){
        // Setup
        webDriverStatus = Mockito.mock(WebDriver.class);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        EBayScraper.setDriverStatus(webDriverStatus);

        // Exercise
        boolean result = EBayScraper.setupDriver();

        // Verify
        Assertions.assertTrue(result);
    }

    @Test
    public void testScraperNoInitialisedInputProvider() throws IOException, InterruptedException {
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testScraperDriverNotInitialised() throws IOException, InterruptedException {
        // Setup
        webDriverStatus = Mockito.mock(WebDriver.class);
        typeInput = Mockito.mock(TypeInput.class);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_NOT_INITIALISED);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setTypeInput(typeInput);

        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();

        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testScraperCarEBayWebsiteNotInitialised() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setTypeInput(typeInput);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertFalse(result);
    }
    @Test
    public void testScraperCarEBayWebsiteNotAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_NOT_AVAILABLE);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testScraperCarEBayWebsiteWithNoApiSystem() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testScraperCarEBayWebsiteWithApiSystemNotInitialised() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIISNOTINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertFalse(result);
    }

    @Test
    public void testScraperCarEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.CAR);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }
    @Test
    public void testScraperForBoatEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.BOAT);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }

    @Test
    public void testScraperPropertyForRentEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.PROPERTY_FOR_RENT);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }

    @Test
    public void testScraperForPropertyForSaleEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.PROPERTY_FOR_SALE);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }

    @Test
    public void testScraperForToysEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.TOYS);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite();
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }

    @Test
    public void testScraperForElectronicsEBayWebsiteAvailable() throws IOException, InterruptedException {
        // Setup
        typeInput = Mockito.mock(TypeInput.class);
        webDriverStatus = Mockito.mock(WebDriver.class);
        EBaySite = Mockito.mock(EBaySite.class);
        apiSystem = Mockito.mock(API.class);
        Mockito.when(typeInput.InsertInput()).thenReturn(TypeInput.ELECTRONICS);
        Mockito.when(webDriverStatus.getDriverStatus()).thenReturn(WebDriver.DRIVER_INITIALISED);
        Mockito.when(EBaySite.getEbayWebsite()).thenReturn(EBaySite.SITE_AVAILABLE);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        EBayScraper.setTypeInput(typeInput);
        EBayScraper.setDriverStatus(webDriverStatus);
        EBayScraper.setEbayWebsite(EBaySite);
        EBayScraper.setapiSystem(apiSystem);
        // Exercise
        boolean result = EBayScraper.scrapeFiveAlertsAndUploadToSite(); //error
        // Verify
        Assertions.assertTrue(result);
        //bug in EbayScraper
    }
}
