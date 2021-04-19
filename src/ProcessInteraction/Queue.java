import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Queue extends Proc implements Comparable<Queue> {

    public static final double OUTPUTRESOLUTION = 100; // measurement resolution
    private static final double PRECISERESOLUTION = 1; // measurement resolution

    public int numberInQueue = 0, accNoInQueue = 0, noMeasurements = 0, outputMeasurements = 0;
    public double timeBetweenArrivals = 0;
    public double accQueueTime = 0;
    public double accTimearrival = 0;
    public Proc sendTo;

    private double[][] data;
    private double arrivalTime = 0;
    private double serviceTime;
    private int index;

    Random rand = new Random();

    public Queue(double serviceTime, int index, double[][] data) {
        this.serviceTime = serviceTime;
        this.index = index;
        this.data = data;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            case ARRIVAL: {
                timeBetweenArrivals = Global.time - arrivalTime;
                arrivalTime = Global.time;
                numberInQueue++;
                if (numberInQueue == 1) {
                    SignalList.SendSignal(READY, this, time + nextDoubleExp(rand, serviceTime));
                }
            }
            break;

            case READY: {
                numberInQueue--;
                if (sendTo != null) {
                    SignalList.SendSignal(ARRIVAL, sendTo, time);
                }
                if (numberInQueue > 0) {
                    SignalList.SendSignal(READY, this, time + nextDoubleExp(rand, serviceTime));
                }
            }
            break;

            case PRECISEMEASURE: {
                noMeasurements++;
                accTimearrival = accTimearrival + timeBetweenArrivals;

                if (numberInQueue != 0) {
                    accQueueTime = accQueueTime + 1.0 * (numberInQueue - 1) * timeBetweenArrivals;
                    accNoInQueue = accNoInQueue + numberInQueue;
                }
                SignalList.SendSignal(PRECISEMEASURE, this, time + PRECISERESOLUTION * rand.nextDouble());
            }
            break;

            case OUTPUTMEASURE: {
                outputMeasurements++;
                data[2 * index - 2][outputMeasurements] = numberInQueue;
                data[2 * index - 1][outputMeasurements] = time;

                if (time + OUTPUTRESOLUTION < LoadBalancerSimulation.SIMULATIONTIME) {
                    SignalList.SendSignal(OUTPUTMEASURE, this, time + OUTPUTRESOLUTION);
                }
            }
            break;
        }

    }


    public double nextDoubleExp(Random rand, double lambda) {
        return Math.log(1 - rand.nextDouble()) * (-lambda);
    }

    @Override
    public int compareTo(Queue queue) {
        return (Integer.compare(this.numberInQueue, queue.numberInQueue));
    }
}