#Exercicio 4
#Guilherme Gonçalves a2022156457

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import networkx as nx

#A
G = nx.DiGraph()
G.add_nodes_from(['A','B','C','D','E'])
G.add_weighted_edges_from([('A','C',12),('A','D',60),('B','A',10),('C','B',20),('C','D',32),('E','A',7)])

fig, ax = plt.subplots()
nx.draw_circular(G,ax=ax, with_labels = True)

pos = nx.shell_layout(G)

edge_labels = nx.get_edge_attributes(G, "weight")
nx.draw_networkx_edge_labels(G,pos,edge_labels)

plt.show()

#B
node1 = input("Introduza o 1º vertice: \n")
node2 = input("Introduza o 2º vertice: \n")
custo = nx.shortest_path_length(G, node1, node2, weight="weight")
caminhoCurto = nx.shortest_path(G, node1, node2)
print (f"O menor caminho de {node1} a {node2} é {caminhoCurto}. O seu custo total é de {custo}.")

#C
densidade = nx.density(G)
matriz = nx.adjacency_matrix(G).todense()
matrix = nx.to_numpy_array (G)
print (f"Densidade: {densidade}")
print (f"Matriz: \n{matriz}")




