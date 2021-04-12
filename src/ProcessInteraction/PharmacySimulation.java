import java.io.IOException;

/*
Home Assignment 1
           Task 6
*/

public class PharmacySimulation {

    private static final int DAYS = 10000;  // Specify the number of days that the simulation should execute
    private static final int SIMULATIONHOURS= 24*DAYS; // Sets the global simulation time in hours
    private static final double SERVICETIME = 0.25; // Service time is uniformly distributed between 10-20 minutes with a mean of 15 min
    private static final double RATE = 4;

    public static void main(String[] args) {

        Signal actSignal;
        new SignalList();

        Generator generator = new Generator(RATE);
        Queue queue = new Queue();

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

        averageTimeEnding = queue.accFinishedQueue/queue.endingNoMeasurements;

        /* Converting decimal hours to hours-minutes */
        int averageTimeEndingHours = (int) averageTimeEnding;
        int averageTimeEndingMinutes = (int) ((averageTimeEnding - averageTimeEndingHours)*60);

        System.out.println("Average time of work-day Ending for Pharmacy: " + averageTimeEndingHours + "." + averageTimeEndingMinutes);

        /* Calculating mean number of customer per hour & mean time per job in system */
        double meanCustomers = 1.0 * queue.accNoInQueue / queue.queueNoMeasurements;
        double meanTime = 1.0 * ((queue.accQueueTime / queue.queueNoMeasurements) + SERVICETIME);

        System.out.println("Mean number of customers in queuing system :" + meanCustomers);
        System.out.println("Mean time per customer in quing system :"  + meanTime);
        System.out.println("Mean queing time per customer in system:" + (meanTime - SERVICETIME));

        System.out.println("Mean arrival time: " + 1.0*queue.accTimeBetweenArrival/queue.queueNoMeasurements);
        System.out.println("Little's Law - Theoretic Effective rate = " + 0.25 + " L/W = " + 1/(meanCustomers/meanTime));

    }
}
