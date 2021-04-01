import java.io.*;

/*
a)
 Number of customers/timeunit = 1/(time between arrivals)
 which is uniformly distributed between 0-2,5. Which
 means that the mean time between customers is 1.25
 and thus is the mean number of customers per second
 ANSWER: 1/1.25 = 0.8

b)
  ANSWER: ~1.75

c)
 2 = 2.6417604912998978
 1.5 = 2.428424304840371
 1.2 =1.9436052366565961
 1.1 = 1.8729674796747968
 1.01 = 1.6175869120654396

d)
 0.9370629370629371
 0.9517102615694165
 0.9325153374233128
 0.8562691131498471
 0.8862512363996043
 0.9244357212953876
 0.857736240913811
 0.897410358565737
 0.8816326530612245
 0.8923988153998026

 MEAN ->	0.90174226748411
 STANDARD DEVIATION ->	0.031561205806202
 VARIANCE -> 0.00099610971194142

 1.6533742331288344
 1.6827852998065764
 1.8623481781376519
 1.8353057199211045
 1.9164989939637826
 1.9098039215686275
 1.6995121951219512
 1.7062374245472838
 1.6852589641434264
 1.8461538461538463

 MEAN -> 1.7797278776493
 STANDARD DEVIATION -> 	0.098018580012975
 VARIANCE -> 0.00960764202776

 ANSWER: According to the simulation results, we can see that E(arrivalTime)=1.3 has a lower
        variance and standard deviation.

e)
 simulationTime = 5000 --> 653.8710010319918
 simulationTime = 10000 --> 1237.1702770780857
 simulationTime = 20000 --> 2423.912892848305

Question: Why does the average number of customers in the queue per time unit increase when
          the simulation time increases?

 Answer: Once the system gets "clogged" the first time, since the number arrival time
         is lower than the service time, the system is unstable. The service will always "lag"
         behind the queue and the queue will therefore grow bigger and bigger as the simulation
         time increases.
 */

public class LabOneSimulation extends GlobalSimulation{
    public static void main(String[] args) throws IOException {
        Event actEvent;
        State actState = new State(); // The state that shoud be used

        // Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);
        insertEvent(MEASURE, 5);

        // The main simulation loop
        while (time < 20000){
            actEvent = eventList.fetchEvent();
            time = actEvent.eventTime;
            actState.treatEvent(actEvent);
        }

        // Printing the result of the simulation, in this case a mean value
        System.out.println("Number of Customers in Queue per time unit:" + "\n"+ 1.0*actState.accumulated/actState.noMeasurements);
    }
}