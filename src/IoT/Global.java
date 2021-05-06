public class Global {

    /* GLOBAL */
    public static final int
            TRANSMIT = 1, SAMPLE = 2, CONTROL = 3,
            STARTRECEIVE = 5, STOPRECEIVE = 6, TRANSMITFAILURE = 7,
            TRANSMITSUCCESS = 8, INIT = 9;

    public static int ROUND;
    public static int AREAHEIGHT;
    public static int AREAWIDTH;
    public static int COMMUNICATIONRANGE;
    public static int NOSENSORS;
    public static double TRANSMITTIME;
    public static double MEANINTERARRIVALTIME;
    public static double CONTROLTIME;
    public static double MEANSAMPLETIME;
    public static double CONFIDENCELEVEL;
    public static double STDDEVSTOPCONDITION;

    public static final int NOFAILURE = 0, NOSUCCESS = 1, SAMPLEDSUCCESSRATE = 2, SAMPLEDFAILPROBABILITY = 3,
            TIME = 4, UPPCONF = 5, LOWCONF = 6;

    public static double time = 0; // Global time variable initialized to 0 for all Process Interaction simulations
}
