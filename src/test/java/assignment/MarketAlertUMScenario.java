package assignment;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import assignment.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.io.IOException;

public class MarketAlertUMScenario {
    WebsiteScraperAlert websiteScraperAlert;
    API apiSystem;
    //tests fail due to unknown error in EBayScraper, first 2 pass.
    //ref: Tutorials
    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum() {
        websiteScraperAlert = new WebsiteScraperAlert();
        websiteScraperAlert.setupWebDriver();
    }
    @When("I login using valid credentials")
    public void iLoginUsingValidCredentials() {
        websiteScraperAlert.validLogin();
    }
    @Then("I should see my alerts")
    public void iShouldSeeMyAlerts() {
        Assertions.assertTrue(websiteScraperAlert.result);
    }
    @When("I login using invalid credentials")
    public void iLoginUsingInvalidCredentials() {
        websiteScraperAlert.invalidLogin();
    }
    @Then("I should see the login screen again")
    public void iShouldSeeTheLoginScreenAgain() {
        Assertions.assertTrue(websiteScraperAlert.result);
    }
    @Given("I am an administrator of the website and I upload {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAlerts(int arg0) throws IOException, InterruptedException {
        apiSystem = Mockito.mock(API.class);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        websiteScraperAlert.uploadNAlerts(arg0, apiSystem);
    }
    @When("I view a list of alerts")
    public void iViewAListOfAlerts() {
        websiteScraperAlert.viewListOfAlerts();
    }
    @Then("each alert should contain an icon")
    public void eachAlertShouldContainAnIcon() {
        Assertions.assertEquals(3, websiteScraperAlert.iconResult);
    }
    @And("each alert should contain a heading")
    public void eachAlertShouldContainAHeading() {
        Assertions.assertEquals(3, websiteScraperAlert.headingResult);
    }
    @And("each alert should contain a description")
    public void eachAlertShouldContainADescription() {
        Assertions.assertEquals(3, websiteScraperAlert.descResult);
    }

    @And("each alert should contain an image")
    public void eachAlertShouldContainAnImage() {
        Assertions.assertEquals(3, websiteScraperAlert.imgResult);
    }

    @And("each alert should contain a price")
    public void eachAlertShouldContainAPrice() {
        Assertions.assertEquals(3, websiteScraperAlert.priceResult);
    }

    @And("each alert should contain a link to the original product website")
    public void eachAlertShouldContainALinkToTheOriginalProductWebsite() {
        Assertions.assertEquals(3, websiteScraperAlert.linkResult);
    }

    @Then("I should see {int} alerts")
    public void iShouldSeeAlerts(int arg0) {
        Assertions.assertEquals(arg0, websiteScraperAlert.resultAlertCount);
    }

    @Given("I am an administrator of the website and I upload more than {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadMoreThanAlerts(int arg0) throws IOException, InterruptedException {
        apiSystem = Mockito.mock(API.class);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        websiteScraperAlert.uploadNAlerts(arg0 + 1, apiSystem);
    }

    @Given("I am an administrator of the website and I upload an alert of type {string}")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAnAlertOfType(String arg0) throws IOException, InterruptedException {
        apiSystem = Mockito.mock(API.class);
        Mockito.when(apiSystem.getApi()).thenReturn(API.APIINITIALISED);
        websiteScraperAlert.uploadAlertType(arg0, apiSystem);
    }
    @And("the icon displayed should be {string}")
    public void theIconDisplayedShouldBe(String arg0) {
        Assertions.assertEquals(arg0, websiteScraperAlert.resultIcon);
    }
}
