package com.n0texpecterr0r.topviewplayer.util;

import java.sql.NClob;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 15:31
 * @describe TODO
 */
public class ModeManager {
    public static final int MODE_DEFAULT = 0x8474;
    public static final int MODE_RANDOM = 0x57457;
    public static final int MODE_SINGLE = 0x4457;

    private volatile static ModeManager sInstance;
    private int mCurrentIndex;
    private int[] mModes = {MODE_DEFAULT,MODE_RANDOM,MODE_SINGLE};

    private ModeManager(){
        mCurrentIndex = 0;
    }

    public static ModeManager getInstance(){
        if (sInstance == null){
            synchronized (ModeManager.class){
                if (sInstance == null){
                    sInstance = new ModeManager();
                }
            }
        }
        return sInstance;
    }

    public int getCurrentMode() {
        return mModes[mCurrentIndex];
    }

    public void changeMode(){
        if (++mCurrentIndex==mModes.length){
            mCurrentIndex = 0;
        }
    }
}
