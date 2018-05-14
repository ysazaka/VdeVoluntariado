package com.yakuzasqn.vdevoluntario.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orhanobut.hawk.Hawk;
import com.yakuzasqn.vdevoluntario.R;
import com.yakuzasqn.vdevoluntario.model.User;
import com.yakuzasqn.vdevoluntario.support.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChooseParticipantsActivity extends AppCompatActivity {

    private List<User> participantsList;
    private List<String> participantsIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_participants);

        participantsList = new ArrayList<>();
        participantsIdList = new ArrayList<>();

        Hawk.put(Constants.CHOSEN_PARTICIPANTS, participantsList);
        Hawk.put(Constants.CHOSEN_PARTICIPANTS_ID, participantsIdList);
    }
}
