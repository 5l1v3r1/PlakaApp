package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Simulakra on 09.12.2017.
 */

public class sub_kullanicigoruntule extends Activity {
    String K_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_kullanicigoruntule);

        //intent'ten plaka ve plakanın ID'si çekiliyor
        Intent intent = sub_kullanicigoruntule.this.getIntent();
        K_ID = intent.getStringExtra("KisiID");

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //kullanıcı bilgileri çekilip gerekli yerlere yerleştiriliyor
        KullaniciBilgiCek();
        //geri tuşu için finish işlemi atanıyor
        GeriTusuListener();
    }

    private void GeriTusuListener() {
        Button btn = (Button) findViewById(R.id.et_geri);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }});
    }

    private void KullaniciBilgiCek() {
        try {
            JSONArray kisiler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            for (int i = 0; i < kisiler.length(); i++) {
                JSONObject jsTemp = kisiler.getJSONObject(i).getJSONObject("message");
                //döngüdeki kişi bizim istediğimiz kişi ise bilgileri çekiyor
                if(jsTemp.getString("ID").equals(K_ID))
                {
                    ((TextView) findViewById(R.id.et_profil_kuladi)).setText(jsTemp.getString("K_Adi"));
                    ((TextView) findViewById(R.id.et_profil_email)).setText(jsTemp.getString("K_Mail"));
                    ((TextView) findViewById(R.id.et_profil_parola)).setText(jsTemp.getString("K_Rep"));
                    return;
                }
            }
        }catch (Exception e){}
    }
}
