package assignment.interfaces;

public interface EBaySite { //similar principle to previous class.
                            // checks if website is available or not
    public static int SITE_NOT_AVAILABLE = 0; //
    public static int SITE_AVAILABLE = 1;
    public int getEbayWebsite();
}