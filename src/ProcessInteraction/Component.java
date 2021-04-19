import java.util.Random;

/* This class implements a simple component with a expected lifetime between 1 and 5 time units.
 *  It's state is either "failed" or not and is organized with a index that defines the component number. */
public class Component extends Proc {

    /* Determines the life-time interval between upper and lower bound. */
    public static final int UPPERBOUND = 5;
    public static final int LOWERBOUND = 1;

    public Proc sendTo;
    public boolean failed;

    private Random rand = new Random();
    private int index; // index can only be written when component is created.

    public Component(int index) {
        this.index = index;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            // Initializes the lifetime with a uniform distribution between UPPER & LOWER BOUND
            case START: {
                failed = false;
                double failTime = (UPPERBOUND - (UPPERBOUND - LOWERBOUND) * rand.nextDouble());
                SignalList.SendSignal(FAIL, this, time + failTime);
            }
            break;

            // Prints the new state together with it's index and sets failed to true
            case FAIL: {
                if (!failed) {
                    // System.out.println("Component " + (index + 1) + " down. Time: " + Global.time);
                }
                failed = true;
            }
            break;
        }
    }
}


