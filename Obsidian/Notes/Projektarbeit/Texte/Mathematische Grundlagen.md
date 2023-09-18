# Initialisierung

## Grundgedanke
Bei der Initialisierung des Netzwerkes soll eine Instanz der Klasse NeuralNetwork erstellt werden. Wichtig ist hierbei, dass das Netzwerk über alle Variablen verfügt, die hierbei wichtig sind, und dass es im Konstruktor möglich ist, wichtige Eigenschaften so dynamisch anzulegen, wie es nur geht. Für den Learn Algorithmus ist später eine LearnRate wichtig, das wird später im Backpropagation Algorithmus wichtig. Abgesehen davon, muss es möglich sein, die Menge der versteckten Schichten, sowie die Menge der Nodes auf diesen festzulegen. für eine leichtere Lesbarkeit, und leichtere Skalierung des Netzwerks, ist es Vorteilhaft, die Schichten, oder auch Layer, in einer eigenen Klasse festzulegen.

  

Diese Layer müssen also auch vorbereitet werden. Wie bereits erwähnt, ist jede Schicht durch Kanten mit der nächsten Schicht verbunden.  Von jedem Knoten aus dem Input Layer gehen Kanten an jeweils jeden Knoten der ersten versteckten Schicht. Und genau so ist es zwischen der Versteckten Schicht zu jeder nachfolgenden versteckten Schicht, bis hin zum Output Layer. Jede diese Kanten ist gewichtet, und diese gewichte müssen in jedem Layer gespeichert werden. Das Input Layer ist mehr Symbolisch dargestellt, da es sich bei den Knoten dieser Schicht nur um die Inputs handelt, mit denen das Netzwerk gefüttert werden soll. Bezogen auf den MNIST Datensatz, bedeutet das, dass jeder Pixel eines Bildes ein Knoten der Input Layer ist. Daraus ergibt sich bei 28 X 28 Pixeln eine Input Schicht von 784 Knoten. Diese unterliegen natürlich noch keine Activation Function/Schwellwertfunktion, und werden daher direkt in die erste versteckte Schicht geleitet. Die Implementation dieser Schichten kann von hier an generalisiert werden.

Jede Schicht sollte alle Gewichte der Kanten speichern, mit denen sie mit der vorherigen Schicht verbunden sind. Diese Gewichte lassen sich in Form einer Matrix speichern. Die erste Dimension dieser Matrix entspricht der Anzahl der Inputs, die der Layer aus der vorherigen Schicht erhält, also die Anzahl der Outputs der vorherigen Schicht. Die Zweite Dimension entspricht der Knoten, über die das Layer verfügt.

Ein bestimmtes Gewicht kann also so notiert werden:

Die Kante von dem 3ten Knoten der Input Schicht zum 2ten Knoten der versteckten Schicht wird gewichtet durch das Gewicht W<sub>3 2</sub>.

Das Layer braucht, im Initialisiert zu werden, also lediglich zwei Werte, die Anzahl der Input Nodes, und die Anzahl der eigenen Output Nodes. Daraus kann ein 2-dimensionales Array erstellt werden, mit eben diesen Abmaßen. An dieser Stelle muss darauf hingewiesen werden, dass es Sinnvoll ist, die Matrix von vornherein transponiert zu speichern, da es für die meisten Rechnungen Notwendig sein wird, diese ohnehin zu Transponieren.
## Code
Das Neuronale Netzwerk braucht zuerst ein Array, in welchem alle Layer gespeichert sind, außerdem ein Double für die LearnRate.

Der Konstruktor hat zunächst die Parameter für die learnRate, und dann noch "variable arguments", welche die jeweilige Anzahl an Knoten pro Schicht angeben sollen.

Wie oben erklärt, ist es nicht Notwendig, eine eigene Schicht für die Input Schicht zu instantiieren. Daher fängt die For-Schleife auch erst bei 1 an. Der erste integer, der übergeben wird, muss die Größe der Input Schicht wiedergeben, beim MNIST Datensatz also 784. Der letzte Integer gibt die Größe der Output Schicht an. Die Output Schicht muss so groß dein, wie die Menge, der möglichen Antworten. Bei dem MNIST handelt es sich um die Handschriftlichen zahlen von 0 - 9, daher muss die Output Schicht 10 Knoten haben. Alle anderen Integer, die dazwischen eingetragen werden, entsprechen der Größe einer jeden versteckten Schicht.
```Java
public class NeuralNetwork {
    Layer[] layers;
    double learnRate;

    // Initialisierung
    public NeuralNetwork(double learnRate, int... layerSizes) {
        layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i], layerSizes[i + 1]);
        }
        this.learnRate = learnRate;
    }
}
```

Um ein Layer zu erstellen, wird, wie oben beschrieben, die Menge der Outputs der vorherigen Schicht, und die Menge der eigenen Knoten gebraucht, damit die Gewichtsmatrix mit den richtigen Dimensionen instantiiert wird.

Bei diesem Schritt ist es wichtig, dass die Gewichte mit zufälligen Werten vor instantiiert werden. Das kann dabei helfen, dass das Netzwerk durch das Zufallsprinzip bereits näher an einem Globalen Minimum der Error Funktion startet, als an einem schlechteren Lokalen Minimum. Außerdem vermeidet man dadurch auch Symmetrie: Wenn alle gewichte mit dem gleichen Wert anfangen, ist auch die Änderungsrate meist die gleiche. Dadurch kommt es vor, dass sich die Neuronen im schlimmsten Fall auf die gleichen Merkmale konzentrieren. Die Vorbelegung dieser Arrays verlagern wir in diesem Beispiel auf eine andere Klasse, um die Hauptklassen hier leserlich zu halten. Der Gesamte Code ist im Anhang zu finden.

Die Gewichtsmatrix wird hier bereits transponiert aufgebaut.
``` Java
public class Layer {

	int numInputNodes, numOutputNodes;
	double[][] weights;

	public Layer(int numInputNodes, int numOutputNodes) {
		this.numInputNodes = numInputNodes;
		this.numOutputNodes = numOutputNodes;
		weights = NNMath.RandomDoubleArrayMatrix(numOutputNodes,numInputNodes);
	}
}
```

# Querry

## Grundgedanke
Die Querry Funktion ist dazu da, um ein Ergebnis vom Netzwerk zu erlangen. Im Prinzip wollen wir ein Array, in welchem für jede Antwortmöglichkeit die Wahrscheinlichkeit zu finden ist, die das Netzwerk der jeweiligen Antwort zumisst. 
Das heißt also, dass die Funktion erstmal im "NeuralNetwork" die Inputs braucht. Die Inputs müssen von Schicht zu Schicht gereicht werden, müssen in den einzelnen Knoten verarbeitet werden und dann als Output zurückgegeben werden. Die Outputs der ersten Schicht werden dann zu den Inputs für die nächste Schicht und immer so weiter bis die Output Schicht die ein Ergebnis liefert.
## Code
```Java
// Abfragen in NeuralNetwork.java
    public double[] Querry(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }
```

# CalculateOutputs
## Grundgedanke
Im Layer Skript müssen die Outputs berechnet werden. Dazu können wir uns die Ursprüngliche Inspiration für neuronale Netzwerke ansehen, das Neuron!
![[Pasted image 20230912175903.png]]
[Quelle](https://miro.medium.com/v2/resize:fit:640/format:webp/1*SJPacPhP4KDEB1AdhOFy_Q.png)
Da es sich nur um eine Lose Inspiration handelt, ist es nicht notwendig, sich tiefer mit dem Gehirn auseinander zusetztes, allerdings war es tatsächlich die Inspiration für das neuronale Netzwerk. Das Gehirn besteht aus einer Vielzahl an Neuronen, Schätzungsweise aus 10¹⁰ bis 10¹¹ Nervenzellen (Quelle Einführung in Neuronale Netzte Burkhard Lenze ToDo).
Wichtig sind bei dem Neuron die folgenden Bestandteile:
- Dendriten
- der Zellkörper
- das Axon
Die ein Neuron hat mehrere ***Dendriten***, die dazu verwendet werden, um Impulse aufzunehmen. Also können sie mit unseren Inputs verglichen werden. Der ***Zellkörper*** ist dafür da, die Impulse zu verarbeiten. Es wurde beobachtet, dass ein bestimmter Schwellwert überschritten werden muss, damit ein Neuron selbst wider einen Impuls abgibt. Dies werden wir mit einer Aktivierungsfunktion ebenfalls simulieren, doch dazu später mehr. zuletzt hat jedes Neuron ein ***Axon***, welches dazu verwendet wird, um einen Impuls abzugeben. Daher ist es mit den Outputs eines Knotens vergleichbar.
Genau wie bei dem Neuron, wird in dem Neuronalen Netz zuerst jeder Input in jeden Knoten geleitet, dort dann verarbeitet, und die Ergebnisse dieser Verarbeitung werden an die nächste Schicht weitergeleitet.

## Mathematik
Sehen wir uns zunächst ein Einfaches Netzwerk mit einer versteckten Schicht an. Jede Schicht hat nur einen Knoten:
![[Pasted image 20230912184748.png]]
[Quelle](https://www.youtube.com/watch?v=hfMk-kjRv4c)

Der erste Knoten wird *a*<sub>0</sub> genannt, und entspricht schlichtweg dem Input. Dieser wird an die erste Schicht geleitet, an alle darin vorhandenen Knoten. Dort wird es erst gewichtet, das heißt mit dem Gewicht *w*<sub>1</sub> , welches der Kante zugewiesen ist multipliziert. Hier wird es jetzt Spannend. Nachdem alle Inputs in unseren Knoten miteinander verrechnet sind, muss das Ergebnis erst noch durch die Aktivierungsfunktion.

### Aktivierungsfunktion
Die am Häufigsten genutze Aktivierungsfunktion ist die Sigmoid Funktion. Diese sieht so aus:
![[Pasted image 20230918125041.png]]
![[Pasted image 20230912192401.png]]
Jeder Wert, der hier hinein läuft, wird auf einen Wert zwischen 0 und 1 verkleinert. 
Um ein Starkes Signal an die Nächsten Schicht zu senden, muss die Summer aller eingegangenen und danach gewichteten Signale Groß genug sein, um nach der Sigmoid Funktion noch näher an der 1 zu sein als an der 0. 
Es gibt eine ganze Reihe dieser Funktionen, hier einige Beispiele mit Erklärung:

***Die Identität***
![[Pasted image 20230912194736.png]]
Hierbei ist ***f(x) = x***. Die Werte können dabei allerdings zu Groß werden, und daher benutzt man lieber die Sigmoid Funktion.

**Die Sprung Funktion**
![[Pasted image 20230912195705.png]]
Diese Funktion gibt bei *{x>=0}* eine 1 aus, bei *{x<0}* immer eine 0.
Dadurch können allerdings Sprunghafte veränderungen im Netztwerk eintreten, die unvorteilhaft sind.

**ReLu**
![[Pasted image 20230912200758.png]]
Ähnlich wie **Die Identität**, allerdings verläuft sie bei *{x<0}* bei 0.
ReLu wird auch häufiger verwendet.

Anmerkung des Autors der Arbeit: Ich habe nach einer Möglichkeit gesucht, die recht aufwendige Sigmoid Funktion etwas kosten effizienter ausrechnen zu lassen. Dabei habe ich einen Hinweis gefunden. Die Sigmoid Funktion kann im vorhinein in Hundert Schritten in eine Look Up Tabelle eingetragen werden, so dass sie nicht mehr jedes mal ausgerechnet werden muss. Dabei habe ich festgestellt, dass das Netzwerk kaum an Geschwindigkeit gewinnt, allerdings 2 bis 4% an Genauigkeit gewinnt. Mir ist nicht vollends klar, woher diese Verbesserung kommt, allerdings sind mir derartige Beobachtungen schon häufiger untergekommen. Meist scheint es daran zu liegen, dass es dem Netzwerk schwerer fällt, sich in einem Lokalen Minimum fest zufahren. Dies ist an dieser Stelle allerdings reine Spekulation.

### Code
Den Code für die Aktivierungsfunktion. Um Die Funktionen später leichter austauschen zu können, Implementieren wir hier die Abstrakte Klasse "Activation", und implementieren dann die Unterklassen Sigmoid und ReLu. Eine Statische Methode "getActivation" ermöglicht es, aus jedem Kontext heraus auf die richtige Activation Function zuzugreifen. Mit der setter Methode kann man eine andere Activation Klasse auswählen.
```java
public abstract class Activation {
    public abstract double ActivationFunction(double weightedInput);

	static Activation activation = new Sigmoid();

    public static Activation geActivation(){
        return activation;
    }
    public static void setActivation(String Activation){
        switch (Activation) {
            case "Sigmoid":
                activation = new Sigmoid();
                break;
            case "ReLu":
                activation = new ReLu();
                break;
            default:
                activation = new Sigmoid();
                break;
        }
    }
}

class Sigmoid extends Activation{
    public double ActivationFunction(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
}

class ReLu extends Activation{
    public double ActivationFunction(double weightedInput) {
        return Math.max(0, weightedInput);
    }
}
```

## Layer Implementation
Nach diesem Stück Vorarbeit kommen wir nun zum Abschluss der Querry. Wie genau Setzen wir das alles jetzt zusammen?
Wie bereits vorbereitet, wird jede Schicht vom NeuralNetwork in der Querry Methode aufgerufen. von jedem Layer wird die Methode "CalculateOutputs(double[] inputs)" aufgerufen, mit den Inputs versorgt, und danach werden die Outputs erwartet, um an die nächste Schicht weitergegeben zu werden.
Um uns Arbeit zu sparen, greifen wir einmal der Thematik vorraus. Für die Umsetzung des Learn Algorithmus brauchen wir die Inputs, die jede Schicht erhalten hat, die gewichteten Inputs auch und die Outputs, die schon durch die Aktivierungsfunktion gegangen sind. Daher müssen wir jetzt erstmal drei Arrays hinzufügen. Wir nennen sie "inputs", "weightedInputs" und "activations". Für unsere versteckte Schicht entsprechen diese Werte also a<sub>0</sub> für "inputs", und a<sub>1</sub> für die "activations"

```java
public class Layer {

	int numInputNodes, numOutputNodes;
	double[][] weights;

    double[] inputs;
    double[] weightedInputs;
    double[] activations;
    .
    .
    .
```

Wenn wir nun die "CalculateOutputs" Methode aufrufen, dann muss das folgendermaßen ablaufen:
Zuerst werden die Inputs gespeichert.
Für jeden Wert der Outputs, die wir hier "activations" nennen, müssen wir zuerst die Summe aller gewichteten Inputs ausrechnen. Das bedeutet, dass eine Schleife nötig ist, die über alle Felder der "weightedInputs" läuft, und dabei die "weights" berücksichtigt. während die Schleife läuft, können direkt die "weightedInputs" abgespeichert werden und direkt danach können die "activations" ebenfalls ausgerechnet und gespeichert werden. Zum Schluss werden die "activations" zurückgegeben.
## Code
```java
    public double[] CalculateOutputs(double[] inputs) {
        this.inputs = inputs;
        Activation activ = Activation.geActivation();
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            double weightedInput = 0;
            for(int nodeIn = 0; nodeIn<numInputNodes; nodeIn++){
                weightedInput += inputs[nodeIn] * weights[nodeOut][nodeIn];
            }
            weightedInputs[nodeOut] = weightedInput;
            activations[nodeOut] = activ.ActivationFunction(weightedInput);
        }
        return activations;
    }
```

# Learn
Nun kommen wir endlich zum Herzstück des Netzwerkes. Das Netzwerk braucht Daten, um um zu lernen. Rückschlüsse zu ziehen und korrekte Vorhersagen zu treffen. Nur wenn es die Trainingsdaten gut verstanden hat kann das Netzwerk die Testdaten richtig Klassifizieren. Aber noch kann das Netzwerk, das hier programmiert wird, nicht lernen.
## Cost Function
Wir fangen hier einmal ganz am Ende an. Bisher kann das Netzwerk eine Ausgabe machen, indem wir ein Bild in die Querry geben. Dafür erhalten wir ein Array an Zahlen zurück. Diese Zahlen ergeben aber noch überhaupt keinen Sinn. Da die Gewichte Zufällig belegt wurden, sind auch die Ergebnisse, die das Netzwerk hervorbringt, rein Zufällig. 
Wie kann das Netzwerk sich dann jetzt verbessern? Bei einem sehr kleinen Netzwerk mit 1 bis 3 Knoten könnte man die Gewichte manuell anpassen. Das wäre aber nur für Probleme möglich, die eben so klein und unbedeutend sind, dass es sich die Mühe eines neuronalen Netzwerkes nicht loht. Bei Größeren Komplexen Problemen möchten wir diesen Prozess so weit es geht Automatisieren.
Intuitiv ist leicht zu verstehen, dass es nötig ist, den eignen Fehler zu kennen, bevor man ihn verbessern kann. Das gilt auch für das Netzwerk. Wir müssen zunächst ausrechnen, wie falsch das Netzwerk war. Dazu nehmen wir jedes Ergebnis aus dem Output Array der Querry, ziehen sie von den erwarteten Werten ab, und summieren alle Werte zusammen. Im folgenden werden die erwarteten Werte "Targets" genannt, also die Ziel-Werte. Um diese zu erhalten, brauchen wir eigentlich nur ein Array, welches genau so groß ist, wie das Output Array, und in diesem Array setzten wir alle Werte auf 0, außer das Feld mit dem Index, welches dem Label des Bildes entspricht. Dieses Feld setzten wir auf 1. Hier ein ausschnitt aus der Klasse MnistMatrix, aus dem Paket MNISTReader, um die Targets zu berechnen.

```Java
public double[] getTargets(){
    double[] targets = new double[10];
    targets[label] = 1.0;
    return targets;
}
    ```

Danach werden die Targets dazu verwendet, um den Fehler des Netzes, oder auch die Kosten des Netzes zu berechnen. 
- Die Outputs werden von den Targets abgezogen
- Alle Ergebnisse dieser Rechnung werden aufaddiert
- Das Ergebnis wird zurück gegeben, und entspricht den Kosten des Netzes

```Java
double Cost(MnistMatrix dataPoint) {
        double[] QuerryOutputs = Querry(dataPoint.getInputs());
        double[] Targets = dataPoint.getTargets();
        double cost = 0;
        for(int i=0; i<Targets.length; i++) {
            cost = Targets[i]-QuerryOutputs[i];
        }
        return cost;
    }
```

## Was bringt uns die Cost Funktion?
Wie geht es jetzt weiter? In Unserem Code wird die Cost Funktion von hier an nicht mehr aufgerufen. Aber Sie ist dennoch wichtig: denn Das Ziel unseres Netzwerkes muss es sein, diese Cost Funktion zu minimieren. 

Wenn wir den Graphen einer Cost Function plotten und die Gewichtungen Gewichte als unabhängige Variable "w" festlegen, um den Verlauf der Funktion "f(w) zu visualisieren, könnte dies beispielsweise folgendermaßen aussehen, gesetzt den Fall, dass wir nur eine einzelne Gewichtung betrachten:
![[Pasted image 20230914193809.png]]
[Quelle](https://www.youtube.com/watch?v=hfMk-kjRv4c)

Für dieses einfache Netzwerk wäre es nun das beste, wenn wir das "w" so wählen, dass ein Globales Minimum erreicht wird. 
## Warum lässt sich das nicht Analytisch berechnen?
man könnte annehmen, dass die Besten Ergebnisse damit erzielt werden könnten, indem man die Tiefpunkte mit der 3. Ableitung errechnet, aber das ist leider nicht so einfach.
Die vielen Dimensionen, und die Hoche Komplexität erlauben das nicht so einfach. Um ein Beispiel zu nennen, der Graph der Cost Funktion ist bei jedem Bild, dass wir in das Netz Fütter ein wenig anders. Daher ist es Sinnvoller sich Schrittweise eine allgemeinen Lösung zu nähern. Dieses Verfahren heißt Gradient Descent. 

## Gradient Descent
Man kann sich das Gradient Descent Verfahren ein wenig so vorstellen wie eine Kugel, die man einen Hügel herabrollen lässt. Entsprechend der Neigung unter ihr, rollt sie auf dem direktesten Weg in das nächste Tal. 
![[Pasted image 20230915145401.png]]
[Quelle](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.2pKHEzMrh3NbrTHsToGwzwHaEA%26pid%3DApi&f=1&ipt=0143a98e2b0b1bd0d6b6adb608301673298c518b31f1b0fe9e95d4b52b86e8cc&ipo=images)

Hierbei sollte das größte Problem des Verfahrens auch schon klar werden: Das nächste Tal ist nicht unbedingt das Tiefste.
![[Pasted image 20230915223421.png]]
[Quelle](https://www.phamduytung.com/blog/2018-11-01-overview-of-gradient-descent-optimization-algorithm/)

Wir initialisieren die Gewichte, also die variable zufällig. So gesehen ist der Startpunkt der imaginären Kugel damit am Anfang eines jeden Netzwerkes zufällig. Es kommt vor, dass sich die Kugel in der Nähe des Globalen Tiefpunktes befindet, es kommt aber oft genug vor, dass sie sich in einem Lokalen Minimum festsetzt. 
Das Größte Problem ist allerdings, ein Phänomen, welches Overshooting oder Überkorrektur genannt wird. Diese Überkorrektur entsteht, wenn die Korrektur zu Groß war, und über den Tiefpunkt hinaus geschossen wird.
![[Pasted image 20230915222826.png]]
[Quelle](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmatlabhelper.com%2Fwp-content%2Fuploads%2F2021%2F06%2FOvershooting-in-gradient-descent.jpg&f=1&nofb=1&ipt=21de0bd972bbc02b6d5213be0ce982643c92092381025c518ba20b15f3105910&ipo=images)

## LearnRate
[3]Um dem Problem mit dem Overshooting zu beheben, bedient man sich der sogenannten LearnRate. Dabei handelt es sich einfach um einen Faktor, mit welchem die Änderungsrate Multipliziert wird. Der Sinn dahinter ist es, die Schritt Größe anzupassen, das heißt wie Stark die Gewichte in eine Richtung angepasst werden. Wenn die Rate zu Groß ist, dann haben wir das Overshooting, bei dem sich das Netzwerk immer wieder über den Tiefpunkt hinausschießt. Wenn die rate zu niedrig ist kann es sein, dass das Netzwerk einfach zu langsam lernt. Es muss also eine Goldene Mitte gefunden werden. Normalerweise benutz man in einem Feed Forward Netzwerk eine Feste Konstante, die meist bei 0,2 oder 0,3 liegt. Im Späteren Verlauf dieser Arbeit werden dazu Tests durchgeführt, um eine Optimale LearnRate zu finden.
![[Pasted image 20230917181526.png]]
[Quelle](https://medium.com/diogo-menezes-borges/what-is-gradient-descent-235a6c8d26b0)

[1]Der Ansatz einer Learnrate wurde auch schon von Widrow und Hoff vorgeschlagen, ihr Ansatz galt den 2 Schichtigen Feed Forward Netzwerken. Die LernRate (λ) wird zunächst recht hoch angesetzt, im Bereich von 1 ≤ λ ≤ 10. Dadurch soll das Netzwerk in groben Schritten der Konvergenz näherkommen. Anschließend wird die LernRate schrittweise verringert, bis sie im Bereich von 0.01 ≤ λ ≤ 0.1 liegt. Dies ermöglicht eine genauere Annäherung an das Minimum.

## Kettenregel
Die Folgenden Kapitel zu den Ableitung, die für die Backpropagation (siehe spätere Kapitel) notwendig sind, sind Größtenteils unter zuhilfenahme von T. Rashids Buch "Neuronale Netze selbst programmieren"[2] und aus dem Video von Sebastian Lague namens "How to Create a Neural Network (and Train it to Identify Doodles)"[3] entstanden. 
Bisher haben wurde die Cost Funktion und die LearnRate behandelt. Die Cost Funltion sagt uns, wie falsch das Netzwerk liegt, und die LearnRate reguliert die Schritt Größe, die beidem Gradient Descent Verfahren in Richtung der Fehler Minima angepasst werden. Allerdings Fehlt uns noch das Wissen um die Richtung. 
Am leichtesten lässt sich dies in einer Zweidimensionalen Darstellung der Cost Funktion erklären.
![[Pasted image 20230917195146.png]]
[Quelle](https://vzahorui.net/optimization/gradient-descent/)

Auf der *Y* Achse ist der Fehler eingetragen, die *X* Achse Repräsentiert unsere Gewichte. Wenn das Gewicht bei 8 liegt, und der Fehler des Netzwerks damit vom Punkt *A* beschrieben wird. Das nächste Minimum liegt bei 6. Daher muss das Gewicht in dieser Richtung verschoben werden, also kleiner werden. 
Mathematisch kann das durch die Steigung im Punkt *A* beschrieben werden. 

Als ersten Ansatz (Sowohl mathematisch, als auch im Code) könnte man zwei Punkte nehmen, die sehr nah beieinander liegen, und damit die Steigung ausrechnen. Diese 2 Punkte sind zum einen das Aktuelle Gewicht, und zum anderen ein Punkt, der minimal von dem Aktuellen Gewicht abweicht, also leicht verschoben wird.
Diese kleine Änderung nennen wir Delta *w* (_Δw_) für weight, also das Gewicht das angepasst wird, wobei Delta eine sehr kleine Änderung an *w* repräsentiert.
Um die Steigung zu berechnen würde man also die Änderung des Errors _Δe_, welche durch die Änderung an dem Gewicht _Δw_ entsteht, durch eben dieses _Δw_  geteilt. 
![[Pasted image 20230918174436.png]]
Mithilfe dieser Formel könnte man die Steigung berechnen. Je nachdem, ob diese Steigung dann Positiv oder Negativ ist, lässt sich herleiten, in welcher Richtung der nächste Tiefpunkt liegt. Damit könnte bereits eine Learn Funktion erstellt werden, jedoch gibt es hier zwei Probleme.
Zum einen ist das Ergebnis bei dieser Herangehensweise bestenfalls eine Annäherung, und zweitens ist es recht aufwändig, so zu verfahren. Da für jede Anpassung an den Gewichten zwei Punkte berechnet werden müssen, muss die Querry also zwei mal angestoßen werden. besser wäre es, die Querry jedes mal nur einmal zu verwenden, dadurch würden wir die Arbeit, die verrichtet werden muss bereits an dieser Stelle halbieren. Und das ist auch möglich, indem wir Ableitungen bilden.

## Ableitung der Cost Funktion
Was genau ist eine Ableitung? Im Prinzip wird dabei der Gedanke verfolgt, was passiert, wenn die kleine Abweichung _Δw_ sich an 0 annähert. Natürlich kann _Δw_ auf den ersten blick nicht 0 sein, weil wir ansonsten durch 0 teilen würden. 
Aber verfolgen wir diesen Gedanken doch einmal an einem Beispiel:
Sei f(x) unsere Funktion:

![[Pasted image 20230919000705.png]]

Wir nennen die kleine Verschiebung von *x* jetzt *h*.
Dann gilt zumindest schon einmal:
![[Pasted image 20230919001324.png]]
Und gekürzt:
![[Pasted image 20230919001411.png]]

Dann wäre die Änderungsrate also:

![[Pasted image 20230919003718.png]]

![[Pasted image 20230919001751.png]]

Wenn wir uns dann die Mühe machen, f(x) einzusetzen, ergibt sich daraus:

![[Pasted image 20230919003350.png]]

Dann fangen wir einmal an zu kürzen:
![[Pasted image 20230919003026.png]]
![[Pasted image 20230919003135.png]]

![[Pasted image 20230919003214.png]]

![[Pasted image 20230919003238.png]]

Und jetzt zum Interessanten Teil. Da wir *h* nicht gleich 0 setzten können, können wir allerdings *h* gegen 0 laufen lassen, dann verwenden wir die Leibniz Notation. Das bedeutet, das wir anstatt  _Δw_ und  _Δh_ wobei _Δ_ eine sehr kleine Vergrößerung darstellt, jetzt _dw_ und _dh_ verwenden, wobei d für eine unendlich kleine Vergrößerung steht, eine sogenannte Infinitesimalzahl.
Das sieht dann ungefähr so aus:

![[LimH.png]]

![[Pasted image 20230919010023.png]]


## Crap den ich Später vielleicht verwenden kann
![[Pasted image 20230917181649.png]]
[Quelle](https://intlyouthscientists.org/2018/09/03/gradient-descent-a-simple-yet-effective-approach-to-artificial-intelligence/)

Die *Y* Koordinate gibt den Fehler des Netztes im Bezug auf zwei gewichte an. Die *Z* und *X* Achsen Repräsentieren die Gewichte. Diese Darstellung ist also ein verschwindend kleines Netzwerk, allerdings wird klar, dass eine Anpassung auf die *Z* und *X* Achse notwendig ist, um näher an ein Minimum zu gelangen.  


## Code


