package com.teamcaffeine.hotswap.navigation;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teamcaffeine.hotswap.ListItemActivity;
import com.teamcaffeine.hotswap.R;
import com.teamcaffeine.hotswap.login.User;
import com.teamcaffeine.hotswap.swap.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    // create objects to reference layout objects
    private Button btnListItem;
    private ListView listviewAllItems;
    private List<String> itemsElementsList;
    private ArrayAdapter<String> itemsAdapter;
    private TextView txtCurrentlyRenting;
    private ListView listviewLending;
    private List<String> lendingElements;
    private ArrayAdapter<String> lendingAdapter;
    private int LIST_ITEM_REQUEST_CODE = 999;

    public ProgressDialog mProgressDialog;

    // Database reference fields
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference users;
    private DatabaseReference items;
    private String userTable = "users";
    private String itemTable = "items";

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private HomeFragmentListener HFL;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnListItem = view.findViewById(R.id.btnListItem);
        listviewAllItems = view.findViewById(R.id.listviewAllItems);
        txtCurrentlyRenting = view.findViewById(R.id.txtCurrentlyRenting);
        listviewLending = view.findViewById(R.id.listviewLending);

        // Get a reference to our user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        users = database.getReference().child(userTable);
        items = database.getReference().child(itemTable);

        itemsElementsList = new ArrayList<String>();

//        listviewAllItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
//                        //set message, title, and icon
//                        .setTitle(R.string.delete)
//                        .setMessage(R.string.delete_item_question)
//                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                DatabaseReference ref = users.child(firebaseUser.getUid());
//                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        User user = dataSnapshot.getValue(User.class);
//
//                                        boolean didRemove = user.removePayment(listviewPayment.getItemAtPosition(position).toString());
//                                        if (didRemove) {
//                                            // Update database
//                                            Map<String, Object> userUpdate = new HashMap<>();
//                                            userUpdate.put(firebaseUser.getUid(), user.toMap());
//                                            users.updateChildren(userUpdate);
//
//                                            // Update UI
//                                            paymentElementsList.remove(position);
//                                            paymentAdapter.notifyDataSetChanged();
//                                        } else {
//                                            Log.i(TAG, "User attempted to delete a nonexistent item");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        Log.e(TAG, "Payment update failed", databaseError.toException());
//                                    }
//                                });
//                                dialog.dismiss();
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create();
//                myQuittingDialogBox.show();
//            }
//        });

        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, itemsElementsList);
        listviewAllItems.setAdapter(itemsAdapter);

        // Query for all entries with a certain child with value equal to something
        Query allUserItems = items.orderByChild("ownerID").equalTo(firebaseUser.getUid());

        // Add listener for Firebase response on said query
        allUserItems.addValueEventListener( new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren() ){
                    Item item = i.getValue(Item.class);
                    itemsElementsList.add(item.getName());
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        btnListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListItemActivity.class);
                startActivityForResult(i, LIST_ITEM_REQUEST_CODE);
            }
        });
    }

    public interface HomeFragmentListener {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HFL = (HomeFragment.HomeFragmentListener) context;  //context is a handle to the main activity, let's bind it to our interface.
    }
}
