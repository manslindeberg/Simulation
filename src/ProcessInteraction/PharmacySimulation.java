/*
Home Assignment 1
           Task 6
*/

public class PharmacySimulation {

    private static final int DAYS = 1000;  // Specify the number of days that the simulation should execute
    private static final int SIMULATIONHOURS = 24 * DAYS; // Sets the global simulation time in hours
    private static final double SERVICETIME = 0.25; // Service time is uniformly distributed between 10-20 minutes with a mean of 15 min
    private static final double RATE = 4;
    private static final int MAX_MEASUREMENTS = (int) (DAYS);

    public static void main(String[] args) {

        double[] outData = new double[DAYS + 1];
        Signal actSignal;
        new SignalList();

        IntervalGenerator generator = new IntervalGenerator(RATE);
        IntervalQueue queue = new IntervalQueue(outData);

        generator.sendTo = queue;
        queue.sendTo = null;

        SignalList.SendSignal(Global.READY, generator, Global.OPENING); // Initializes Generator with average 4 customers per hours between 9-17
        SignalList.SendSignal(Global.MEASURE, queue, Global.OPENING); // Initializes measurements

        while (Global.time < SIMULATIONHOURS) {
            actSignal = SignalList.FetchSignal();
            Global.time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        double averageTimeEnding; // Final average time on the day when the pharmacist has finished all the jobs

        averageTimeEnding = queue.accFinishedQueue / queue.endingNoMeasurements;

        /* Converting decimal hours to hours-minutes */
        int averageTimeEndingHours = (int) averageTimeEnding;
        int averageTimeEndingMinutes = (int) ((averageTimeEnding - averageTimeEndingHours) * 60);
        int length = String.valueOf(averageTimeEndingMinutes).length();

        if (length < 2) {
            System.out.println("Average time of work-day Ending for Pharmacy: " + averageTimeEndingHours + "." + "0" + averageTimeEndingMinutes);
        } else {
            System.out.println("Average time of work-day Ending for Pharmacy: " + averageTimeEndingHours + "." + averageTimeEndingMinutes);
        }

        Measurement stats = new Measurement();
        double mean = stats.mean(outData);
        double variance = stats.variance(outData, mean);
        double[] interval = stats.confidenceInterval(outData, mean, 99.9);

        stats.printMean(mean);
        stats.printVariance(variance);
        stats.printConfidenceInterval(interval, mean, 99.9);

        /* Calculating mean number of customer per hour & mean time per job in system */
        double meanCustomers = 1.0 * queue.accNoInQueue / queue.queueNoMeasurements;

        System.out.println("Number of measurements:" + queue.queueNoMeasurements);
        System.out.println("Mean number of customers in queuing system :" + meanCustomers);
        System.out.println("Mean arrival time: " + 1.0 * queue.accTimeBetweenArrival / queue.arrivalNoMeasurements);

        String[] variable = new String[]{
                "closing",
        };

        double[][] tmp = new double[1][DAYS+1];
        tmp[0] = outData;
        stats.printDataToFile("pharmacy.m", variable, tmp);
    }
}
