package assignment.interfaces;

public interface TypeInput { //based on given scenario alert scenario in assignments.
                            // the typeinput indicates the type of alert being generated
    public static int CAR = 1;
    public static int BOAT = 2;
    public static int PROPERTY_FOR_RENT = 3;
    public static int PROPERTY_FOR_SALE = 4;
    public static int TOYS = 5;
    public static int ELECTRONICS = 6;

    public int InsertInput();
}
