|Befehl|Erklärung|
|---|---|
|`hamachi`|Gibt den aktuellen Status, die PID, die eigene Client ID, den Online Status, den Nickname und den verknüpften LogMeIn Account aus.|
|`hamachi set-nick NICKNAME`|Den eigenen Nickname ändern.|
|`hamachi login`  <br>`hamachi logon`|Am LogMeIn Hamachi Server anmelden und das Netzwerk starten.|
|`hamachi logoff`  <br>`hamachi logout`|Vom LogMeIn Hamachi Server abmelden und das Netzwerk abschalten.|
|`hamachi list`|Listet alle Netzwerke auf, in denen man ist. Außerdem werden ausführliche Infos zu anderen Nutzern angezeigt, die in diesen Netzwerken sind.|
|`hamachi peer CLIENT-ID`|Gibt Informationen zu dem Nutzer mit der CLIENT-ID aus.|
|`hamachi network NETZWERK-ID`|Gibt Informationen zum Netzwerk mit der NETZWERK-ID heraus.|
|`hamachi create NETZWERK-ID [PASSWORT]`|Erstellt ein neues Netzwerk mit einem Namen eigener Wahl (NETZWERK-ID) und optional geschützt durch ein PASSWORT.|
|`hamachi set-pass NETZWERK-ID PASSWORT`|Ändert das Passwort des Netzwerks NETZWERK-ID in Passwort (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi set-access NETZWERK-ID [lock\|unlock] [manual\|auto]`|Legt fest, ob Nutzer erst vom Besitzer des Netzwerkes freigeschaltet werden müssen oder ob sie automatisch beitreten können (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi delete NETZWERK-ID`|Löscht das Netzwerk NETZWERK-ID (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi evict NETZWERK-ID CLIENT-ID`|Wirft den Nutzer CLIENT-ID aus dem Netzwerk NETZWERK-ID (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi approve NETZWERK-ID CLIENT-ID`|Schaltet den Nutzer CLIENT-ID für das Netzwerk NETZWERK-ID frei (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi reject NETZWERK-ID CLIENT-ID`|Weist den Nutzer CLIENT-ID ab und lässt ihn nicht ins Netzwerk NETZWERK-ID (Befehl steht nur für den Besitzer des Netzwerks zur Verfügung).|
|`hamachi join NETZWERK-ID [PASSWORT]`  <br>`hamachi do-join NETZWERK-ID [PASSWORT]`|Beitritt zum Netzwerk NETZWERK-ID, wofür eventuell ein PASSWORT benötigt wird.|
|`hamachi leave NETZWERK-ID`|Verlassen des Netzwerks NETZWERK-ID.|
|`hamachi go-online NETZWERK-ID`|Meldet einen im Netzwerk NETZWERK-ID an.|
|`hamachi go-offline NETZWERK-ID`|Meldet einen im Netzwerk NETZWERK-ID ab. Man bleibt aber in anderen Netzwerken weiter angemeldet.|
|`hamachi attach LogMeIn-ACCOUNT`  <br>`hamachi attach-net LogMeIn-ACCOUNT`|Verbindet den Computer mit einem LogMeIn-ACCOUNT.|
|`hamachi cancel`|Löst die Verbindung zum angegebenen LogMeIn-ACCOUNT.|
|`hamachi set-ip-mode ipv4 \| ipv6 \| both`|Legt die Art der zu benutzenden IP-Version fest.|
|`hamachi check-update`|Überprüft, ob eine neue Version für LogMeIn Hamachi for Linux BETA verfügbar ist.|