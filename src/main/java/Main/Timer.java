package Main;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Random;

public class Timer {
    public static void main(String[] args) {
//        generateRoshTiming("1543");

    }

    public static void generateRoshTiming(String startNumber) {
        String seconds = makeSeconds(startNumber);
        int minutes = makeMinutes(startNumber);
        String result = makeResult(minutes, seconds, startNumber);
//        System.out.println(result);
        copy(result);
    }

    private static void copy(String copiedString) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(copiedString);
        clipboard.setContents(stringSelection, null);
    }

    private static String selectAegis() {
        ArrayList<String> aegis = new ArrayList<String>();
        aegis.add(":aegis_2017:");
        aegis.add(":aegis_2018:");
        aegis.add(":aegis_2019:");

        Random rand = new Random();
        int c = rand.nextInt(aegis.size());
//        System.out.println(c);


        String selected = aegis.get(c);
        ;

        return selected;
    }

    private static String makeResult(int minutes, String seconds, String startNumber) {
        int aeg = minutes + 5;
        int minRosh = minutes + 8;
        int maxRosh = minutes + 11;
        String textedTime = "" + startNumber + " :ti10_compendium:" + toText(aeg, seconds) + "  (R)" + toText(minRosh, seconds) + "-" + toText(maxRosh, seconds);
//        String textedTime = "" + startNumber + " (A)" + toText(aeg, seconds) + "  (R)" + toText(minRosh, seconds) + "-" + toText(maxRosh, seconds);


//     ! Рома передаст смайлики, заменить на эту строку
//        String textedTime = ":grave:" + startNumber + selectAegis() + toText(aeg, seconds) + ":bts_rosh:" + toText(minRosh, seconds) + "-" + toText(maxRosh, seconds);
        return textedTime;
    }


    private static String toText(int minutes, String seconds) {
        return String.valueOf(minutes) + ":" + seconds;
    }


    private static int makeMinutes(String startNumber) {
        String mins = "";
        for (int i = 0; i < startNumber.length() - 2; i++) {
            mins += startNumber.charAt(i);
        }
        int minutes = Integer.parseInt(mins);
        return minutes;
    }

    private static String makeSeconds(String startNumber) {
        char sec = startNumber.charAt(startNumber.length() - 2);
        char sec2 = startNumber.charAt(startNumber.length() - 1);
        String result = String.valueOf(sec);
        result += String.valueOf(sec2);

//        System.out.println("результат секунд = " + result);
        return result;
    }
}
