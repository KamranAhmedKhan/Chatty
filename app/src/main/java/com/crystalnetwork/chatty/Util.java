package com.crystalnetwork.chatty;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by NM on 8/9/2015.
 */
public class Util extends Activity {
    public static void toast(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
}
