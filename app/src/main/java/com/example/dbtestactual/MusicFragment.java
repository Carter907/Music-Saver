package com.example.dbtestactual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class MusicFragment extends Fragment {

    public MusicFragment() {
        super(R.layout.music_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle instanceBundle) {

        View view = inflater.inflate(R.layout.music_fragment, root, false);

        return view;
    }
}
