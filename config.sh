#!/bin/bash

cd src/IoT

# compilation
javac WirelessNetworkSimulation.java Config.java Gateway.java Global.java Measurement.java NetworkArea.java Proc.java Sensor.java Signal.java Signal.java SignalList.java SimpleFileWriter.java

for i in {1..10}
do
	echo $i
	# run
	let sensors=$i\*1000
	sed -i "s/^\(NOSENSORS=\).*/\1$sensors/" config.properties
	sed -i "s/^\(ROUND=\).*/\1$i/" config.properties
	java WirelessNetworkSimulation
done

#clean-up
rm *.class


