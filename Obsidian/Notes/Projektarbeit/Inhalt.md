
## Inhalt

1. Einleitung
	- [[Motivation]]
	- [[Gehirn lol???  ]]  
2. [[Grundlagen]]
	1. [[Grundlagen#Wofür wird ein Netzwerk verwende?|Wofür wird ein Netzwerk verwende?]]
	2. [[Grundlagen#Abgrenzung/ welche Art von Netzwerk werde ich hier bauen|Abgrenzung/ welche Art von Netzwerk werde ich hier bauen]]
	3. Grundlegender AUfbau eines Netztes (Initialisieren/Querry/learn)
	4. Der MNIST Datensatz
3. Mathematische Grundlagen
	1. Grundlage für die Querry (input x weight + bias -> Activation FUnktion)
	2. Activation funktions
	3. Grundlagen für Backpropagation
		1. error funktion
		2. kettenregel
		3. Ableitung für eine schicht
		4. ableitung für 2 schichten
		5. ableitung generalisieren
4. Netzwerk Coden
	1. Initialisiereng
	2. Querry
	3. Backpropagation
		1. NodeValues
		2. OutputlayerNodeValues
		3. HiddenlayerBodevalues
		4. Bias anpassung
		5. BatchSizes Anpassung
	4. Visualisierung
		1. Anzeige von Bildern
		2. Erstellen von Tabellen um Netzwerke vergkeichen zu können
5. Netzwerk Testen
	1. Verschiedene Konfigurationen Testen
		1. Test für eine schicht
		2. Test für 2 Schichten
		3. BatchSize EInfluss testen
	2. Trainingsdaten Größe anpassen und einfluss Testen
		1. Overfitting produzieren
	3. Betrachtung von Fehlerhaften Predictions
		1. Ausgabe von ein Paar interessanten Bildern, die auch für Menschen schwer zu erkenen sind
	4. Potentiell noch andere Activation FUnktions ausprobieren
	5. Potentiell noch Momentum einprogrammieren
	6. Potentiell noch regularisierungsverfahren unetrsuchen/einfügen
6. Fazit..?
	1. Disskusion über mögliche Schritte, um das Netzwerk noch besser zu Trainieren
	2. EInführen von Noise in den Trainigsdatensatz
	3. Test auf einem anderen Datensatz/ Modebilder/ Doodles
	4. Ausblick auf verbesserungen eines Netzwerkes (Konvolutional/Language Model)
7. Literaturverzeichniss
8. Anhang
	1. [[Projektarbeit/Performance Notes|Performance Notes]]
	2. [[Visualization]]


## Inhalt Muster
1.  Einleitung
    
    -   Hintergrund und Motivation des Themas
    -   Zielsetzung der Projektarbeit
    -   Überblick über die Struktur der Arbeit
2.  Theoretische Grundlagen
    
    -   Einführung in Neuronale Netzwerke
    -   Aufbau und Funktionsweise eines Neurons
    -   Mathematische Grundlagen: Aktivierungsfunktionen, Gewichtungen, Bias
    -   Architekturen von Neuronalen Netzwerken (z.B. Feedforward, Convolutional, Recurrent)
3.  Trainingsverfahren und Optimierung
    
    -   Überblick über Trainingsdaten und -verfahren
    -   Backpropagation-Algorithmus
    -   Gradientenabstieg und Optimierungsmethoden
    -   Regularisierung und Vermeidung von Overfitting
4.  Anwendungsbeispiele
    
    -   Praxisorientierte Beispiele für den Einsatz von Neuronalen Netzwerken
    -   Bilderkennung und -klassifikation
    -   Sprachverarbeitung und Textanalyse
    -   Empfehlungssysteme
    -   Autonome Fahrzeuge
5.  Implementierung eines Neuronalen Netzwerks
    
    -   Auswahl einer Programmiersprache und Frameworks (z.B. Python, TensorFlow, PyTorch)
    -   Implementierung eines einfachen Neuronalen Netzwerks
    -   Datenvorbereitung und -aufbereitung
    -   Training und Evaluation des Netzwerks
6.  Evaluation und Ergebnisse
    
    -   Evaluation der implementierten Lösung
    -   Vergleich mit vorhandenen Ansätzen und Benchmarks
    -   Diskussion der Ergebnisse und Interpretation
7.  Fazit und Ausblick
    
    -   Zusammenfassung der wichtigsten Erkenntnisse
    -   Bewertung des Theorie-Praxis-Transfers
    -   Offene Fragen und zukünftige Entwicklungen
8.  Literaturverzeichnis
    
    -   Auflistung der verwendeten Quellen, Bücher, wissenschaftlichen Artikel
9.  Anhang
    
    -   Codebeispiele und -dokumentation
    -   Abbildungen und Diagramme
    -   Weitere ergänzende Informationen