import cv2
import datetime as dt

def escreverFicheiro(data):
    if data:
        file = open("data.txt","a")
        file.write(data + " " + str(dt.datetime.now()) + "\n")
        file.close()
    else:
        print("Não foram encontrados dados")

vid = cv2.VideoCapture(0)
detector = cv2.QRCodeDetector()

while(True):
    ret, frame = vid.read()
    data, vertices_array, binary_qrcode = detector.detectAndDecode(frame)
    escreverFicheiro(data)

    cv2.imshow('frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

vid.release()
cv2.destroyAllWindows()
