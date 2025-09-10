import matplotlib.pyplot as plt
from skimage import data
from skimage.color import rgb2gray
from skimage import filters, feature

name = 'astronaut'
caller = getattr(data,name)
image = caller()

gray = rgb2gray(image)
gray = filters.gaussian(gray, sigma = 1)
edges = feature.canny(gray, sigma=1)
cropped = edges[0:(image.shape[0]-200),100:(image.shape[1])]

plt.figure()

plt.subplot(121)
plt.title('Original image')
plt.imshow(image)

plt.subplot(122)
plt.title('Filtered image')
plt.imshow(cropped, cmap='gray')

plt.show()
