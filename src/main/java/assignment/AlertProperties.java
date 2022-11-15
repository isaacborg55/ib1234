package assignment;

public class AlertProperties {
    protected int alertType;
    protected String heading;
    protected String description;
    protected String url;
    protected String imageUrl;
    protected String postedBy = "12c87e79-dede-4c7d-a47c-2b07b7a7a009";
    protected int priceInCents;

    //this class is set to be used for the body of the Json Object as specified in the assignment
    public AlertProperties(int alertType, String heading, String description, String url, String imageUrl, int priceInCents) {
        this.alertType = alertType;
        this.heading = heading;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.priceInCents = priceInCents;
    }

    public void setAlertType(int alertType){
        this.alertType = alertType;
    }
}
