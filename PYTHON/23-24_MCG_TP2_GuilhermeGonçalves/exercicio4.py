import cv2
import numpy as np

vid = cv2.VideoCapture(0)

corR = int(input("Quantidade de vermelho (0-255): "))
corB = int(input("Quantidade de verde (0-255): "))
corC = int (input("Quantidade de azul (0-255): "))

while(True):
    ret, frame = vid.read()

    rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    lower_bound = np.array([230,230,230])
    upper_bound = np.array([255,255,255])
    mask = cv2.inRange(rgb, lower_bound, upper_bound)

    white = cv2.bitwise_and(frame, frame, mask=mask)
    #white = cv2.colorChange(white, corR, corB, corC)
    
    cv2.imshow('original', frame)
    cv2.imshow('white', white)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()