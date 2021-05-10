% Collision probability

probability = ones(1,10);

total1 = sum(nofailure1) + sum(nosuccess1);
probability(1) = sum(nofailure1)/total1;

total2 = sum(nofailure2) + sum(nosuccess2);
probability(2) = sum(nofailure2)/total2

total3 = sum(nofailure3) + sum(nosuccess3);
probability(3) = sum(nofailure3)/total3

total4 = sum(nofailure4) + sum(nosuccess4);
probability(4) = sum(nofailure4)/total4

total5 = sum(nofailure5) + sum(nosuccess5);
probability(5) = sum(nofailure5)/total5

total6 = sum(nofailure6) + sum(nosuccess6);
probability(6) = sum(nofailure6)/total6

total7 = sum(nofailure7) + sum(nosuccess7);
probability(7) = sum(nofailure7)/total7

total8 = sum(nofailure8) + sum(nosuccess8);
probability(8) = sum(nofailure8)/total8;

total9 = sum(nofailure9) + sum(nosuccess9);
probability(9) = sum(nofailure9)/total9

total10 = sum(nofailure10) + sum(nosuccess10);
probability(10) = sum(nofailure10)/total10

meanarrivaltime = 4000;
transmittime = 1;
arrivalrate = @(nosensors) nosensors/meanarrivaltime;
theoretic = @(nosensors) 1 - exp(-2.*arrivalrate(nosensors).*transmittime);

x = linspace(1000,10000,10);
t = linspace(1000,10000,10000);
figure(2)
plot(t, 100.*theoretic(t), 'k--');
hold on
plot(x,100.*probability,'r+');

title('Centralized wireless sensor network simulation w/o CA')
xlabel('\it Number of sensors \rm / n')
ylabel('\it  Packet loss probability \rm / %')
legend('Theoretic', 'Simulation')
