## Stichpunkte für den Aufbau
- Zielsetzung
- Motivation
- Abgrenzung

## **Wofür wird ein Netzwerk verwende?**

Neuronale netzwerke werden vor allem für Klassifikationsverfahren verwendet. In der Praxis gibt es viele Anwendungsbereiche, in denen es Vorteilhaft ist, Große Mengen von Daten automatisch zu Klassifizieren. Einige Beispiele wären zB die Bild und schriffterkennung, die man dabei verwendet, Kennschilder von Autos Maschinel auszulesen. Solche Technologien werden immer häufiger auf parkplätzen und Autobahnen eingesetzt. Aber auch fast Jedes handy kann mittlerweile schrifft erkennen, die mit der Kammera aufgenommen wird. Auch in der Medizin, beim Auswerten von Röntgenbildern, in der Biologie, zum erkennen von Pflanzen auf Fotos und noch vielem mehr werden Neuronale Netzwerke eingesetzt.  
Es gibt kaum einen bereich, in dem sich nicht eine mögliche anwendung für Neuronale Netzwerke finden lässt. Ganz besonders aufwändige Netzwerke werden mittlerweile auch für konstruktive Aufgaben verwendet, wie zB Sprachmodelle wie ChatGPT und Bilder Generierende KIs wie Midjourney.

H<sub>Fuk of</sub>

$H_{Fuk of}$

Netzwerke werden bei Problemen eingesetzt, die Kompliziert genug sind, dass die Lösung nicht manuell (ToDo) programmiert werden kann, es sich allerdings um solche Datenmengen handelt, dass es sich nicht lohnt, Menschen mit dieser Aufgabe zu betrauen.  
Generell wird ein Datensatz mit Labeln versehen, und dieser wird dazu verwendet, das Netzwerk zu Trainieren. Die Daten werden von Menschen mit Labeln versehen.

## **Abgrenzung/ welche Art von Netzwerk wird hier gebaut?**

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

```Java
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

## Grundgedanke Querry

Die Querry Funktion ist dazu da, um ein Ergebnis vom Netzwerk zu erlangen. Im Prinzip wollen wir ein Array, in welchem für jede Antwortmöglichkeit die Wahrscheinlichkeit zu finden ist, die das Netzwerk der jeweiligen Antwort zumisst. 
Das heißt also, dass die Funktion erstmal im "NeuralNetwork" die Inputs braucht. Die Inputs müssen von Schicht zu Schicht gereicht werden, müssen in den einzelnen Knoten verarbeitet werden und dann als Output zurückgegeben werden. Die Outputs der ersten Schicht werden dann zu den Inputs für die nächste Schicht und immer so weiter bis die Output Schicht die ein Ergebnis liefert.

## Querry Code

```Java
// Abfragen in NeuralNetwork.java
    public double[] Querry(double[] inputs) {
        for (Layer layer : layers) {
            inputs = layer.CalculateOutputs(inputs);
        }
        return inputs;
    }
```

## Grundgedanke CalculateOutputs

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

## Berechnung Der Outputs

Sehen wir uns zunächst ein Einfaches Netzwerk mit einer versteckten Schicht an. Jede Schicht hat nur einen Knoten:
![[Pasted image 20230912184748.png]]
[Quelle](https://www.youtube.com/watch?v=hfMk-kjRv4c)

Der erste Knoten wird $a_0$ genannt, und entspricht schlichtweg dem Input. Dieser wird an die erste Schicht geleitet, an alle darin vorhandenen Knoten. Dort wird es erst gewichtet, das heißt mit dem Gewicht $w_1$ , welches der Kante zugewiesen ist multipliziert. Hier wird es jetzt Spannend. Nachdem alle Inputs in unseren Knoten miteinander verrechnet sind, muss das Ergebnis erst noch durch die Aktivierungsfunktion.

### Aktivierungsfunktion

Die am Häufigsten genutzte Aktivierungsfunktion ist die Sigmoid Funktion. Diese sieht so aus:
$$\sigma (x)=\frac{1}{(1+e^{-x})}$$
![[Pasted image 20230912192401.png]]
Jeder Wert, der hier hinein läuft, wird auf einen Wert zwischen 0 und 1 verkleinert. 
Um ein Starkes Signal an die Nächsten Schicht zu senden, muss die Summer aller eingegangenen und danach gewichteten Signale Groß genug sein, um nach der Sigmoid Funktion noch näher an der 1 zu sein als an der 0. 
Es gibt eine ganze Reihe dieser Funktionen, hier einige Beispiele mit Erklärung:

***Die Identität***
![[Pasted image 20230912194736.png]]
Hierbei ist ***f(x) = x***. Die Werte können dabei allerdings zu Groß werden, und daher benutzt man lieber die Sigmoid Funktion.

**Die Sprung Funktion**
![[Pasted image 20230912195705.png]]
Diese Funktion gibt bei $\text{\{x>=0\}}$ eine 1 aus, bei $\text{\{x<0\}}$ immer eine 0.
Dadurch können allerdings Sprunghafte Veränderungen im Netzwerk eintreten, die unvorteilhaft sind.

**ReLu**
![[Pasted image 20230912200758.png]]
Ähnlich wie **Die Identität**, allerdings verläuft sie bei $\text{\{x<0\}}$ bei 0.
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
Um uns Arbeit zu sparen, greifen wir einmal der Thematik vorraus. Für die Umsetzung des Learn Algorithmus brauchen wir die Inputs, die jede Schicht erhalten hat, die gewichteten Inputs auch und die Outputs, die schon durch die Aktivierungsfunktion gegangen sind. Daher müssen wir jetzt erstmal drei Arrays hinzufügen. Wir nennen sie "inputs", "weightedInputs" und "activations". Für unsere versteckte Schicht entsprechen diese Werte also $a_0$ für "inputs", und $a_1$ für die "activations"

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
Intuitiv ist leicht zu verstehen, dass es nötig ist, den eignen Fehler zu kennen, bevor man ihn verbessern kann. Das gilt auch für das Netzwerk. Wir müssen zunächst ausrechnen, wie falsch das Netzwerk war. Dazu nehmen wir jedes Ergebnis aus dem Output Array der Querry, ziehen sie von den erwarteten Werten ab, und summieren alle Werte zusammen. Damit sich die Fehler nicht gegenseitig aufheben, müssen sie alle Positiv sein. Ein einfacher Weg dies umzusetzen, ist es, jeden Fehler zum Quadrat zu nehmen. Dadurch werden die Fehler außerdem betont, was hilfreich sein kann. Im folgenden werden die erwarteten Werte "Targets" genannt, also die Ziel-Werte. Um diese zu erhalten, brauchen wir eigentlich nur ein Array, welches genau so groß ist, wie das Output Array, und in diesem Array setzten wir alle Werte auf 0, außer das Feld mit dem Index, welches dem Label des Bildes entspricht. Dieses Feld setzten wir auf 1. Hier ein ausschnitt aus der Klasse MnistMatrix, aus dem Paket MNISTReader, um die Targets zu berechnen.

```Java
public double[] getTargets(){
    double[] targets = new double[10];
    targets[label] = 1.0;
    return targets;
} 
```


Danach werden die Targets dazu verwendet, um den Fehler des Netzes, oder auch die Kosten des Netzes zu berechnen. 
- Die Outputs werden von den Targets abgezogen
- Die Fehler werden Quadriert
- Alle Ergebnisse dieser Rechnung werden aufaddiert
- Das Ergebnis wird zurück gegeben, und entspricht den Kosten des Netzes

```Java
//Den Fehler berechnen mit der Cost Funktion
double Cost(MnistMatrix dataPoint) {
    double[] QuerryOutputs = Querry(dataPoint.getInputs());
    double[] Targets = dataPoint.getTargets();
    double cost = 0;
    for(int i=0; i<Targets.length; i++) {
        double error = Targets[i]-QuerryOutputs[i];
        cost += error*error;
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

# Kettenregel

Die Folgenden Kapitel zu den Ableitung, die für die Backpropagation (siehe spätere Kapitel) notwendig sind, sind Größtenteils unter zuhilfenahme von T. Rashids Buch "Neuronale Netze selbst programmieren"[2] und aus dem Video von Sebastian Lague namens "How to Create a Neural Network (and Train it to Identify Doodles)"[3] entstanden. 
Bisher haben wurde die Cost Funktion und die LearnRate behandelt. Die Cost Funltion sagt uns, wie falsch das Netzwerk liegt, und die LearnRate reguliert die Schritt Größe, die beidem Gradient Descent Verfahren in Richtung der Fehler Minima angepasst werden. Allerdings Fehlt uns noch das Wissen um die Richtung. 
Am leichtesten lässt sich dies in einer Zweidimensionalen Darstellung der Cost Funktion erklären.
![[Pasted image 20230917195146.png]]
[Quelle](https://vzahorui.net/optimization/gradient-descent/)

Auf der $Y$ Achse ist der Fehler eingetragen, die $X$ Achse Repräsentiert unsere Gewichte. Wenn das Gewicht bei 8 liegt, und der Fehler des Netzwerks damit vom Punkt $A$ beschrieben wird. Das nächste Minimum liegt bei 6. Daher muss das Gewicht in dieser Richtung verschoben werden, also kleiner werden. 
Mathematisch kann das durch die Steigung im Punkt $A$ beschrieben werden. 

Als ersten Ansatz (Sowohl mathematisch, als auch im Code) könnte man zwei Punkte nehmen, die sehr nah beieinander liegen, und damit die Steigung ausrechnen. Diese 2 Punkte sind zum einen das Aktuelle Gewicht, und zum anderen ein Punkt, der minimal von dem Aktuellen Gewicht abweicht, also leicht verschoben wird.
Diese kleine Änderung nennen wir Delta $w$ ($\Delta w$) für weight, also das Gewicht das angepasst wird, wobei Delta eine sehr kleine Änderung an $w$ repräsentiert.
Um die Steigung zu berechnen würde man also die Änderung des Errors $\Delta e$, welche durch die Änderung an dem Gewicht $\Delta w$ entsteht, durch eben dieses $\Delta w$ teilen. $$Änderungsrate = \frac{\Delta e}{\Delta w}$$
Mithilfe dieser Formel könnte man die Steigung annähernd berechnen. Je nachdem, ob diese Steigung dann Positiv oder Negativ ist, lässt sich herleiten, in welcher Richtung der nächste Tiefpunkt liegt. Damit könnte bereits eine Learn Funktion erstellt werden, jedoch gibt es hier zwei Probleme.
Zum einen ist das Ergebnis bei dieser Herangehensweise bestenfalls eine Annäherung, und zweitens ist es recht aufwändig, so zu verfahren. Da für jede Anpassung an den Gewichten zwei Punkte berechnet werden müssen, muss die Querry also zwei mal angestoßen werden. besser wäre es, die Querry jedes mal nur einmal zu verwenden, dadurch würden wir die Arbeit, die verrichtet werden muss bereits an dieser Stelle halbieren. Und das ist auch möglich, indem wir Ableitungen bilden.

## Ableitung einer Beispiel Cost Funktion

Was genau ist eine Ableitung? Im Prinzip wird dabei der Gedanke verfolgt, was passiert, wenn die kleine Abweichung $\Delta w$sich an 0 annähert. Natürlich kann $\Delta w$auf den ersten Blick nicht 0 sein, weil wir ansonsten durch 0 teilen würden. 
Aber verfolgen wir diesen Gedanken doch einmal an einem Beispiel:
Sei $f(w)$ unsere Funktion: $$f(w)=w^2+3$$
Wir nennen die kleine Verschiebung von $w$ jetzt $h$.
Dann gilt zumindest schon einmal: $$\Delta w=(w+h)-w$$Und gekürzt:$$\Delta w=h$$
Dann wäre die Änderungsrate also:$$Änderungsrate = \frac{\Delta e}{\Delta w}$$ $$\frac{\Delta e}{\Delta w}=\frac{f(w+h)-f(w)}{h}$$
Wenn wir uns dann die Mühe machen, $f(w)$ aus zuschreiben, ergibt sich daraus:
$$\frac{\Delta e}{\Delta w}=\frac{(w+h)^2+3-(w^2+3)}{h}$$
Dann fangen wir an klammern auszurechnen:
$$\frac{\Delta e}{\Delta w}=\frac{w^2+w*h+w*h+h^2+3-w^2-3}{h}$$
$$\frac{\Delta e}{\Delta w}=\frac{w^2+w*h+w*h+h^2-w^2}{h}$$
$$\frac{\Delta e}{\Delta w}=\frac{w*h+w*h+h^2}{h}$$
$h$ kürzen:
$$\frac{\Delta e}{\Delta w}=w+w+h$$
$$\frac{\Delta e}{\Delta w}=2w+h$$

Und jetzt zum Interessanten Teil. Da wir $h$ nicht gleich 0 setzten können, können wir allerdings $h$ gegen 0 laufen lassen, dann verwenden wir die Leibniz Notation. Das bedeutet, das wir anstatt  $\Delta w$ und  $\Delta e$ wobei $\Delta$ eine sehr kleine Vergrößerung darstellt, jetzt $dw$ und $de$ verwenden, wobei $d$ für eine unendlich kleine Vergrößerung steht, eine sogenannte Infinitesimalzahl.
Das sieht dann ungefähr so aus:
$$\frac{de}{dw}=\lim_{h\to 0} 2w +h$$
$$\frac{de}{dw}=2w$$
Diese Formel nennt man eine Ableitung, und sie gibt die Steigung des Ursprünglichen Graphen in jedem gegebenen Punkt im Bezug auf $w$ an. Mit anderen Worten, Wenn unsere Fehlerfunktion wie in diesem Beispiel $f(w)$ ist, dann beschreibt $2w$ in jedem gegebenen Punkt den man für $w$ einsetzt die Steigung, und somit auch die Richtung, in welcher ein Tiefpunkt zu finden ist. Genau wie bei dem Beispiel mit der Kugel, würde die Kugel die Steigung herab rollen.

### Kettenregel für 2 Schichten

Nun versuchen wir die Ableitung an dem Netzwerk. Zur Erinnerung, so sah unser kleines hypothetisches Netzwerk aus:

![[Pasted image 20230912184748.png]]

wir wollen uns erstmal nur auf den letzten Knoten mit seinem Input konzentrieren. Die frage ist, **wie verändert sich der Fehler des Netzwerks, wenn ich $w_2$ anpasse?**
Wir fangen am besten damit an, jede unabhängige Rechnung aufzuschreiben:
die erste Rechnung, in der $w_2$ vorkommt, ist die Multiplikation mit den Outputs aus der versteckten Schicht.
$$Z_2 = a_1*w_2$$
Diese wird an die Schwellwert Funktion $A$ gegeben:
$$a_2 = A(Z_2)$$
und danach wird das Ergebnis an die Cost Funktion gegeben:
$$c=C(a_2)$$
Es ist ersichtlich, dass $w_2$ nicht direkt in der Cost Funktion vorkommt. Wie also ist es möglich, die Ableitung der Cost Funktion im Bezug auf die Gewichte zu bilden?
$$\frac{dc}{dw_2}$$
Hier kommt die Kettenregel ins Spiel. Es ist möglich, die Abhängigkeiten der Reihe nach aufzuschreiben, und miteinander zu Multiplizieren. 
Wir fangen mit $Z_2$ im Bezug auf $w_2$ an, multiplizieren dies mit $a_2$ im Bezug auf $Z_2$ und schließlich Multiplizieren wir $c$ im Bezug auf $a_2$.
$$\frac{ dc }{ dw_{ 2 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ dw_{ 2 } }$$
Das dies durchaus möglich ist kann man daran erkenne, dass wenn man die einzelnen Komponenten der Brüche wegkürzt, tatsächlich $$\frac{ dc }{ dw_{ 2 } }$$  übrig bleibt.
Nun können wir die Einzelnen Komponenten unabhängig voneinander Ableiten, das heißt dass wir im Code eine Große Flexibilität erhalten haben.

### Kettenregel für 3 Schichten

Nun sehen wir uns mal an, was passiert, wenn man eine Schicht hinzufügt. Wie man die Ableitung bildet, um die Änderungsrate im Bezug auf $w_2$ zu berechnen haben wir im Letzten Kapitel gesehen. Wie würden wir also eine Ableitung bilden, welche uns die Änderungsrate im Bezug auf $w_1$ berechnet?
Wir suchen:$$\frac{ dc }{ dw_{ 1 } }$$
Dazu müssen wir zuerst alle Rechnungen im Gesamten Netzwerk angeben.

![[Pasted image 20230912184748.png]]

Wir Stellen die Einzelnen Rechnungen auf:
$$Z_1 = a_0*w_1$$
Input in das Netz mit dem zugeteilten Gewicht multipliziert
$$a_1=A(Z_1)$$
Schwellwert Funktion (Sigmoid)
$$Z_2=a_1*w_2$$
Output der versteckten Schicht mit dem zugeteilten Gewicht  multipliziert
$$a_2=A(Z_2)$$
Letzte Schwellwert Funktion (Sigmoid)
$$c=C(a_2)$$
Cost Funktion

Und nun zum Grundgedanken der Kettenregel zurück. Das Ziel ist es, die Änderungsrate der Cost Funktion (hier $c$) im Bezug auf Änderungen an den Gewichten der ersten versteckten Schicht zu ermitteln. Das heißt wie hängt $dc$ von $dw$ ab?
$$\frac{dc}{dw_1}$$
$c$ ist abhängig von $a_2$, welches wiederum abhängig ist von $Z_2$, dieses von $a_2$, dieses ist abhängig von $a$, dieses wieder von $w_1$, also dem Ende unsere Untersuchung.
$$\frac{ dc }{ dw_{ 1 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ da_{ 1 } }*
\frac{ da_{ 1 } }{ dZ_{ 1 } }*
\frac{ dZ_{ 1 } }{ dw_{ 1 } }$$
Es ist an dieser Stelle anzumerken, dass im Vergleich zur Cost in Abhängigkeit von $w_2$, die Abhängigkeit von $Z_2$ nicht mehr $w_2$ ist, sondern $a_2$. Das liegt daran, dass wir uns bereits um $w_2$ in der vorherigen Ableitung gekümmert haben.
Hier wird ersichtlich, dass sich durch das hinzunehmen einer weiteren Schicht 2 Terme zu der Ableitung hinzugefügt wurden. das sind die Terme $Z_2$ in Abhängigkeit zu $a_1$, als auch $a_1$ in Abhängigkeit von $Z_1$. 
Und genau so wird das für jede weitere Schicht sein, denn Jede Schicht verarbeitet zwei Rechnungen, und zwar das Anwenden der Gewichte, und die Schwellwert Funktion.

## Backpropagation

Kommen wir nun zum Abschluss der Learn Methode. Alle bisherigen Erkenntnisse Gipfeln im Backpropagation Algorithmus. Wie der Name vermuten lässt, handelt es sich um einen Algorithmus, der unser Netzwerk zurückverfolgt, das heißt er fängt hinten an, und Arbeitet sich nach vorne vor. 
Wie wir im letzten Kapitel gesehen haben, ist die Ableitung der letzten Schicht im Bezug zu den letzten Gewichten im Netz die kleinste Formel. 
$$\frac{ dc }{ dw_{ 2 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ dw_{ 2 } }$$
$$\frac{ dc }{ dw_{ 1 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ da_{ 1 } }*
\frac{ da_{ 1 } }{ dZ_{ 1 } }*
\frac{ dZ_{ 1 } }{ dw_{ 1 } }$$
Die ersten beiden Terme sind zudem gleich, und der letzte Term ist ebenfalls nahezu gleich. Lediglich die zwei Terme dazwischen werden mit jeder Schicht hinzugefügt. Wir werden also versuchen einen Algorithmus zu schreiben, welcher zunächst einmal nur die ersten beiden Terme für die Output Schicht errechnet, diese dann zurückgibt, sodass sie an die vorherige Schicht gegeben werden können. Danach muss das Ergebnis, welches wir von hier an NodeValues nennen, mit dem letzten Term verrechnet werden. Auch der letzte Term muss von jeder Schicht mit den jeweils eigenen Werten gerechnet werden, daher ist das auch der letzte Schritt. 

Als erstes betrachten wir die Ableitung der Cost Funktion:
$$\frac{ dc }{ da_{ 2 } }$$
So ungefähr verlief die Rechnung:
$Cost(Targets, Outputs) = (Targets - Outputs)^2$

Es gibt eine verallgemeinerte Formel, mit der man recht schnell Simple Ableitungen bilden kann:
$$\frac{dy}{dx}=nax^{n-1}$$
also wäre demnach die Ableitung der Cost Funktion:
$$\frac{dc}{da_2}=2(Targets-Outputs)$$
Diese Formel kann man in der Layer Klasse umsetzten:

```Java
private double CostAbleitung(double activation, double expectedOutput) {
    return 2*(activation - expectedOutput);
}
```

Jetzt wollen wir uns die Ableitung der Sigmoid Funktion ansehen, also das Ergebnis von
$$\frac{da_2}{dZ_2}$$
die Ableitung so zu bilden, wie wir es zuvor gemacht haben, ist recht aufwendig, daher können wir uns einfach auf das Ergebnis anderer Mathematiker verlassen.
Die Sigmoid Funktion`[2, 4]`:
$$\sigma (x)=\frac{1}{(1+e^{-x})}$$
Und ihre Ableitung:
$$\sigma '(x)=\sigma (x)(1-\sigma(x))$$
Daraus lässt sich eine Einfach Methode bauen, die wir dann aufrufen können. Wir fügen die Methode ActivationDerivative(double weightedInput) in unserer Sigmoid Klasse hinzu.

```Java
class Sigmoid extends Activation{
    //Die Sigmoid Funktion
    public double ActivationFunction(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
    //Die Ableitung der Sigmoid Funktion
    public double ActivationAbleitung(double weightedInput) {
        double activation = ActivationFunction(weightedInput);
        return activation * (1.0 - activation);
    }
}
```

Die Sigmoid Funktion und ihre Ableitung benötigen die Gewichteten Inputs als Eingabe Parameter. Diese werden wären der Querry berechnet und werden zwischengespeichert. In der Methode CalculateOutputs in der Layer Klasse werden die Inputs zuerst mit den Gewichten Multipliziert, das Ergebnis wird zwischengespeichert, und anschließend wird die Schwellwert Funktion verwendet. 

Der letzte Term ist die der Input im Bezug zu den Gewichten.
Dabei gilt 
$$Z_2 = a_1*w_2$$
Also wird die Ableitung demnach so gebildet:
$$\frac{\Delta Z_2}{\Delta w_2}=\frac{a_1*(w_2+h)-a_1*w_2}{h}$$
$$\frac{\Delta Z_2}{\Delta w_2}=\frac{a_1*w_2+a_1*h-a_1*w_2}{h}$$
$$\frac{\Delta Z_2}{\Delta w_2}=\frac{a_1*h}{h}$$
$$\frac{\Delta Z_2}{\Delta w_2}=a_1$$
Der Limes ist hier nicht einmal mehr nötig. Um es einmal in Worte zu fassen, bedeutet diese Ableitung einfach nur, dass die Änderungsrate im Bezug auf $w_2$ vollständig von den Inputs $a_1$ abhängig ist. Also wenn $a_1$ = 0 ist, dann haben die Gewichte auch keinen Einfluss mehr. Wenn $a_1$ = 1 ist, dann ist der Einfluss von den Gewichten genau so groß, wie die Gewichte selbst sind. Wenn $a_1$ = 2 ist, dann haben die Gewichte einen Doppelten Einfluss und so weiter. 

Wir suchen nach dieser Ableitung:
$$\frac{ dc }{ dw_{ 2 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ dw_{ 2 } }$$
und haben jetzt alle Terme zusammen, die darin vorkommen. Der erste Term war 
$$\frac{dc}{da_2}=2(Targets-Outputs)$$
Welchen wir in der Methode CostAbleitung festgehalten haben.
Der Zweite Term war
$$\sigma '(x)=\sigma (x)(1-\sigma(x))$$
Diesen haben wir in der Sigmoid Activation Klasse bereits in der activationAbleitung Methode festgehalten.
Der Dritte Term war
$$\frac{\Delta Z_2}{\Delta w_2}=a_1$$
wobei $a_1$ den unbearbeiteten Inputs dieser Schicht entspricht, und in der Querry bereits aufgefangen wurden und im 2 Dimensionalen Array "Inputs" abgespeichert wurden.

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
$$\frac{\Delta Z_2}{\Delta w_2}=a_1$$
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
	            double derivativeCostWrtWeight = inputs[nodeIn] * nodeValues[nodeOut];
	            CostSteigungW[nodeIn][nodeOut] += derivativeCostWrtWeight;
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

Die LearnRate sollte mittlerweile bekannt sein. Dabei handelt es sich um eine Zahl, mit der die Anpassungsrate verrechnet wird, um ein Over Shooting zu vermeiden. Wenn das Netzwerk zu Große Schritte in Richtung eines Fehlerminimums geht, kann es dazu kommen, dass es über den Tiefpunkt hinaushüpft, im schlimmsten falle sogar komplett aus dem Tal des Tiefpunkts hinausspringt. Bei einer zu geringen LearnRate kann es sein, dass das Netzwerk zu langsam lernt. In dem Fall müssen die Daten in mehreren Epochen Trainiert werden, was im schlimmsten Fall zu Overfitting führen kann. Jedoch kann es hilfreich sein, dass die Einzelnen Anpassungen keinen zu großen Einfluss haben. Ein gutes Netzwerk muss bei der Anpassung der Gewichte einen Kompromiss finden zwischen den 10 Zahlen. Wenn jedes mal immer nur ein Bild betrachtet wird, und die LearnRate zu Groß ist, neigt das Netzwerk schnell zu einzelnen Zahlen.

![[Pasted image 20230917181526.png]]
[Quelle](https://medium.com/diogo-menezes-borges/what-is-gradient-descent-235a6c8d26b0)


# Ergebnisse der Ersten Testreihen
Kommen wir nun zu den Testreihen. Sehen wir uns zunächst einmal an, wie verlässlich das Netzwerk Arbeitet. Dazu initialisieren wir das Netzwerk mit einer Reihe Test Einstellungen. Das Netzwerk soll eine versteckte Schicht haben, mit 100 Knoten. Wir Trainieren auf der vollen Größe des Datensatzes, das heißt 60.000 Bilder. Eine Epoche sollte für die ersten Testreihen ausreichend sein. Zur BatchSize kommen wir später, im Moment liegt sie bei eins. Das heißt, dass immer nur ein Bild betrachtet und gelernt wird. Die LearnRate stellen wir einfach mal auf 0.25 ein, diese Rate hat sich für einfache Feed Forward Netzwerke bewährt. Diese Einstellungen werden ausgegeben, und in den Letzten beiden Spalten werden jeweils die Genauigkeit des Netzwerkes auf den Trainings Daten und auf den Test Daten eingetragen. Um die Genauigkeit zu berechnen, wird einfach der Gesamte Datensatz einmal durch die Querry des Netzwerkes berechnet, und der Anteil an Korrekt Klassifizierten Bildern ist die Genauigkeit des Netzwerkes.
Hier sind die ersten 6 Durchläufe:

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
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>13,11%</td>
		<td>12,53%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>24,16%</td>
		<td>24,16%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>18,74%</td>
		<td>18,41%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>25,66%</td>
		<td>25,80%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>24,06%</td>
		<td>24,69%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.25</td>
		<td>13,70%</td>
		<td>13,27%</td>
	</tr>
</table>

Zunächst fällt auf, dass die Ergebnisse stark schwanken. Das lässt darauf schließen, dass die LearnRate zu hoch eingestellt ist. Die Nächste Testreihe sollte mit verschiedenen Raten gemacht werden.

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
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.01</td>
		<td>74,44%</td>
		<td>74,44%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.015</td>
		<td>66,98%</td>
		<td>67,23%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.02</td>
		<td>66,03%</td>
		<td>65,82%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.025</td>
		<td>59,86%</td>
		<td>60,67%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.03</td>
		<td>51,43%</td>
		<td>51,62%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.035</td>
		<td>56,43%</td>
		<td>56,73%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.04</td>
		<td>51,18%</td>
		<td>50,81%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.045</td>
		<td>53,51%</td>
		<td>53,72%</td>
	</tr>
	<tr>
		<td>[780, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.05</td>
		<td>47,86%</td>
		<td>47,80%</td>
	</tr>
	</table>

Wie unschwer zu erkennen ist, nimmt die Genauigkeit der Netzwerke ab, je höher die LearnRate ist. Wahrscheinlich muss die LearnRate weiter verringert werden.

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
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.009</td>
			<td>77,33%</td>
			<td>77,70%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.008</td>
			<td>73,47%</td>
			<td>74,08%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.007</td>
			<td>74,50%</td>
			<td>75,37%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.006</td>
			<td>77,84%</td>
			<td>78,80%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.005</td>
			<td>80,05%</td>
			<td>81,09%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.004</td>
			<td>78,94%</td>
			<td>79,47%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.003</td>
			<td>79,45%</td>
			<td>79,77%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.002</td>
			<td>79,27%</td>
			<td>79,61%</td>
		</tr>
		<tr>
			<td>[780, 100, 10]</td>
			<td>60000</td>
			<td>1</td>
			<td>1</td>
			<td>0.001</td>
			<td>74,58%</td>
			<td>75,27%</td>
		</tr>
</table>

Diese Ergebnisse sehen schon viel besser aus. Es ist ersichtlich, dass sich bei einer LearnRate von 0.004 und kleiner keine Steigerung der Genauigkeit mehr feststellen lässt. Daher versuchen wir im nächsten Schritt die Epochen zu erhöhen. Durch das wiederholte lernen des Datensatzes sollten bessere Ergebnisse erzielt werden. Die LearnRate setzten wir hierbei auf 0.003, da wir in der letzten Testreihe Feststellen konnten, dass sich der die Optimale LearnRate zwischen 0.006 und 0.002 befinden muss, entscheiden wir uns für 0.003. Bei mehr Epochen können wir ruhig eine kleinere LearnRate wählen, da das Netzwerk genug Zeit haben müsste, um ein Tiefpunkt zu finden.

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

Bei dem ersten Versuch wurde der Gesamte Datensatz 5 mal gelernt. Im Vergleich zu den Vorherigen Tests lässt sich bereits eine Steigerung der Genauigkeit um fast 8% erkennen. Bei noch mehr Epochen lässt sich weiterhin erkennen, dass sich die Ergebnisse verbessern, allerdings konvergieren die Ergebnisse langsam gegen 92%. Außerdem dauert es mittlerweile auch sehr lange, die vielen Epochen zu trainieren. 

Bisher haben wir alle Netzwerke mit einer Versteckten Schicht Trainiert, die genau 100 Knoten beherbergt. Die nächsten Versuchsreihen haben verschiedene Konfigurationen für die Versteckten Schichten. Wir sehen uns einige Variationen im Bezug auf Anzahl der Schichten sowie Anzahl der Knoten an, allesamt mit einer LearnRate von 0.003 und einer Epochen Zahl von 25, da wir mit dieser Konfiguration bereits einige Erfolge erzielt haben. 

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
		<td>[784, 100, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,12%</td>
		<td>90,80%</td>
	</tr>
	<tr>
		<td>[784, 100, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,36%</td>
		<td>91,20%</td>
	</tr>
	<tr>
		<td>[784, 100, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,85%</td>
		<td>91,81%</td>
	</tr>
	<tr>
		<td>[784, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,36%</td>
		<td>93,40%</td>
	</tr>
	<tr>
		<td>[784, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,08%</td>
		<td>92,93%</td>
	</tr>
	<tr>
		<td>[784, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,25%</td>
		<td>93,07%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,41%</td>
		<td>93,36%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,50%</td>
		<td>93,30%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,59%</td>
		<td>93,67%</td>
	</tr>
	<tr>
		<td>[784, 500, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>94,70%</td>
		<td>94,45%</td>
	</tr>
	<tr>
		<td>[784, 500, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>94,47%</td>
		<td>93,88%</td>
	</tr>
	<tr>
		<td>[784, 500, 300, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>94,78%</td>
		<td>94,50%</td>
	</tr>
</table>

Wir können anhand der Ergebnisse feststellen, dass mehr Schichten und mehr Knoten auch bessere Ergebnisse liefern. Wenn wir kleinere Schichten und weniger Knoten wählen, dann bekommen wir auch schwächere Ergebnisse.

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
		<td>[784, 10, 10, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>76,93%</td>
		<td>77,66%</td>
	</tr>
	<tr>
		<td>[784, 10, 10, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>75,46%</td>
		<td>75,74%</td>
	</tr>
	<tr>
		<td>[784, 10, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>81,43%</td>
		<td>81,95%</td>
	</tr>
	<tr>
		<td>[784, 10, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>80,76%</td>
		<td>81,64%</td>
	</tr>
	</table>

Allerdings sind die Unterschiede in diesem Beispiels nur sehr klein. Interessant zu sehen ist aber, dass weit weniger Epochen nötig sind, um ein Netzwerk zu Trainieren, welches über mehr Schichten und Knoten verfügt. Hier Trainieren wir mit 2 versteckten Schichten und jeweils 300 und 100 Knoten:

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
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>1</td>
		<td>1</td>
		<td>0.003</td>
		<td>83,78%</td>
		<td>84,79%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>5</td>
		<td>1</td>
		<td>0.003</td>
		<td>90,44%</td>
		<td>90,72%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>10</td>
		<td>1</td>
		<td>0.003</td>
		<td>91,78%</td>
		<td>92,01%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>15</td>
		<td>1</td>
		<td>0.003</td>
		<td>92,48%</td>
		<td>92,27%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>20</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,27%</td>
		<td>92,86%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>25</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,37%</td>
		<td>93,46%</td>
	</tr>
	<tr>
		<td>[784, 300, 100, 10]</td>
		<td>60000</td>
		<td>30</td>
		<td>1</td>
		<td>0.003</td>
		<td>93,99%</td>
		<td>93,88%</td>
	</tr>
</table>

Im Vergleich zu den vorherigen Testreihen, wird die Genauigkeit von über 90% bereits nach 5 Epochen anstatt den 20 Epochen erreicht, die nötig waren, um das kleinere Netzwerk mit einer Versteckten Schicht und 100 Knoten zu trainieren.

Um die Ergebnisse ein wenig besser Einordnen zu können, betrachten wir einmal, wie ein Nicht funktionierendes Netzwerk aussehen würde. Wir nehmen an, dass das Netzwerk jedes Bild als eine 0 Klassifiziert. Dann wäre das Netzwerk in 10% der Fällen immer noch richtig. Das heißt wenn es tatsächlich dazu kommt, dass das Ergebnis des Netzwerkes immer 0 ist, dann läge die Genauigkeit bei 10%, und nicht bei 0%, wie man fälschlicherweise annehmen könnte. In dem Fall, dass das Netzwerk tatsächlich eine geringere Genauigkeit als ungefähr 10% erzielt, dann muss davon ausgegangen werden, dass das Netzwerk Gezielt Fehler macht. Unter Umständen Maximiert das Netzwerk den Fehler dann. Dadurch würde auf jeden Fall die Vermutung nahe liegen, dass das Netzwerk bereits Korrekt Klassifizieren kann, allerdings gezielt das Falsche Ergebnis ausgibt. In den meisten Fällen würde ein nicht funktionierendes Netzwerk allerdings rund um die 10% Genauigkeit erreichen, das heißt zwischen 8% und 12%. Wenn das Netzwerk nicht verstanden hat, was es tun soll, und einfach nur wild rät, wird es ebenfalls ungefähr bei 10% Genauigkeit landen, mit leichten Abweichungen. Wenn wir es also mit einem Netzwerk zu tun haben, das 20% Genauigkeit erreicht, dann können wir davon ausgehen, dass es bereits ein gewisses Verständnis darüber erlangt hat, wie die Bilder zu klassifizieren sind. 

Wenden wir uns nun Möglichkeiten zu, das Netzwerk zu verbessern. Dazu sehen wir uns zuerst die Biases, und danach die Batches an.

# Biases
## Erklärung zum Nutzen

Die Erklärung und die Bilder in diesem Abschnitt basieren auf einem Thread aus stackoverflow[5], sowie eigenen Erfahrungen.

Biases geben uns die Möglichkeit, die Schwellwert Funktion zu verschieben. Kurzgefasst, das Netzwerk wird Felxibler. 
Um etwas genauer zu werden, sehen wir uns einmal an, wie die Graphen aussehen im Bezug auf die Inputs $x$, nachdem sie mit den Gewichten $w$ verrechnet werden. Für $w$ nehmen wir als Beispiels Werte 0.5, 1, und 3.

![[Pasted image 20231005144238.png]]

Alle Graphen verlaufen durch den 0 Punkt. Dies sorgt allerdings auch dafür, dass das Netzwerk recht unflexibel ist. Nehmen wir an, dass es zwei Klassen gibt, die das Netzwerk unterscheiden können soll, und Klasse 1 befindet sich in $x$ < 1 sowie $y$ < 1, dann wäre es schlicht unmöglich, mit einem dieser Graphen eine einfache Klassifizierung vorzunehmen. Wenn wir allerdings einen Bias $b$ addieren, dann lässt sich so ein Graph entlang der $y$ Achse verschieben.

![[Pasted image 20231005144628.png]]

Mit diesem Graphen wäre unser Beispiel dann gelöst, alle Elemente der Klasse 1 liegen unterhalb des Graphen, während alle Elemente der Klasse 2 größer als 1 in beiden Achsen sind, und damit über dem Graphen liegen.
Um die Berechnung im Neuron einmal darzustellen, hier eine Graphik:

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

# Batches
## Motivation
Bisher waren wir darauf angewiesen, dass wir sehr geringe LearnRates verwenden, um zu vermeiden, dass das Netzwerk zu stark in eine Richtung neigt, wenn es ein Bild gelernt hat. Wie bereits zuvor erwähnt, Sieht die Cost Funktion für jedes Lable anders aus, an sich sogar für jedes Bild anders. Das ziel des Gradient Descent Verfahrens ist es, einen guten Kompromiss zwischen den verschiedenen Tiefpunkten der Cost Funktionen der Bilder zu finden.
Bisher wurden die Gewichte nach jedem Bild, das gelernt wurden, angepasst, das heißt dass sich das Netzwerk in dem Moment jeweils mehr auf das Bild spezialisiert hat. Ein besserer Ansatz wäre es, mehrere Bilder nacheinander zu betrachten, und dann eine durchschnittliche Änderungsrate zu berechnen, die allen Bildern gleichermaßen gerecht wird. Hier kommen die Batches ins Spiel.
Anstatt nur ein Bild zu übergeben, legen wir eine Menge an Bildern Fest (BatchSize) und übergeben diese Menge. Die Änderungsraten werden aufaddiert, und erst zum Schluss wird eine Durchschnittliche Änderungsrate errechnet und danach erst mit den Gewichten und Biases verrechnet.
## Code für die Batches
Die Anpassung im Code ist dank unsere bisherigen Vorarbeit recht Simpel.
Wir müssen lediglich eine Schleife in der "Learn" Methode der NeuralNetwor Klasse einbauen, welche die Methode "UpdateGradient" für jedes Bild aufruft, und erst nach Abschluss der Schleife die Änderungsraten mit der Methode "ApplyAllGradients" an den Gewichten und Biases verrechnet. Dabei wird die LearnRate übergeben, welche jetzt, um tatsächlich den Durchschnitt zu bilden, durch die Menge der im Batch enthaltenen Bilder geteilt wird.

```java
// Training über Batch
public void learn(MnistMatrix[] batch) {
    for (MnistMatrix data : batch) {
        UpdateAllGradients(data);
    }
    // Ganz Wichtig! Der Durchschnitt wir errechnet durch
    // LearnRate / BatchSize
    ApplyAllGradients(this.learnRate / batch.length);
    ClearAllGradients();
}
```





## ToDo

- [ ] Flow chart
- [x] Tools zur Überwachung
- [x] Ergebnisse der ersten Testreihen
- [ ] wie funktionmiert es ohne batch sizes
- [ ] bioses ohne batch sizes >> kaum EInfluss?
- [ ] Vorteil von Batch sizes
- [ ] bioses in Verbindung mit Lernzyklen
- [ ] Testreihe zum austarieren: best of Einstellungen für ein gutes Netzwerk
- [ ] Fazit
- [ ] Einleitung
- [x] Anna lieb haben
- [ ] 

## Weitere ToDos

- [x] die Cost Funktion muss überarbeitet werden. Das Quadrieren des Fehlers ist notwendig
  - [x] Jeder Fehler der verrechnet wird, muss Positiv sein. Nur die Differenz zählt
- [x] Lenze Email schreiben
- [x] Prüfungssystem Anmelden
- [ ] Blocksatz
- [x] Donnerstag 10 Uhr





# Quellen
1. B. Lenze, Einführung in die Mathematik neuronaler Netze. Berlin: Logos Verlag; 2009.
2. T. Rashid, Neuronale Netze selbst programmieren - Ein verständlicher Einstieg mit Python. Paderborn: O’Reilly Verlag; 2017.
3. S. Lague, How to Create a Neural Network (and Train it to Identify Doodles). Hrsg. auf dem YouTube Kanal [Sebastian Lague](https://www.youtube.com/@SebastianLague). Ohne Jahr [zitiert am 16. September 2023]. Abrufbar unter: URL: https://www.youtube.com/watch?v=hfMk-kjRv4c.
4. URL: https://ichi.pro/de/ableitung-der-sigmoid-funktion-91708302791054
5. URL: https://stackoverflow.com/questions/2480650/what-is-the-role-of-the-bias-in-neural-networks