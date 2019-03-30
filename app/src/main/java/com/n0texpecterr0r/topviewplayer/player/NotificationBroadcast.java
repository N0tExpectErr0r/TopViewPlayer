package com.n0texpecterr0r.topviewplayer.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.n0texpecterr0r.topviewplayer.IPlayerService;
import com.n0texpecterr0r.topviewplayer.detail.view.DetailActivity;

import static com.n0texpecterr0r.topviewplayer.player.PlayerService.*;

/**
 * 接收Notification广播
 *
 * @author N0tExpectErr0r
 * @time 2019/03/30
 */
public class NotificationBroadcast extends BroadcastReceiver {
    private IPlayerService mPlayerService;
    private boolean isInit = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = IPlayerService.Stub.asInterface(service);
            isInit = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case ACTION_INIT:
                Log.d("NotificationLog", "init");
                Intent serviceIntent = new Intent(context, PlayerService.class);
                context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case ACTION_PREV:
                Log.d("NotificationLog", "prev");
                if (isInit)
                    prev();
                break;
            case ACTION_NEXT:
                Log.d("NotificationLog", "next");
                if (isInit)
                    next();
                break;
            case ACTION_ACTION:
                Log.d("NotificationLog", "action");
                if (isInit) {
                    try {
                        if (mPlayerService.isPlaying())
                            pause();
                        else
                            play();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void play() {
        try {
            mPlayerService.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        try {
            mPlayerService.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void next() {
        try {
            mPlayerService.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void prev() {
        try {
            mPlayerService.prev();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
