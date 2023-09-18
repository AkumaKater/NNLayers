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
Um dem Problem mit dem Overshooting zu beheben, bedient man sich der sogenannten LearnRate. Dabei handelt es sich einfach um einen Faktor, mit welchem die Änderungsrate Multipliziert wird. Normalerweise benutz man in einem Feed Forward Netzwerk eine Feste Konstante dazu, die meist bei 0,2 oder 0,3 liegt. [1]