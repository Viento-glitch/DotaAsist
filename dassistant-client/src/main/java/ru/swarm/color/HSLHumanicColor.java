package ru.swarm.color;

public class HSLHumanicColor {
    private float HMax;
    private float HMin;
    private float SMax;
    private float SMin;
    private float LMax;
    private float LMin;


    public HSLHumanicColor(float HMax, float HMin, float SMax, float SMin, float LMax, float LMin) {
        this.HMax = HMax;
        this.HMin = HMin;
        this.SMax = SMax;
        this.SMin = SMin;
        this.LMax = LMax;
        this.LMin = LMin;
    }

    public boolean InRange(HSLColor color) {
        /*System.out.println(color);
        System.out.println(color.inRangeHue(HMin, HMax));
        System.out.println(color.inRangeLightness(LMin, LMax));
        System.out.println(color.inRangeSaturation(SMin, SMax));
         */
        return color.inRangeHue(HMin, HMax) && color.inRangeLightness(LMin, LMax) && color.inRangeSaturation(SMin, SMax);
    }

    public HSLHumanicColor copy() {
        return new HSLHumanicColor(HMax, HMin, SMax, SMin, LMax, LMin);
    }
    public static HSLHumanicColor create() {
        return new HSLHumanicColor(0, 360, 10, 100, 5, 95);
    }
    public HSLHumanicColor dark() {
        LMax = 35;
        LMin = 5;
        return this;
    }
    public HSLHumanicColor light() {
        LMin = 55;
        LMax = 65;
        return this;
    }
    public HSLHumanicColor middleLightness() {
        LMin = 34;
        LMax = 56;
        return this;
    }
    public HSLHumanicColor anyLightness() {
        LMin = 5;
        LMax = 75;
        return this;
    }
    public HSLHumanicColor noLightComputation() {
        LMin = 0;
        LMax = 100;
        return this;
    }
    public HSLHumanicColor anySaturation() {
        SMin = 5;
        SMax = 100;
        return this;
    }
    public HSLHumanicColor noSaturationComputation() {
        SMin = 0;
        SMax = 100;
        return this;
    }
    public HSLHumanicColor weakSaturation() {
        SMin = 17;
        SMax = 50;
        return this;
    }
    public HSLHumanicColor strongSaturation() {
        SMin = 60;
        SMax = 100;
        return this;
    }
    public HSLHumanicColor middleSaturation() {
        SMin = 37;
        SMax = 67;
        return this;
    }
    public HSLHumanicColor white() {
        LMin = 65;
        LMax = 100;
        return this;
    }
    public HSLHumanicColor onlyWhite() {
        LMin = 80;
        LMax = 100;
        SMin = 0;
        SMax = 100;
        HMin = 0;
        HMax = 360;
        return this;
    }
    public HSLHumanicColor darkShadow() {
        LMin = 3;
        LMax = 18;
        return this;
    }
    public HSLHumanicColor lightShadow() {
        LMin = 15;
        LMax = 30;
        return this;
    }
    public HSLHumanicColor shadow() {
        LMin = 3;
        LMax = 30;
        return this;
    }
    public HSLHumanicColor black() {
        LMin = 0;
        LMax = 10;
        return this;
    }
    public HSLHumanicColor onlyBlack() {
        LMin = 0;
        LMax = 7;
        SMin = 0;
        SMax = 100;
        HMin = 0;
        HMax = 360;
        return this;
    }
    public HSLHumanicColor red() {
        HMin = 350;
        HMax = 10;
        return this;
    }
    public HSLHumanicColor containRed() {
        HMin = 241;
        HMax = 119;
        return this;
    }
    public HSLHumanicColor orange() {
        HMin = 10;
        HMax = 40;
        return this;
    }
    public HSLHumanicColor yellow() {
        HMin = 40;
        HMax = 67;
        return this;
    }
    public HSLHumanicColor green() {
        HMin = 67;
        HMax = 150;
        return this;
    }
    public HSLHumanicColor containGreen() {
        HMin = 1;
        HMax = 239;
        return this;
    }
    public HSLHumanicColor cyan() {
        HMin = 150;
        HMax = 210;
        return this;
    }
    public HSLHumanicColor blue() {
        HMin = 210;
        HMax = 254;
        return this;
    }
    public HSLHumanicColor containBlue() {
        HMin = 121;
        HMax = 359;
        return this;
    }
    public HSLHumanicColor purple() {
        HMin = 255;
        HMax = 267;
        return this;
    }
    public HSLHumanicColor magenta() {
        HMin = 267;
        HMax = 285;
        return this;
    }
    public HSLHumanicColor pink() {
        HMin = 285;
        HMax = 349;
        return this;
    }
    public HSLHumanicColor grey() {
        HMin = 0;
        HMax = 360;
        SMin = 0;
        SMax = 9;
        LMin = 20;
        LMax = 65;
        return this;
    }
    @Override
    public String toString() {
        return "HSLHumanicColor{" +
                "HMax=" + HMax +
                ", HMin=" + HMin +
                ", SMax=" + SMax +
                ", SMin=" + SMin +
                ", LMax=" + LMax +
                ", LMin=" + LMin +
                '}';
    }

    public float getHMax() {
        return HMax;
    }

    public void setHMax(float HMax) {
        this.HMax = HMax;
    }

    public float getHMin() {
        return HMin;
    }

    public void setHMin(float HMin) {
        this.HMin = HMin;
    }

    public float getSMax() {
        return SMax;
    }

    public void setSMax(float SMax) {
        this.SMax = SMax;
    }

    public float getSMin() {
        return SMin;
    }

    public void setSMin(float SMin) {
        this.SMin = SMin;
    }

    public float getLMax() {
        return LMax;
    }

    public void setLMax(float LMax) {
        this.LMax = LMax;
    }

    public float getLMin() {
        return LMin;
    }

    public void setLMin(float LMin) {
        this.LMin = LMin;
    }
}
