package plakaapp.plakaapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


        // Mail gonderme alanı

        final Button mgonder = (Button) findViewById(R.id.btn_mail_gonder);

        mgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailler[] = {"gonderilecekmail1@gmail.com","gonderilecekmail2@gmail.com"};
                MailGonder.mgonder("Başlık1","içerik yazısı", mailler, HomeScreen.this);
            }
        });

        //mail gonderme alanı




    }

    //menu
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("asd");
                    return true;
                case R.id.navigation_list:
                    mTextMessage.setText("2");
                    return true;
                case R.id.navigation_report:
                    mTextMessage.setText("3");
                    return true;
                case R.id.navigation_activity:
                    mTextMessage.setText("4");
                    return true;
                case R.id.navigation_about:
                    mTextMessage.setText("4");
                    return true;
            }
            return false;
        }

    };
    //menu
}
