package com.stak.smartlockserver.lock.drivers.utils;

/**
 * Created by gospo on 04.01.15.
 */
public class ToneGenerator {

    public static final int SAMPLE_RATE = 42100;

    public double[] generateTone(double frequency, int samplesCount) {
        double[] samples = new double[samplesCount];
        for (int i = 0; i < samplesCount; ++i) {
            samples[i] = Math.sin(2 * Math.PI * i / (SAMPLE_RATE/frequency));
        }
        return samples;
    }

    public byte[] samplesToPCM16(double[] samples) {
        byte[] pcm16 = new byte[samples.length*2];
        for(int i = 0; i < samples.length ; i++) {
            final short val = (short) ((samples[i] * 32767));
            pcm16[2*i] = (byte) (val & 0x00ff);
            pcm16[2*i+1] = (byte) ((val & 0xff00) >>> 8);
        }
        return pcm16;
    }

    public byte[] pcm16ToStereo(byte[] left, byte[] right) {
        byte[] stereo = new byte[left.length*2];
        for(int i = 0 ; i < left.length ; i+=2) {
            stereo[2*i] = left[i];
            stereo[2*i+1] = left[i+1];
            stereo[2*i+2] = right[i];
            stereo[2*i+3] = right[i+1];
        }
        return stereo;
    }
}
