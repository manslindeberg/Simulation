import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Queue extends Proc implements Comparable<Queue> {
    public int numberInQueue = 0, accNoInQueue;
    public double accQueueTime, accTimeBetweenArrival;
    public int endingNoMeasurements = 0;
    public int queueNoMeasurements = 0;

    public double finishedQueue = 0, accFinishedQueue;
    public Proc sendTo;

    private double arrivalTime = OPENING;
    private double timeBetweenArrival;

    Random rand = new Random();

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            case ARRIVAL: {

                timeBetweenArrival = Global.time - arrivalTime;
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

                if (Global.time % 24 > Global.CLOSING && numberInQueue == 0) {

                    endingNoMeasurements++;
                    finishedQueue = Global.time % 24;
                    accFinishedQueue = accFinishedQueue + finishedQueue;

                    // If we have measured the last customer of the day, the new measurement signal is sent at next days opening time
                    SignalList.SendSignal(MEASURE, this, time + (HOURSPERDAY - OPENING));
                    arrivalTime = time + (HOURSPERDAY - OPENING);
                } else {
                    queueNoMeasurements++;
                    accTimeBetweenArrival = accTimeBetweenArrival + timeBetweenArrival;
                    if (numberInQueue != 0) {
                        accQueueTime = accQueueTime + 1.0 * (numberInQueue - 1) * timeBetweenArrival;
                        accNoInQueue = accNoInQueue + (numberInQueue);
                    }
                    SignalList.SendSignal(MEASURE, this, time + 0.3 * rand.nextDouble());
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