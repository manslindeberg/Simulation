% failprobability 2
size = nnz(time1);

y = ones(1,10);
y(1) = mean(averagefailrate1(1:size));
size = nnz(time2);
y(2) = mean(averagefailrate2(1:size));
size = nnz(time3);
y(3) = mean(averagefailrate3(1:size));
size = nnz(time4);
y(4) = mean(averagefailrate4(1:size));
size = nnz(time5);
y(5) = mean(averagefailrate5(1:size));
size = nnz(time6);
y(6) = mean(averagefailrate6(1:size));
size = nnz(time7);
y(7) = mean(averagefailrate7(1:size));
size = nnz(time8);
y(8) = mean(averagefailrate8(1:size));
size = nnz(time9);
y(9) = mean(averagefailrate9(1:size));
size = nnz(time10);
y(10) = mean(averagefailrate10(1:size));
x = linspace(1000,10000,10);
t = linspace(1000, 10000,1000);

meanarrivaltime = 4000;
transmittime = 1;
arrivalrate = @(nosensors) nosensors/meanarrivaltime;
theoretic = @(nosensors) 1 - exp(-2.*arrivalrate(nosensors).*transmittime);

x = linspace(1000,10000,10);
t = linspace(1000,10000,10000);
plot(t, 100.*theoretic(t), 'k--');
hold on
plot(x,100.*y,'r+');

title('Centralized wireless sensor network simulation w/o CA')
xlabel('\it Number of sensors \rm / n')
ylabel('\it  Packet loss probability \rm / %')
legend('Theoretic', 'Simulation')