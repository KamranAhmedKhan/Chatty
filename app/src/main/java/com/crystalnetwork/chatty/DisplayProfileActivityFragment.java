package com.crystalnetwork.chatty;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayProfileActivityFragment extends Fragment {

    public DisplayProfileActivityFragment() {
    }
    Firebase firebase;
    Firebase loginedUser;
    ImageView dp;
    private TextView name_textView;
    private TextView email_textView;
    private TextView location_textView;
    private TextView about_textView;
    TextView editProfile;
    private View Root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_display_profile, container, false);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");
        loginedUser = firebase.child("Users").child(firebase.getAuth().getUid());

        //Edit Button function
        EditProfile();
        //Set Texts of Profile after retrieving from Firebase
        setProfileTexts();
        ProgressBar();

        dp=(ImageView)Root.findViewById(R.id.dp);
        //if(firebase.user.image!=null){R.drawable.firebaseString}else{same as R.drawable.user}
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.user_transparent);
        Bitmap b = getRoundedShape(bMap);
        dp.setImageBitmap(b);
        return Root;
    }

    //Note!
    public void setProfileTexts(){
        //Reminder... it should be received form other fragment as a user object... In this fragment just set from object
        name_textView = (TextView)Root.findViewById(R.id.name_textView);
        email_textView = (TextView)Root.findViewById(R.id.email_textView);
        location_textView = (TextView)Root.findViewById(R.id.location_textView);
        about_textView = (TextView)Root.findViewById(R.id.about_textView);
        final TextView noOfPosts = (TextView)Root.findViewById(R.id.noOfPosts);
        final TextView noOfFollowings = (TextView)Root.findViewById(R.id.noOfFollowings);
        final TextView noOfFollowers = (TextView)Root.findViewById(R.id.noOfFollowers);

        loginedUser.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfPosts.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfFollowers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfFollowings.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                location_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        loginedUser.child("about").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                about_textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void EditProfile(){
        editProfile = (TextView)Root.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new EditProfile()).addToBackStack("Display MyProfile")
                        .commit();
            }
        });

        View home = (View)Root.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            }
        });
    }

    public void ProgressBar(){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait....");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);
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
