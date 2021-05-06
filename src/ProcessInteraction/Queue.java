import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Queue extends Proc implements Comparable<Queue> {

    public static final double OUTPUTRESOLUTION = 100; // measurement resolution
    private static final double PRECISERESOLUTION = 0.1; // measurement resolution

    public int numberInQueue = 0, accNoInQueue = 0, noMeasurements = 0, outputMeasurements = 0;
    public double timeInQueue = 0;
    public double accQueueTime = 0;
    public Proc sendTo;

    private double serviceTime;
    private int index;
    private ArrayList<Double> queueTime;

    Random rand = new Random();

    public Queue(double serviceTime, int index) {
        this.serviceTime = serviceTime;
        this.index = index;
        queueTime = new ArrayList<>();
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            case ARRIVAL: {
                queueTime.add(time);
                numberInQueue++;
                // this is when queueing starts --> timestamp x.source sent it which was the dispatcher
                if (numberInQueue == 1 ) {
                    SignalList.SendSignal(READY, this,time + nextDoubleExp(rand, serviceTime));
                }
            }
            break;

            case READY: {
                numberInQueue--;
                timeInQueue = time - queueTime.get(0).doubleValue();
                if (timeInQueue > 30) {
                    System.out.println(timeInQueue);
                }
                queueTime.remove(0);

                // this is when we serve it -> the timestamp of when it arrival can be found from source
                if (sendTo != null) {
                    SignalList.SendSignal(ARRIVAL,null, time);
                }
                if (numberInQueue > 0) {
                    SignalList.SendSignal(READY, this, time + nextDoubleExp(rand, serviceTime));
                }
            }
            break;

            case PRECISEMEASURE: {
                noMeasurements++;
                accQueueTime = accQueueTime + timeInQueue;
                accNoInQueue = accNoInQueue + numberInQueue;
                SignalList.SendSignal(PRECISEMEASURE, this, time + 2*PRECISERESOLUTION * rand.nextDouble());
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