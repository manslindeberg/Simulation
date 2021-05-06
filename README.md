# Simulation

This is a simple simulation of a centralized sensor network (*meaning that it communicates to each other
and to the ouside world through a gatewaye/basestation*) that uses the pure ALOHA protocol. It could be used
for planning and optimization of paramters i.e. back-off time, distribution, number of sensors per gateway,
communication range etc.

### Facts
- The program uses a discrete-event simulation technique called "Process Interaction Method". 
  
- The parameters are configured with the `config.properies` file
- An example script of how to loop through various parameter changes can be found in the `config.sh` file
- In its current implemenation, the program will sample the mean throughput and the mean fail probability rate
at a sampling rate that can be changed in the configuration file. This information, together with time-stamps
  and a confidence interval of the sampled means is outputed to a `.m`- file (*MATLAB*) in the end of
  the simulation
  
- The simulation stops when the sampled means have achieved a standard deviation lower than what is congifured in the 
configuration file