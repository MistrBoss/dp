%setenv('NSIGHT_CUDA_DEBUGGER','1')
close all; clear; clc;

%method = '_nearest';
%method = '_biquadr';
method = '_bicubic';

type = 'single'; %zm�nit zde a na �. 32

n = 35;

path = 'kernels\ImgScale\ImgScale\kernel_';
kernel = parallel.gpu.CUDAKernel([path, type, method, '.ptx'], [path, type, method, '.cu']);

%obr�zky
im_gpu = cell(1,n);

h = zeros(1, n, type);
w = zeros(1, n, type);

s0 = 'hotel_images\aa';

%nahr�v�n� obr�zk� na grafiku
tic
for i = 1:n
    if i < 10
        s = [s0,'0',int2str(i),'.tif'];
    else
        s = [s0,int2str(i),'.tif'];
    end
    
    im = single(imread(s)); %nahr�n� obr�zku a p�evod
    
    [n1,n2,n3] = size(im);
    h(i) = n1;
    w(i) = n2;
    im_gpu{i} = gpuArray(im); %nahr�n� obr�zku na grafiku
end
disp('Nahr�v�n� v�ech obr�zk� do pam�ti grafiky:');
toc

%p�ev�d�n� obr�zk� na jasov� matice
for i=1:n
    im_gpu{i} = sum(im_gpu{i}, 3) ./ 3; %p�evod na jasovou matici
end;
disp('P�evod v�ech obr�zk� na jasovou matici:');
toc

log = zeros(n, n, type, 'gpuArray');

%cyklus
for i=1:n
    %ft prvn�ho obr�zku
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
        
        %dopln�n� druh�ho obr�zku nulami na jednotnou velikost
        im2 = zeros(h(i), w(i), type, 'gpuArray');
        im2(1:h2, 1:w2) = im2Sc;

        %ft druh�ho obr�zku
        ft2 = fft2(im2);
        ft2Norm = ft2 ./ abs(ft2);

        %k��ov� korelace ve frekven�n�m spektru
        ccW = ft1Norm .* conj(ft2Norm);

        %p�evod do oby�ejn�ho sou�adnicov�ho syst�mu
        ccC = real(ifft2(ccW));

        %hled�n� maxim�ln�ho korela�n�ho koeficientu
        ccCMax = max(ccC);
        ccCMax = max(ccCMax);
        log(i,j) = ccCMax;
        disp(['obr1: ',int2str(i),';  obr2: ',int2str(j)]);
    end
end
disp(['Cyklus ',int2str(n),' obr�zk�:']);
log_gpu = gather(log);
toc

clearvars -except log_gpu;