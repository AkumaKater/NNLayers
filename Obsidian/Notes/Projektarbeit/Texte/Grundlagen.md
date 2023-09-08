## Wofür wird ein Netzwerk verwende?

Neuronale netzwerke werden vor allem für Klassifikationsverfahren verwendet. In der Praxis gibt es viele Anwendungsbereiche, in denen es Vorteilhaft ist, Große Mengen von Daten automatisch zu Klassifizieren. Einige Beispiele wären zB die Bild und schriffterkennung, die man dabei verwendet, Kennschilder von Autos Maschinel auszulesen. Solche Technologien werden immer häufiger auf parkplätzen und Autobahnen eingesetzt. Aber auch fast Jedes handy kann mittlerweile schrifft erkennen, die mit der Kammera aufgenommen wird. Auch in der Medizin, beim Auswerten von Röntgenbildern, in der Biologie, zum erkennen von Pflanzen auf Fotos und noch vielem mehr werden Neuronale Netzwerke eingesetzt.
Es gibt kaum einen bereich, in dem sich nicht eine mögliche anwendung für Neuronale Netzwerke finden lässt. Ganz besonders aufwändige Netzwerke werden mittlerweile auch für konstruktive Aufgaben verwendet, wie zB Sprachmodelle wie ChatGPT und Bilder Generierende KIs wie Midjourney.

Netzwerke werden bei Problemen eingesetzt, die Kompliziert genug sind, dass die Lösung nicht manuell (ToDo) programmiert werden kann, es sich allerdings um solche Datenmengen handelt, dass es sich nicht lohnt, Menschen mit dieser Aufgabe zu betrauen.
Generell wird ein Datensatz mit Labeln versehen, und dieser wird dazu verwendet, das Netzwerk zu Trainieren. Die Daten werden von Menschen mit Labeln versehen. 


## Abgrenzung/ welche Art von Netzwerk werde ich hier bauen
In dieser Arbeit soll ein Grundlegendes und einfaches neuronales Netzwerk Programmiert, erklärt und getestet werden. Die Einfachste Form eines solchen Netzwerkes ist ein Feed Forward Netzwerk.
Die Ansprüche an dieses Netzwerk sind die Folgenden:
- Variable Anzahl an Layern
- Variable Anzahl an Nodes
- Epochen
- Learnrate
- Batches

Darüber hinaus wird das Netzwerk Tabelarisch ausgaben über die erziehlte Genauigkeit auf den Trainings und test Datensätzen ausgeben, um anpassungen an den Einstellungen vergleichbar zu machen.
Außerdem wird man ein Trainiertes Netzwerk abspeichern können.

Augenmerk dieser Arbeit wird allerdings Hauptsächlich die Grundlagen und Praktische Umsetzung eines Feed Forward Netzwerkes in seiner einfachsten Grundform:
- Initialisierung
- Abfrage
- Lernen

## Grundlegender Aufbau eines Netztes (Initialisieren/Querry/learn)

Die drei Grundmethoden des Netztwerkes sind die Initialisierung, die Abfrage und das Lernen.
Bei der Initialisierung soll es möglich sein, die Größe des Netzwerkes einzustellen, also die menge an Hidden Layer und Nodes.
Bei der Abfrage werden die Einzelnen Daten aus dem Datensatz in Objekte umgewandelt, die für das Netzwerk genormt sind. Das Netzwerk rechnet eine Wahrscheinlichkeit für jede mögliche Antwort aus. Zum Beispiel besteht der MNIST Datensatz aus Bildern von handschriftlich geschriebenen Zahlen. Das Netzwerk wird also ein Array ausgeben, das aus 10 Zahlen besteht, welche die Wahrscheinlichkeit angeben, die das Netzwerk den 10 Zahlen von 0 bis 9 zugewiesen hat.
Der Learn (ToDo)

## Der MNIST Datensatz
Der Datensatz, mit dem dieses Netzwerk getestet werden soll, ist der MNIST Datensatz. Es handelt sich dabei um eine Teilmenge aus den NIST Datensätzen. Dieser ist frei herunterzuladen, und wird häufig dazu genutzt, Netzwerke zu trainieren.
Der Datensatz besteht aus 70.000 Bilder, von denen 60.000 zum trainieren verwendet werden. Die Bilder enthalten jeweils eine Handschriftlich gezeichnete Zahl zwischen 0 un 9. 

![[Pasted image 20230908173244.png]]
(Quelle: https://www.kaggle.com/datasets/hojjatk/mnist-dataset)

Um den Datensatz einzulesen, wird in diesem Projekt der mnist-data-reader von Author
Türkdoğan Taşdelen von seinem Github Repository "https://github.com/turkdogan/mnist-data-reader" verwendet. Darin sind zwei Klassen wichtig:
Der MnistDataReader liest die Dateien ein. Die Bilder und die Label sind getrennt gespeichert, und müssen für das Netzwerk 