package ru.swarm.color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SParser {
    HSLHumanicColor textColor;
    private ArrayList<SymbolRange> dictionary;
    public SParser(HSLHumanicColor textColor, ArrayList<SymbolRange> dictionary) {
        this.textColor = textColor;
        this.dictionary = dictionary;
    }
    public ArrayList<ImageSymbol> parse(BufferedImage image) {
        ArrayList<ImageSymbol> symbols = new ArrayList<>();
        ArrayList<boolean[]> container = new ArrayList<>();
        for (int i = 0; i < image.getWidth(); i++) {
            boolean contain = false;
            boolean line[] = new boolean[image.getHeight()];
            for(int o = 0 ; o < image.getHeight(); o++) {
                Color color = new Color(image.getRGB(i, 0));
                float[] hsl = HSLColor.RGB2HSL(color.getRed(), color.getGreen(), color.getBlue());
                line[o] = false;
                if (textColor.InRange(new HSLColor(hsl[0], hsl[1], hsl[2]))) {
                    contain = true;
                    line[o] = true;
                } else {
                    line[o] = false;
                }
            }
            if (contain) {
                container.add(line);
            }
            if (!contain&&!container.isEmpty()) {
                ImageSymbol symbol = new ImageSymbol(container);
                //System.out.println(symbol);
                symbols.add(symbol);
                container = new ArrayList<>();
            }
        }
        return symbols;
    }
    public String toText(ArrayList<ImageSymbol> symbols) {
        String text = "";
        for (ImageSymbol symbol : symbols) {
            text+=Symbol2Text(symbol);
        }
        return text;
    }
    public String Symbol2Text(ImageSymbol symbol) {
        for (SymbolRange range : dictionary) {
            if (range.InRange(symbol.fillRelation())) {
                return range.getText();
            }
        }
        return "";
    }
}
