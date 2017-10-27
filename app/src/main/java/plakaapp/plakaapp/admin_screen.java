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

        TabHost tabh = (TabHost)findViewById(android.R.id.tabhost);

        TabSpec tab1 = tabh.newTabSpec("tab menü üyeler");
        tab1.setIndicator("Üye");
        tab1.setContent(new Intent(this,tab_uyeler.class));
        tabh.addTab(tab1);

        TabSpec tab2 = tabh.newTabSpec("tab menü sorular");
        tab2.setIndicator("Soru");
        tab2.setContent(new Intent(this,tab_sorular.class));
        tabh.addTab(tab2);

        TabSpec tab3 = tabh.newTabSpec("tab menü sorular");
        tab3.setIndicator("Plaka");
        tab3.setContent(new Intent(this,tab_plakalar.class));
        tabh.addTab(tab3);

        TabSpec tab4 = tabh.newTabSpec("tab menü sorular");
        tab4.setIndicator("Cins");
        tab4.setContent(new Intent(this,tab_cinsler.class));
        tabh.addTab(tab4);

        TabSpec tab5 = tabh.newTabSpec("tab menü sorular");
        tab5.setIndicator("Tür");
        tab5.setContent(new Intent(this,tab_turler.class));
        tabh.addTab(tab5);
    }
}
