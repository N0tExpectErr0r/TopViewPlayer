package com.n0texpecterr0r.topviewplayer.album;

import com.n0texpecterr0r.topviewplayer.album.bean.Album;
import com.n0texpecterr0r.topviewplayer.base.MvpBaseView;

public class AlbumContract {
    public interface AlbumView extends MvpBaseView {
        void showAlbum(Album album);
    }

    public interface AlbumPresenterCallback{
        void solveAlbum(Album album);
        void error();
    }

    public interface AlbumPresenter{
        void getAlbum(String albumId);
    }

    public interface AlbumModel{
        void getAlbum(AlbumPresenterCallback AlbumPresenterCallback, String albumId);
    }
}