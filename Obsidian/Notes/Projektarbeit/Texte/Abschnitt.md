## Kettenregel für 2 Schichten
Nun versuchen wir die Ableitung an dem Netzwerk. Zur Erinnerung, so sah unser kleines hypothetisches Netzwerk aus:

![[Pasted image 20230912184748.png]]

wir wollen uns erstmal nur auf den letzten Knoten mit seinem Input konzentrieren. Die frage ist, **wie verändert sich der Fehler des Netzwerks, wenn ich** **w<sub>2</sub>** **anpasse?**
Wir fangen am besten damit an, jede unabhängige Rechnung aufzuschreiben:
die erste Rechnung, in der w<sub>2</sub> vorkommt, ist die Multiplikation mit den Outputs aus der versteckten Schicht.
![[Pasted image 20230920001258.png]]
Diese wird an die Schwellwert Funktion *A* gegeben:
![[Pasted image 20230920001519.png]]
und danach wird das Ergebnis an die Cost Funktion gegeben:
![[Pasted image 20230920001633.png]]

Es ist ersichtlich, dass w<sub>2</sub> nicht direkt in der Cost Funktion vorkommt. Wie also ist es möglich, die Ableitung der Cost Funktion im Bezug auf die Gewichte zu bilden?
![[Pasted image 20230920001956.png]]

Hier kommt die Kettenregel ins Spiel. Es ist möglich, die Abhängigkeiten der Reihe nach aufzuschreiben, und miteinander zu Multiplizieren. 
Wir fangen mit Z<sub>2</sub> im Bezug auf w<sub>2</sub> an, multiplizieren dies mit a<sub>2</sub> im Bezug auf Z<sub>2</sub> und schließlich Multiplizieren wir c im Bezug auf a<sub>2</sub>.

![[Pasted image 20230920235108.png]]

Das dies durchaus möglich ist kann man daran erkenne, dass wenn man die einzelnen Komponenten der Brüche wegkürzt, tatsächlich 
![[Pasted image 20230920001956.png]]  
übrig bleibt.
Nun können wir die Einzelnen Komponenten unabhängig voneinander Ableiten, das heißt dass wir im Code eine Große Flexibilität erhalten haben.

## Kettenregel für 3 Schichten
Nun sehen wir uns mal an, was passiert, wenn man eine Schicht hinzufügt. Wie man die Ableitung bildet, um die Änderungsrate im Bezug auf w<sub>2</sub> zu berechnen haben wir im Letzten Kapitel gesehen. Wie würden wir also eine Ableitung bilden, welche uns die Änderungsrate im Bezug auf w<sub>1</sub> berechnet?
Wir suchen:

![[Pasted image 20230920181830.png]]

Dazu müssen wir zuerst alle Rechnungen im Gesamten Netzwerk angeben.

![[Pasted image 20230912184748.png]]

Wir Stellen die Einzelnen Rechnungen auf:
![[Pasted image 20230920180830.png]]   Input in das Netz mit dem zugeteilten Gewicht multipliziert
![[Pasted image 20230920180912.png]]  Schwellwert Funktion (Sigmoid)
![[Pasted image 20230920001258.png]]   Output der versteckten Schicht mit dem zugeteilten Gewicht  multipliziert
![[Pasted image 20230920001519.png]]   Letzte Schwellwert Funktion (Sigmoid)
![[Pasted image 20230920001633.png]]   Cost Funktion

Und nun zum Grundgedanken der Kettenregel zurück. Das Ziel ist es, die Änderungsrate der Cost Funktion (hier c) im Bezug auf Änderungen an den Gewichten der ersten versteckten Schicht zu ermitteln. Das heißt wie hängt *dc* von *dw* ab?

![[Pasted image 20230920181830.png]]

c ist abhängig von a<sub>2</sub>, welches wiederum abhängig ist von Z<sub>2</sub>, dieses von a<sub>2</sub>, dieses ist abhängig von a, dieses wieder von w<sub>1</sub>, also dem Ende unsere Untersuchung.

![[Pasted image 20230920212443.png]]

Es ist an dieser Stelle anzumerken, dass im Vergleich zur Cost in Abhängigkeit von w<sub>2</sub>, die Abhängigkeit von Z<sub>2</sub> nicht mehr w<sub>2</sub> ist, sondern a<sub>2</sub>. Das liegt daran, dass wir uns bereits um w<sub>2</sub> in der vorherigen Ableitung gekümmert haben.
Hier wird ersichtlich, dass sich durch das hinzunehmen einer weiteren Schicht 2 Terme zu der Ableitung hinzugefügt wurden. das sind die Terme ![[Pasted image 20230920213957.png]] in Abhängigkeit zu ![[Pasted image 20230920214053.png]], als auch ![[Pasted image 20230920214208.png]] in Abhängigkeit von ![[Pasted image 20230920214238.png]]. 
Und genau so wird das für jede weitere Schicht sein, denn Jede Schicht verarbeitet zwei Rechnungen, und zwar das Anwenden der Gewichte, und die Schwellwert Funktion.

## Backpropagation
Kommen wir nun zum Abschluss der Learn Methode. Alle bisherigen Erkenntnisse Gipfeln im Backpropagation Algorithmus. Wie der Name vermuten lässt, handelt es sich um einen Algorithmus, der unser Netzwerk zurückverfolgt, das heißt er fängt hinten an, und Arbeitet sich nach vorne vor. 
Wie wir im letzten Kapitel gesehen haben, ist die Ableitung der letzten Schicht im Bezug zu den letzten Gewichten im Netz die kleinste Formel. 

![[Pasted image 20230920235100.png]]

![[Pasted image 20230920212443.png]]

Die ersten beiden Terme sind zudem gleich, und der letzte Term ist ebenfalls nahezu gleich. Lediglich die zwei Terme dazwischen werden mit jeder Schicht hinzugefügt. Wir werden also versuchen einen Algorithmus zu schreiben, welcher zunächst einmal nur die ersten beiden Terme für die Output Schicht errechnet, diese dann zurückgibt, sodass sie an die vorherige Schicht gegeben werden können. Danach muss das Ergebnis, welches wir von hier an NodeValues nennen, mit dem letzten Term verrechnet werden. Auch der letzte Term muss von jeder Schicht mit den jeweils eigenen Werten gerechnet werden, daher ist das auch der letzte Schritt. 

Als erstes betrachten wir die Ableitung der Cost Funktion:
![[Pasted image 20230921000346.png]]
So ungefähr verlief die Rechnung:
Cost(Targets, Outputs) = (Targets - Outputs)² 

Es gibt eine verallgemeinerte Formel, mit der man recht schnell Simple Ableitungen bilden kann:

![[Pasted image 20230921004843.png]]

also wäre demnach die Ableitung der Cost Funktion:

![[Pasted image 20230921004959.png]]

Diese Formel kann man in der Layer Klasse umsetzten:

```Java
private double NodeCostDerivative(double activation, double expectedOutput) {
    return 2*(activation - expectedOutput);
}
```

Jetzt wollen wir uns die Ableitung der Sigmoid Funktion ansehen, also das Ergebnis von
![[Pasted image 20230921002631.png]]
die Ableitung so zu bilden, wie wir es zuvor gemacht haben, ist recht aufwendig, daher können wir uns einfach auf das Ergebnis anderer Mathematiker verlassen.
Die Sigmoid Funktion:
![[Pasted image 20230918125041.png]]
Und ihre Ableitung:
![[Pasted image 20230921001429.png]]

Daraus lässt sich eine Einfach Methode bauen, die wir dann aufrufen können. Wir fügen die Methode ActivationDerivative(double weightedInput) in unserer Sigmoid Klasse hinzu.

```Java
class Sigmoid extends Activation{
    //Die Sigmoid Funktion
    public double ActivationFunction(double weightedInput) {
        return 1.0 / (1 + Math.exp(-weightedInput));
    }
    //Die Ableitung der Sigmoid Funktion
    public double ActivationDerivative(double weightedInput) {
        double activation = ActivationFunction(weightedInput);
        return activation * (1.0 - activation);
    }
}
```

Die Sigmoid Funktion und ihre Ableitung benötigen die Gewichteten Inputs als Eingabe Parameter. Diese werden wären der Querry berechnet und werden zwischengespeichert. In der Methode CalculateOutputs in der Layer Klasse werden die Inputs zuerst mit den Gewichten Multipliziert, das Ergebnis wird zwischengespeichert, und anschließend wird die Schwellwert Funktion verwendet. 