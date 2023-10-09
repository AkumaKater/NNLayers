# Biases
## Erklärung zum Nutzen

Die Erklärung und die Bilder in diesem Abschnitt basieren auf einem Thread aus stackoverflow[5], sowie eigenen Erfahrungen.

Biases geben uns die Möglichkeit, die Schwellwert Funktion zu verschieben. Kurzgefasst, das Netzwerk wird Felxibler. 
Um etwas genauer zu werden, sehen wir uns einmal an, wie die Graphen aussehen im Bezug auf die Inputs $x$, nachdem sie mit den Gewichten $w$ verrechnet werden. Für $w$ nehmen wir als Beispiels Werte 0.5, 1, und 3.

![[Pasted image 20231005144238.png]]

Alle Graphen verlaufen durch den 0 Punkt. Dies sorgt allerdings auch dafür, dass das Netzwerk recht unflexibel ist. Nehmen wir an, dass es zwei Klassen gibt, die das Netzwerk unterscheiden können soll, und Klasse 1 befindet sich in $x$ < 1 sowie $y$ < 1, dann wäre es schlicht unmöglich, mit einem dieser Graphen eine einfache Klassifizierung vorzunehmen. Wenn wir allerdings einen Bias $b$ addieren, dann lässt sich so ein Graph entlang der $y$ Achse verschieben.

![[Pasted image 20231005144628.png]]

Mit diesem Graphen wäre unser Beispiel dann gelöst, alle Elemente der Klasse 1 liegen unterhalb des Graphen, während alle Elemente der Klasse 2 größer als 1 in beiden Achsen sind, und damit über dem Graphen liegen.
Um die Berechnung im Neuron einmal darzustellen, hier eine Graphik[5]:

![[Pasted image 20231005133406.png]]

Nachdem die Gewichte mit den Inputs multipliziert wurden, werden die Biases dazu addiert. Dieser Wert wird dann an die Schwellenfunktion übergeben. Hier ein beispiel mit der Sigmoid Funktion:

![[Pasted image 20231005145933.png]]

Der Bias von 2 und -2 verschiebt die Sigmoid Funktion nach Rechts und nach Links. Dadurch hat das Netzwerk eine Größere Kontrolle darüber, wie Stark ein Input sein muss, um ein Neuron/Knoten zu Aktivieren.

## Mathematische Umsetzung

Sehen wir uns als nächstes also an, was wir im Netzwerk ändern müssen. Bisher sah die Rechnung so aus:$$Z_2 = a_1 * w_2$$jetzt Addieren wir allerdings noch den Bias: $$Z_2 = a_1 * w_2 + b$$
Zuvor haben wir $Z_2$ im Bezug auf $w_2$ betrachtet, um die Ableitung zu bilden. Nun müssen wir dies Eigentlich noch einmal machen, um festzustellen, ob wir an unseren bisherigen Formeln etwas ändern müssten.
$$\frac{\Delta Z_2 }{\Delta w_2 } = \frac{ a_1 * (w_2+h) + b -(a_1 * w_2 + b) }{ h }$$
$$\frac{\Delta Z_2 }{\Delta w_2 } = \frac{ a_1*w_2+a_1*h + b -a_1 * w_2 - b }{ h }$$
$$\frac{\Delta Z_2 }{\Delta w_2 } = \frac{ a_1*w_2+a_1*h-a_1 * w_2 }{ h }$$
$$\frac{\Delta Z_2 }{\Delta w_2 } = \frac{a_1*h }{ h }$$
$$\frac{\Delta Z_2 }{\Delta w_2 } = a_1$$
Die Konstante $c$ fällt einfach weg, und hat keinen Einfluss. Daher müssen wir am bestehenden Code nichts weiter hinzufügen. Aber wie genau berechnen wir die Änderungsrate für die Biases?

Die Änderungsrate der Biases kann ebenfalls mit der Ableitung berechnet werden, also schreiben wir:
$$\frac{ dc }{ db_{ 2 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ db_{ 2 } }$$
Die ersten beiden Terme sind uns bereits bekannt, und im Code haben wir sie NodeValues genannt. Nur der letzte Term hat sich verändert, also müssen wir die Ableitung bilden.
$$\frac{\Delta Z_2 }{\Delta b_2 } = \frac{ a_1 * w_2 + b+h -(a_1 * w_2 + b) }{ h }$$
$$\frac{\Delta Z_2 }{\Delta b_2 } = \frac{ a_1 * w_2 + b+h -a_1 * w_2 - b }{ h }$$
$$\frac{\Delta Z_2 }{\Delta b_2 } = \frac{ a_1 * w_2 +h -a_1 * w_2 }{ h }$$
$$\frac{\Delta Z_2 }{\Delta b_2 } = \frac{ h  }{ h }$$
$$\frac{\Delta Z_2 }{\Delta b_2 } = 1$$
Die Ableitung entspricht einfach 1. Das bedeutet, dass die Änderungsrate für die Biases in unserem Code so aussehen müsste:
$$Änderungsrate_b = 1*NodeValues$$

## Biases im Code

Zunächst brauchen wir genau wie bei den Gewichten und der Änderungsrate für die Gewichte, eine Instanzvariable für die Biases in der Layer Klasse, und eine Instanzvariable für die Änderungsrate der Biases:

```java
double[] bias;
// --> Steigung der Cost Funktion im Bezug auf die Biases b
double[] CostSteigungB;
```

Die Biases werden zum Schluss verrechnet, und das auch nur einmal, und nicht mit jedem Gewicht. Daher brauchen wir nur ein einfaches Array, und dieses muss so groß sein, wie die Anzahl an Knoten in der Schicht. Genau wie die Gewichte, müssen die Biases ebenfalls mit Zufallszahlen initialisiert werden. Dies geschieht im Konstruktor:

```java
bias = NNMath.RandomDoubleArray(numOutputNodes);
CostSteigungB = new double[numOutputNodes];
```

Dann kommen wir zu Methode "CalculateOutputs". Anstatt die Variable weightedInput am Anfang der äußeren Schleife mit 0 zu initialisieren, fangen wir mit den Biases an:

```java
public double[] CalculateOutputs(double[] inputs) {
    this.inputs = inputs;
    Activation activ = Activation.geActivation();
    for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
        //double weightedInput = 0;
        //Wird nun mit dem Bias initialisiert
        double weightedInput = bias[nodeOut];
        for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
            weightedInput += inputs[nodeIn] * weights[nodeOut][nodeIn];
        }
        weightedInputs[nodeOut] = weightedInput;
        activations[nodeOut] = activ.ActivationFunction(weightedInput);
    }
    return activations;
}
```

Jetzt muss die Methode "UpdateGradients" angepasst werden, damit die Änderungsraten nicht nur für die gewichte, sondern auch für die Biases gespeichert werden.

```java
public void UpdateGradients(double[] nodeValues) {
    for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
        for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
            double derivativeCostWrtWeight = inputs[nodeIn] * nodeValues[nodeOut];
            CostSteigungW[nodeIn][nodeOut] += derivativeCostWrtWeight;
        }
        //Hier werden die Änderungsraten für die Biases gespeichert
        CostSteigungB[nodeOut] = 1 * nodeValues[nodeOut];
    }
}
```

Und zuletzt  müssen die Änderungsraten auch von den Biases abgezogen werden, und danach müssen die Änderungsraten zurückgesetzt werden. Die Änderungsrate muss ebenfalls mit der LearnRate multipliziert werden.

```java
public void ApplyGradient(double learnrate) {
    for (int i = 0; i < numOutputNodes; i++) {
        for (int j = 0; j < numInputNodes; j++) {
            weights[i][j] -= CostSteigungW[j][i] * learnrate;
        }
        //Die Änderungsraten der Biases müssen von den Biases abgezogen werden
        bias[i] -= CostSteigungB[i] * learnrate;
    }
}
public void ClearGradient() {
    this.CostSteigungW = new double[numInputNodes][numOutputNodes];
    //Die Änderungsraten für die Biases müssen auch zurückgesetzt werden
    this.CostSteigungB = new double[numOutputNodes];
}
```

Das Netzwerk verfügt nun über Biases, und damit über eine größere Flexibilität. Als nächstes Testen wir das neue Netzwerk.

## Testreihen mit Biases

Zunächst einmal Testen wir die LearnRate. Unter Umständen kann sich etwas geändert haben.

#### Ohne Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.009</td>
		<td>76,22%</td>
		<td>76,61%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.008</td>
		<td>72,43%</td>
		<td>73,05%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.007</td>
		<td>76,30%</td>
		<td>78,00%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.006</td>
		<td>78,16%</td>
		<td>77,91%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.005</td>
		<td>79,07%</td>
		<td>80,02%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.004</td>
		<td>78,64%</td>
		<td>79,38%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.003</td>
		<td>81,03%</td>
		<td>81,46%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.002</td>
		<td>79,05%</td>
		<td>79,90%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.001</td>
		<td>72,60%</td>
		<td>73,36%</td>
	</tr>
</table>

#### Mit Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.009</td>
		<td>75,22%</td>
		<td>75,30%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.008</td>
		<td>78,36%</td>
		<td>78,83%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.007</td>
		<td>79,92%</td>
		<td>80,95%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.006</td>
		<td>79,24%</td>
		<td>79,48%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.005</td>
		<td>78,86%</td>
		<td>79,74%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.004</td>
		<td>78,98%</td>
		<td>79,62%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.003</td>
		<td>78,23%</td>
		<td>78,52%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.002</td>
		<td>78,28%</td>
		<td>79,13%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.001</td>
		<td>72,84%</td>
		<td>73,47%</td>
	</tr>
</table>

Im Vergleich ist nicht direkt sichtbar, ob das Netzwerk mit Biases bessere Ergebnisse erzielt. Bei einer LernRate von 0.003 sehen wir keine Verbesserung, sondern sogar eine niedrigere Genauigkeit.
Dennoch ist es Ratsam, Netzwerke mit Biases zu verwenden. Nehmen wir an, alle Pixel in einem Bild wären leer, dann würde bei einem Netzwerk ohne Biases in jedem Output nur 0 als Ergebnis möglich sein. Sollten wir das Netzwerk darauf trainieren wollen, auch unbeschriebene Bilder erkennen zu können, wäre das Ohne Biases schlicht nicht möglich. In unserem beispiel mit dem MNIST Datensatz ist der nutzen leider nicht direkt sichtbar, aber für andere Aufgaben kann es unumgänglich sein, Biases zu verwenden.
Zur Vollständigkeit hier noch ein Vergleich mit Training von verschieden vielen Epochen. Dabei ist auch keine große Verbesserung festzustellen. Unterschiede von 1-2% können dem Zufall zugeschrieben werden.
#### Ohne Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,86%</td>
		<td>88,33%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>10</td>
		<td>1</td>
		<td>0.003</td>
		<td>89,08%</td>
		<td>89,49%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>15</td>
		<td>1</td>
		<td>0.003</td>
		<td>89,75%</td>
		<td>90,15%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>20</td>
		<td>1</td>
		<td>0.003</td>
		<td>90,57%</td>
		<td>90,70%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,47%</td>
		<td>91,72%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>30</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,91%</td>
		<td>91,93%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>35</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,40%</td>
		<td>91,53%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>40</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,20%</td>
		<td>91,32%</td>
	</tr>
</table>

#### Mit Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>86,98%</td>
		<td>87,63%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>10</td>
		<td>1</td>
		<td>0.003</td>
		<td>89,09%</td>
		<td>89,58%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>15</td>
		<td>1</td>
		<td>0.003</td>
		<td>89,87%</td>
		<td>90,16%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>20</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,04%</td>
		<td>91,15%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,31%</td>
		<td>91,85%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>30</td>
		<td>1</td>
		<td>0.003</td>
		<td>92,21%</td>
		<td>92,32%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>35</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,77%</td>
		<td>91,98%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>40</td>
		<td>1</td>
		<td>0.003</td>
		<td>92,26%</td>
		<td>92,44%</td>
	</tr>
</table>

Eine Letzte Möglichkeit, vielleicht doch einen Nutzen Nutzen sichtbar zu machen, liegt in der Verringerung der Knoten in der Versteckten Schicht. 
These: Die erhöhte Flexibilität könnte das Netzwerk dahingehend effizienter machen, dass es weniger Knoten braucht, um vergleichbare Ergebnisse zu erzielen.

#### Ohne Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 10, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>70,48%</td>
		<td>70,41%</td>
	</tr>
	<tr>
		<td>[784, 20, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>82,34%</td>
		<td>81,93%</td>
	</tr>
	<tr>
		<td>[784, 30, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>81,99%</td>
		<td>82,77%</td>
	</tr>
	<tr>
		<td>[784, 40, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>83,50%</td>
		<td>84,34%</td>
	</tr>
	<tr>
		<td>[784, 50, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>85,64%</td>
		<td>86,54%</td>
	</tr>
	<tr>
		<td>[784, 60, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>86,35%</td>
		<td>87,03%</td>
	</tr>
	<tr>
		<td>[784, 70, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>85,36%</td>
		<td>86,00%</td>
	</tr>
	<tr>
		<td>[784, 80, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,50%</td>
		<td>87,87%</td>
	</tr>
	<tr>
		<td>[784, 90, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,62%</td>
		<td>88,29%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,14%</td>
		<td>87,74%</td>
	</tr>
	<tr>
		<td>[784, 150, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>88,40%</td>
		<td>89,08%</td>
	</tr>
	<tr>
		<td>[784, 200, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>89,50%</td>
		<td>90,08%</td>
	</tr>
</table>

#### With Bias
<table>
	<tr>
		<td>HLayersSizes</td>
		<td>DataSize</td>
		<td>Epochen</td>
		<td>BatchSize</td>
		<td>Learnrate</td>
		<td>ACtrainingD</td>
		<td>ACtestD</td>
	</tr>
	<tr>
		<td>[784, 10, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>71,86%</td>
		<td>72,21%</td>
	</tr>
	<tr>
		<td>[784, 20, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>80,29%</td>
		<td>81,08%</td>
	</tr>
	<tr>
		<td>[784, 30, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>82,55%</td>
		<td>82,88%</td>
	</tr>
	<tr>
		<td>[784, 40, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>84,54%</td>
		<td>85,50%</td>
	</tr>
	<tr>
		<td>[784, 50, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>84,18%</td>
		<td>85,02%</td>
	</tr>
	<tr>
		<td>[784, 60, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>85,59%</td>
		<td>86,27%</td>
	</tr>
	<tr>
		<td>[784, 70, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>85,42%</td>
		<td>85,95%</td>
	</tr>
	<tr>
		<td>[784, 80, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,12%</td>
		<td>87,72%</td>
	</tr>
	<tr>
		<td>[784, 90, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>86,76%</td>
		<td>87,39%</td>
	</tr>
	<tr>
		<td>[784, 100, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>87,41%</td>
		<td>87,72%</td>
	</tr>
	<tr>
		<td>[784, 150, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>88,31%</td>
		<td>88,86%</td>
	</tr>
	<tr>
		<td>[784, 200, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>88,98%</td>
		<td>89,62%</td>
	</tr>
</table>

Leider kann die These mit diesem Datensatz nicht bestätigt werden. Ein Anderer Datensatz könnte die These bestätigen, oder die These ist vollständig falsch.