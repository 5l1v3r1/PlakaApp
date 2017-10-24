package plakaapp.plakaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Typeface;

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
}
