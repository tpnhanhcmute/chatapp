package com.example.chatapp.common;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.chatapp.R;

public class DialogManager {

    static DialogManager dialogManager;
    static Dialog _mCustomDialog;

    public static DialogManager GetInstance(Activity activity){
        if(dialogManager == null){
            dialogManager = new DialogManager();
            dialogManager._mCustomDialog = new Dialog(activity);
            dialogManager._mCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogManager._mCustomDialog.setContentView(R.layout.custom_dialog);
        }
        return dialogManager;
    }

    public void ShowLoading(){
        _mCustomDialog.show();
    }
    public  void HideLoading(){
        _mCustomDialog.dismiss();
    }
}
