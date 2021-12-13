package Main;

import java.awt.*;

public class PixelScan {
    public static void main(String[] args) {
        while (true) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            checkColorOfBase();
            System.out.println(getColor(457, 446));
        }
    }

    private static void checkColorOfBase() {
        String color = getColor(208, 878);
        System.out.println(color);
        if (color.equals("r=189,g=186,b=189")) {
            System.out.println("radiant");
        }
        if (color.equals("")) {
        }
    }

    private static String getColor(int x, int y) {
        try {
            Robot r = new Robot();
            Color color = r.getPixelColor(x, y);
//            System.out.println(color);
            String result = String.valueOf(color).substring(15);
            result = result.substring(0, result.length() - 1);
            return result;

        } catch (AWTException e) {
            e.printStackTrace();
        }

        return null;
    }
}
