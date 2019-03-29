package com.n0texpecterr0r.topviewplayer;

import com.n0texpecterr0r.topviewplayer.bean.Song;

interface OnChangeSongListener {

    void onChanged(in Song song);

    void onAction(boolean isPlaying);
}
