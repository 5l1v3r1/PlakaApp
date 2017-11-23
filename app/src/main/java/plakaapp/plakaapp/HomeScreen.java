package plakaapp.plakaapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

    //menu
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    public Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String kuladi = intent.getStringExtra("K_Adi");//kullanıcı adı
        String id = intent.getStringExtra("ID"); //kullanıcı idsi

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        //başlangıç sayfası için
        fragment = new fragment_home();
        final FragmentTransaction transactionb = fragmentManager.beginTransaction();
        transactionb.replace(R.id.main_container, fragment).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment = new fragment_home();
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        fragment = new fragment_home();
                        break;
                    case R.id.navigation_addtext:
                        fragment = new fragment_hizliyazi();
                        break;
                    case R.id.navigation_search:
                        fragment = new fragment_arama();
                        break;
                    case R.id.navigation_notification:
                        fragment = new fragment_bildirim();
                        break;
                    case R.id.navigation_profil:
                        fragment = new fragment_profil();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }
    //menu
}
