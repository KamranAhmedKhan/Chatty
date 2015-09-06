package com.crystalnetwork.chatty;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class DisplayProfileActivity extends ActionBarActivity {

    public static ActionBar actionBar;
    Firebase firebase;
    EditText addUser_editText;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://fb-todolist.firebaseio.com/");

        actionBar = getSupportActionBar();
        actionBar.hide();


        // check for Internet status
        FillContainer();

    }

    public void MyProfile(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new DisplayProfileActivityFragment()).addToBackStack("Home")
                .commit();
    }

    public void AddUser(){
        //Create Dialog instance
        final Dialog addDialog = new Dialog(this);
        //Tell the dialog to use custom_adduser_dialogbox.xml as its layout
        addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addDialog.setContentView(R.layout.custom_adduser_dialogbox);
        addDialog.show();

        addUser_editText = (EditText)addDialog.findViewById(R.id.addUser_editText);

        //Publish Button Function
        Button searchButton = (Button)addDialog.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addUser_editText.getText().toString().matches("")) {
                    addUser_editText.setError("Field must be filled");
                } else if (addUser_editText.getText().toString().length() < 7) {/*length should change to 11 for valid password*/
                    addUser_editText.setError("Please enter valid email address");
                } else {
                    firebase.child("UsersList").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue().toString().matches(addUser_editText.getText().toString())) {
                                FriendsProfileFragment.searchedUserString = dataSnapshot.getKey();//Value passed to open Profile by this UID
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, new FriendsProfileFragment()).addToBackStack("Home")
                                        .commit();
                                addDialog.dismiss();

                            } else {
                                TextView error = (TextView) addDialog.findViewById(R.id.error);
                                error.setText("User doesn't exist");
                            }
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
            }
        });

        Button cancelButton = (Button)addDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_search:
                //DialogBox for Add User
                AddUser();
                return true;
            case R.id.action_profile:
                MyProfile();
                return true;
            case R.id.action_Logout:
                //Logout Function here - DialogBox for confirmation of Logout
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Logout");

        // set dialog message
        alertDialogBuilder
                .setMessage("You want to Logout!")
                .setCancelable(false)
                .setPositiveButton("Logout",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        firebase.unauth();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new SigninFragment())
                                .commit();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void FillContainer() {
        // get Internet status
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SigninFragment())
                        .commit();
            }
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("No Internet Connection").setCancelable(false);
        // Set Icon
        alertDialog.setIcon(R.drawable.ic_report_problem);
        // Setting Dialog Message
        alertDialog.setMessage("You don't have internet connection.");
        // Setting OK Button
        alertDialog.setNeutralButton("Try Again!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                FillContainer();
                dialog.cancel();
            }
        });
        // Create it
        AlertDialog internetDialog = alertDialog.create();
        // Showing Alert Message
        internetDialog.show();
    }
}
