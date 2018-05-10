%setenv('NSIGHT_CUDA_DEBUGGER','1')
close all; clear; clc;

%method = '_nearest';
%method = '_biquadr';
method = '_bicubic';

type = 'single';

n = 35;

path = 'kernels\ImgScale\ImgScale\kernel_';
kernel = parallel.gpu.CUDAKernel([path, type, method, '.ptx'], [path, type, method, '.cu']);

%obrazky
im_gpu = cell(1,n);

h = zeros(1, n, type);
w = zeros(1, n, type);

s0 = 'hotel_images\aa';

%nahravani obrazku na grafiku
tic
for i = 1:n
    if i < 10
        s = [s0,'0',int2str(i),'.tif'];
    else
        s = [s0,int2str(i),'.tif'];
    end
    
    im = single(imread(s)); %nahrani obrazku a prevod
    
    [n1,n2,n3] = size(im);
    h(i) = n1;
    w(i) = n2;
    im_gpu{i} = gpuArray(im); %nahrani obrazku na grafiku
end
disp('Nahravani vsech obrazku do pameti grafiky:');
toc

%prevadeni obrazku na jasove matice
for i=1:n
    im_gpu{i} = sum(im_gpu{i}, 3) ./ 3; %prevod na jasovou matici
end;
disp('Prevod vsech obrazku na jasovou matici:');
toc

log = zeros(n, n, type, 'gpuArray');

%cyklus
for i=1:n
    %ft prvniho obrazku
    ft1 = fft2(im_gpu{i});
    ft1Norm = ft1 ./ abs(ft1);
        
    for j=1:n
        if i==j
            continue;
        end;
        
        scale = (min(h(i)/h(j), w(i)/w(j)));
        h2 = round(scale * h(j));
        h2 = min(h2, h(i));
        w2 = round(scale * w(j));
        w2 = min(w2, w(i));
        scale_h = h(j)/h2;
        scale_w = w(j)/w2;
        
        %im2Sc = imresize(im_gpu{j}, scale);
        
        im2Sc = zeros(h2, w2, type, 'gpuArray');
        kernel.GridSize = w2;
        kernel.ThreadBlockSize = h2;
        im2Sc = feval(kernel, im2Sc, im_gpu{j}, int32(h(j)), int32(w(j)), scale_h, scale_w);
        
        %doplneni druheho obrazku nulami na jednotnou velikost
        im2 = zeros(h(i), w(i), type, 'gpuArray');
        im2(1:h2, 1:w2) = im2Sc;

        %ft druheho obrazku
        ft2 = fft2(im2);
        ft2Norm = ft2 ./ abs(ft2);

        %krizova korelace ve frekvencnim spektru
        ccW = ft1Norm .* conj(ft2Norm);

        %prevod do obycejneho souradnicoveho systemu
        ccC = real(ifft2(ccW));

        %hledani maximalniho korelacniho koeficientu
        ccCMax = max(ccC);
        ccCMax = max(ccCMax);
        log(i,j) = ccCMax;
        disp(['obr1: ',int2str(i),';  obr2: ',int2str(j)]);
    end
end
disp(['Cyklus ',int2str(n),' obrazku:']);
log_gpu = gather(log);
toc

clearvars -except log_gpu;