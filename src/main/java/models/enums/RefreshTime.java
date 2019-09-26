package models.enums;

public enum RefreshTime {
    MIN30(30), HOUR1(60), HOUR2(120), HOUR6(360), HOUR12(720);

    private int time;

    RefreshTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public static String name(RefreshTime val) {
        if (val.time == 30) {
            return "30 minutes";
        } else if (val.time == 60) {
            return "1 hour";
        }
        return val.time / 60 + " hours";
    }
}
