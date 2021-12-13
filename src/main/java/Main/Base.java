package Main;

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

public class Base implements NativeKeyListener {
    Base() {
// Get the logger for "com.github.kwhat.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

// Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }

    public static void main(String[] args) {
        Visual vis  =new Visual();
        vis.setVisible(true);
    }

    boolean ctrl = false;
    boolean a = false;
    boolean c = false;

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());
        if (key.equals("Ctrl")) {
            ctrl = true;
        }
        if (ctrl == true) {
            if (key.equals("A")) {
                a = true;
            }
        }
        if (a == true) {
            if (key.equals("C")) {
                c = true;
                try {
                    sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                String text = "";
                text = getText();
                if (text != "") {
                    if (text.charAt(0) == '-') {
                        final int timingAegis = 500;
                        int finedTime = timingAegis + Integer.parseInt(takeAegTime(text));
                        String result = String.valueOf(takeTime(text) - finedTime);
                        Timer.generateRoshTiming(result);
                        boolean ctrl = false;
                        boolean a = false;
                        boolean c = false;
                    } else {
                        if (isNumeric(text)) {
                            Timer.generateRoshTiming(text);
                            boolean ctrl = false;
                            boolean a = false;
                            boolean c = false;
                        }
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
        i++;

        String time = "";
        for (int j = i; j < text.length(); j++) {
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
        String text = null;
        try {
            text = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }
}
