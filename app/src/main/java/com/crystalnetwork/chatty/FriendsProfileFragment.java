package com.crystalnetwork.chatty;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class FriendsProfileFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FriendsProfileFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FriendsProfileFragment newInstance(String param1, String param2) {
//        FriendsProfileFragment fragment = new FriendsProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public FriendsProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Firebase firebase;
    Firebase loginedUser;
    Firebase searchedUser;
    ImageView friend_dp;
    private TextView name_textView;
    private TextView email_textView;
    private TextView location_textView;
    private TextView about_textView;
    private Button follow_button;
    public static String searchedUserString;
    String userKey;
    View Root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Root = inflater.inflate(R.layout.fragment_friends_profile, container, false);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");
        loginedUser = firebase.child("Users").child(firebase.getAuth().getUid());
        searchedUser = firebase.child("Users").child(searchedUserString);//String UID received by Add Friend Dialog BOx

        Home();
        setProfileTexts();
        Follow();

        friend_dp=(ImageView)Root.findViewById(R.id.friend_dp);
        //if(firebase.user.image!=null){R.drawable.firebaseString}else{same as R.drawable.user}
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.user_transparent);
        Bitmap b = getRoundedShape(bMap);
        friend_dp.setImageBitmap(b);
        return Root;
    }

    String logined_email;
    String searchedUser_email;
    private void setProfileTexts(){
        loginedUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logined_email = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //Reminder... it should be received form other fragment as a user object... In this fragment just set from object
        name_textView = (TextView)Root.findViewById(R.id.friend_name_textView);
        email_textView = (TextView)Root.findViewById(R.id.friend_email_textView);
        location_textView = (TextView)Root.findViewById(R.id.friend_location_textView);
        about_textView = (TextView)Root.findViewById(R.id.friend_about_textView);
        final TextView friendsPost = (TextView)Root.findViewById(R.id.friendsPost);
        final TextView friendsFollowing = (TextView)Root.findViewById(R.id.friendsFollowing);
        final TextView friendsFollowers = (TextView)Root.findViewById(R.id.friendsFollowers);


        searchedUser.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsPost.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsFollowers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsFollowing.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email_textView.setText(dataSnapshot.getValue().toString());
                searchedUser_email = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                location_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        searchedUser.child("about").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                about_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    private void Follow(){
        follow_button = (Button)Root.findViewById(R.id.follow_button);
        loginedUser.child("following").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                searchedUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot Snapshot) {
                        searchedUser_email = Snapshot.getValue().toString();
                        if (dataSnapshot.getValue().toString().matches(searchedUser_email)){
                            follow_button.setText("Following");
                            follow_button.setBackgroundResource(R.drawable.following_button);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
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


        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow_button.getText().toString().matches("Follow")) {
                    loginedUser.child("following").child(searchedUserString).setValue(searchedUser_email);
                    follow_button.setBackgroundResource(R.drawable.following_button);
                    follow_button.setText("Following");
                    Util.toast(getActivity(), "Now you are Following");

                    loginedUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            logined_email = dataSnapshot.getValue().toString();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    searchedUser.child("followers").child(firebase.getAuth().getUid()).setValue(logined_email);
                }
                else if(follow_button.getText().toString().matches("Following")){

                    loginedUser.child("following").child(searchedUserString).setValue(null);
                    follow_button.setText("Follow");
                    follow_button.setBackgroundResource(R.drawable.follow_button);
                    Util.toast(getActivity(), "Now you are not Following");

                    searchedUser.child("followers").child(firebase.getAuth().getUid()).setValue(null);
                    }
                }
            });

    }

    private void Home(){
        View home = (View)Root.findViewById(R.id.friend_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        follow_button.setText("Follow");
        follow_button.setBackgroundResource(R.drawable.follow_button);
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
//paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawOval(new RectF(0, 0, targetWidth, targetHeight), paint) ;
//paint.setColor(Color.TRANSPARENT);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }
}
