import java.io.*;

/*
d)
Mean number of customers in queuing system 1: 3.4541541541541543
Mean number of customers in queuing system 2: 3.341109897746451
Mean number of customers in queuing system 3: 3.271557788944724

e)
Mean number of customers in queuing system 1: 3.463736042651645
Mean number of customers in queuing system 2: 20154.435164835166

*/
//Denna klass ärver Global så att man kan använda time och signalnamnen utan punktnotation
public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal är den senast utplockade signalen i huvudloopen nedan.

    	Signal actSignal;
    	new SignalList();

    	//Här nedan skapas de processinstanser som behövs och parametrar i dem ges värden.

    	Queue Q1 = new Queue(0);
    	Queue Q2 = new Queue(0);

    	//Now time before READY takes 2 times longer

		Q1.sendTo = Q2;
		Q2.sendTo = null;

		Generator Generator = new Generator();
    	Generator.lambda = 9; // Generator ska generera nio kunder per sekund
    	Generator.sendTo = Q1; //De genererade kunderna ska skickas till kösystemet QS

    	//Här nedan skickas de första signalerna för att simuleringen ska komma igång.
    	SignalList.SendSignal(READY, Generator, 0);
    	SignalList.SendSignal(MEASURE, Q1, 0);
    	SignalList.SendSignal(MEASURE, Q2, 0);


    	// Detta är simuleringsloopen:

    	while (time < 10000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);

    	}
    	//Slutligen skrivs resultatet av simuleringen ut nedan:
    	System.out.println("Mean number of customers in queuing system 1: " + 1.0*Q1.accNoInQueue /Q1.noMeasurements);
		System.out.println("Mean number of customers in queuing system 2: " + 1.0*Q2.accNoInQueue /Q2.noMeasurements);

	}
}