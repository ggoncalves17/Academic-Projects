#Exercicio 1
#Guilherme Gonçalves a2022156457

import numpy as np
import matplotlib.pyplot as plt

def grafico(x):
    y = []
    y = -x**2
    return y

x = np.arange(-50,51)
y = grafico(x)

ax = plt.plot(x,y)
plt.show()



