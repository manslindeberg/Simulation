import java.util.ArrayList;
import java.util.Random;

public class Controller extends Proc {

    public Proc sendTo;
    public boolean systemUp;
    public double systemUpTime;

    private Random rand = new Random();
    private ArrayList<Component> components;

    private boolean oneFailed = false;
    private boolean threeFailed = false;

    public Controller(ArrayList<Component> compontents) {
        this.components = compontents;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {

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

                if (!(oneFailed && threeFailed)) {
                    SignalList.SendSignal(CONTROL, this, time + 1);
                }
            }
            break;

            case MEASURE: {
                systemUp = false;

                int index = 0;
                for (Component component : components) {
                    index++;
                    if (!component.failed) {
                        systemUp = true;
                        SignalList.SendSignal(MEASURE, this, time + 1);
                    } else {
                        //nothing happens
                    }
                }

                if (systemUp == false) {
                    systemUpTime = time;
                }
            }
            break;
        }
    }
}
