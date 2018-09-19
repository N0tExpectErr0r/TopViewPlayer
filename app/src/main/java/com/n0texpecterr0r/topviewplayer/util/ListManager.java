package com.n0texpecterr0r.topviewplayer.util;

import com.n0texpecterr0r.topviewplayer.base.AbstractListManager;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 16:35
 * @describe TODO
 */
public class ListManager {
    public static final int TYPE_LOCAL = 0x55687;
    public static final int TYPE_ONLINE = 0x4548;

    private static int managerType;

    public static AbstractListManager getCurrentManager(){
           if (managerType == TYPE_LOCAL){
               return LocalListManager.getInstance();
           }else if (managerType == TYPE_ONLINE){
               return OnlineListManager.getInstance();
           }
           return null;
    }

    public static void setManagerType(int type){
        managerType = type;
    }
}
