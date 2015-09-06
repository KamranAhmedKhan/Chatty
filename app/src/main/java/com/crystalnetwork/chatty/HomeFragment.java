package com.crystalnetwork.chatty;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link HomeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;


//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }


    public static Activity activity;
    Firebase firebase;
    Firebase loginedUser;
    Firebase loginedWall;
    String userName;
    String userImage;
    String userMsg;
    EditText post_editText;
    String wallKeys;
    ArrayList<Post> list;
    CustomAdapter ca;
    View Root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Root = inflater.inflate(R.layout.fragment_home, container, false);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");


        //ActionBar code
        DisplayProfileActivity.actionBar.show();
        DisplayProfileActivity.actionBar.setTitle("Home");

        //ListView code
        ListView listView = (ListView) Root.findViewById(R.id.listView);
        activity = getActivity();
        list = new ArrayList<Post>();
        Collections.reverse(list);
        ca = new CustomAdapter(activity, list);
        listView.setAdapter(ca);



        loginedUser = firebase.child("Users").child(firebase.getAuth().getUid());
        loginedUser.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userName = dataSnapshot.getValue().toString();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
        loginedUser.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userImage = dataSnapshot.getValue().toString();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


        //Retrieving posts objects from Firebase
        GetPostByFirebase();

        //Post PLUS Button Function
        postButton();

        return Root;
    }

    private void GetPostByFirebase(){
        firebase.child("Wall").child(firebase.getAuth().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    list.add(dataSnapshot.getValue(Post.class));
                    ca.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void postButton(){
        ImageButton new_postButton = (ImageButton)Root.findViewById(R.id.new_postButton);
        new_postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog instance
                final Dialog postDialog = new Dialog(getActivity());
                //Tell the dialog to use custom_post_dialogbox.xml as its layout
                postDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                postDialog.setContentView(R.layout.custom_post_dialogbox);
                postDialog.show();

                post_editText = (EditText)postDialog.findViewById(R.id.post_editText);

                //Publish Button Function
                Button publishButton = (Button)postDialog.findViewById(R.id.publishButton);
                publishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (post_editText.getText().toString().matches("")) {
                            post_editText.setError("You can't post empty Message");
                        } else{
                            userMsg = post_editText.getText().toString();
                            //Below code for my Profile Posts
                            loginedWall = firebase.child("Wall").child(firebase.getAuth().getUid()).push();
                            final Map<String,String> testpost = new HashMap<String,String>();
                            testpost.put("name",userName);
                            testpost.put("image",userImage);
                            testpost.put("msg",userMsg);
                            testpost.put("time" ," - "+Time());
                            testpost.put("date",Date());

                            loginedWall.setValue(testpost);//post in MyWall Node
                            wallKeys = loginedWall.getKey();
                            loginedUser.child("Posts").push().setValue(wallKeys);//Post in Logined User "Posts" Node
                            loginedUser.child("Wall").push().setValue(wallKeys);//Post in Logined User "Wall" Node

                            //Below code for my Followers Wall Post
                            loginedUser.child("followers").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    firebase.child("Users").child(dataSnapshot.getKey()).child("Wall").push().setValue(wallKeys);
                                    firebase.child("Wall").child(dataSnapshot.getKey()).child(wallKeys).setValue(testpost);
//                                Util.toast(getActivity(), "Post published");
//                                postDialog.dismiss();
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                            Util.toast(getActivity(), "Post published");
                            postDialog.dismiss();
                        }
                    }
                });

                //Skit Button Function
                Button skipButton = (Button)postDialog.findViewById(R.id.skipButton);
                skipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postDialog.dismiss();
                    }
                });
            }
        });
    }


    private String Time(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    private String Date(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        return df.format(dateobj);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DisplayProfileActivity.actionBar.hide();
    }
    
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
