import java.util.ArrayList;

public class HomeAssignmentOne {

    private static final int SIMULATIONTIME = 100000;
    private static final double QUEUESERVICETIME = 0.5; // Exponential distribution
    private static final double DISPARRIVALTIME = 2.0; // Uniform distribution
    private static final int QUEUEBOUND = 5;

    public static void main(String[] args) {

        Signal actSignal;
        new SignalList();

        // Creates 5 Queue systems and adds it to an ArrayList
        ArrayList<QS> queues = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            QS queue = new QS(QUEUESERVICETIME);
            queue.sendTo = null;
            queues.add(queue);

        }

        // Load-balancer / Dispatcher
        Dispatcher disp = new Dispatcher(queues, QUEUEBOUND, Global.RANDOM);

        Gen Generator = new Gen();
        Generator.lambda = (1/DISPARRIVALTIME);
        Generator.sendTo = disp;

        // KICK-START
        SignalList.SendSignal(Global.READY, Generator, Global.time);
        queues.forEach(queue -> SignalList.SendSignal(Global.MEASURE, queue, Global.time));

        while (Global.time < SIMULATIONTIME) {
            actSignal = SignalList.FetchSignal();
            Global.time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        //TODO
        //- Make sure that the system follows Little's Law?
        for (QS queue : queues) {
            System.out.println("Mean number of customers in queuing system " + (queues.indexOf(queue) + 1) + " :" + 1.0 * queue.accumulated / queue.noMeasurements);
            System.out.println("--------------------------------------------------------------------");
        }
    }
}
