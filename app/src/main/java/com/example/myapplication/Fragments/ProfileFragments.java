package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragments extends Fragment {

    private FirebaseAuth mAuth;

    public ProfileFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragments, container, false);

        init(view);
        return view;
    }

    private void init(View view){
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                startActivity(new Intent(ProfileFragments.this.getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.create().show();
                break;
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}