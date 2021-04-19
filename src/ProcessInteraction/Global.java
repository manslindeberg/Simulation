public class Global {

    /* GLOBAL */
    public static final int ARRIVAL = 1, READY = 2, MEASURE = 3,
            START = 4, FAIL = 5, CONTROL = 7,
            PRECISEMEASURE = 8, OUTPUTMEASURE = 9;// Signal types

    public static double time = 0; // Global time variable initialized to 0 for all Process Interaction simulations

    /* ASSIGNMENT 1 - TASK 5 */
    public static final int RANDOM = 1, ROUNDROBIN = 2, LEASTJOBAMOUNT = 3; // Load-balancing algorithms

    /* ASSIGNMENT 1 - TASK 6 */
    public static final int OPENING = 9, CLOSING = 17; // Pharmacy Opening & Closing Hours
    public static final int HOURSPERDAY = 24;

}
