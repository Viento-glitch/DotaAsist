package ru.sa.dotaassist.client;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Random;

public class TimeManager {
    public static void generateRoshanTiming(String startNumber) {
        String seconds = makeSeconds(startNumber);
        int minutes = makeMinutes(startNumber);
        String result = makeResult(minutes, seconds);
        copy(result);
    }

    private static void copy(String copiedString) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(copiedString);
        clipboard.setContents(stringSelection, null);
    }

    private static String selectAegis() {
        ArrayList<String> aegis = new ArrayList<>();
        aegis.add(":aegis_2017:");
        aegis.add(":aegis_2018:");
        aegis.add(":aegis_2019:");

        Random random = new Random();
        int c = random.nextInt(aegis.size());
        return aegis.get(c);
    }

    private static String makeResult(int minutes, String seconds) {
        int aeg = minutes + 5;
        int minRoshan = minutes + 8;
        int maxRoshan = minutes + 11;
        return getTextedTime(minutes, seconds,  aeg, minRoshan, maxRoshan);
    }

    private static String getTextedTime(int minutes, String seconds,  int aeg, int minRoshan, int maxRoshan) {
        if (View.smilesBoolean) {
            return ":grave:" + minutes + ":" + seconds + selectAegis() + toText(aeg, seconds) + ":bts_rosh:" + toText(minRoshan, seconds) + "-" + toText(maxRoshan, seconds) + " ";
        } else {
            return "" + minutes + ":" + seconds + " (A)" + toText(aeg, seconds) + "  (R)" + toText(minRoshan, seconds) + "-" + toText(maxRoshan, seconds);
        }
    }

    private static String toText(int minutes, String seconds) {
        return minutes + ":" + seconds;
    }

    private static int makeMinutes(String startNumber) {
        StringBuilder minutes = new StringBuilder();
        for (int i = 0; i < startNumber.length() - 2; i++) {
            minutes.append(startNumber.charAt(i));
        }
        return Integer.parseInt(minutes.toString());
    }

    private static String makeSeconds(String startNumber) {
        char sec = startNumber.charAt(startNumber.length() - 2);
        char sec2 = startNumber.charAt(startNumber.length() - 1);
        String result = String.valueOf(sec);
        result += String.valueOf(sec2);

        return result;
    }
}
