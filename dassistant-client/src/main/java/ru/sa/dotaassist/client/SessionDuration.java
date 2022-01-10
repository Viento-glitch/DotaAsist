package ru.sa.dotaassist.client;

public class SessionDuration {
    //todo make worked

    long duration;
    int seconds = Math.toIntExact(duration % 1000);
    int minutes = seconds % 60;
    int hours = minutes % 60;

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMilliseconds() {
        String mils = String.valueOf(duration);
        return Integer.parseInt(mils.substring(mils.length() - 3));
    }
}

