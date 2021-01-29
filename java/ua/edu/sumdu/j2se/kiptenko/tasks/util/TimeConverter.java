package ua.edu.sumdu.j2se.kiptenko.tasks.util;

public class TimeConverter {
    public static String SecondsToDays(int n) {
        StringBuilder date = new StringBuilder();

        int days = n / (24 * 3600);
        if (days > 1) {
            date.append(days).append(" ").append("days ");
        }
        else if (days > 0) {
            date.append(days).append(" ").append("day ");
        }

        n = n % (24 * 3600);
        int hours = n / 3600;
        if (hours > 1){
            date.append(hours).append(" ").append("hours ");
        }
        else if (hours > 0) {
            date.append(hours).append(" ").append("hour ");
        }

        n %= 3600;
        int minutes = n / 60;
        if (minutes > 1) {
            date.append(minutes).append(" ").append("minutes ");
        }
        else if (minutes > 0) {
            date.append(minutes).append(" ").append("minute ");
        }

        n %= 60;
        int seconds = n;
        if (seconds > 1) {
            date.append(seconds).append(" ").append("seconds ");
        }
        else if (seconds > 0) {
            date.append(seconds).append(" ").append("second ");
        }

        return date.toString();
    }
}
