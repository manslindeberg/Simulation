import java.util.Random;
import java.util.*;

public class Component extends Proc {

    public Proc sendTo;
    public boolean failed;

    private Random rand = new Random();

    private int index;

    public Component(int index) {
        this.index = index;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            case START: {
                failed = false;
                double failTime = (5 - rand.nextInt(4));
                SignalList.SendSignal(FAIL, this, time + failTime);
            }
            break;

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


