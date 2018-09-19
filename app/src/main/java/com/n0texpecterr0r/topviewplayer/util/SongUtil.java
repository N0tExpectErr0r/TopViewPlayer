package com.n0texpecterr0r.topviewplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import com.n0texpecterr0r.topviewplayer.local.bean.LocalSong;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Nullptr
 * @date 2018/9/10 12:50
 * @describe 音乐扫描类
 */
public class SongUtil {

    /**
     * 查找本地音乐列表
     *
     * @param context 上下文
     * @return 返回本地音乐列表
     */
    public static List<LocalSong> queryLocalSong(Context context) {
        List<LocalSong> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
                null, null, null, AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalSong song = new LocalSong();
                song.setName(cursor.getString(cursor.getColumnIndexOrThrow(Media.TITLE)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(Media.SIZE)));
                song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(Media.ALBUM)));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                song.setImgUrl(getAlbumPic(albumId,context));
                if (song.getSize() > 1000 * 800) {
                    list.add(song);
                }
            }
            cursor.close();
        }
        return list;
    }

    private static String getAlbumPic(String albumId, Context context) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + albumId),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        return album_art;
    }
}
