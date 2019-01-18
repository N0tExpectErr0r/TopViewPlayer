// IPlayerService.aidl
package com.n0texpecterr0r.topviewplayer;

// Declare any non-default types here with import statements
import com.n0texpecterr0r.topviewplayer.bean.Song;
import com.n0texpecterr0r.topviewplayer.OnChangeSongListener;
import com.n0texpecterr0r.topviewplayer.OnPreparedListener;

interface IPlayerService {
      // SongListManager
      void setSongList(in List<Song> songList);
      void setCurrentIndex(int currentIndex);
      void prev();
      void next();
      boolean isSongListEmpty();
      Song getCurrentSong();
      // ModeManager
      int getCurrentMode();
      void changeMode();
      // MediaPlayer
      void resume();
      void pause();
      void play();
      void seekTo(int time);
      int getDuration();
      int getCurrentTime();
      boolean isPlaying();
      void setOnline(boolean isOnline);
      // Listener
      void addChangeListener(OnChangeSongListener listener);
      void addPrepareListener(OnPreparedListener listener);
}
