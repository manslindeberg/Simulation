import java.util.ArrayList;

public class LoadBalancerSimulation {

    public static final int SIMULATIONTIME = 100000;
    private static final double QUEUESERVICETIME = 0.5; // Exponential distribution
    private static final double DISPARRIVALTIME = 0.12; // Uniform distribution
    private static final int QUEUEBOUND = 5;
    private static final int MAX_MEASUREMENTS = (int) ((SIMULATIONTIME) / (Queue.OUTPUTRESOLUTION) + 1); // maximum number of measurement points

    public static void main(String[] args) {

        Signal actSignal;
        new SignalList();

        double[][] queueData = new double[2 * QUEUEBOUND][MAX_MEASUREMENTS];

        // Creates 5 Queue systems and adds it to an ArrayList
        ArrayList<Queue> queues = new ArrayList<>();
        for (int i = 1; i <= QUEUEBOUND; i++) {
            Queue queue = new Queue(QUEUESERVICETIME, i, queueData);
            queue.sendTo = null;
            queues.add(queue);
        }

        // Load-balancer / Dispatcher
        Dispatcher disp = new Dispatcher(queues, QUEUEBOUND, Global.RANDOM);

        Generator Generator = new Generator();
        Generator.lambda = (1 / DISPARRIVALTIME);
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
        double totalMeanArrivalTime = 0;

        Measurement stats = new Measurement();

        /* The mean number of customers per timeunit as well as the mean time in queue is calculated for each queue */
        for (Queue queue : queues) {
            double meanCustomers = (double) queue.accNoInQueue / queue.noMeasurements;
            double meanTime = (1.0 * (queue.accQueueTime / queue.noMeasurements) + QUEUESERVICETIME);
            double meanArrivalTime = 1.0 * (queue.accTimearrival) / queue.noMeasurements;

            totalMeanCustomers = totalMeanCustomers + meanCustomers;
            totalMeanTime = totalMeanTime + meanTime;
            totalMeanArrivalTime = totalMeanArrivalTime + meanArrivalTime;
        }

        // Averaging over the five queue for system mean times
        totalMeanCustomers = totalMeanCustomers / QUEUEBOUND;
        totalMeanTime = totalMeanTime / QUEUEBOUND;
        totalMeanArrivalTime = totalMeanArrivalTime / QUEUEBOUND;

        System.out.println("Mean number of customers in queuing system: " + totalMeanCustomers);
        System.out.println("Mean time per customer in quing system: " + totalMeanTime);
        System.out.println("Mean arrival time per queue:" + totalMeanArrivalTime);

        /* Little's Law */
        System.out.println(" Little's Law - Effective rate = " + DISPARRIVALTIME + " L/W = " + 1 / (totalMeanCustomers / totalMeanTime) / QUEUEBOUND);
        System.out.println("--------------------------------------------------------------------");

        double accmeanvariance = 0;
        for (int i = 0; i < 2*QUEUEBOUND; i += 2) {
            stats.printMean(stats.mean(queueData[i]));
            accmeanvariance = accmeanvariance + stats.variance(queueData[i], totalMeanCustomers);
            stats.printVariance(stats.variance(queueData[i], totalMeanCustomers));
            stats.printConfidenceInterval(stats.confidenceInterval(queueData[i], totalMeanCustomers, 99), totalMeanCustomers, 99);
        }

        System.out.println("mean var" + accmeanvariance/5);

        /* Export variables to MATLAB*/
        String[] variables = new String[]{
                "q1",
                "t1",
                "q2",
                "t2",
                "q3",
                "t3",
                "q4",
                "t4",
                "q5",
                "t5",
        };

        stats.printDataToFile("balancer.m", variables, queueData);
    }
}
