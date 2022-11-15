package assignment.interfaces;

public interface API { //interface class set for API used primarily for testing to
                        // check for cases when both API is initialised or not initialised
    public static int APIISNOTINITIALISED = 0;
    public static int APIINITIALISED = 1;
    public int getApi();
}