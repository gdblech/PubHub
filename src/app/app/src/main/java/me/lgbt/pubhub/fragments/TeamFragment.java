package me.lgbt.pubhub.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.lgbt.pubhub.R;

/**
 * @author Geoffrey Blech
 * currently unused, exists as the fragment to be used by the team option in the main fragment menu.
 */

public class TeamFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team, container, false);
    }
}
