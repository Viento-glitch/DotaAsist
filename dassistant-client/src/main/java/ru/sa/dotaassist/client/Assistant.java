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
    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());
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
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            String text = getText();
            if (!text.isEmpty()) {
                if (text.charAt(0) == '-') {
                    final int timingAegis = 500;
                    int finedTime = timingAegis + Integer.parseInt(takeAegTime(text));
                    String result = String.valueOf(takeTime(text) - finedTime);
                    Timer.generateRoshanTiming(result);

                    isCtrlPressed = false;
                    isAllSelected = false;
                    isAllCopied = false;
                } else {
                    if (isNumeric(text)) {
                        Timer.generateRoshanTiming(text);

                        isCtrlPressed = false;
                        isAllSelected = false;
                        isAllCopied = false;
                    }
                }
            }
        }
    }

    private int takeTime(String text) {
        int i = 0;
        while (text.charAt(i) != ' ') {
            i++;
        }

        String time = "";
        for (int j = i + 1; j < text.length(); j++) {
            time += text.charAt(j);
        }
        return Integer.parseInt(time);
    }

    private String takeAegTime(String text) {
        int i = 0;
        String aegisTime = "";
        while (text.charAt(i) != ' ') {
            aegisTime += text.charAt(i);
            i++;
        }
        return aegisTime;
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

        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
//        String key = NativeKeyEvent.getKeyText(e.getKeyCode());
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
