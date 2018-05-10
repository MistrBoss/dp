a = cell(1,15);
a{7} ='http://image.dovolena.cz/de/13501/fti-summer/006780_5305124.jpg';
a{10} ='http://image.dovolena.cz/de/giata/37342_cat008802_069_01.jpg';
a{13} ='http://image.dovolena.cz/de/giata/37342_cat009629_110_10.jpg';
a{4} ='http://image.dovolena.cz/de/giata/37342_010590_11837774.jpg';
a{11} ='http://image.dovolena.cz/de/giata/37342_cat008103_113_02.jpg';
a{1} ='http://image.dovolena.cz/de/giata/37342_cat009015_115_06.jpg';
a{9} ='http://image.dovolena.cz/de/giata/37342_cat011203_140_04.jpg';
a{6} ='http://image.dovolena.cz/de/13501/adac-winter/cat013326_136_04.jpg';
a{14} ='http://image.dovolena.cz/de/13501/adac-summer/cat012556_124_06.jpg';
a{5} ='http://image.dovolena.cz/de/giata/37342_cat010488_142_06.jpg';
a{15} ='http://image.dovolena.cz/de/13501/adac-summer/cat013842_136_04.jpg';
a{8} ='http://image.dovolena.cz/de/13501/toc-summer/lbc402_zimmer1_0912.jpg';
a{12} ='http://image.dovolena.cz/de/giata/37342_cat009533_052_02.jpg';
a{2} ='http://image.dovolena.cz/de/giata/37342_cat011000_055_02.jpg';
a{3} ='http://image.dovolena.cz/de/giata/37342_cat011855_060_02.jpg';

close all;

startfile = 1;
endfile = 15;

best = zeros(endfile - startfile+1,4);

kernel_up = [-0.0625 -0.0625 -0.0625; -0.0625 0.5 -0.0625; -0.0625 -0.0625 -0.0625];

kernel_down = [0 0.125 0; 0.125 0.5 0.125; 0 0.125 0];

for i=startfile:endfile
    im = imread(a{i});
    [m,n] = size(im(:,:,1));
    
    im_br = sum(im,3)/3;
    im_ft = fft2(im_br);
    
    kernel_up_sized = zeros(m,n);
    kernel_up_sized(1:2,1:2) = kernel_up(2:3,2:3);
    kernel_up_sized(m,1:2) = kernel_up(1,2:3);
    kernel_up_sized(1:2,n) = kernel_up(2:3,1);
    kernel_up_sized(m,n) = kernel_up(1,1);
    kernel_up_ft = fft2(kernel_up_sized);
    
    im_conv1_ft = im_ft.*kernel_up_ft;
    im_conv1 = ifft2(im_conv1_ft);
    
    kernel_down_sized = zeros(m,n);
    kernel_down_sized(1:2,1:2) = kernel_down(2:3,2:3);
    kernel_down_sized(m,1:2) = kernel_down(1,2:3);
    kernel_down_sized(1:2,n) = kernel_down(2:3,1);
    kernel_down_sized(m,n) = kernel_down(1,1);
    kernel_down_ft = fft2(kernel_down_sized);
    
    im_conv2_ft = im_conv1_ft.*kernel_down_ft;
    im_conv2 = ifft2(im_conv2_ft);
    
    im_conv1_8 = uint8(zeros(m,n,3));
    im_conv1_8(:,:,1) = uint8(im_conv1);
    im_conv1_8(:,:,2) = uint8(im_conv1);
    im_conv1_8(:,:,3) = uint8(im_conv1);
    im_conv2_8 = uint8(zeros(m,n,3));
    im_conv2_8(:,:,1) = uint8(im_conv2);
    im_conv2_8(:,:,2) = uint8(im_conv2);
    im_conv2_8(:,:,3) = uint8(im_conv2);
    
%    if i==1 || i==2 % || i==9
%        figure;
%        image(im_conv1_8);
%        figure; 
%        image(im_conv2_8);
%    end;
    
    sharpness = sum(sum(im_conv1 > 20));
    
    oversharpness = sum(sum(im_conv1-im_conv2 > 20));
    
    best(i-startfile+1,1) = i;
    best(i-startfile+1,2) = sharpness;
    best(i-startfile+1,3) = oversharpness;
    best(i-startfile+1,4) = sharpness / max(250,oversharpness-250);
end;

[sbest, sx] = sort(best(:,4),'descend');
best = best(sx,:);