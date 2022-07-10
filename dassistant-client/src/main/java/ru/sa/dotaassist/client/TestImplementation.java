package ru.sa.dotaassist.client;

import java.util.logging.Logger;

public class TestImplementation {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TestImplementation.class.getName());
        /*int seconds = toSeconds("4356");

        logger.info(String.valueOf(seconds));*/
        String text = "-500 1000";
        String[] temp = text.split(" ");
        temp[0]=temp[0].replace("-", "");
        int аегисВСекундах = toSeconds(temp[0]);
        int текущееВремяВСекундах = toSeconds(temp[1]);
        int времяСмертиРошана = текущееВремяВСекундах-(300-аегисВСекундах);

        logger.info("смерть рошана: "+secondsToText(времяСмертиРошана)+" аегис: "
                +secondsToText(аегисВСекундах)+" минимальный тайминг возрождения: "+secondsToText(getRoshanMin(времяСмертиРошана))+" максимальный тайминг возрождения: "+secondsToText(getRoshanMax(времяСмертиРошана)));
    }

    public static int toSeconds(String text) {
        int number = Integer.parseInt(text);
        int minutes = number/100;
        int seconds = minutes*60;
        seconds+=number%100;
        return seconds;
    }
    public static int getAegis(int seconds) {
        return seconds+300;
    }
    public static int getRoshanMin(int seconds) {
        return seconds+(8*60);
    }
    public static int getRoshanMax(int seconds) {
        return seconds+(11*60);
    }
    public static String secondsToText(int seconds) {
        int minutes = seconds/60;
        return String.valueOf(minutes)+":"+String.valueOf(seconds-(minutes*60));
    }
}
