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

        System.out.println((int) (random.nextDouble() * bound) + 1);

        Sherlock();
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
            // TODO Auto-generated catch block
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

}

class SchonVorhndenException extends RuntimeException {
    public SchonVorhndenException(String message) {
        super(message);
    }
}
