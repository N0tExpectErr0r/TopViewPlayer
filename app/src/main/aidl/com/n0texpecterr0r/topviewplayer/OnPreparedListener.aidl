// OnPreparedListener.aidl
package com.n0texpecterr0r.topviewplayer;

// Declare any non-default types here with import statements
import com.n0texpecterr0r.topviewplayer.bean.Song;

interface OnPreparedListener {
    void onPrepared(in Song curSong);
}
