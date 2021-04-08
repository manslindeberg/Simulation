import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc implements Comparable<QS> {
    public int numberInQueue = 0, accNoInQueue, noMeasurements;
    public double timeBetweenArrivals = 0;
    public double accQueueTime = 0;
    public double accTimearrival = 0;

    public Proc sendTo;

    private double arrivalTime = 0;
    private double serviceTime;
    private double tempTime;

    Random rand = new Random();

    public QS(double serviceTime) {
        this.serviceTime = serviceTime;
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

            case MEASURE: {
                noMeasurements++;
                accTimearrival = accTimearrival + timeBetweenArrivals;
                if (numberInQueue != 0) {
                    accQueueTime = accQueueTime + 1.0*(numberInQueue-1)*timeBetweenArrivals;
                    accNoInQueue = accNoInQueue + (numberInQueue);
                }
                SignalList.SendSignal(MEASURE, this, time + 1 * rand.nextDouble());
            }
            break;
        }
    }


    public double nextDoubleExp(Random rand, double lambda) {
        return Math.log(1-rand.nextDouble()) * (-lambda);
    }

    @Override
    public int compareTo(QS queue) {
        return (Integer.compare(this.numberInQueue, queue.numberInQueue));
    }
}