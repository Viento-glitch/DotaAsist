package Main;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Timer {
    public static void main(String[] args) {
        generateRoshTiming("1500");
    }

    public static void generateRoshTiming(String startNumber) {
        String seconds = makeSeconds(startNumber);
        int minutes = makeMinutes(startNumber);
        String result = makeResult(minutes, seconds);
//        System.out.println(result);
                copy(result);
    }

    private static void copy(String copiedString) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(copiedString);
        clipboard.setContents(stringSelection, null);
    }

    private static String makeResult(int minutes, String seconds) {
        int aeg = sum(minutes, 5);
        int minRosh = sum(minutes, 8);
        int maxRosh = sum(minutes, 11);
        String textedTime = toText(aeg, seconds) + " " + toText(minRosh, seconds) + " " + toText(maxRosh, seconds);
        return textedTime;
    }

    private static String toText(int minutes, String seconds) {
        return String.valueOf(minutes) + seconds;
    }


    private static int sum(int minutes, int i) {
        return minutes + i;
    }

    private static int makeMinutes(String startNumber) {
        int minutes = Integer.parseInt(String.valueOf(startNumber.charAt(startNumber.length() - 3)));
        minutes += (Integer.parseInt(String.valueOf(startNumber.charAt(startNumber.length() - 4))) * 10);
        return minutes;
    }

    private static String makeSeconds(String startNumber) {
        char sec = startNumber.charAt(startNumber.length() - 2);
        char sec2 =startNumber.charAt(startNumber.length()-1);
        String result = String.valueOf(sec);
        result += String.valueOf(sec2);

//        System.out.println("результат секунд = " + result);
        return result;
    }
}
