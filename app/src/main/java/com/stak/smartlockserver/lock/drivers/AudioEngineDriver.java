package com.stak.smartlockserver.lock.drivers;

import android.media.AudioTrack;

import com.stak.smartlockserver.lock.drivers.utils.ToneGenerator;

import static android.media.AudioFormat.CHANNEL_OUT_STEREO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioTrack.MODE_STREAM;
import static android.media.AudioTrack.getMinBufferSize;
import static com.stak.smartlockserver.lock.drivers.utils.ToneGenerator.SAMPLE_RATE;

/**
 * Created by gospo on 04.01.15.
 */
public class AudioEngineDriver implements EngineDriver{

    ToneGenerator toneGenerator = new ToneGenerator();

    private static final int BUFFER_SIZE = getMinBufferSize(SAMPLE_RATE, CHANNEL_OUT_STEREO, ENCODING_PCM_16BIT);

    Thread playAudioThread;

    public void spinForward(long time) {
        spin("right", time);
    }

    public void spinBackward(long time) {
        spin("left", time);
    }

    private void spin(String channel, long time) {
        if(playAudioThread == null || !playAudioThread.isAlive()) {
            playAudioThread = new Thread(new PlayAudioRunnable(channel, time));
            playAudioThread.start();
        }
    }

    private AudioTrack createAudioTrack() {
        return new AudioTrack(STREAM_MUSIC, SAMPLE_RATE, CHANNEL_OUT_STEREO, ENCODING_PCM_16BIT, BUFFER_SIZE, MODE_STREAM);
    }

    class PlayAudioRunnable implements Runnable {
        private String channel;
        private long time;

        public PlayAudioRunnable(String channel, long time) {
            this.channel = channel;
            this.time = time;
        }

        @Override
        public void run() {
            double[] tone = toneGenerator.generateTone(15000, (int) (SAMPLE_RATE * time / 1000));
            byte[] pcm16 = toneGenerator.samplesToPCM16(tone);
            byte[] empty = new byte[pcm16.length];
            byte[] stereo = {};
            switch (channel) {
                case "left":
                    stereo = toneGenerator.pcm16ToStereo(pcm16, empty);
                    break;
                case "right":
                    stereo = toneGenerator.pcm16ToStereo(empty, pcm16);
                    break;
            }

            AudioTrack audioTrack = createAudioTrack();
            audioTrack.play();
            audioTrack.write(stereo, 0, stereo.length);
            audioTrack.release();
        }
    }

}
