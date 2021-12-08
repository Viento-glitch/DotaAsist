package Main;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Time;

import static java.lang.Thread.sleep;

public class Main implements NativeKeyListener {
    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalScreen.getInstance().addNativeKeyListener(new Main());
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
                String text = getText();
                if (isNumeric(text)) {
                    Timer.generateRoshTiming(text);
                    boolean ctrl = false;
                    boolean a = false;
                    boolean c = false;
                }
            }
        }
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
