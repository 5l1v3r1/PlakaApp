package plakaapp.plakaapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Simulakra on 29.10.2017.
 */

public class JSONtask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params){
        String value="";
        try {

            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            value = bf.readLine();

        }catch (Exception ex){
            System.out.println(ex);
        }
        return value;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }

}