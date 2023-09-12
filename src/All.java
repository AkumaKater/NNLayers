import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class All {

    public static void main(String[] args) {
        Random random = new Random();
        int bound = 60;
        //System.out.println((int) (random.nextDouble() * bound) + 1);

        double WrtCntGestern = 1927;
        double WrtCntHeute = 1927-WrtCntGestern;
        double Datum = 13;
        double[] WrtCntArray = {1927, 620};
        double WrtCnt = 0;
        for(Double d : WrtCntArray){WrtCnt+=d;}
        double Seiten = WrtCnt / 250;
        double SeitenMitHeute = Seiten + WrtCntHeute/250;
        double WrtProTag = roundToDecimalPlaces(Mathe(Datum, Seiten), 2);
        double WrtProTagRounded = roundToDecimalPlaces(WrtProTag*250.0, 1);


        System.out.println("Worte Pro tag zu schreiben: " +WrtProTagRounded);
        System.out.println("So viel hast du heute geschrieben: "+WrtCntHeute);
        System.out.println("So viel Prozent hast du schon: "+ roundToDecimalPlaces(WrtCntHeute/(WrtProTag*250)*100, 1) +"%");
        System.out.println("So viele Worte fehlen dir heute noch: "+ (WrtProTagRounded-WrtCntHeute));
        System.out.println("So weit bist du mit der Projektarbeit: "+roundToDecimalPlaces(((WrtCnt+WrtCntHeute)/(50*250))*100, 1)+"%");
        System.out.println("Du hast So viele Seiten geschafft: " +roundToDecimalPlaces(SeitenMitHeute,1)+"/"+50);
    }

    public static double roundToDecimalPlaces(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }

    public static double math2(double WorteBisher, Double Datum){
        return (50*250-WorteBisher)-(21-Datum)*3.5*250;
    }

    public static double Mathe(double Tag, double Seiten){
        return (50.0 - Seiten) / (21.0 - Tag);
    }

    /**
     * @return
     */
    public static void Sherlock() {
        String file = "/home/kater/Projekte/testlayer/NNLayers/src/Text.txt";
        String base = "";
        String gesammteLaufzeit = "0:00";
        try {
            List<String> lines = Files.readAllLines(Paths.get(file));
            for (String line : lines) {
                base += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> timeList = extractTimeList(base);
        for (String time : timeList) {
            gesammteLaufzeit = TimeAdd(gesammteLaufzeit, time);
        }

        System.out.println("Gesammmte laufzeit ist: " + gesammteLaufzeit);
    }

    public static List<String> extractTimeList(String input) {
        List<String> timeList = new ArrayList<>();
        String[] lines = input.split("\n");
        Pattern timePattern = Pattern.compile("(\\d+:\\d+)");
        Pattern timePattern2 = Pattern.compile("(\\d+:\\d+:\\d+)");

        for (String line : lines) {
            Matcher matcher = timePattern2.matcher(line);
            if (matcher.find()) {
                timeList.add(matcher.group());
            } else {
                matcher = timePattern.matcher(line);
                if (matcher.find()) {
                    timeList.add(matcher.group());
                }
            }
        }

        return timeList;
    }

    public static String TimeAdd(String time1, String time2) {

        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");

        if (parts1.length == 3) {
            parts1[0] = Integer.parseInt(parts1[0]) * 60 + Integer.parseInt(parts1[1]) + "";
            parts1[1] = parts1[2];
        }
        if (parts2.length == 3) {
            parts2[0] = Integer.parseInt(parts2[0]) * 60 + Integer.parseInt(parts2[1]) + "";
            parts2[1] = parts2[2];
        }

        int minutes1 = Integer.parseInt(parts1[0]);
        int seconds1 = Integer.parseInt(parts1[1]);

        int minutes2 = Integer.parseInt(parts2[0]);
        int seconds2 = Integer.parseInt(parts2[1]);

        int totalSeconds = (minutes1 + minutes2) * 60 + seconds1 + seconds2;

        int resultMinutes = totalSeconds / 60;
        int resultSeconds = totalSeconds % 60;

        String resultTime = "0:00";
        if (resultMinutes < 60) {
            resultTime = resultMinutes + ":" + String.format("%02d", resultSeconds);
        } else {
            int resultHours = resultMinutes / 60;
            resultMinutes = resultMinutes % 60;
            resultTime = resultHours + ":" + String.format("%02d", resultMinutes) + ":"
                    + String.format("%02d", resultSeconds);
        }
        return resultTime;
    }

    public static void Math() {
        String file = "src/Text.txt";//src\Text.txt
        double base = 0.0;
        File fileD = new File(file);
        System.out.println(fileD.getAbsolutePath());
        

        try {
            List<String> lines = Files.readAllLines(Paths.get(file));
            for (String line : lines) {
                if (!line.isEmpty())
                base += Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Zusammen:  " + base);
    }

}

class SchonVorhndenException extends RuntimeException {
    public SchonVorhndenException(String message) {
        super(message);
    }
}
