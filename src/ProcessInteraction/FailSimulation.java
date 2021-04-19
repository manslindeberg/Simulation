import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
Home Assignment 1
           Task 7
*/

public class FailSimulation {

    private static final int SIMULATIONRUNS = 1000;
    private static final double CONFIDENCELEVEL = 99.9;

    public static void main(String[] args) {

        double accSystemUpTime = 0; // Accumulated lifetime of system - updated each round
        double[] arrSystemUpTime = new double[SIMULATIONRUNS]; // Data vector - systemUpTime measurements per simulationround

        Random rand = new Random();
        Measurement stat = new Measurement();

        int round = 0;
        while (round < SIMULATIONRUNS) {
            Global.time = 0; // Resets global time variable each run

            Signal actSignal;
            new SignalList();

            ArrayList<Component> components = new ArrayList<>(); // List that contains the five components

            /* Initializes each Component with a life-time between UPPER and LOWER BOUND declared in Component.class  */
            for (int i = 0; i < 5; i++) {
                Component component = new Component(i);
                components.add(component);
                SignalList.SendSignal(Global.START, component, Global.time);
            }

            Controller controller = new Controller(components);
            SignalList.SendSignal(Global.CONTROL, controller, Global.time + rand.nextDouble());    // Start control operation each timeunit
            SignalList.SendSignal(Global.MEASURE, controller, Global.time + rand.nextDouble());    // Starts measurement each timeunit


            /* time unit increments until the last Component has failed and the systemUpTime is updated */
            while (controller.systemUpTime == 0) {
                actSignal = SignalList.FetchSignal();
                Global.time = actSignal.arrivalTime;
                actSignal.destination.TreatSignal(actSignal);
            }
            arrSystemUpTime[round] = controller.systemUpTime;
            round++;
        }
        double mean = stat.mean(arrSystemUpTime);
        double variance = stat.variance(arrSystemUpTime, mean);
        double standardDev = stat.standardDeviation(arrSystemUpTime, mean);
        double[] interval = stat.confidenceInterval(arrSystemUpTime, mean, CONFIDENCELEVEL);

        stat.printMean(mean);
        stat.printVariance(variance);
        stat.printStandardDeviation(standardDev);
        stat.printConfidenceInterval(interval, mean, CONFIDENCELEVEL);

        double[][] outData = new double[][]{
                arrSystemUpTime,
                new double[]{mean},
                new double[]{variance},
                new double[]{standardDev},
                interval
        };
        String[] variables = new String[]{
                "systemTime",
                "mean",
                "variance",
                "standdev",
                "confinterval",
        };
        stat.printDataToFile("failsimulation.m", variables, outData);
    }
}
