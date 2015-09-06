package com.crystalnetwork.chatty;


import android.content.Intent;
import android.database.Cursor;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.nio.charset.CharsetDecoder;

public class EditProfile extends Fragment {


    private OnFragmentInteractionListener mListener;


    public EditProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    Firebase firebase;
    Firebase loginedUser;
    ImageView imageView;
    ImageButton captureButton;
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    TextView name_editText;
    EditText age_editText;
    EditText location_editText;
    EditText about_editText;
    Button updateButton;
    private View Root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");
        loginedUser = firebase.child("Users").child(firebase.getAuth().getUid());

//        TakeImage();
        captureButton = (ImageButton)Root.findViewById(R.id.imageButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);

            }
        });

        //Update Button Function
        UpdateButton();
        //Back Button Function
        BackButton();

        return Root;
    }

    public void UpdateButton(){
        name_editText = (TextView)Root.findViewById(R.id.name_editText);
        loginedUser.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_editText.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        age_editText = (EditText)Root.findViewById(R.id.age_editText);
        location_editText = (EditText)Root.findViewById(R.id.location_editText);
        about_editText = (EditText)Root.findViewById(R.id.about_editText);

        updateButton = (Button)Root.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (age_editText.getText().toString().matches("")) {
                    age_editText.setError("Fields must be filled");
                } else if (location_editText.getText().toString().matches("")) {
                    location_editText.setError("Fields must be filled");
                } else if (about_editText.getText().toString().matches("")) {
                    about_editText.setError("Fields must be filled");
                } else {
                    loginedUser.child("age").setValue(age_editText.getText().toString());
                    loginedUser.child("location").setValue(location_editText.getText().toString());
                    loginedUser.child("about").setValue(about_editText.getText().toString());
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new DisplayProfileActivityFragment())
                            .commit();
                }
            }
        });


//        Uncomment after complete Capture Image Functionality
//        TakeImage();
    }

    public void BackButton(){
        View back_Profile = (View)Root.findViewById(R.id.back_Profile);
        back_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new DisplayProfileActivityFragment())
                        .commit();
            }
        });
    }

//    public void TakeImage(){
////        //Function to Capture image or Select image from memory and set to image_layout
////        captureButton = (ImageButton)Root.findViewById(R.id.imageButton);
////        image_layout = (RelativeLayout)Root.findViewById(R.id.image_layout);
////
////        captureButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                image_layout.setBackground(R.drawable./*image*/);
////
////            }
////        });
//
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                imageView = (ImageView)Root.findViewById(R.id.imageView);
//                Bitmap bitmap = selectedImageUri;
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}


