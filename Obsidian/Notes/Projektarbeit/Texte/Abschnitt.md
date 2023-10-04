## Learn Algorithmus

Wir fangen nun mit der Learn Methode an.
 ```java 
// Training
public void learn(MnistMatrix data) {
	UpdateAllGradients(data);
	ApplyAllGradients(this.learnRate);
	ClearAllGradients();
}
```

UpdateAllGradients() wird die Methode sein, in der die Rechnungen aus dem letzten Kapital angewandt werden. Der Name kommt daher, dass die Ergebnisse der Rechnungen nicht sofort mit den Gewichten verrechnet werden, sondern zunächst als Steigung (Gradient) gespeichert wird. Dies liegt daran, dass später noch die Batches hinzukommen. Dabei handelt es sich um ein Konzept, bei dem der Durchschnitt mehrerer Berechnungen  als Änderung an den Gewichten verwendet wird. Für den Moment und zu Demonstrationszwecken gehen wir noch nicht im Code darauf ein. Es werden die Grundvoraussetzungen dennoch bereits jetzt geschaffen, um uns später Arbeit zu ersparen.

ApplyAllGradients() wird dazu verwendet, um die Gradients mit den Gewichten zu verrechnen. 
ClearAllGradients() setzt die Gradients einfach wieder auf Null, damit die Nächsten Rechnungen vorgenommen werden können.

### UpdateAllGradients

Wie wir in den Rechnungen im letzten Kapitel gesehen haben, lässt sich die Ableitung für die verschiedenen Kosten Funktionen im Bezug auf die Gewichte der verschiedenen Schichten leicht erweitern. Die ersten Zwei Teil-Ableitungen, also die Ableitung der Cost Funktion und die Ableitung der Schwellwert Funktion der Output Schicht bleiben für jede Schicht gleich, sind so gesehen aber einzigartig in der Reihenfolge. Daher werden sie Initial berechnet in der Methode CalculateOutputLayerNodeValues(). Diese gibt dabei NodeValues zurück, die wir zwischenspeichern und dann übergeben können. Die NodeValues werden dann bereits für die Gewichte der Output Schicht zu Ende berechnet. Wie im letzten Kapitel gezeigt, fehlt nur noch die Multiplikation mit der Ableitung von Berechnung der Gewichte. 

![[Pasted image 20230921231251.png]]

Mit anderen Worten, die NodeValues werden mit den unveränderten Inputs in die Outputschicht multipliziert.

Wie werden dann alle Gewichte einer jeden versteckten Schicht berechnet? 
Wie oben bereits beschrieben, werden für jede versteckte Schicht zwei Rechnungen zwischengeschoben, das sind jeweils die Ableitung für die Gewichtung in Bezug zu den Activations, sowie die Activations im Bezug zu den Gewichteten Inputs der vorherigen Schicht. Da die ersten Beiden Terme sich von der ersten Berechnung nicht unterscheiden, werden sie am besten wiederverwendet. Das heißt, sie sind ja bereits als NodeValues, also dem Ergebniss der CalculateOutputLayerNodeValues() Methode gespeichert. Daher übergeben wir die NodeValues der Methode CalculateOutputLayerNodeValues(), und übergeben sie der Methode CalculateHiddenLayerNodeValues(). Genau wie zuvor lassen wir uns die Ergebnisse als NodeValues übergeben, und speichern sie zwischen. Und genau wie zuvor, müssen sie noch mit der Ableitung der Gewichteten Inputs verrechnet werden, das heißt mit den ungewichteten Inputs der vorherigen Schicht.
Dieser Schritt mit den CalculateHiddenLayerNodeValues() lässt sich in einer Schleife leicht auf alle Schichten Anwenden.

Im Code sieht das dann so aus:
```java
void UpdateAllGradients(MnistMatrix dataPoint) {
    Querry(dataPoint.getInputs());

    Layer outputLayer = layers[layers.length - 1];
    double[] nodeValues = 
    outputLayer.CalculateOutputLayerNodeValues(dataPoint.getTargets());
    outputLayer.UpdateGradients(nodeValues);

    for (int index = layers.length - 2; index >= 0; index--) {
        Layer hiddenLayer = layers[index];
        nodeValues = 
	        hiddenLayer.CalculateHiddenLayerNodeValues(layers[index + 1], nodeValues);
            hiddenLayer.UpdateGradients(nodeValues);
    }
}
```

Und so sieht also der Backpropagation Algorithmus aus. Man fängt bei der letzten Schicht, der Output Schicht an, und passt die Gewichte an. Dann geht es weiter zur vorletzten Schicht und so weiter, bis zur Input Schicht. Die Gewichte werden sozusagen von Hinten nach vorne angepasst.
Kommen wir nu zur Implementierung der CalculateOutputLayerNodeValues() und CalculateHiddenLayerNodeValues() Methoden.

### CalculateOutputLayerNodeValues

Sowohl die CalculateOutputLayerNodeValues() Methode, als auch die CalculateHiddenLayerNodeValues() Methode werden in der Layer Klasse implementiert.
Die ersten NodeValues, die für die Output Schicht berechnet werden, entsprechen der Multiplikation aus der Ableitung der Cost Funktion und der Ableitung der Schwellwert Funktion der Output Schicht.
Die NodeValues werden danach zurückgegeben.
```java
public double[] CalculateOutputLayerNodeValues(double[] expectedOutputs) {
    double[] nodeValues = new double[expectedOutputs.length];
    for (int i = 0; i < nodeValues.length; i++) {
        double costDerivative = CostAbleitung(activations[i], expectedOutputs[i]);
        double activationAbleitung = 
	 Activation.geActivation().ActivationAbleitung(weightedInputs[i]);
        nodeValues[i] = activationAbleitung * costDerivative;
    }
    return nodeValues;
}
```

In der Aufrufenden Methode, UpdateAllGradients(), werden die NodeValues zwischengespeichert, und danach direkt an die Methode UpdateGradients() der Output Schicht weitergegeben. Dort werden sie mit den Ungewichteten Inputs verrechnet, wodurch die erste Matrix entsteht, welche die Änderungen an den Gewichten enthält, die noch nicht mit den Gewichten verrechnet wurden.

```java
public void UpdateGradients(double[] nodeValues) {
    for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
        for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
            double derivativeCostWrtWeight = inputs[nodeIn] * nodeValues[nodeOut];                CostSteigungW[nodeIn][nodeOut] += derivativeCostWrtWeight;
        }
    }
}
```

Das Array CostSteigungW muss außerdem ebenfalls in der Klasse Layer festgehalten werden. Hier werden die Anpassungen, die wir mithilfe der Kettenregel ausrechnen abgespeichert, bevor sie mit den Gewichten verrechnet werden.

```java
public class Layer {

    int numInputNodes, numOutputNodes;
    double[][] weights;
    //--> Steigung der Cost Funktion im Bezug auf das Gewicht W
    double[][] CostSteigungW;

    double[] inputs;
    double[] weightedInputs;
    double[] activations;
```
### CalculateHiddenLayerNodeValues

Nun sehen wir uns an, wie die Anpassungen an den Gewichten in den Versteckten Schichten berechnet werden.

```java

public double[] CalculateHiddenLayerNodeValues(Layer oldLayer, double[] nodeValues) {
    double[] newNodeValues = new double[numOutputNodes];
    Activation ac = Activation.geActivation();
    for(int i=0; i < numOutputNodes; i++){
        for(int j=0; j < nodeValues.length; j++){
            newNodeValues[i] += nodeValues[j]*oldLayer.weights[j][i];
        } 
        newNodeValues[i] *= ac.ActivationAbleitung(weightedInputs[i]);
    return newNodeValues;
}
```

Die Gewichte der vorherigen Schicht und die NodeValues, die übergeben wurden werden mithilfe des Punkt Produkt verrechnet. Die Schicht, die zuvor berechnet wurde, muss in den Übergabe Parametern mitgegeben werden, und wird hier OldLayer genannt. Wenn wir gerade die Methode das erste mal aufrufen, dann wurde die Output Schicht bereits von der Methode CalculateOutputLayerNodeValues() berechnet und muss als OldLayer an diese Methode übergeben werden.
Bei dem Punkt Produkt muss auf die Dimensionen der Gewichts-Matrix geachtet werden. In unserem Netzwerk wurde diese Matrix Transponiert aufgebaut, das heißt dass die Werte Senkrecht mit den Werten der vorherigen NodeValues verrechnet werden müssen.

Auch diese Methode gibt wieder NodeValues zurück, welche in der aufrufenden Methode mit den ungewichteten Inputs dieser Schicht verrechnet werden müssen, und somit die Anpassungen berechnen, die an den Gewichten vorgenommen werden müssen. Falls es weitere versteckte Schichten gibt, werden die NodeValues weitergereicht, womit sich viele aufrufe und Rechnungen sparen lassen.

### ApplyAllGradients

In dieser Methode werden alle Anpassungen, die wir zuvor ausgerechnet haben, und die dem 2D Array CostSteigungW gespeichert wurden, mit den Gewichten verrechnet. Im NeuralNetwork Skript wird über alle Schichten iteriert:

```java
private void ApplyAllGradients(double learnrate) {
    for (Layer layer : layers) {
        layer.ApplyGradient(learnrate);
    }
}
```

Und in der Layer Klasse werden sie verrechnet:

```java
public void ApplyGradient(double learnrate) {
    for(int i=0; i<numOutputNodes;i++){
        for(int j=0; j<numInputNodes;j++){
            weights[i][j] -= CostSteigungW[j][i]*learnrate;
        }
    }
}
```

Wichtig zu beachten ist hier, dass die Steigung der Cost Funktion, die wir ausgerechnet haben, also die Anpassung an den Gewichten von den Gewichten *abgezogen* wird. Wenn die Steigung nämlich Positiv ist, dann läge der Tiefpunkt in entgegengesetzter Richtung, wenn sie negativ ist, liegt der Tiefpunkt voraus.

![[Pasted image 20230917195146.png]]
[Quelle](https://vzahorui.net/optimization/gradient-descent/)

Um es an einem Beispiel zu verdeutlichen, die Steigung in Punkt A ist positiv. Daher müsste man die Steigung von den Gewichten abziehen, um näher an den Tiefpunkt zu gelangen. In Punkt F ist die Steigung negativ, und muss daher ebenfalls abgezogen werden, wodurch das Gewicht vergrößert wird.

### ClearAllGradients

Zum Schluss müssen die Steigungen die wir verrechnet haben wieder auf Null gesetzt werden, nachdem sie verrechnet wurden.
Wir iterieren über alle Schichten:

```java
private void ClearAllGradients() {
    for (Layer layer : layers) {
        layer.ClearGradient();
    }
}
```

Und initialisieren neue Arrays in den Schichten:

```java
public void ClearGradient() {
    this.CostSteigungW = new double[numInputNodes][numOutputNodes];
}
```

Das ist der Abschluss. Wir haben nun ein Funktionsfähiges Netzwerk erstellt. Es ist nun möglich, das Netzwerk mit Daten zu füttern, und Ergebnisse zu erwarten. Die Testreihen dazu werden in den Nächsten Kapiteln behandelt.

# Tool um das Netzwerk zu überprüfen

Es gibt einige Möglichkeiten, das Netzwerk zu testen, aber Sinnvoll sind ist es in diesem Fall, Einfache Mittel zu verwenden.
Zunächst muss der Datensatz eingelesen und Nutzbar gemacht werden.
Um den Datensatz einzulesen, wird in diesem Projekt der mnist-data-reader des Authors Türkdoğan Taşdelen von seinem Github Repository "[https://github.com/turkdogan/mnist-data-reader"](https://github.com/turkdogan/mnist-data-reader%22) verwendet. Darin sind zwei Klassen wichtig:  
Der MnistDataReader liest die Dateien ein. Die Bilder und die Label sind getrennt gespeichert, und müssen für das Netzwerk zusammen gebracht werden. Daraus werden Objekte der zweiten Klasse erstellt, MnistMatrix. Diese Jedes Objekt der Klasse MnistMatrix enthält ein 2 Dimensionales Array, welches die Helligkeit eines Jeden Pixels des Bildes enthält, ein Wert zwischen 0 und 255. Außerdem kennt das Objekt das Label des Bildes.  
Auf diese Weise kann der Datensatz eingelesen und Nutzbar gemacht werden.
Das Projekt wurde für diese Projektarbeit angepasst und erweitert. Hinzugekommen ist eine Klasse MnistBuffer, welche eine bestimmte Menge an Bildern aus dem Datensatz einliest, und dann als Array zurückgibt. Dies ist hilfreich für die Spätere Umsetzung der Batches, welche später behandelt werden.
Die Klasse MNISTPrinter gibt ein String zurück, welches eine ASCII Darstellung der Bilder ermöglicht. Je nach Helligkeit eines Pixels wird ein größeres oder kleineres Zeichen ausgegeben. Hier ein Beispiel:

![[Pasted image 20231001133125.png]]

Oben Links in der Ecke steht außerdem bereits, welches Lable das Bild trägt. Teilweise sind die Zahlen auch für Menschen schlecht erkennbar, wie zum Beispiel diese 5:

![[Pasted image 20231001133317.png]]

Die Letzte Klasse ist die MNISTHTML Klasse. Diese wird dazu verwendet, um eine Ausgabe im HTML Format auszugeben. Dabei werden mehrere Beispiele aus dem Trainingsdatensatz und aus dem Testdatensatz dargestellt, zusammen mit den Outputs des Netzwerkes.
Über der 3, die oben als Beispiel gezeigt wurde, wird dies Angezeigt:

![[Pasted image 20231001133707.png]]

Das Netzwerk hat das Bild Korrekt als eine 3 Klassifizier, dies wird Grün dargestellt. Die Wahrscheinlichkeiten für jede mögliche Ausgabe werden in der Tabelle darunter dargestellt. Wir können eine Wahrscheinlichkeit von 5.52% für das Ergebnis "0" erkennen. Die Korrekte Ausgabe "3" wurde mit einer Wahrscheinlichkeit von 89,16% angegeben. Ein sehr gutes Ergebnis. An dieser Stelle soll darauf hingewiesen werden, dass die Prozentzahlen alles möglichen Ausgaben zusammengerechnet nicht 100% ergeben. Die Wahrscheinlichkeiten sind unabhängig voneinander. Sie zeigen lediglich an, wie wahrscheinlich eine Ausgabe ist, oder unwahrscheinlich, sprich, 5,52% Wahrscheinlichkeit das dieses Bild eine 0 darstellt, und eine 94,48% Wahrscheinlichkeit, dass es keine 0 ist.
So sehen die Ergebnisse für die 5 aus:

![[Pasted image 20231001134329.png]]

Hier wird Sichtbar, wie sich das Netzwerk irren kann. Das Netzwerk gibt eine 44% Wahrscheinlichkeit für die Ergebnisse "4" und "6" an. Die "5", die hier tatsächlich dargestellt wird, erhält nur eine 2,4% Wahrscheinlichkeit. Dieser Fehler ist allerdings leicht zu verzeihen, da auch Menschen häufig eine 6 erkennen. Es ist wichtig zu verstehen, dass bei diesen Datensätzen auch Menschen keine 100% Genauigkeit erreichen würden.

Kommen wir nun zu generelleren Merkmalen des Netzwerkes. Die Betrachtung einzelner Bilder sagt uns noch nicht viel über die gesamte Leistung eines Netzwerkes aus. Das Netzwerk kann Configuriert werden. Einige Merkmale wurden schon genannt, allerdings werden wir uns alle Einstellungen einmal einzeln ansehen. In der Klasse ConfigLoader wird eine Datei "config.json" eingelesen, welche alle Einstellungen für unser Netzwerk enthält.

### LayerSizes

Die Anzahl der Layer und die Anzahl ihrer jeweiligen Nodes müssen als erstes Festgelegt werden. Das ziel ist es, ein Netzwerk zu finden, das Präzise genug ist, um gute Klassifikationen zu errechnen, aber auch nicht zu Groß zu sein, da der Aufwand beim Training des Netzes Exponentiell Steigt. Mit Jede weiteren Schicht kommt eine Gewichts Matrix dazu, und diese sind Quadratisch, und wachsen daher zusammen mit der Anzahl der Knoten in einer Schicht Exponentiell. 
Die Erste Schicht ist die Input Schicht. Diese sollte nicht frei gewählt werden, sondern anhand des zu lernenden Datensatzes angepasst werden. Jedes Bild im MNIST Datensatz enthält 784 Pixel, das ist also die Größe unserer Input Schicht.
Auch die Output Schicht sollte nicht frei gewählt werden, sondern der Anzahl der möglichen Label entsprechen. Der MNIST Datensatz hat 10 Zahlen, 0-9, also gibt es 10 mögliche Label, also muss die Output Schicht 10 Knoten enthalten.

### SplitIndex

In unserem Fall werden die Trainings Daten und die Test Daten gemeinsam eingelesen, und mit diesem Index kann festgelegt werden, wie viele Bilder des gesamten Datensatzes als Trainingsdatensatz benutzt werden sollen. Dies ist hilfreich um später die Effekte zu kleiner Datensätze zu demonstrieren, sowie leichter ein Over Fitting entstehen zu lassen. Dazu später mehr.

### TrainingCycles/Epochen

In unserem Netzwerk werden sie TrainingCycles genannt, in der Fachliteratur allerdings meist Epochen. Dabei handelt es sich um die Anzahl an Durchläufen, die das Netzwerk den Trainingsdatensatz lernt. Es kann durchaus nützlich sein, die Daten mehrmals zu durchlaufen, das heißt die Selben Daten wiederholt zu lernen. Dabei muss allerdings ebenfalls aufgepasst werden, da es zu einem Phänomen kommen kann, welches Over Fitting genannt wird. Dabei Handelt es sich um den Umstand, dass ein Ausreichend Großes Netzwerk einen Datensatz auch Auswendig lernen kann, anstatt generelle Rückschlüsse über die Natur der Daten zu ziehen. Dies ist häufig daran sichtbar, dass das Netzwerk eine ungewöhnlich Hohe Genauigkeit auf den Trainingsdaten aufweist, auf den Testdaten allerdings weit zurückfällt. Damit Demonstriert das Netzwerk, dass es die Trainingsdaten auswendig gelernt hat, und die Testdaten nicht viel schlechter versteht. 

### LearnRate
Die LearnRate sollte mittlerweile bekannt sein. Dabei handelt es sich um eine Zahl, mit der die Anpassungsrate verrechnet wird, um ein Over Shooting zu vermeiden. Wenn das Netzwerk zu Große Schritte in Richtung eines Fehlerminimums geht, kann es dazu kommen, dass es über den Tiefpunkt hinaushüpft, im schlimmsten falle sogar komplett aus dem Tal des Tiefpunkts hinausspringt.

![[Pasted image 20230917181526.png]]
[Quelle](https://medium.com/diogo-menezes-borges/what-is-gradient-descent-235a6c8d26b0)
