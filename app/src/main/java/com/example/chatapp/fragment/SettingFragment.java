package com.example.chatapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.activity.EditProfileActivity;
import com.example.chatapp.activity.LoginActivity;
import com.example.chatapp.R;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {
    LinearLayout linearLayoutLogout;
    LinearLayout linearLayoutEditProfile;
    TextView textViewDisplayName;
    TextView textViewEmail;
    CircleImageView profile_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Mapping(view);
        SetListener();

        SetData();
        return view;
    }

   private  void SetData(){
       User user = SharedPreference.getInstance(getContext()).getUser();
       if(user.avatarUrl!= null)Glide.with(getContext()).load(user.avatarUrl).into(profile_image);
       if(user.email != null) textViewEmail.setText(user.email);
       if(user.name != null) textViewDisplayName.setText(user.name);
   }

    private void SetListener() {
        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenConfirmPopup();
            }
        });
        linearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void OpenConfirmPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        //builder.setTitle("");
        builder.setMessage("Do you want to sign out?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreference.getInstance(getContext()).setUser(null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Mapping(View view) {
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout);
        linearLayoutEditProfile = view.findViewById(R.id.linearLayoutEditProfile);
        textViewDisplayName = view.findViewById(R.id.textViewDisplayName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        profile_image = view.findViewById(R.id.profile_image);
    }
}