# Minecraft CityBuild System

Ein vollständiges und professionelles Citybuild-Plugin für PaperMC Server.

## Features

### 🏗️ Grundstücksverwaltung
- Grundstücke erstellen und verwalten
- Grundstückverkauf und Kauf
- Grundstückstypen (Wohngebiet, Gewerbe, etc.)
- Grundstücksschutz durch Bauperrechte

### 🏙️ Zonen-System
- Verschiedene Zonentypen (RESIDENTIAL, COMMERCIAL, INDUSTRIAL, PARK, PUBLIC)
- Zonenentwicklung und Level-System
- Automatische Zonenevaluierung
- Steuerverwaltung pro Zone

### 💰 Wirtschaftssystem
- Spieler-Konten mit Startguthaben (10.000$)
- Transaktionsverlauf
- Einkommens- und Ausgabentracking
- Businessstufen-System

### 🛡️ Schutzmaßnahmen
- Grundstücksschutz vor Baumod-Zugriffen
- Besitzerverifikation
- Berechtigung zum Abbauen/Bauen

## Installation

1. **Voraussetzungen:**
   - PaperMC Server (1.20.4+)
   - Java 17+
   - Maven

2. **Build:**
   ```bash
   mvn clean package
   ```

3. **Installation:**
   - Die JAR-Datei in den `plugins` Ordner kopieren
   - Server neustarten

## Commands

### Grundstücke
```
/plot pos1                  - Erste Position setzen
/plot pos2                  - Zweite Position setzen
/plot create <type>         - Grundstück erstellen
/plot info                  - Info zum aktuellen Grundstück
/plot sell <price>          - Grundstück zum Verkauf anbieten
```

### Zonen
```
/zone pos1                  - Erste Zone-Position
/zone pos2                  - Zweite Zone-Position
/zone create <name> <type>  - Zone erstellen
/zone info                  - Zone-Informationen
/zone upgrade               - Zone upgraden
```

### Wirtschaft
```
/cityecon balance           - Kontostand anzeigen
/cityecon top               - Top 10 reichste Spieler
```

### Allgemein
```
/city help                  - Hilfe anzeigen
/city info                  - Plugin-Informationen
/city stats                 - Persönliche Statistiken
```

## Datenbankstruktur

Das Plugin verwendet SQLite mit folgenden Tabellen:
- `plots` - Grundstücke
- `zones` - Zonen
- `player_economy` - Spieler-Wirtschaft
- `transactions` - Transaktionsverlauf
- `buildings` - Gebäude (zukünftig)

## Zukünftige Features

- [ ] Gebäude-System mit verschiedenen Gebäudetypen
- [ ] Automatisches Steuersystem
- [ ] NPC-Shops und Trader
- [ ] Verkehrssystem (Straßen, Busse)
- [ ] Ressourcenproduktion in Zonen
- [ ] Web-Dashboard für Stadt-Statistiken
- [ ] Multiplayer-Grundstücke
- [ ] Versicherungs- und Finanzierungssystem
- [ ] Events und Festivals

## Lizenz

Das Plugin steht unter der MIT-Lizenz.

## Support

Bei Fragen oder Problemen bitte ein Issue auf GitHub erstellen.
