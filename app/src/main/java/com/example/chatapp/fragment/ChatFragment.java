package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        replaceFragment(new ChatListFragment());

        BottomNavigationView chatCallNavigation = view.findViewById(R.id.chat_call_navigation);
        chatCallNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chatItem:
                    replaceFragment(new ChatListFragment());
                    break;
                case R.id.callItem:
                    replaceFragment(new CallListFragment());
                    break;
            }
            return true;
        });
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_in_chat_layout, fragment);
        fragmentTransaction.commit();
    }
}