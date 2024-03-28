package com.example.e_paging2024.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_paging2024.Database.AppDatabase;
import com.example.e_paging2024.Models.User;
import com.example.e_paging2024.R;
import com.example.e_paging2024.databinding.FragmentUpdateUserBinding;


public class UpdateUserFragment extends Fragment {


    private static final String ARG_PARAM_USER = "ARG_PARAM_USER";


    // TODO: Rename and change types of parameters
    private User mUser;
    FragmentUpdateUserBinding binding;
    AppDatabase db;


    public UpdateUserFragment() {
        // Required empty public constructor
    }

    public static UpdateUserFragment newInstance(User user) {
        UpdateUserFragment fragment = new UpdateUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_PARAM_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mUser != null){
            binding.textInputEditTextName.setText(mUser.getName());
            binding.textInputEditTextFlightNumber.setText(mUser.getFilghtNumber());
            binding.textInputEditTextTime.setText(mUser.getTime());
        }

        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.textInputEditTextName.getText().toString();
                String flightNumber = binding.textInputEditTextFlightNumber.getText().toString();
                String time = binding.textInputEditTextTime.getText().toString();

                if (name.isEmpty()){
                    binding.textInputLayoutName.setError("Please Enter Name");

                } else if (flightNumber.isEmpty()) {
                    binding.textInputLayoutFlightNumber.setError("Please Enter Flight Number");
                } else if (time.isEmpty()) {
                    binding.textInputLayoutTime.setError("Please Set Time");
                }else {
                    db = Room.databaseBuilder(getActivity(), AppDatabase.class,"users-db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                    mUser.setName(name);
                    mUser.setFilghtNumber(flightNumber);
                    mUser.setTime(time);
                    db.userDao().insert(mUser);
                    mListener.doneUpdate();

                }
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancleUpdate();
            }
        });


    }
    UpdateListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener= (UpdateListener) context;
    }

    public interface UpdateListener{
        void cancleUpdate();
        void doneUpdate();
    }
}