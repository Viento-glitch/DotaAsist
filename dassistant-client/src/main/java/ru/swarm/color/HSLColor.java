package ru.swarm.color;

public class HSLColor {
    private float h;
    private float s;
    private float l;
    public HSLColor(float H, float S, float L) {
        h = H;
        l = L;
        l = L;
    }
    public HSLColor(int R, int G, int B) {
        float[] hsl = RGB2HSL(R, G, B);
        h = hsl[0];
        s = hsl[1];
        l = hsl[2];
    }
    public static float[] RGB2HSL(int R, int G, int B) {
        float[] hsl = new float[3];
        float r = ((float)R)/255;
        float g = ((float)G)/255;
        float b = ((float)B)/255;
        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);

        hsl[2] = (max+min)/2*100;
        if (max==min) {
            hsl[0] = 0;
            hsl[1] = 0;
        } else {
            float d = max - min;
            hsl[1] = (hsl[2]/100 > 0.5 ? d / (2 - max - min) : d / (max + min))*100;
            if (r > g && r > b)
                hsl[0] = (g - b) / d + (g < b ? 6.0f : 0.0f);

            else if (g > b)
                hsl[0] = (b - r) / d + 2.0f;

            else
                hsl[0] = (r - g) / d + 4.0f;
            hsl[0] /= 6.0f*2.77/1000;
        }
        return hsl;
    }
    public static int[] HSL2RGB(float h, float s, float l){
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        int[] rgb = {to255(r), to255(g), to255(b)};
        return rgb;
    }
    private static int to255(float v) { return (int)Math.min(255,256*v); }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    public boolean inRangeHue(float min, float max) {
        if (max<min) return h<max || h>min;
        else return h>min && h<max;
    }
    public boolean inRangeSaturation(float min, float max) {
        return s>min && s<max;
    }
    public boolean inRangeLightness(float min, float max) {
        return l>min && l<max;
    }

    @Override
    public String toString() {
        return "HSLColor{" +
                "h=" + h +
                ", s=" + s +
                ", l=" + l +
                '}';
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.s = s;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }
}
