package plakaapp.plakaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by cumen on 29.10.2017.
 */

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);


        final TextView tvKulAdi = (TextView) findViewById(R.id.tv_Kul_Adi);

        Intent intent = getIntent();
        String kuladi = intent.getStringExtra("K_Adi");//kullanıcı adı
        String id = intent.getStringExtra("ID"); //kullanıcı idsi

        tvKulAdi.setText(kuladi);

    }
}
