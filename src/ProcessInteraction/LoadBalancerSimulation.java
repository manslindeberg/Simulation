import java.util.ArrayList;

public class LoadBalancerSimulation {

    public static final int SIMULATIONTIME = 1000000;
    private static final double QUEUESERVICETIME = 0.5; // Exponential distribution
    private static final double DISPARRIVALTIME = 0.12; // Uniform distribution
    private static final int QUEUEBOUND = 5;
    private static final int MAX_MEASUREMENTS = (int) ((SIMULATIONTIME) / (Queue.OUTPUTRESOLUTION) + 1); // maximum number of measurement points

    public static void main(String[] args) {

        Signal actSignal;
        new SignalList();

        // Creates 5 Queue systems and adds it to an ArrayList
        ArrayList<Queue> queues = new ArrayList<>();
        for (int i = 1; i <= QUEUEBOUND; i++) {
            Queue queue = new Queue(QUEUESERVICETIME, i);
            queue.sendTo = null;
            queues.add(queue);
        }

        // Load-balancer / Dispatcher
        Dispatcher disp = new Dispatcher(queues, QUEUEBOUND, Global.LEASTJOBAMOUNT);

        Generator Generator = new Generator();
        Generator.interarrival = DISPARRIVALTIME;
        Generator.sendTo = disp;

        // Kick-starts simulations by starting generator and measurement
        SignalList.SendSignal(Global.READY, Generator, Global.time);
        queues.forEach(queue -> SignalList.SendSignal(Global.PRECISEMEASURE, queue, Global.time));
        queues.forEach(queue -> SignalList.SendSignal(Global.OUTPUTMEASURE, queue, Global.time));

        while (Global.time < SIMULATIONTIME) {
            actSignal = SignalList.FetchSignal();
            Global.time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        double totalMeanCustomers = 0;
        double totalMeanTime = 0;

        Measurement stats = new Measurement();

        double arrivalrate = 0;
        /* The mean number of customers per timeunit as well as the mean time in queue is calculated for each queue */
        for (Queue queue : queues) {
            double meanCustomers = (double) queue.accNoInQueue / queue.noMeasurements;
            double meanTime = queue.accQueueTime / queue.noMeasurements;

            totalMeanCustomers = totalMeanCustomers + meanCustomers;
            totalMeanTime = totalMeanTime + meanTime;
        }

        System.out.println("Mean number of customers in queuing system: " + totalMeanCustomers);
        System.out.println("Simulated time per customer in quing system: " + totalMeanTime/QUEUEBOUND);

        /* Little's Law */
        System.out.println("Theoretic time per customer in system: " + DISPARRIVALTIME * totalMeanCustomers);
        System.out.println("--------------------------------------------------------------------");
    }
}
