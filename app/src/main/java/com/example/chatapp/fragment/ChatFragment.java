package com.example.chatapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.adapter.SearchAdapter;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.Search;
import com.example.chatapp.model.request.SearchRequest;
import com.example.chatapp.model.response.GetContactResponse;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.model.response.SearchResponse;
import com.example.chatapp.service.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    EditText editTextSearch;
    ImageButton imageButtonSearch;
    RecyclerView rcListSearch;
    List<Search> searchList;
    SearchAdapter searchAdapter;
    FrameLayout frame_in_chat_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
       Mapping(view);
       SetData();
       SetListener();
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
        editTextSearch.clearFocus();
        rcListSearch.setVisibility(View.GONE);
        return view;
    }

    private void SetListener() {

        frame_in_chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.clearFocus();
                editTextSearch.setText("");
                rcListSearch.setVisibility(View.GONE);
            }
        });
        editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList.clear();
                searchAdapter.notifyDataSetChanged();
                rcListSearch.setVisibility(View.VISIBLE);
            }
        });
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallSearch();
            }
        });
    }

    private void CallSearch() {
        String query = editTextSearch.getText().toString();
        if(TextUtils.isEmpty(query)) return;

        Search(query);


    }
    private  void Search(String query){
        APIService apiService = APIService.getAPIService();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.userID = SharedPreference.getInstance(getContext()).getUser().userID;
        searchRequest.query = query;
        apiService.search(searchRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(!response.isSuccessful()) {
                    if(response.body()!= null){
                        Toast.makeText(getContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if(response.body().isError) {
                    Toast.makeText(getContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    return;
                }

                Type type = new TypeToken<SearchResponse>(){}.getType();
                SearchResponse searchResponse = new Gson().fromJson(new Gson().toJson(response.body().data),type);
                if(searchResponse.listSearch.size() ==0){
                    Toast.makeText(getContext(),"No user", Toast.LENGTH_SHORT).show();
                    return;
                }
                rcListSearch.setVisibility(View.VISIBLE);
                searchList.clear();
                searchList.addAll(searchResponse.listSearch);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SetData() {
        searchList = new ArrayList<>();
        searchList.add(new Search());
        searchList.add(new Search());
        searchList.add(new Search());
        searchList.add(new Search());
        searchList.add(new Search());

        searchAdapter = new SearchAdapter(this.getContext(),searchList);
        rcListSearch.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
        rcListSearch.setLayoutManager(layoutManager);
        rcListSearch.setAdapter(searchAdapter);

        searchAdapter.notifyDataSetChanged();


    }

    private void Mapping(View view) {
        editTextSearch = view.findViewById(R.id.editTextSearch);
        imageButtonSearch = view.findViewById(R.id.imageButtonSearch);
        rcListSearch = view.findViewById(R.id.rcListSearch);
        frame_in_chat_layout = view.findViewById(R.id.frame_in_chat_layout);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_in_chat_layout, fragment);
        fragmentTransaction.commit();
    }
}