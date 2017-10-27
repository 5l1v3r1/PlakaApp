package plakaapp.plakaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi
    }

    public void checkLogin(View v)
    {
        if(false) {

            Toast.makeText(getApplicationContext(), "The text you want to display", Toast.LENGTH_LONG);
        }
        else{
            Intent intent = new Intent(this, admin_screen.class);
            startActivity(intent);
        }
    }

}
