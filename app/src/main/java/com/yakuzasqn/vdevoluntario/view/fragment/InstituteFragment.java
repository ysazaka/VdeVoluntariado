package com.yakuzasqn.vdevoluntario.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.InstituteAdapter;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;
import com.yakuzasqn.vdevoluntario.view.activity.InstituteDataActivity;
import com.yakuzasqn.vdevoluntario.view.activity.NewInstituteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstituteFragment extends Fragment {

    private List<Group> groupList;
    private InstituteAdapter adapter;
    private RecyclerView.OnItemTouchListener listener;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;

    public InstituteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_institute, container, false);

        LinearLayout llNewInstitute = v.findViewById(R.id.ll_new_institute);
        llNewInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewInstituteActivity.class);
                startActivity(intent);
            }
        });

        /***************************************************************
         Montagem do RecyclerView e do Adapter
         ****************************************************************/

        groupList = new ArrayList<>();

        final RecyclerView rvInstitute = v.findViewById(R.id.rv_institute);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        rvInstitute.setLayoutManager(mLayoutManager);

        if (listener != null){
            rvInstitute.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), InstituteDataActivity.class);

                Group chosenGroup = groupList.get(position);
                Hawk.put(Constants.CHOSEN_GROUP, chosenGroup);

                startActivity(intent);
            }
        });

        rvInstitute.addOnItemTouchListener(listener);

        /***************************************************************
         Recuperar dados do Firebase
         ****************************************************************/

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Carregando grupos, aguarde...", true);
        mRef = FirebaseUtils.getBaseRef().child("groups");
        // Cria listener
        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de grupos
                groupList.clear();

                // Recuperar grupos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Group group = dados.getValue(Group.class);
                    groupList.add(group);
                }

                adapter = new InstituteAdapter(getContext(), groupList);
                adapter.notifyDataSetChanged();
                rvInstitute.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, getActivity());
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);
        dialog.dismiss();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRef.addValueEventListener(valueEventListenerGroup);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRef.removeEventListener(valueEventListenerGroup);
    }

}
