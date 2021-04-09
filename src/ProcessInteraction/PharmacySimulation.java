import java.io.IOException;

public class PharmacySimulation {

    private static final int DAYS = 10000;
    private static final int SIMULATIONHOURS= 24*DAYS;
    private static final double SERVICETIME = 0.25;
    private static final double RATE = 4;

    public static void main(String[] args) throws IOException {

        //Signallistan startas och actSignal deklareras. actSignal är den senast utplockade signalen i huvudloopen nedan.

        Signal actSignal;
        new SignalList();

        //Now time before READY takes 2 times longer
        Generator generator = new Generator(RATE);
        Queue queue = new Queue();

        generator.sendTo = queue;
        queue.sendTo = null;

        SignalList.SendSignal(Global.READY, generator, Global.OPENING);
        SignalList.SendSignal(Global.MEASURE, queue, Global.OPENING);

        while (Global.time < SIMULATIONHOURS) {
            actSignal = SignalList.FetchSignal();
            Global.time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        double averageTimeEnding;

        averageTimeEnding = queue.accFinishedQueue/queue.endingNoMeasurements;

        int averageTimeEndingHours = (int) averageTimeEnding;
        int averageTimeEndingMinutes = (int) ((averageTimeEnding - averageTimeEndingHours)*60);

        System.out.println("Average time of work-day Ending for Pharmacy: " + averageTimeEndingHours + "." + averageTimeEndingMinutes);

        double meanCustomers = 1.0 * queue.accNoInQueue / queue.queueNoMeasurements;
        double meanTime = 1.0 * ((queue.accQueueTime / queue.queueNoMeasurements) + SERVICETIME);

        System.out.println("Mean number of customers in queuing system :" + meanCustomers);
        System.out.println("Mean time per customer in quing system :"  + meanTime);
        System.out.println("Mean queing time per customer in system:" + (meanTime - SERVICETIME));

        System.out.println("Mean arrival time: " + 1.0*queue.accTimeBetweenArrival/queue.queueNoMeasurements);
        System.out.println("Little's Law - Theoretic Effective rate = " + 0.25 + " L/W = " + 1/(meanCustomers/meanTime));

    }
}
