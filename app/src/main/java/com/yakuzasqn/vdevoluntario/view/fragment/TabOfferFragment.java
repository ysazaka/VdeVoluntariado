package com.yakuzasqn.vdevoluntario.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakuzasqn.vdevoluntario.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabOfferFragment extends Fragment {


    public TabOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_offer, container, false);

        return v;
    }

}
