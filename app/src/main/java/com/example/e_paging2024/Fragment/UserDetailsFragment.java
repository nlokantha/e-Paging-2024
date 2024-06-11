package com.example.e_paging2024.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_paging2024.Controls.Methods;
import com.example.e_paging2024.Database.AppDatabase;
import com.example.e_paging2024.Models.User;
import com.example.e_paging2024.R;
import com.example.e_paging2024.databinding.FragmentUserDetailsBinding;
import com.example.e_paging2024.databinding.UserListItemsBinding;

import java.util.ArrayList;

public class UserDetailsFragment extends Fragment {


    public UserDetailsFragment() {
        // Required empty public constructor
    }

    FragmentUserDetailsBinding binding;
    AppDatabase db;
    ArrayList<User> users = new ArrayList<>();
    UserAdapter adapter;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "users-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();


        users.clear();
        users.addAll(db.userDao().getAll());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserAdapter();
        binding.recyclerView.setAdapter(adapter);


        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancleUserDetails();
            }
        });
    }

    class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserViewHolder(UserListItemsBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            user = users.get(position);
            holder.setupUI(user);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            UserListItemsBinding mbinding;
            User mUser;

            public UserViewHolder(UserListItemsBinding vhBinding) {
                super(vhBinding.getRoot());
                mbinding = vhBinding;

                mbinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateEPageCustomDialog(mUser);
                    }
                });

                mbinding.buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Methods.showCustomDialog(getActivity(), "Update User", null, "Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mListener.updateUser(mUser);
//                            }
//                        },"No",null);
                        UpdateCustomDialog(mUser);


                    }
                });

                mbinding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setTitle("Are you sure ?")
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        db.userDao().delete(mUser);
//                                        users.remove(mUser);
//                                        notifyItemRemoved(getAdapterPosition());
//                                    }
//                                })
//                                .setNegativeButton("No", null)
//                                .create().show();
                        DeleteCustomDialog(mUser, getAdapterPosition());
                    }
                });

            }

            public void setupUI(User user) {
                this.mUser = user;
                mbinding.textViewName.setText(user.getName());
                mbinding.textViewFlightNumber.setText(user.getFilghtNumber());
                mbinding.textViewTime.setText(user.getTime());

            }
        }

    }
    private void DeleteCustomDialog(User user, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertDialog = inflater.inflate(R.layout.custom_dialog,null);
        builder.setView(alertDialog);

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

        AppCompatButton buttonClose = alertDialog.findViewById(R.id.buttonClose);
        AppCompatButton buttonConfirm = alertDialog.findViewById(R.id.buttonSelectNext);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.userDao().delete(user);
                users.remove(position);
                adapter.notifyItemRemoved(position);
                alert.dismiss();
            }
        });
    }
    private void UpdateCustomDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertDialog = inflater.inflate(R.layout.update_custom_dialog,null);
        builder.setView(alertDialog);

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

        AppCompatButton buttonClose = alertDialog.findViewById(R.id.buttonClose);
        AppCompatButton buttonConfirm = alertDialog.findViewById(R.id.buttonSelectNext);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateUser(user);
                alert.dismiss();
            }
        });
    }
    private void UpdateEPageCustomDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertDialog = inflater.inflate(R.layout.send_custom_dialog,null);
        builder.setView(alertDialog);

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

        AppCompatButton buttonClose = alertDialog.findViewById(R.id.buttonClose);
        AppCompatButton buttonConfirm = alertDialog.findViewById(R.id.buttonSelectNext);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showInBoard(user);
                alert.dismiss();
            }
        });
    }

    UserDetailsFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (UserDetailsFragmentListener) context;
    }

    public interface UserDetailsFragmentListener {
        void cancleUserDetails();
        void updateUser(User user);
        void showInBoard(User user);
    }


}