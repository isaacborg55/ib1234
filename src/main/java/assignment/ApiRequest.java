package assignment;
import com.google.gson.Gson;
import assignment.interfaces.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.List;
import java.net.URI;
//ref: https://docs.oracle.com/en/java/javase/12/docs/api/java.net.http/java/net/http/package-summary.html
public class ApiRequest {
    protected API apiSystem;
    public ApiRequest(){}
    public void setapiSystem(API apiSystem){ this.apiSystem = apiSystem; }
    public int sendPostRequest(String jsonString) throws IOException, InterruptedException {
        if (apiSystem != null){
            if (apiSystem.getApi() == API.APIINITIALISED){ //ref: https://zetcode.com/java/getpostrequest/
                HttpClient client = HttpClient.newHttpClient(); //ref: https://docs.oracle.com/en/java/javase/12/docs/api/java.net.http/java/net/http/HttpClient.html
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.marketalertum.com/Alert"))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());
                return response.statusCode(); //for response code 201
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int sendDeleteRequest() throws IOException, InterruptedException { //sending deleting request to api
        if (apiSystem != null){
            if (apiSystem.getApi() == API.APIINITIALISED){
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.marketalertum.com/Alert?userId=12c87e79-dede-4c7d-a47c-2b07b7a7a009"))
                        .DELETE()
                        .build(); //ref: https://stackoverflow.com/questions/63560572/how-to-send-delete-request-using-httpclient

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                return response.statusCode();
            } else {
                return -1;
            }
        }
        return - 1;
    }
    public boolean sendFiveAlerts(List<AlertProperties> alertPropertiesList) throws IOException, InterruptedException {
        for (int i = 0; i < alertPropertiesList.size(); i++){
            //ref: https://www.geeksforgeeks.org/convert-java-object-to-json-string-using-gson/
            //converting to Json using Gson
            String jsonString = new Gson().toJson(alertPropertiesList.get(i));
            if (sendPostRequest(jsonString) != 201){
                return false;
            }
        }
        return true;
    }
}
