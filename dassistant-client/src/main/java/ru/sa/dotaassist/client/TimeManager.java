package ru.sa.dotaassist.client;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Random;

public class TimeManager {
    public static String generateRoshanTiming(String startNumber, boolean isTest) {
        String seconds = makeSeconds(startNumber);
        int minutes = makeMinutes(startNumber);
        String result = makeResult(minutes, seconds);
        if (!isTest) copy(result);
        else {
            System.out.println(result);
        }
        return result;
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
        return getTextedTime(minutes, seconds, aeg, minRoshan, maxRoshan);
    }

    private static String getTextedTime(int minutes, String seconds, int aeg, int minRoshan, int maxRoshan) {

        //todo Я не знаю как обратится к классу настроек потому вынять из него значение use-smiles я не могу
        //todo реализуй данное действие в этом if

        if (true) {
            return ":grave:" + minutes + ":" + seconds +
                    selectAegis() + toText(aeg, seconds) +
                    ":bts_rosh:" + toText(minRoshan, seconds) + "-" + toText(maxRoshan, seconds) + " ";
        } else {
            return minutes + ":" + seconds +
                    " (A)" + toText(aeg, seconds) +
                    " (R)" + toText(minRoshan, seconds) + "-" + toText(maxRoshan, seconds);
        }
    }

    private static String toText(int minutes, String seconds) {
        return minutes + ":" + seconds;
    }

    public static int makeMinutes(String startNumber) {
        StringBuilder minutes = new StringBuilder();
        for (int i = 0; i < startNumber.length() - 2; i++) {
            minutes.append(startNumber.charAt(i));
        }
        int result = 0;
        try {
            result = Integer.parseInt(minutes.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static String makeSeconds(String startNumber) {
        char sec = '0';
        if (startNumber.length() > 1) sec = startNumber.charAt(startNumber.length() - 2);
        char sec2 = startNumber.charAt(startNumber.length() - 1);
        String result = String.valueOf(sec);
        result += String.valueOf(sec2);
        return result;
    }
}
