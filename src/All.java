
public class All {

    public static void main(String[] args) {
        int WrtProSeite = 4754/22;

        String filePath = "Obsidian/Notes/Projektarbeit/Texte/Mathematische Grundlagen.md";
        double AktWrtCntMathematischeGrundlagen = WordCounter.countWords(filePath)-59;
        filePath = "Obsidian/Notes/Projektarbeit/Texte/Einleitung.md";
        double AktWrtCntEinleitung = WordCounter.countWords(filePath)-12;

        double WrtCntGestern = AktWrtCntEinleitung + 2856 +274+478+328+1014+1047.0+1337.0+ 193.0;
        double WrtCntHeute = AktWrtCntMathematischeGrundlagen-WrtCntGestern + AktWrtCntEinleitung;
        double[] WrtCntArray = {AktWrtCntMathematischeGrundlagen, AktWrtCntEinleitung};
        double WrtCnt = 0;
        for(Double d : WrtCntArray){WrtCnt+=d;}
        double Seiten = WrtCnt / WrtProSeite;
        

        System.out.println("Worte Pro Seite: "+WrtProSeite+"\nWorte Insgesammt übrig: "+(int)(WrtProSeite*50-WrtCnt)+" / "+WrtProSeite*50);
        System.out.println("So viel hast du heute geschrieben: "+WrtCntHeute);
        System.out.println("So weit bist du mit der Projektarbeit: "+roundToDecimalPlaces(((WrtCnt)/(50*WrtProSeite))*100, 1)+"%");
        System.out.println("Du hast So viele Seiten geschafft: " +roundToDecimalPlaces(Seiten,1)+"/"+50);
    }

    public static double roundToDecimalPlaces(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }

    public static double math2(double WorteBisher, Double Datum, double WrtProSeite){
        return (50*WrtProSeite-WorteBisher)-(22-Datum)*3.5*WrtProSeite;
    }

    public static double Mathe(double Tag, double Seiten){
        return (50.0 - Seiten) / (22.0 - Tag);
    }  
}

class SchonVorhndenException extends RuntimeException {
    public SchonVorhndenException(String message) {
        super(message);
    }
}

/*
(5|20)(7|50)
(2|30) 



\frac{ dc }{ dw_{ 2 } }=
\frac{ dc }{ da_{ 2 } }*
\frac{ da_{ 2 } }{ dZ_{ 2 } }*
\frac{ dZ_{ 2 } }{ dw_{ 2 } }






 */