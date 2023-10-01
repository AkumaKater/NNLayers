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

