% Average Throughput



meanarrivaltime = 4000;
transmittime = 1;
arrivalrate = @(nosensors) nosensors/meanarrivaltime;
theoretic = @(nosensors) arrivalrate(nosensors).*transmittime.*exp(-2.*arrivalrate(nosensors).*transmittime);
y = ones(1,6);

size = nnz(averagesuccesrateCA6);
y(1) = mean(averagesuccesrateCA6(1:size));
size = nnz(averagesuccesrateCA7);
y(2) = mean(averagesuccesrateCA7(1:size));
size = nnz(averagesuccesrateCA8);
y(3) = mean(averagesuccesrateCA8(1:size));
size = nnz(averagesuccesrateCA9);
y(4) = mean(averagesuccesrateCA9(1:size));
size = nnz(averagesuccesrateCA10);
y(5) = mean(averagesuccesrateCA10(1:size));
size = nnz(averagesuccesrateCA10);
y(6) = mean(averagesuccesrateCA11(1:size));
x = linspace(6000,11000,6);

figure(1)
hold on
errorbar(x,y,err(1:6),'r+--')
title('Centralized wireless sensor network simulation')
xlabel('\it Communication range \rm / m')
ylabel('\it  Throughput \rm / packets per second')
legend('Simulation')

