import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc implements Comparable<QS> {
	public int numberInQueue = 0, accumulated, noMeasurements;
	public double scaleFactor;
	public Proc sendTo;

	private double serviceTime;

	Random rand = new Random();

	public QS(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case ARRIVAL: {
				numberInQueue++;
				if (numberInQueue == 1) {
					SignalList.SendSignal(READY, this, time + nextDoubleExp(rand, serviceTime));
				}
			}
			break;

			case READY: {
				numberInQueue--;
				if (sendTo != null) {
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}
				if (numberInQueue > 0) {

					SignalList.SendSignal(READY, this, time + nextDoubleExp(rand, serviceTime));
				}
			}
			break;

			case MEASURE: {
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2 * rand.nextDouble());
			}
			break;
		}
	}


	public double nextDoubleExp(Random rand, double lambda) {
		return  Math.log(1-rand.nextDouble())*(-lambda);
	}

	@Override
	public int compareTo(QS queue) {
		return (Integer.compare(this.numberInQueue, queue.numberInQueue));
	}
}