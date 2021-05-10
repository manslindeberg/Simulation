%% run

err100200 = ones(1,10);
for i=1:10
    err100200(i) = (confCA100200(2,i) - confCA100200(1,i))
end
