package clock.socoolby.com.clock.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import clock.socoolby.com.clock.ClockApplication;
import clock.socoolby.com.clock.R;

public class Player {
    private final static String TAG = Player.class.getSimpleName();
    private static final float BEEP_VOLUME = 1.0f;
    private MediaPlayer mediaPlayer;
    private LinkedList<Integer> playList;
    private static Player playerInstance;
    private HashMap<Integer, Integer> NUM_AUDIO = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> WEEK_AUDIO = new HashMap<Integer, Integer>();

    private boolean isReporttime = false;

    public static Player getInstance() {
        if (playerInstance == null) {
            synchronized (Player.class) {
                if (playerInstance == null) {
                    init();
                }
            }
        }
        return playerInstance;
    }

    private static void init() {
        playerInstance = new Player();
        playerInstance.NUM_AUDIO.put(0, R.raw.n0);
        playerInstance.NUM_AUDIO.put(1, R.raw.n1);
        playerInstance.NUM_AUDIO.put(2, R.raw.n2);
        playerInstance.NUM_AUDIO.put(3, R.raw.n3);
        playerInstance.NUM_AUDIO.put(4, R.raw.n4);
        playerInstance.NUM_AUDIO.put(5, R.raw.n5);
        playerInstance.NUM_AUDIO.put(6, R.raw.n6);
        playerInstance.NUM_AUDIO.put(7, R.raw.n7);
        playerInstance.NUM_AUDIO.put(8, R.raw.n8);
        playerInstance.NUM_AUDIO.put(9, R.raw.n9);
        playerInstance.NUM_AUDIO.put(10, R.raw.n10);
        playerInstance.NUM_AUDIO.put(20, R.raw.n20);
        playerInstance.NUM_AUDIO.put(30, R.raw.n30);
        playerInstance.NUM_AUDIO.put(40, R.raw.n40);
        playerInstance.NUM_AUDIO.put(50, R.raw.n50);
        playerInstance.NUM_AUDIO.put(60, R.raw.n60);
        playerInstance.WEEK_AUDIO.put(0, R.raw.sunday);
        playerInstance.WEEK_AUDIO.put(1, R.raw.monday);
        playerInstance.WEEK_AUDIO.put(2, R.raw.tuesday);
        playerInstance.WEEK_AUDIO.put(3, R.raw.wednesday);
        playerInstance.WEEK_AUDIO.put(4, R.raw.thursday);
        playerInstance.WEEK_AUDIO.put(5, R.raw.friday);
        playerInstance.WEEK_AUDIO.put(6, R.raw.saturday);
    }


    private void play(final Context activity) {
        if (playList.size() == 0) {
            isReporttime = false;
            return;
        }
        isReporttime = true;
        Integer resourceID = playList.get(0);
        playList.remove(0);

        AssetFileDescriptor file = activity.getResources().openRawResourceFd(resourceID);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                    mp.stop();
                    play(activity);
                }
            });
            mediaPlayer.start();
        } catch (IOException ioe) {
            mediaPlayer = null;
        }

    }

    public void reportTime(Context activity, int year, int month, int day, int hour, int minute, int today) {
        if (isReporttime)
            return;
        Log.d(TAG, String.format("reportTime Year:%d Month:%d Day:%d Hour:%d Minute:%d  Today:%d", year, month, day, hour, minute, today));
        LinkedList<Integer> playList = new LinkedList<Integer>();
        playList.add(R.raw.todayis);

        int monthTenDigit = month / 10 * 10;
        int monthUnit = month % 10;
        if (monthTenDigit >=10)
            playList.add(NUM_AUDIO.get(monthTenDigit));
        if (monthUnit > 0)
            playList.add(NUM_AUDIO.get(monthUnit));
        playList.add(R.raw.month);

        int dayTenDigit = day / 10 * 10;
        int dayUnit = day % 10;
        if (dayTenDigit >= 10)
            playList.add(NUM_AUDIO.get(dayTenDigit));
        if (dayUnit > 0)
            playList.add(NUM_AUDIO.get(dayUnit));
        playList.add(R.raw.day);

        playList.add(WEEK_AUDIO.get(today));


        int hourUnit;
        if (hour >= 20) {
            playList.add(R.raw.n20);
            hourUnit = hour % 20;
            if (hourUnit > 0) {
                playList.add(NUM_AUDIO.get(hourUnit));
            }
        } else if (hour >= 10) {
            playList.add(R.raw.n10);
            hourUnit = hour % 10;
            if (hourUnit > 0) {
                playList.add(NUM_AUDIO.get(hourUnit));
            }
        } else {
            playList.add(NUM_AUDIO.get(hour));
        }
        playList.add(R.raw.hour);

        int minuteUnit = minute % 10;
        int minuteTenDigit = minute / 10 * 10;
        playList.add(NUM_AUDIO.get(minuteTenDigit));
        if (minuteUnit > 0)
            playList.add(NUM_AUDIO.get(minuteUnit));
        playList.add(R.raw.minute);

        play(playList, activity);


    }

    private void play(LinkedList<Integer> playList, Context activity) {
        this.playList = playList;
        if (mediaPlayer == null)
            mediaPlayer = buildMediaPlayer();
        play(activity);
    }

    private AssetFileDescriptor tickFile = null;

    public void playTick(Context activity) {
        if (!ScreenManager.isScreenOn() || ScreenManager.isApplicationBroughtToBackground(ClockApplication.getContext()))
            return;
        if (mediaPlayer == null)
            mediaPlayer = buildMediaPlayer();
        if (!isReporttime) {
            if (tickFile == null)
                tickFile = activity.getResources().openRawResourceFd(R.raw.tick);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(tickFile.getFileDescriptor(), tickFile.getStartOffset(), tickFile.getLength());
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                        mp.stop();
                    }
                });
                mediaPlayer.start();
            } catch (IOException ioe) {
                mediaPlayer = null;
            }
        }

    }

    private static MediaPlayer buildMediaPlayer() {
        MediaPlayer mediaPlayer = MediaPlayer.create(ClockApplication.getContext(), R.raw.tick);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return mediaPlayer;
    }
}
