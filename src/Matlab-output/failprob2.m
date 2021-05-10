% failprobability 2
size = nnz(timeCA1);

y = ones(1,10);
y(1) = mean(averagefailrateCA1(1:size));
size = nnz(timeCA2);
y(2) = mean(averagefailrateCA2(1:size));
size = nnz(timeCA3);
y(3) = mean(averagefailrateCA3(1:size));
size = nnz(timeCA4);
y(4) = mean(averagefailrateCA4(1:size));
size = nnz(timeCA5);
y(5) = mean(averagefailrateCA5(1:size));
size = nnz(timeCA6);
y(6) = mean(averagefailrateCA6(1:size));
size = nnz(timeCA7);
y(7) = mean(averagefailrateCA7(1:size));
size = nnz(timeCA8);
y(8) = mean(averagefailrateCA8(1:size));
size = nnz(timeCA9);
y(9) = mean(averagefailrateCA9(1:size));
size = nnz(timeCA10);
y(10) = mean(averagefailrateCA10(1:size));
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