package com.stak.smartlockserver.lock.drivers;

import android.media.AudioTrack;

import java.util.Random;

import static android.media.AudioFormat.CHANNEL_OUT_STEREO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioTrack.MODE_STREAM;
import static android.media.AudioTrack.getMinBufferSize;
import static com.stak.smartlockserver.lock.drivers.utils.ToneGenerator.SAMPLE_RATE;

/**
 * Created by gospo on 04.01.15.
 * Driver for my messed up audio jack output ;[
 */
public class RAudioEngineDriver implements EngineDriver {

    private static final int BUFFER_SIZE = getMinBufferSize(SAMPLE_RATE, CHANNEL_OUT_STEREO, ENCODING_PCM_16BIT);

    private Thread playingThread = new Thread(new PlayingRunnable());

    boolean playFlag = true;
    long time;

    public RAudioEngineDriver() {
        playingThread.start();
    }

    @Override
    public void spinForward(long time) { play(time); }

    @Override
    public void spinBackward(long time) { play(time); }

    public void play(long time) {

        this.time = time;
        playFlag = false;
    }

    public AudioTrack createAudioTrack() {
        return new AudioTrack(STREAM_MUSIC, SAMPLE_RATE, CHANNEL_OUT_STEREO, ENCODING_PCM_16BIT, BUFFER_SIZE, MODE_STREAM);
    }

    private class PlayingRunnable implements Runnable {

        @Override
        public void run() {
            Random random = new Random();
            byte[] noise = new byte[BUFFER_SIZE];
            random.nextBytes(noise);

            AudioTrack audioTrack = createAudioTrack();
            while(true) {
                audioTrack.play();
                while(playFlag) {
                    audioTrack.write(noise, 0, noise.length);
                }
                audioTrack.release();
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                audioTrack = createAudioTrack();
                playFlag = true;
            }
        }
    }
}
