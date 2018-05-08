close all; clc; clear;

method = 'nearest';
%method = 'bicubic';

type = 'single'; %zmìnit zde a na ø. 27

n = 35;

%obrázky
im_gpu = cell(1, n);

h = zeros(1, n, type);
w = zeros(1, n, type);

s0 = 'hotel_images\aa';

%nahrávání obrázkù
tic
for i = 1:n
    if i < 10
        s = [s0,'0',int2str(i),'.tif'];
    else
        s = [s0,int2str(i),'.tif'];
    end;
    
    im = single(imread(s)); %nahrání obrázku a pøevod
    
    [n1,n2,n3] = size(im);
    h(i) = n1;
    w(i) = n2;
    im_gpu{i} = im; %nahrání obrázku
end;
disp('Nahrávání všech obrázkù:');
toc

%pøevádìní obrázkù na jasové matice
for i=1:n
    im_gpu{i} = sum(im_gpu{i}, 3) ./ 3; %pøevod na jasovou matici
end;
disp('Pøevod všech obrázkù na jasovou matici:');
toc

log_cpu = zeros(n, n, type);

%cyklus
for i=1:n
    %ft prvního obrázku
    ft1 = fft2(im_gpu{i});
    ft1Norm = ft1 ./ abs(ft1);
        
    for j=1:n
        if i==j
            continue;
        end;
        
        scale = min(h(i)/h(j), w(i)/w(j));
        h2 = round(scale * h(j));
        h2 = min(h2, h(i));
        w2 = round(scale * w(j));
        w2 = min(w2, w(i));

        im2Sc = imresize(im_gpu{j}, [h2, w2], method);

        %doplnìní druhého obrázku nulami na jednotnou velikost
        im2 = zeros(h(i), w(i), type);
        im2(1:h2, 1:w2) = im2Sc;

        %ft druhého obrázku
        ft2 = fft2(im2);
        ft2Norm = ft2 ./ abs(ft2);

        %køížová korelace ve vlnovím spektru
        ccW = ft1Norm .* conj(ft2Norm);

        %pøevod do obyèejného souøadnicového systému
        ccC = real(ifft2(ccW));

        %hledání maximálního korelaèního koeficientu
        ccCMax = max(ccC);
        ccCMax = max(ccCMax);
        log_cpu(i,j) = ccCMax;
        disp(['obr1: ',int2str(i),';  obr2: ',int2str(j)]);
    end;
end;
disp(['Cyklus ',int2str(n),' obrázkù:']);
time = toc

clearvars -except log_cpu;
