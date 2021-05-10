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
    public boolean transmitting;

    private boolean inRange;
    private double[][] data;
    private NetworkArea area;

    public Sensor(double transmitTime, double meanSleepTime,
                  Gateway gateway, int index, NetworkArea area, double[][] data) {
        this.index = index;
        this.data = data;
        this.transmitTime = transmitTime;
        this.meanSleepTime = meanSleepTime;
        this.gateway = gateway;
        this.area = area;
        rand = new Random();
        transmitting = false;
    }

    public void TreatSignal(Signal signal) {
        switch (signal.signalType) {
            /* start transmitting session with gateway station */

            case INIT: {
                SignalList.SendSignal(TRANSMIT, this, this, time + nextDoubleExp(rand, meanSleepTime));
            }
            break;

            case TRANSMIT: {
                if (!inRange()) {
                    SignalList.SendSignal(DUMMYTRANSMIT, this, this, time + transmitTime);
                    System.out.println("not in range: " + x + " " + y);
                    transmitting = true;
                } else {
                    boolean channelBusy = false;

                    for (Sensor s: area.getSensors()) {
                        /* check if sensor is in range of sensing */
                        if (inRange(x, y, s.getX(), s.getY())) {
                            /* check if currently transmitting */
                            if (s.transmitting) {
                                channelBusy = true;
                                break;
                            }
                        }
                    }
                    if (channelBusy) {
                        SignalList.SendSignal(TRANSMIT, this, this, time + backOffTime());
                    } else {
                        SignalList.SendSignal(STARTRECEIVE, this, gateway, time);
                        SignalList.SendSignal(TRANSMIT, this, this, time + nextDoubleExp(rand, meanSleepTime));
                    }
                }
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

            /* this signal is used for sensors that will transmit signals that won't reach the gateway,
             * but still may cause other sensors to back-off their transmission */
            case DUMMYTRANSMIT: {
                transmitting = false;
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
        return (dist < getRadius());
    }

    private double backOffTime() {
        return (rand.nextDouble() * (Global.UPLIMIT - Global.LOWLIMIT) + Global.LOWLIMIT);
    }
}
