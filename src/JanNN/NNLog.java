package JanNN;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NNLog {

    static NNLog logger;
    String message = "|";
    File file = new File("/home/kater/Dokumente/Obsidian/Notes/Projektarbeit/Performance Notes.md");
    File sideFile = new File("/home/kater/Dokumente/Obsidian/Notes/Projektarbeit/SideNote.md");

    public static NNLog getLogger() {
        if(logger == null){
            logger = new NNLog();
        }
        return logger;
    }

    public void log(String message){
        try {
            FileWriter writer = new FileWriter(file, true); // Öffne im Append-Modus
            writer.write("\n"+message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }
    public void add(String message){
        this.message += message+"|";
    }
    public void add(int message){
        this.message += message+"|";
    }
    public void add(double message){
        this.message += message+"|";
    }
    public void flush(){
        log(message);
        message = "|";
    }
    public void sideLog(String message){
        try {
            FileWriter writer = new FileWriter(sideFile, true); // Öffne im Append-Modus
            writer.write("\n"+message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Schreiben in die Datei.");
        }
    }
}
