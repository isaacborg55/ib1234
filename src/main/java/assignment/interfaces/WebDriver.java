package assignment.interfaces;

public interface WebDriver { //similar principle to previous class.
                             //Checks if webdriver is initialised or not
    public static int DRIVER_INITIALISED = 0;
    public static int DRIVER_NOT_INITIALISED = 1;
    public int getDriverStatus();
}
