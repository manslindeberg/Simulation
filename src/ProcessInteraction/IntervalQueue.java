import java.util.*;

/* This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
   signal names without dot notation */

class IntervalQueue extends Proc implements Comparable<IntervalQueue> {

    public int numberInQueue = 0, accNoInQueue = 0;
    public double accQueueTime = 0, accTimeBetweenArrival = 0;
    public int arrivalNoMeasurements = 0;
    public int endingNoMeasurements = 0;
    public int queueNoMeasurements = 0;
    public double finishedQueue = 17, accFinishedQueue;
    public Proc sendTo;

    private double arrivalTime = OPENING;
    private double timeBetweenArrival;
    private double[] data;

    Random rand = new Random();

    public IntervalQueue(double[] data) {
        this.data = data;
    }
    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            case ARRIVAL: {
                timeBetweenArrival = Global.time - arrivalTime; // Variable that stores the time between when ARRIVAL was latest called
                arrivalTime = Global.time;
                numberInQueue++;

                if (numberInQueue == 1) {
                    SignalList.SendSignal(READY, this, time + (20 - 10 * rand.nextDouble()) / 60);
                }
            }
            break;

            case READY: {
                numberInQueue--;
                if (sendTo != null) {
                    SignalList.SendSignal(ARRIVAL, sendTo, time);
                }
                if (numberInQueue > 0) {
                    SignalList.SendSignal(READY, this, time + (20 - 10 * rand.nextDouble()) / 60);
                }
            }
            break;

            case MEASURE: {
                /* Measures the "ending service time" only when the store is closed. The last job is then
                 * measured when there's no customers in the queue. */
                if (Global.time % 24 > Global.CLOSING && numberInQueue == 0) {
                    endingNoMeasurements++;
                    finishedQueue = Global.time % 24;
                    SignalList.SendSignal(OUTPUTMEASURE, this, time);
                    accFinishedQueue = accFinishedQueue + finishedQueue;

                    // If we have measured the last customer of the day, the new measurement signal is sent at next days opening time
                    double next = this.nextDoubleExp(rand, 4);
                    SignalList.SendSignal(MEASURE, this, time + (HOURSPERDAY - (time % 24) + OPENING) + next);
                    arrivalTime = time + (HOURSPERDAY - (time % 24) + OPENING);
                } else {
                    /* Measures the number of customers in the queue only during the opening hours & when theres jobs left in the queue. */
                    queueNoMeasurements++;

                    if (Global.time % 24 < Global.CLOSING) {
                        accTimeBetweenArrival = accTimeBetweenArrival + timeBetweenArrival; // Accumulated time between arrival
                        arrivalNoMeasurements++;
                    }

                    if (numberInQueue != 0) {
                        accNoInQueue = accNoInQueue + (numberInQueue);
                    }
                    SignalList.SendSignal(MEASURE, this, time + 2 * 0.0167 * rand.nextDouble());
                }
            }

            case OUTPUTMEASURE: {
               data[endingNoMeasurements] = finishedQueue;
            }
            break;
        }
    }

    /* Function returns a exponentially distributed number with a mean rate of lambda */
    public double nextDoubleExp(Random rand, double lambda) {
        return Math.log(1 - rand.nextDouble()) / (-lambda);
    }

    /* Overrides comparable based on number of jobs in queue */
    @Override
    public int compareTo(IntervalQueue queue) {
        return (Integer.compare(this.numberInQueue, queue.numberInQueue));
    }
}