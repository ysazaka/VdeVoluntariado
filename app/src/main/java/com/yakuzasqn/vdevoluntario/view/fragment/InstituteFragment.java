package com.yakuzasqn.vdevoluntario.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.view.activity.NewInstituteActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstituteFragment extends Fragment {

    private TextView tvNewInstitute;

    public InstituteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_institute, container, false);

        tvNewInstitute = v.findViewById(R.id.tv_new_institute);
        tvNewInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewInstituteActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
