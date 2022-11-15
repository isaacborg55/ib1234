package assignment;

import com.google.gson.Gson;
import assignment.interfaces.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ApiRequestTests {
    ApiRequest apiRequest;
    AlertProperties alertProperties;
    String jsonString;
    API apiService;
    List<AlertProperties> alertPropertiesList = new LinkedList<>();

    @BeforeEach
    public void setup() {
        apiRequest = new ApiRequest();
        alertProperties = new AlertProperties(
                1,
                "1991 Mercedes-Benz 300E E 4MATIC",
                "991 Mercedes-Benz 300E Sedan Brown AWD Automatic E 4MATIC",
                "https://www.ebay.com/itm/374352988409?hash=item57292d48f9:g:GmgAAOSwYdxjcRBZ&amdata=enc%3AAQAHAAAAoKV%2FIDDBQMQGgcus8vbO%2FCs5EBP4pCyE5rVolU%2Bly6E4j%2Fh5j9POdIjpKpgCS905v%2BTJ7pzh6x37WHl5k58kDrAgU60lRRjSZ5J4UYpeji1KrPz%2BKp0f0awa8uvjS8io%2FUFEQ52R3DcWE3pZqCcmFpMMLzdx53TkpQTm7QQfRIPZtFHLnMocfH3OPqvUKyLZuTvq9uSoDxeg0G7GmzXCoKU%3D%7Ctkp%3ABk9SR_qcr7KOYQ",
                "https://i.ebayimg.com/images/g/GmgAAOSwYdxjcRBZ/s-l500.jpg",
                250000
        ); //test alerts
        jsonString = new Gson().toJson(alertProperties);
    }

    @AfterEach
    public void teardown(){
        apiRequest = null;
        alertProperties = null;
        jsonString = null;
    }

    @Test
    public void testPostRequestWithoutApiSystem() throws IOException, InterruptedException {
        // Exercise
        int result = apiRequest.sendPostRequest(jsonString);

        // Verify
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void testPostRequestApiSystemNotInitialisied() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIISNOTINITIALISED);
        apiRequest.setapiSystem(apiService);

        // Exercise
        int result = apiRequest.sendPostRequest(jsonString);

        // Verify
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void testPostRequestWithJson() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIINITIALISED);
        apiRequest.setapiSystem(apiService);
        // Exercise
        int result = apiRequest.sendPostRequest(jsonString);
        // Verify
        Assertions.assertEquals(201, result);
    }

    @Test
    public void testPostRequestWithApiSystemAndInvalidInput() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIINITIALISED);
        apiRequest.setapiSystem(apiService);
        alertProperties.setAlertType(50); //Type 50 is not valid
        jsonString = new Gson().toJson(alertProperties);
        // Exercise
        int result = apiRequest.sendPostRequest(jsonString);
        // Verify
        Assertions.assertEquals(201, result);
        //should fail if issues in WebsiteScraperAlert are fixed
    }

    @Test
    public void testPostRequestWithApiSystemAndWrongJson() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIINITIALISED);
        apiRequest.setapiSystem(apiService);
        jsonString = "{ \"wrongBody\": true }";
        // Exercise
        int result = apiRequest.sendPostRequest(jsonString);
        // Verify
        Assertions.assertEquals(400, result);
    }

    @Test
    public void testDeleteRequestNoApi() throws IOException, InterruptedException {
        // Exercise
        int result = apiRequest.sendDeleteRequest();
        // Verify
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void testDeleteRequestApiSystemNotInitialised() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIISNOTINITIALISED);
        apiRequest.setapiSystem(apiService);
        // Exercise
        int result = apiRequest.sendDeleteRequest();
        // Verify
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void testDeleteRequestApiSystemInitialisied() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIINITIALISED);
        apiRequest.setapiSystem(apiService);

        // Exercise
        int result = apiRequest.sendDeleteRequest();

        // Verify
        Assertions.assertEquals(200, result);
    }

    @Test
    public void testSendFiveAlertsToWebsiteCorrectJson() throws IOException, InterruptedException {
        // Setup
        apiService = Mockito.mock(API.class);
        Mockito.when(apiService.getApi()).thenReturn(API.APIINITIALISED);
        apiRequest.setapiSystem(apiService);
        for (int i = 0; i < 5; i++){
            alertPropertiesList.add(alertProperties);
        }
        // Exercise
        boolean result = apiRequest.sendFiveAlerts(alertPropertiesList);
        Collection<Invocation> invocations = Mockito.mockingDetails(apiService).getInvocations();
        // Verify
        Assertions.assertTrue(result);
        Assertions.assertEquals(5, invocations.size());
    }
}
