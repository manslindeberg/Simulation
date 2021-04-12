import java.util.Random;
import java.util.*;

/* This class implements a simple component with a expected lifetime between 1 and 5 time units.
*  It's state is either "failed" or not and is organized with a index that defines the component number. */
public class Component extends Proc {

    public Proc sendTo;
    public boolean failed;

    private Random rand = new Random();
    private int index; // index can only be written when component is created.

    public Component(int index) {
        this.index = index;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            // Initializes the lifetime with a uniform distribution between 5 and 1
            case START: {
                failed = false;
                double failTime = (5 - rand.nextInt(4));
                SignalList.SendSignal(FAIL, this, time + failTime);
            }
            break;

            // Sets the component state to "failed" and prints the new state together with it's index
            case FAIL: {
                if (!failed) {
                    System.out.println("Component " + (index + 1) + " down. Time: " + Global.time);
                }
                failed = true;
            }
            break;
        }
    }
}


