import java.io.IOException;
import java.util.ArrayList;

public class FailSimulation {

    private static final int SIMULATIONRUNS = 1000;

    public static void main(String[] args) throws IOException {

        int round = 0;
        double accSystemUpTime = 0;

        while (round < SIMULATIONRUNS) {
            Global.time = 0;

            Signal actSignal;
            new SignalList();

            ArrayList<Component> components = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Component component = new Component(i);
                components.add(component);
                SignalList.SendSignal(Global.START, component, Global.time);
            }

            Controller controller = new Controller(components);
            SignalList.SendSignal(Global.CONTROL, controller, Global.time + 1);
            SignalList.SendSignal(Global.MEASURE, controller, Global.time + 1);


            while (controller.systemUpTime == 0) {
                actSignal = SignalList.FetchSignal();
                Global.time = actSignal.arrivalTime;
                actSignal.destination.TreatSignal(actSignal);
            }
            System.out.println("System Up time Round " + round + ": " + controller.systemUpTime);
            accSystemUpTime = accSystemUpTime + controller.systemUpTime;
            round++;
        }

        System.out.println("Average System Up-Time:" + accSystemUpTime/round);
    }
}
