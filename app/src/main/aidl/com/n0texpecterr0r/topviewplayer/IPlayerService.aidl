// IPlayerService.aidl
package com.n0texpecterr0r.topviewplayer;

// Declare any non-default types here with import statements

interface IPlayerService {
      void start();
      void pause();
      void setSource(String url);
      void seekTo(int time);
      int getDuration();
      int getCurrentTime();
      boolean isPlaying();
}
