Bei diesem Test wurden die Biases nicht mit Zufallszahlen vor initialisiiert.

| HLayersSizes |DataSize| Epochen | BatchSize | Learnrate | ACtrainingD | ACtestD |
|----------|----------|----------|----------|----------|----------|----------
| (300)  (100) |60000|1|50|0.25|42,51%|41,79%|
| (300)  (100) |60000|10|50|0.25|91,95%|92,18%|
| (300)  (100) |60000|100|50|0.25|93,68%|93,20%|

# Performance Notes Random false
Bei diesem Test wurden die Biases und die Gewichte nicht mit Zufallszahlen vor initialisiiert.

| HLayersSizes |DataSize| Epochen | BatchSize | Learnrate | ACtrainingD | ACtestD |
|----------|----------|----------|----------|----------|----------|----------
| (300)  (100) |60000|1|50|0.25|11,24%|11,35%|
| (300)  (100) |60000|10|50|0.25|20,99%|21,15%|
| (300)  (100) |60000|100|50|0.25|20,72%|20,98%|