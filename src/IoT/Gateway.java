import java.util.ArrayList;
import java.util.Random;

/* This class represents a gateway/ basestation which sensors transmits their packages to. */
public class Gateway extends Proc {
    private int x; // x coordinate in network area
    private int y; // y coordinate in network area

    /* List of transmitting (active sensors) - note that the gateway only can
    receive one sensor message at a time. Since we still want a sensor to be
    able to interfere during the transmit time but still not be counted several
    times, these have been divided to failedSensors and activeSensors. */
    private ArrayList<Proc> activeSensors;
    private ArrayList<Proc> failedSensors;

    private double[][] samples;
    private Random seed;
    private double accFailure = 0;
    private double accMeanFailRate = 0;
    private double accSucess = 0;
    private double accMeanSucessRate = 0;
    private double meanFailProbability = 0;
    private int noSamples = 0;


    public Gateway(int x, int y, double[][] samples) {
        this.x = x;
        this.y = y;
        this.samples = samples;
        seed = new Random();
        activeSensors = new ArrayList<>();
        failedSensors = new ArrayList<>();
    }

    public void TreatSignal(Signal x) {

        switch (x.signalType) {

            /* gateway has received the start of a sensor transmission */
            case STARTRECEIVE: {
                assert x.source instanceof Sensor;
                activeSensors.add(x.source);
                SignalList.SendSignal(STOPRECEIVE, x.source, this, time + TRANSMITTIME);
            }
            break;

            /* gateway has ended the transmission session with the sensor */
            case STOPRECEIVE: {
                assert x.source instanceof Sensor;
                if (!failedSensors.contains(x.source)) {
                    SignalList.SendSignal(TRANSMITSUCCESS, this, x.source, time);
                    accSucess++;
                } else {
                    failedSensors.remove(x.source);
                }
                activeSensors.remove(x.source); // remove from active sensors after deterministic TRANSMITTIME
            }
            break;

            /* gateway is controlling if there's any interfering transmissions */
            case CONTROL: {
                if (activeSensors.size() > 1) {
                    /* mark every interfering transmissions as failed */
                    for (Proc sensor : activeSensors) {
                        if (!failedSensors.contains(sensor)) {
                            SignalList.SendSignal(TRANSMITFAILURE, this, sensor, time);
                            accFailure++;
                            failedSensors.add(sensor);
                        }
                    }
                }
                SignalList.SendSignal(CONTROL, this, this, time + Global.CONTROLTIME);
            }
            break;

            case SAMPLE: {
                meanFailProbability = accFailure / (accSucess + accFailure);
                accMeanFailRate = accFailure / Global.time;
                accMeanSucessRate = accSucess / Global.time;
                samples[Global.SAMPLEDFAILPROBABILITY][noSamples] = meanFailProbability;
                samples[Global.SAMPLEDSUCCESSRATE][noSamples] = accMeanSucessRate;
                samples[Global.TIME][noSamples] = time;
                noSamples++;
                SignalList.SendSignal(SAMPLE, this, this, time + 2*Global.MEANSAMPLETIME * seed.nextDouble());
            }
            break;
        }
    }
    public int getNoSamples() {
        return noSamples;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void updateSeed() {
        seed = new Random();
    }

}
