import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// This class implements a
public class Dispatcher extends Proc {

    public Proc sendTo;

    // DEFINITIONS
    private int mode;
    private int bound;
    private int roundRobinIndex = 0;

    private ArrayList<Queue> queues;

    private Random randIndex = new Random();

    public Dispatcher(ArrayList<Queue> queues, int bound, int mode) {
        this.queues = queues;
        this.bound = bound;
        this.mode = mode;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case ARRIVAL: {
                switch (mode) {
                    case RANDOM: {
                        sendTo = queues.get(randIndex.nextInt(bound));
                        SignalList.SendSignal(ARRIVAL, sendTo, time);
                    }
                    break;

                    case ROUNDROBIN: {
                        roundRobinIndex = (roundRobinIndex + 1) % bound;
                        sendTo = queues.get(roundRobinIndex);
                        SignalList.SendSignal(ARRIVAL, sendTo, time);
                    }
                    break;

                    case LEASTJOBAMOUNT: {

                        // sort queues by the number of jobs in queue in ascending order
                        Collections.sort(queues);

                        // queues with equal number of jobs in queue is uniformly distributed
                        int i = 0;
                        while (i < bound - 1) {
                            if (queues.get(i).numberInQueue != queues.get(i + 1).numberInQueue) {
                                break;
                            }
                            i++;
                        }
                        if (i == 0) {
                            sendTo = queues.get(0);
                        } else {
                            sendTo = queues.get(randIndex.nextInt(i));
                        }
                        SignalList.SendSignal(ARRIVAL, sendTo, time);
                    }
                    break;
                }
            }
        }
    }
}

