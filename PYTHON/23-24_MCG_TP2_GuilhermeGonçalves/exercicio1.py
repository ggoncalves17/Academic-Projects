import matplotlib.pyplot as plt
from skimage import data
from skimage.color import rgb2gray
from skimage.io import imread


#path = str(input("Introduza o caminho da imagem que pretende inserir: "))
#imagem = imread(path)

name = 'coffee'
caller = getattr(data,name)
image = caller()

gray = rgb2gray(image)

plt.figure()

plt.subplot(131), plt.imshow(gray, cmap='Reds')

plt.subplot(132), plt.imshow(gray, cmap='Greens')

plt.subplot(133), plt.imshow(gray, cmap='Blues')

plt.show()

