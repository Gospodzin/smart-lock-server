package com.stak.smartlockserver.lock.drivers;

/**
 * Created by gospo on 04.01.15.
 */
public interface EngineDriver {
    void spinForward(long time);
    void spinBackward(long time);
}
