package clock.socoolby.com.clock.utils;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateModel implements Serializable {
    private final static String TAG = DateModel.class.getSimpleName();

    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;

    public DateModel() {
        setDataString(getNowWithTime());
    }

    public DateModel(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * @param timeString 12:00 || 12:00:00
     */
    public void setTimeString(String timeString) {
        if (timeString.length() == 8) {
            String temp2[] = timeString.split(":");
            hour = Integer.parseInt(temp2[0]);
            minute = Integer.parseInt(temp2[1]);
            second = Integer.parseInt(temp2[2]);
        } else if (timeString.length() == 5) {
            String temp2[] = timeString.split(":");
            hour = Integer.parseInt(temp2[0]);
            minute = Integer.parseInt(temp2[1]);
        } else {
            new RuntimeException("set time format Illigal");
        }
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * @param dataFormatString 2014-02-02 || 2014-02-02 22:22 || 2014-02-02 22:22:22 Other==getNow()
     */
    public void setDataString(String dataFormatString) {
        if (dataFormatString.length() == 19 || dataFormatString.length() == 16) {
            String txt[] = dataFormatString.split(" ");
            String temp[] = txt[0].split("-");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            day = Integer.parseInt(temp[2]);

            String temp2[] = txt[1].split(":");
            hour = Integer.parseInt(temp2[0]);
            minute = Integer.parseInt(temp2[1]);
            second = Integer.parseInt(temp2[2]);
        } else if (dataFormatString.length() == 10) {
            String temp[] = dataFormatString.split("-");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            day = Integer.parseInt(temp[2]);
            hour = 0;
            minute = 0;
        } else if (dataFormatString.length() == 13) {
            String txt[] = dataFormatString.split(" ");
            String temp[] = txt[0].split("-");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            day = Integer.parseInt(temp[2]);
        } else if (dataFormatString.length() == 5) {
            String temp2[] = dataFormatString.split(":");
            hour = Integer.parseInt(temp2[0]);
            minute = Integer.parseInt(temp2[1]);
        } else {
            setDataString(getNowWithTime());
        }


    }

    public String toString() {
        return getDateString() + " " + getShortTimeString();
    }

    public String getDateString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public String getShortTimeString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public String getTimeString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public static String getNowWithTime() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

    public int minusTime(DateModel strEnd) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(strEnd.getShortTimeString());
            d2 = df.parse(this.getShortTimeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.SECOND_IN_MILLIS);

    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public String getToday() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EE");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try {
            d1 = df.parse(this.getDateString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayFormat.format(d1);
    }

    public int getWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }


    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
