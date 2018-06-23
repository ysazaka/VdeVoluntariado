package com.yakuzasqn.vdevoluntario.support;

public class Constants {
    public static final String USER_SESSION = "USER_SESSION";
    public static final String GROUP = "GROUP";
    public static final String CHOSEN_GROUP = "CHOSEN_GROUP";
    public static final String CHOSEN_USER_FOR_CHAT = "CHOSEN_USER_FOR_CHAT";
    public static final String CHOSEN_GROUP_FOR_CHAT = "CHOSEN_GROUP_FOR_CHAT";
    public static final String CHOSEN_PARTICIPANTS = "CHOSEN_PARTICIPANTS";
    public static final String CHOSEN_PARTICIPANTS_ID = "CHOSEN_PARTICIPANTS_ID";
    public static final String OFFER = "OFFER";
    public static final String DEMAND = "DEMAND";

    public static final byte VOLUNTEER = 1;
    public static final byte INSTITUTE = 2;

    public static final int RESULT_LOGIN_CONFIRMED = 11;
    public static final int RESULT_UPDATED = 22;

    public static final int REQUEST_CODE_GALLERY = 1111;
    public static final int REQUEST_CODE_CONTRIBUTE = 2222;
    public static final int REQUEST_CODE_OFFER = 3333;
    public static final int REQUEST_CODE_CREATE_INSTITUTE = 4444;
    public static final int REQUEST_CODE_CREATE_ACCOUNT = 5555;
    public static final int REQUEST_CODE_MAIN = 6666;
    public static final int REQUEST_CODE_USER_DATA = 7777;

    /* Login Activity */
    public static final int MAIN_ACTIVITY = 1;
    public static final int FORGOT_PASSWORD_ACTIVITY = 2;
    public static final int CREATE_ACCOUNT_ACTIVITY = 3;

    /* User Data Activity */
    public static final int UPDATE_USER_ACTIVITY = 1;

    /* Institute Data Activity */
    public static final int CHECK_INSTITUTE_ACTIVITY = 1;
    public static final int CREATE_POST_OFFER_ACTIVITY = 2;
    public static final int CREATE_POST_DEMAND_ACTIVITY = 3;
    public static final int CHECK_CHAT_ACTIVITY = 4;
    public static final int MANAGE_PARTICIPANTS_ACTIVITY = 5;

    /* ConfigFragment */
    public static final int USER_DATA_ACTIVITY = 1;
    public static final int CREATE_POST_ACTIVITY = 2;
    public static final int MANAGE_POST_ACTIVITY = 3;
    public static final int LOGIN_ACTIVITY = 4;
}
