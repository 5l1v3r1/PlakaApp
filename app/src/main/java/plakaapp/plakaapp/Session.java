package plakaapp.plakaapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cumen on 10.12.2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin, String kadi, String ID){
        editor.putBoolean("loggedInmode", logggedin);
        editor.putString("kadi", kadi);
        editor.putString("ID", ID);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

    public String SKadi(){
        return prefs.getString("kadi" , "bos"); //bos default olarak tanımlanan
    }

    public String Sid(){
        return prefs.getString("ID", "11"); //11 default olarak tanımlanan
    }
}
