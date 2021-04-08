import java.util.ArrayList;

public class HomeAssignmentOne {

    private static final int SIMULATIONTIME = 200000;
    private static final double QUEUESERVICETIME = 0.5; // Exponential distribution
    private static final double DISPARRIVALTIME = 0.12; // Uniform distribution
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
        Dispatcher disp = new Dispatcher(queues, QUEUEBOUND, Global.ROUNDROBIN);

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
        double totalMeanCustomers = 0;
        double totalMeanTime = 0;

        for (QS queue : queues) {
            double meanCustomers = 1.0 * queue.accNoInQueue / queue.noMeasurements;
            double meanTime = 1.0 * ((queue.accQueueTime / queue.noMeasurements) + QUEUESERVICETIME);
            System.out.println("Mean number of customers in queuing system " + (queues.indexOf(queue) + 1) + " :" + meanCustomers);
            System.out.println("Mean time per customer in quing system " + (queues.indexOf(queue) + 1) + " :" + meanTime);
            System.out.println("Mean arrival time per queue:" + 1.0*(queue.accTimearrival)/ queue.noMeasurements);
            System.out.println("--------------------------------------------------------------------");

            totalMeanCustomers = totalMeanCustomers + meanCustomers;
            totalMeanTime = totalMeanTime + meanTime;
        }

        totalMeanTime = totalMeanTime/QUEUEBOUND;
        System.out.println(" Little's Law - Effective rate = " + DISPARRIVALTIME + " L/W = " + 1/(totalMeanCustomers/totalMeanTime));

    }
}
