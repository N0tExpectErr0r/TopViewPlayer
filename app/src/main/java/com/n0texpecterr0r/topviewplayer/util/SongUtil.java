package com.n0texpecterr0r.topviewplayer.util;

import android.content.Context;
import android.database.Cursor;
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
                if (song.getSize() > 1000 * 800) {
                    list.add(song);
                }
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 格式化获取到的时间
     */
    public static String formatTime(long duration) {
        String min = (duration/1000/60<10)?"0"+(duration/1000/60):""+(duration/1000/60);
        String sec = (duration/1000%60<10)?"0"+(duration/1000%60):""+(duration/1000%60);
        return min+":"+sec;
    }
}
