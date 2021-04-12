import java.util.ArrayList;

public class LoadBalancerSimulation {

    private static final int SIMULATIONTIME = 200000;
    private static final double QUEUESERVICETIME = 0.5; // Exponential distribution
    private static double DISPARRIVALTIME = 0.12; // Uniform distribution
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
        Dispatcher disp = new Dispatcher(queues, QUEUEBOUND, Global.LEASTJOBAMOUNT);

        Gen Generator = new Gen();
        Generator.lambda = (1 / DISPARRIVALTIME);
        Generator.sendTo = disp;

        // Kick-starts simulations by starting generator and measurement
        SignalList.SendSignal(Global.READY, Generator, Global.time);
        queues.forEach(queue -> SignalList.SendSignal(Global.MEASURE, queue, Global.time));

        while (Global.time < SIMULATIONTIME) {
            actSignal = SignalList.FetchSignal();
            Global.time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        double totalMeanCustomers = 0;
        double totalMeanTime = 0;
        double totalMeanArrivalTime = 0;

        /* The mean number of customers per timeunit aswell as the mean time in queue is calculated for each queue */
        for (QS queue : queues) {
            double meanCustomers = 1.0 * queue.accNoInQueue / queue.noMeasurements;
            double meanTime = 1.0 * ((queue.accQueueTime / queue.noMeasurements) + QUEUESERVICETIME);
            double meanArrivalTime = 1.0 * (queue.accTimearrival) / queue.noMeasurements;

            totalMeanCustomers = totalMeanCustomers + meanCustomers;
            totalMeanTime = totalMeanTime + meanTime;
            totalMeanArrivalTime = totalMeanArrivalTime + meanArrivalTime;
        }

        System.out.println("Mean number of customers in queuing system: " + totalMeanCustomers/QUEUEBOUND);
        System.out.println("Mean time per customer in quing system: " + totalMeanTime/QUEUEBOUND);
        System.out.println("Mean arrival time per queue:" + totalMeanArrivalTime/QUEUEBOUND);
        System.out.println("--------------------------------------------------------------------");
        totalMeanTime = totalMeanTime / QUEUEBOUND; // mean time in queue per job is averaged over all queues

        /* Little's Law */
        System.out.println(" Little's Law - Effective rate = " + DISPARRIVALTIME + " L/W = " + 1 / (totalMeanCustomers / totalMeanTime));

    }
}
