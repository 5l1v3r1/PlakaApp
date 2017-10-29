package plakaapp.plakaapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;

/**
 * Created by cumen on 26.10.2017.
 */

public class admin_screen extends TabActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_screen_activity);


        Intent intent = getIntent();
        String adminid = intent.getStringExtra("ID"); //admin idsi

        TabHost tabh = (TabHost)findViewById(android.R.id.tabhost);

        TabSpec tab1 = tabh.newTabSpec("tab menü üyeler");
        tab1.setIndicator("Üyeler");
        tab1.setContent(new Intent(admin_screen.this,tab_uyeler.class).putExtra("IDD",adminid));
        tabh.addTab(tab1);

        TabSpec tab2 = tabh.newTabSpec("tab menü sorular");
        tab2.setIndicator("Sorular");
        tab2.setContent(new Intent(this,tab_sorular.class));
        tabh.addTab(tab2);

        TabSpec tab3 = tabh.newTabSpec("tab menü plakalar");
        tab3.setIndicator("Plakalar");
        tab3.setContent(new Intent(this,tab_plakalar.class));
        tabh.addTab(tab3);

        TabSpec tab4 = tabh.newTabSpec("tab menü cinsler");
        tab4.setIndicator("Cinsler");
        tab4.setContent(new Intent(this,tab_cinsler.class));
        tabh.addTab(tab4);

        TabSpec tab5 = tabh.newTabSpec("tab menü türler");
        tab5.setIndicator("Türler");
        tab5.setContent(new Intent(this,tab_turler.class));
        tabh.addTab(tab5);
    }
}
