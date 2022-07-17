package ru.swarm.color;

import java.util.ArrayList;

public class ImageSymbol {
    private ArrayList<boolean[]> container;
    public ImageSymbol(ArrayList<boolean[]> container) {
        this.container = container;
    }
    public float fillLineRelation(int x) {
        int filled = 0;
        for (boolean i : container.get(x)) {
            System.out.println(i);
            if (i) filled++;
        }
        return ((float)filled)/((float) container.get(x).length);
    }
    public float fillRelation() {
        float[] relations = new float[container.size()];
        for (int i = 0; i < container.size(); i++) {
            relations[i] = fillLineRelation(i);
            System.out.println(relations[i]);
        }

        float average = 0;
        for (float i : relations) {
            average+=i;
        }
        average/= (float) relations.length;
        System.out.println(average);
        return average;
    }
}
