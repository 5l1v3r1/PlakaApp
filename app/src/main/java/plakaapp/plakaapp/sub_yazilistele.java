package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Simulakra on 29.11.2017.
 */

public class sub_yazilistele  extends Activity {
    String P_ID;
    JSONArray yazilar,uyeler,iller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_yazilistele);

        Intent intent = sub_yazilistele.this.getIntent();
        String plaka = intent.getStringExtra("Plaka");
        P_ID=intent.getStringExtra("PlakaID");
        ((TextView) findViewById(R.id.tv_plaka)).setText(plaka);

        ListeDoldur();
    }

    private void ListeDoldur() {
        ListView listemiz = (ListView) findViewById(R.id.lv_plakalar);
        try {
            yazilar = new JSONArray(new JSONtask().execute(Config.YLISTELE_URL).get());
            uyeler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            iller = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());

            List listplakalar = new ArrayList<>();
            for (int i = 0; i < yazilar.length(); i++) {
                JSONObject jsonChildNode = yazilar.getJSONObject(i);

                String PlakaID = jsonChildNode.getJSONObject("message").getString("PlakaID");
                String YazarID = jsonChildNode.getJSONObject("message").getString("YazarID");
                String KonumID= jsonChildNode.getJSONObject("message").getString("KonumID");
                String Rep= jsonChildNode.getJSONObject("message").getString("Rep");
                String Yazi= jsonChildNode.getJSONObject("message").getString("Yazi");
                String Tarih= jsonChildNode.getJSONObject("message").getString("YazilmaTarih");

                if(PlakaID.equals(P_ID)) {
                    for (int j = 0; j < uyeler.length(); j++) {
                        if (uyeler.getJSONObject(j).getJSONObject("message").getString("ID") == YazarID) {
                            YazarID = uyeler.getJSONObject(j).getJSONObject("message").getString("K_Adi");
                            break;
                        }
                    }
                    for (int j = 0; j < iller.length(); j++) {
                        if (iller.getJSONObject(j).getJSONObject("message").getString("il_kodu") == KonumID) {
                            KonumID = iller.getJSONObject(j).getJSONObject("message").getString("il_adi");
                            break;
                        }
                    }

                    Tarih = Tarih.substring(0,Tarih.indexOf('T'))+" "+Tarih.substring(Tarih.indexOf('T')+1,Tarih.indexOf(".000Z"));

                    Yazi+="\n\nKonum: "+KonumID+"\nYazar: "+YazarID+"\nTarih: "+Tarih+"\nRep: "+Rep;

                    listplakalar.add(new Plakalar(Yazi));
                }
            }

            ListAdapter adapter = new ListAdapter(sub_yazilistele.this, listplakalar);
            listemiz.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //// TODO: 26.11.2017 Yazı işlemleri

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }
}
