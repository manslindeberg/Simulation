% Average Throughput

size = nnz(time1);

meanarrivaltime = 4000;
transmittime = 1;
arrivalrate = @(nosensors) nosensors/meanarrivaltime;
theoretic = @(nosensors) arrivalrate(nosensors).*transmittime.*exp(-2.*arrivalrate(nosensors).*transmittime);
y = ones(1,10);
y(1) = mean(averagesuccesrate1(1:size));
size = nnz(time2);
y(2) = mean(averagesuccesrate2(1:size));
size = nnz(time3);
y(3) = mean(averagesuccesrate3(1:size));
size = nnz(time4);
y(4) = mean(averagesuccesrate4(1:size));
size = nnz(time5);
y(5) = mean(averagesuccesrate5(1:size));
size = nnz(time6);
y(6) = mean(averagesuccesrate6(1:size));
size = nnz(time7);
y(7) = mean(averagesuccesrate7(1:size));
size = nnz(time8);
y(8) = mean(averagesuccesrate8(1:size));
size = nnz(time9);
y(9) = mean(averagesuccesrate9(1:size));
size = nnz(time10);
y(10) = mean(averagesuccesrate10(1:size));
x = linspace(1000,10000,10);
t = linspace(1000, 10000,1000);

figure(1)
plot(t, theoretic(t),'k--')
hold on
plot(x,y,'r+')
title('Centralized wireless sensor network simulation w/o CA')
xlabel('\it Number of sensors \rm / n')
ylabel('\it  Throughput \rm / packets per second')
legend('Theoretic', 'Simulation')

