#Exercicio 3
#Guilherme Gonçalves a2022156457

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import networkx as nx

G = nx.Graph()
G.add_nodes_from(['A','B','C','D','E'])
G.add_edges_from([('A','B'),('A','D'),('B','A'),('B','C'),('B','D'),('C','B'),('C','E'),('D','A'),('D','B'),('D','E'),('E','C'),('E','D')])

fig, ax = plt.subplots()
nx.draw_circular(G,ax=ax, with_labels = True)
plt.show()
