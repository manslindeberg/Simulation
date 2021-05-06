import java.util.ArrayList;
import java.util.Arrays;

/* This class describes a communication area with a certain area that contains sensors and
*  gateways that can be used for communication. */
public class NetworkArea {

    private int areaX;                      // width of area
    private int areaY;                      // height of area
    private int noSensors;                  // number of sensors in area
    private Gateway gateway;                // gateway in area (only supports 1 per area) but more areas can be used
    private double[][] measurements;        // global data matrix that stores measure values in area
    private Measurement samples;            // measurement object

    public NetworkArea(int areaX, int areaY, int noSensors) {
        this.areaX = areaX;
        this.areaY = areaY;
        this.noSensors = noSensors;
        /* contains failures & sucess for each sensors + sampled failure & success rate
        + upper and lower confidence interval for sampled success rate */
        measurements = new double[7][noSensors];
        samples = new Measurement();
    }

    /* Initializes Communication Area */
    public void init() {
        addGateway(this.areaX / 2, this.areaY / 2); // placed in center of area
        for (int index = 0; index < noSensors; index++) {
            addSensor(index); // adds sensor to random position within the city
        }
        SignalList.SendSignal(Global.CONTROL, gateway, gateway, Global.time);
        SignalList.SendSignal(Global.SAMPLE, gateway, gateway, Global.time + 100);
    }

    /* Adds a gateway to area with position x and y */
    public void addGateway(int x, int y) {
        if (x > areaX) {
            System.out.println("Gateway X-coordinate is outside the area boundries. Exiting...");
            System.exit(0);
        }

        if (y > areaY) {
            System.out.println("Gateway Y-coordinate is outside the area boundries. Exiting...");
            System.exit(0);
        }
        gateway = new Gateway(x, y, measurements);
    }

    /* Sets the gateway coordinate - currently only support for 1 gateway per network Area */
    public void setGateway(Gateway gw) {
        if (gw.getX() > areaX) {
            System.out.println("Gateway X-coordinate is outside the area boundries. Exiting...");
            System.exit(0);
        }

        if (gw.getY() > areaY) {
            System.out.println("Gateway Y-coordinate is outside the area boundries. Exiting...");
            System.exit(0);
        }
        gateway = gw;
    }

    /* Adds a sensor at position x and y with index i */
    public void addSensor(int x, int y, int index) {
        if (gateway == null) {
            System.out.println("No gateway is assigned to the area. Please assign a gateway to the Network Area" +
                    "before adding sensors.");
            System.exit(0);
        }

        if (x > areaX) {
            System.out.println("Sensor X-coordinate is outside the area boundaries. Exiting...");
            System.exit(0);
        }

        if (y > areaY) {
            System.out.println("Sensor Y-coordinate is outside the area boundaries. Exiting...");
            System.exit(0);
        }
        Sensor s = new Sensor(Global.TRANSMITTIME, Global.MEANINTERARRIVALTIME, gateway, index, measurements);
        s.setRadius(Global.COMMUNICATIONRANGE);
        s.setPosition(x, y);
    }

    /* Adds a sensor with index i at a uniform random position between 0 and AREAHEIGHT/AREAWIDTH */
    public void addSensor(int index) {
        if (gateway == null) {
            System.out.println("No gateway is assigned to the area. Please assign a gateway to the Network Area" +
                    "before adding sensors.");
            System.exit(0);
        }
        Sensor s = new Sensor(Global.TRANSMITTIME, Global.MEANINTERARRIVALTIME, gateway, index, measurements);
        s.setRandomPosition(areaX, areaY);
        s.setRadius(Global.COMMUNICATIONRANGE);
        if (s.inRange()) {
            SignalList.SendSignal(Global.INIT, s, s, Global.time); // kick-starts the sensor transmission scheme
        }
    }

    /* Stops simulation if standard deviation stop condition is met */
    public boolean stopSimulation(double stopDeviation, int round) {
        int size = gateway.getNoSamples();

        if (size > 100) {
            double[] tmp = new double[size];

            /* copy number of variables to tmp array */
            for (int i = 0; i < size; i++) {
                tmp[i] = measurements[Global.SAMPLEDSUCCESSRATE][i];
            }

            double sampledMean = samples.mean(tmp);
            double sampledDev = samples.standardDeviation(tmp, sampledMean);

            if ((sampledDev / Math.sqrt(size)) < stopDeviation) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* Update the confidence interval for each and write to measurements matrix */
    public void writeConfidenceInterval(int round) {
        int size = gateway.getNoSamples();
        double[] tmp = new double[size];

        /* copy number of variables to tmp array */
        for (int i = 0; i < size; i++) {
            tmp[i] = measurements[Global.SAMPLEDFAILPROBABILITY][i];
        }

        double sampledMean = samples.mean(tmp);
        double[] conf = samples.confidenceInterval(tmp, sampledMean, Global.CONFIDENCELEVEL);
        measurements[Global.LOWCONF][0] = conf[0];
        measurements[Global.UPPCONF][0] = conf[1];
    }

    /* Prints out the data into a MATLAB file */
    public void printDataToMatlab(int round) {
        String[] var = {"nofailure" + Integer.toString(round), "nosuccess" + Integer.toString(round),
                "averagesuccesrate" + Integer.toString(round), "averagefailrate" + Integer.toString(round),
                "time" + Integer.toString(round), "uppconf" + Integer.toString(round),
                "lowconf" + Integer.toString(round)};
        samples.printDataToFile("wireless_simulation" + Integer.toString(round) + ".m", var, measurements);
    }

    /*
    public boolean confidenceLevelOverlap() {
        oldConfidenceInterval = newConfidenceInterval;
        double meanNoSuccess = samples.mean(measurements[Global.NOSUCCESS]);
        newConfidenceInterval = samples.confidenceInterval(measurements[Global.NOSUCCESS], meanNoSuccess,
                Global.CONFIDENCELEVEL);

        measurements[2][noConfidenceIntervals] = newConfidenceInterval[1]; // upper confidence level
        measurements[3][noConfidenceIntervals] = newConfidenceInterval[0]; // lower confidence level
        noConfidenceIntervals++;

        if (newConfidenceInterval[0] > oldConfidenceInterval[1]
        || newConfidenceInterval[1] < oldConfidenceInterval[0]) {
            return false;
        } else {
            return true;
        }
    }
   */
}