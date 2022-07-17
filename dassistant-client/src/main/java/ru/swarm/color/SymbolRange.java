package ru.swarm.color;

public class SymbolRange {
    private float min;
    private float max;
    private String text;

    public SymbolRange(float min, float max, String text) {
        this.min = min;
        this.max = max;
        this.text = text;
    }

    public boolean InRange(float value) {
        return value>min&&value<max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
