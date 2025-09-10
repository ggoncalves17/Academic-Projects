#Exercicio 2
#Guilherme Gonçalves a2022156457

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv("./data.csv")
x = df['month_number']
y = df['total_units']
profit = df['total_profit']

#Plot1
plt.subplot(2,2,1)
plt.plot(x,y)
plt.title("Sales data of total units")
plt.subplot(2,2,1).set_xlabel("Month Number")


#Plot2
plt.subplot(2,2,2)
plt.plot(x,profit)
plt.title("Sales data of total profit")
plt.subplot(2,2,2).set_xlabel("Month Number")
plt.subplot(2,2,2).set_ylabel("Sales units in number")



plt.show()


