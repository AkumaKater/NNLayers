import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WordCounter {
    public static void main(String[] args) {
        String filePath = "Obsidian/Notes/Projektarbeit/Texte/Mathematische Grundlagen.md";
        int wordCount = countWords(filePath);
        System.out.println("Anzahl der Worte in der Datei: " + wordCount);
    }

    public static int countWords(String filePath) {
        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Worte aufteilen (Whitespace als Trennzeichen)
                wordCount += words.length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordCount;
    }

    public static void writeNumberToFile(String fileName, double cnt) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(currentDate + " - " + cnt);
            writer.newLine(); // Neue Zeile einfügen
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void writeNumberToFile2(String fileName, double cnt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String lineToWrite = currentDate + " - " + cnt;
            writer.write(lineToWrite);
            writer.newLine();

            // Alle vorhandenen Zeilen in eine temporäre Datei kopieren
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String tempFileName = "temp_" + fileName;
                try (BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFileName))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        tempWriter.write(line);
                        tempWriter.newLine();
                    }
                }
                // Die temporäre Datei in die ursprüngliche Datei umbenennen
                File tempFile = new File(tempFileName);
                File originalFile = new File(fileName);
                tempFile.renameTo(originalFile);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
