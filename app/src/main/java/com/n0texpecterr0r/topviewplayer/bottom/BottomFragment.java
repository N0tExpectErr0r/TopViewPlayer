package com.n0texpecterr0r.topviewplayer.bottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.n0texpecterr0r.topviewplayer.R;

/**
 * @author Created by Nullptr
 * @date 2018/9/19 13:35
 * @describe TODO
 */
public class BottomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom,container,false);

        return view;
    }
}
