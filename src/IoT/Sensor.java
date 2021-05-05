import java.util.Random;

/* This clas represents a sensor that on average sends a package every "meanSleepTime"
* in which the transmission takes "transmitTime" seconds. Each sensor will log
* the number of failed and successful packets.  */
public class Sensor extends Proc {

    private int x;                      // x coordinate in network area in m
    private int y;                      // y coordinate in network area in m
    private int r;                      // unit disk graph coverage radius in m
    private double transmitTime;        // deterministic transmission time in s
    private double meanSleepTime;       // exponentially distributed intermediate sleep time in s
    final private Gateway gateway;      // communication gateway/basestation that packets are sent to

    private Random rand;
    private int index;

    private boolean inRange;
    private double[][] data;
    public Sensor(double transmitTime, double meanSleepTime,
                  Gateway gateway, int index, double[][] data) {
        this.index = index;
        this.data = data;
        this.transmitTime = transmitTime;
        this.meanSleepTime = meanSleepTime;
        this.gateway = gateway;
        rand = new Random();
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            /* start transmitting session with gateway station */

            case INIT: {
                SignalList.SendSignal(TRANSMIT, this, this, time + nextDoubleExp(rand, meanSleepTime));
            }
            break;

            case TRANSMIT: {
                SignalList.SendSignal(STARTRECEIVE, this, gateway, time);
                double sleep = nextDoubleExp(rand, meanSleepTime);
                SignalList.SendSignal(TRANSMIT, this, this, time
                        + sleep);
            }
            break;

            case TRANSMITFAILURE: {
                data[NOFAILURE][index]++;
            }
            break;

            case TRANSMITSUCCESS: {
                 data[NOSUCCESS][index]++;
            }
            break;
        }

    }

    public void setSleepTime(double mean) {
        meanSleepTime = mean;
    }

    private int getY() {
        return y;
    }

    private int getX() {
        return x;
    }

    private int getRadius() {
        return r;
    }

    /* Exponentially distributed random number with mean lambda */
    private double nextDoubleExp(Random rand, double lambda) {
        return Math.log(1 - rand.nextDouble()) * (-lambda);
    }

    /* Set sensor position with uniformly random coordinates */
    public void setRandomPosition(int maxX, int maxY) {
        x = rand.nextInt(maxX);
        y = rand.nextInt(maxY);
    }

    /* Set sensor position at coordinate sensorsX, sensorY */
    public void setPosition(int sensorX, int sensorY) {
        x = sensorX;
        y = sensorY;
    }

    public void setRadius(int radius) {
        r = radius;
    }

    /* to check whether a sensor is even in range of the gateway or not*/
    public boolean inRange() {
        inRange = inRange(getX(), getY(), gateway.getX(), gateway.getY());
        return inRange;
    }

    /* Determines if the sensor is in range of the gateway or not */
    private boolean inRange(int sensorX, int sensorY, int gatewayX, int gatewayY) {
        double dist = Math.sqrt(Math.pow(sensorX - gatewayX, 2) + Math.pow(sensorY - gatewayY, 2));
        return  (dist < getRadius());
    }
}
