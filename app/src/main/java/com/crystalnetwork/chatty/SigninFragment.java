package com.crystalnetwork.chatty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SigninFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SigninFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SigninFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SigninFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static SigninFragment newInstance(String param1, String param2) {
//        SigninFragment fragment = new SigninFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    Firebase firebase;
    Firebase firebase_user;
    EditText email;
    EditText password;
    Button signin_up;
    TextView textView;
    TextView signup_Textview;
    View Root;
    Context c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Root = inflater.inflate(R.layout.fragment_signin, container, false);
        Firebase.setAndroidContext(getActivity());
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");
        firebase_user = firebase.child("Users");
        email = (EditText)Root.findViewById(R.id.email);
        password = (EditText)Root.findViewById(R.id.password);
        signin_up = (Button)Root.findViewById(R.id.email_sign_in_button);
        textView = (TextView)Root.findViewById(R.id.textView);
        signup_Textview = (TextView)Root.findViewById(R.id.signup_TextView);
        c = getActivity();

        //Monitoring Firebase Authentication
        if (firebase.getAuth() != null) {
            // user is logged in
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
                }else{
                    signin();
                    signup();
                }

        return Root;
    }


    public void signin() {
                signin_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText("SigningIn Please Wait.....");
                        if (email.getText().toString().matches("")) {
                            email.setError("Field must be filled");
                        } else if (password.getText().toString().matches("")) {
                            password.setError("Field must be filled");
                        } else {
                            //Login
                            firebase.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
//                                    Toast.makeText(c, "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider(), Toast.LENGTH_LONG).show();

                                    //If login successfull Intent for MAIN Activity......
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.container, new HomeFragment())
                                            .commit();
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    // there was an error in login
                                    textView.setText(firebaseError.toString());
                                }
                            });
                        }
                    }
                });
            }

    //Create Account Clickable Text
    public void signup() {
                signup_Textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new Signup_Fragment()).addToBackStack(null)
                                .commit();
                    }
                });
            }


    /**
             * This interface must be implemented by activities that contain this
             * fragment to allow an interaction in this fragment to be communicated
             * to the activity and potentially other fragments contained in that
             * activity.
             * <p/>
             * See the Android Training lesson <a href=
             * "http://developer.android.com/training/basics/fragments/communicating.html"
             * >Communicating with Other Fragments</a> for more information.
             */
    public interface OnFragmentInteractionListener {
                // TODO: Update argument type and name
                public void onFragmentInteraction(Uri uri);
            }

}
