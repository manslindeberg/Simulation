import java.util.ArrayList;
import java.util.Random;

/* This class defines a "Controller" that controls a set of rules applied on a system of "Components". In this
 * scenario, the controller controls if either component with index 1 or 3 have failed. If 1 have failed,
 * then also 2 & 5 fails and if 3 fails then also 4 fails. It then sends a FAIL signal to the components. */

public class Controller extends Proc {

    private static final double RESOLUTION = 0.001;

    public double systemUpTime;

    private ArrayList<Component> components; // Contains a list of components that the controller keeps track of
    private boolean oneFailed = false;       // State variable that keeps track of if component 1 has failed before control or not
    private boolean threeFailed = false;     // State variable that keeps track of if component 3 has failed before control or not

    /* Constructor*/
    public Controller(ArrayList<Component> components) {
        this.components = components;
    }

    /* Treats signal type CONTROL and MEASURE that arrives to this process */
    public void TreatSignal(Signal x) {
        Random rand = new Random();
        switch (x.signalType) {

            // Controls the component system rules
            case CONTROL: {

                // If Component 1 HASN't failed
                if (!oneFailed) {
                    if (components.get(0).failed) {
                        if (!components.get(1).failed) {
                            SignalList.SendSignal(FAIL, components.get(1), time);
                        }

                        if (!components.get(4).failed) {
                            SignalList.SendSignal(FAIL, components.get(4), time);
                        }
                        oneFailed = true;
                    }
                }

                // If Component three HASN't failed
                if (!threeFailed) {
                    if (components.get(2).failed) {
                        if (!components.get(3).failed) {
                            SignalList.SendSignal(FAIL, components.get(3), time);
                        }
                        threeFailed = true;
                    }
                }

                // As long as either Component 1 and 3 is running we keep controlling the system
                if (!(oneFailed && threeFailed)) {
                    SignalList.SendSignal(CONTROL, this, time + 2 * RESOLUTION * rand.nextDouble());
                }
            }
            break;

            case MEASURE: {
                boolean systemUp = false;

                // Measures if there's any component that hasn't failed in time of measurement
                for (Component component : components) {
                    if (!component.failed) {
                        systemUp = true;
                        SignalList.SendSignal(MEASURE, this, time + 2 * RESOLUTION * rand.nextDouble());
                        break;
                    }
                }

                if (!systemUp) {
                    systemUpTime = time;

                    /* Verification: Fastest and slowest up-time per system run is determined by the UPPER and LOWER bounds.
                     * In assignment 1 - task 7 - this corresponds to 1 and 5 .*/
                    if (systemUpTime <= Component.LOWERBOUND || systemUpTime >= Component.UPPERBOUND) {
                        systemUpTime = 0;
                    }
                }
            }
            break;
        }
    }
}
