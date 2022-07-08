package ru.sa.dotaassist.client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Assistant implements NativeKeyListener {

    public static final String CTRL_KEY_TEXT = "Ctrl";
    public static final String A_KEY_TEXT = "A";
    public static final String C_KEY_TEXT = "C";

    boolean isCtrlPressed = false;
    boolean isAllSelected = false;
    boolean isAllCopied = false;

    Assistant() {
        // Get the logger for "com.github.kwhat.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        String key = NativeKeyEvent.getKeyText(event.getKeyCode());
        if (key.equals(CTRL_KEY_TEXT)) {
            isCtrlPressed = true;
        }

        if (isCtrlPressed && key.equals(A_KEY_TEXT)) {
            isAllSelected = true;
        }

        if (isAllSelected && key.equals(C_KEY_TEXT)) {
            isAllCopied = true;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String text = getText();
            if (!text.isEmpty()) {
                process(text, false);
            }

            isCtrlPressed = false;
            isAllSelected = false;
            isAllCopied = false;
        }
    }

    public void process(String text, boolean isTest) {
        if (text.charAt(0) == '-') {
            int spentTime = +Integer.parseInt(takeAegTime(text));
            TimeManager.generateRoshanTiming(
                    takeTime(text, spentTime),
                    isTest);

        } else {
            if (isNumeric(text)) {
                TimeManager.generateRoshanTiming(text, isTest);
            }
        }
    }

    private String takeTime(String text, int spentTime) {
        int i = 0;
        while (text.charAt(i) != ' ') {
            i++;
        }
        while (text.charAt(i) == ' ') {
            i++;
        }

        StringBuilder time = new StringBuilder();
        for (int j = i; j < text.length(); j++) {
            time.append(text.charAt(j));
        }

        int spentMinutes = spentTime / 60;
        int spentSeconds = spentTime - spentMinutes * 60;

        int originMinutes = TimeManager.makeMinutes(String.valueOf(time));
        int originSeconds = TimeManager.makeSeconds(String.valueOf(time));

        String minutes = String.valueOf(originMinutes - spentMinutes);
        String seconds = String.valueOf(originSeconds - spentSeconds);

        if (seconds.length() < 2) {
            seconds += "0" + seconds;
        }
        return minutes + seconds;

    }

    private String takeAegTime(String text) {
        int i = 0;
        StringBuilder aegisTime = new StringBuilder();
        while (text.charAt(i) != ' ') {
            aegisTime.append(text.charAt(i));
            i++;
        }
        return aegisTime.toString();
    }

    boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    private static String getText() {
        String text = "";
        try {
            text = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        //not need before
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        //not need before
    }
}
