import java.util.ArrayList;
import java.util.Random;

/* This class defines a "Controller" that controls a set of rules applied on a system of "Components". In this
* scenareio, the controller controls if either component with index 1 or 3 have failed. If 1 have failed,
* then also 2 & 5 fails and if 3 fails then also 4 fails. It then sends a FAIL signal to the components. */

public class Controller extends Proc {

    public boolean systemUp;
    public double systemUpTime;

    private ArrayList<Component> components;
    private boolean oneFailed = false;  // State variable that keeps track of if component 1 has failed before control or not
    private boolean threeFailed = false; // State variable that keeps track of if component 3 has failed before control or not

    /* Constructor*/
    public Controller(ArrayList<Component> compontents) {
        this.components = compontents;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

            // Controls the component system rules
            case CONTROL: {

                if (!oneFailed) {
                    if (components.get(0).failed) {
                        System.out.println("1 failed--> 2 & 5 fail");

                        if (!components.get(1).failed) {
                            SignalList.SendSignal(FAIL, components.get(1), time);
                        }

                        if (!components.get(4).failed) {
                            SignalList.SendSignal(FAIL, components.get(4), time);
                        }
                        oneFailed = true;
                    }
                }

                if (!threeFailed) {
                    if (components.get(2).failed) {
                        System.out.println("3 failed--> 4 fail");
                        if (!components.get(3).failed) {
                            SignalList.SendSignal(FAIL, components.get(3), time);
                        }
                        threeFailed = true;
                    }
                }
                // When both component 1 and 3 have failed we stop controlling the system
                if (!(oneFailed && threeFailed)) {
                    SignalList.SendSignal(CONTROL, this, time + 1);
                }
            }
            break;

            case MEASURE: {
                systemUp = false;

                // Measures if there's any component that hasn't failed in time of measurement
                for (Component component : components) {
                    if (!component.failed) {
                        systemUp = true;
                        SignalList.SendSignal(MEASURE, this, time + 1);
                    }
                }

                if (!systemUp) {
                    systemUpTime = time;
                }
            }
            break;
        }
    }
}
