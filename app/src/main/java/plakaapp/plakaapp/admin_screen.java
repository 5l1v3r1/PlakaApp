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

        TabSpec tab1 = tabh.newTabSpec("tab menü 1. seçenek");
        TabSpec tab2 = tabh.newTabSpec("tab menü 2. seçenek");

        tab1.setIndicator("Üyeler");
        tab1.setContent(new Intent(this,tab_uyeler.class));
        tab2.setIndicator("Sorular");
        tab2.setContent(new Intent(this,tab_sorular.class));
        tabh.addTab(tab1); tabh.addTab(tab2);
    }
}
