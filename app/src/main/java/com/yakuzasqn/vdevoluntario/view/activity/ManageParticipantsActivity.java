package com.yakuzasqn.vdevoluntario.view.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.adapter.ParticipantListAdapter;
import com.yakuzasqn.vdevoluntario.model.Group;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;
import com.yakuzasqn.vdevoluntario.support.FirebaseUtils;
import com.yakuzasqn.vdevoluntario.support.RecyclerItemClickListener;
import com.yakuzasqn.vdevoluntario.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ManageParticipantsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RecyclerView.OnItemTouchListener listener;

    private List<User> participantsList;
    private User chosenParticipant;

    private DatabaseReference mRef;
    private ValueEventListener valueEventListenerGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_participants);

        Utils.setBackableToolbar(R.id.mp_toolbar, "Gerenciar participantes", ManageParticipantsActivity.this);

        rv = findViewById(R.id.rv_manage_participants);
        LinearLayout llAddParticipants = findViewById(R.id.mp_ll_add_participants);

//        Group group = Hawk.get(Constants.CHOSEN_GROUP);

        setupRecycle();
        getParticipantsFromFirebase();
    }

    // Corrigir comportamento da seta de voltar - Toolbar customizada
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecycle() {
        rv.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        if (listener != null){
            rv.removeOnItemTouchListener(listener);
        }
        listener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                chosenParticipant = participantsList.get(position);
                removeParticipantDialog();
            }
        });

        rv.addOnItemTouchListener(listener);
    }

    private void getParticipantsFromFirebase(){
        mRef = FirebaseUtils.getBaseRef().child("groups").child("participants");
        participantsList = new ArrayList<>();

        // Cria listener
        valueEventListenerGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar ArrayList de chats
                participantsList.clear();

                // Recuperar chats
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    User participant = dados.getValue(User.class);
                    participantsList.add(participant);
                }

                ParticipantListAdapter adapter = new ParticipantListAdapter(ManageParticipantsActivity.this, participantsList);
                rv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToast(R.string.toast_failLoadingData, ManageParticipantsActivity.this);
            }
        };

        mRef.addValueEventListener(valueEventListenerGroup);
    }

    private void removeParticipantDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageParticipantsActivity.this);
        builder
                .setMessage("Deseja realmente remover este participante?")
                .setTitle("Remoção de usuário")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        participantsList.remove(chosenParticipant);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
