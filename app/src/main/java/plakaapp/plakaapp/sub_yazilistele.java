package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simulakra on 29.11.2017.
 */

public class sub_yazilistele  extends Activity {
    String P_ID,K_ID;
    JSONArray yazilar,uyeler,iller,gozuken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_yazilistele);

        //intent'ten plaka ve plakanın ID'si çekiliyor
        Intent intent = sub_yazilistele.this.getIntent();
        String plaka = intent.getStringExtra("Plaka");
        P_ID=intent.getStringExtra("PlakaID");
        K_ID=intent.getStringExtra("KisiID");
        //plaka en başta yazdırılıyor
        ((TextView) findViewById(R.id.tv_plaka)).setText(plaka);

        //takip menüsü tiklanilan itemin altinda tanımlaniyor
        takipPopup = new PopupMenu(sub_yazilistele.this, ((TextView) findViewById(R.id.tv_plaka)));
        takipPopup.getMenuInflater().inflate(R.menu.plakaislem, takipPopup.getMenu());
        //plaka yazılarının bulunduğu listview uygun formatla dolduruluyor
        ListeDoldur();
        //takip ediyor ise popup'ta checked edilsin
        TakipKontrol();
    }

    private void TakipKontrol() {
        try {
            JSONArray takipler = new JSONArray(new JSONtask().execute(Config.TaLISTELE_URL).get());
            for (int i = 0; i < takipler.length(); i++) {
                JSONObject jsonChildNode = takipler.getJSONObject(i);
                if(P_ID.equals(jsonChildNode.getJSONObject("message").getString("Plaka_ID"))
                        && K_ID.equals(jsonChildNode.getJSONObject("message").getString("Uye_ID")))
                {
                    takipPopup.getMenu().findItem(R.id.takip).setChecked(true);
                    return;
                }
            }
        } catch (Exception e){}
    }

    //plaka takip popup'u
    PopupMenu takipPopup;
    //plakaya basılınca menü çıkma listener'i
    public void LabelClick(View v){
        //item click eventi için listener oluşturuluyor
        takipPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.isChecked()) {
                    try {
                        Log.d("q1",new JSONtask().execute(Config.Taekle_URL(K_ID,P_ID)).get());
                        takipPopup.getMenu().findItem(R.id.takip).setChecked(false);
                    } catch (Exception e){}
                } else {
                    try {
                        Log.d("q2",new JSONtask().execute(Config.Tasil_URL(K_ID,P_ID)).get());
                        takipPopup.getMenu().findItem(R.id.takip).setChecked(true);
                    } catch (Exception e){}
                }
                return true;
            }
        });

        takipPopup.show();//tıklanınca takip menüsü çıkıyor

    }

    private void ListeDoldur() {
        final ListView listemiz = (ListView) findViewById(R.id.lv_plakalar);
        gozuken=new JSONArray();
        try {
            //gerekli olan diziler api yardımı ile çekiliyor
            yazilar = new JSONArray(new JSONtask().execute(Config.YLISTELE_URL).get());
            uyeler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            iller = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());

            //plaka için bulunan yazıları bulmak için tüm yazılar taranıyor
            List listplakalar = new ArrayList<>();
            for (int i = 0; i < yazilar.length(); i++) {
                JSONObject jsonChildNode = yazilar.getJSONObject(i);

                gozuken.put(jsonChildNode.getJSONObject("message").getString("ID"));
                //seçili yazının bilgileri çekiliyor
                String PlakaID = jsonChildNode.getJSONObject("message").getString("PlakaID");
                String YazarID = jsonChildNode.getJSONObject("message").getString("YazarID");
                String KonumID= jsonChildNode.getJSONObject("message").getString("KonumID");
                String Rep= jsonChildNode.getJSONObject("message").getString("Rep");
                String Yazi= jsonChildNode.getJSONObject("message").getString("Yazi");
                String Tarih= jsonChildNode.getJSONObject("message").getString("YazilmaTarih");

                //yazı plakaya ait ise işlemlere devam ediliyor
                if(PlakaID.equals(P_ID)) {
                    //yazarı ve ili string olarak ID'lerinden çekiliyor
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

                    //tarih göze şık gelecek formata değiştiriliyor
                    Tarih = Tarih.substring(0,Tarih.indexOf('T'))+" "+Tarih.substring(Tarih.indexOf('T')+1,Tarih.indexOf(".000Z"));

                    //tüm yazı ek bilgileri ile birleştiriliyor
                    Yazi+="\n\nKonum: "+KonumID+"\nYazar: "+YazarID+"\nTarih: "+Tarih+"\nRep: "+Rep;

                    //listview'de gösterilmek için diziye ekleniyor
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    //yazi işlem menüsü tiklanilan itemin altinda tanımlaniyor
                    PopupMenu popup = new PopupMenu(sub_yazilistele.this, listemiz.getChildAt(position));
                    popup.getMenuInflater().inflate(R.menu.yaziislem, popup.getMenu());

                    //item click eventi için listener oluşturuluyor
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            try {

                                SharedPreferences sharedPref = sub_yazilistele.this.getPreferences(Context.MODE_PRIVATE);
                                int repGiris = sharedPref.getInt("yaziRep" + gozuken.get(position), 0);

                                if(repGiris!=0)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
                                    builder.setMessage("Bu yazı için bu cihazdan zaten rep girişi yapılmış.")
                                            .setNegativeButton("Tamam", null)
                                            .create()
                                            .show();
                                }
                                else {
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("yaziRep" + gozuken.get(position), 1);
                                    editor.commit();
                                    //// TODO: 03.12.2017 repGiriş
                                    switch (item.getItemId()) {
                                        case R.id.artirep:
                                            AlertDialog.Builder bb1 = new AlertDialog.Builder(sub_yazilistele.this);
                                            bb1.setMessage("Bu yazı için bu cihazdan zaten rep girişi yapılmış.")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            break;
                                        case R.id.eksirep:
                                            AlertDialog.Builder bb2 = new AlertDialog.Builder(sub_yazilistele.this);
                                            bb2.setMessage("Bu yazı için bu cihazdan zaten rep girişi yapılmış.")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            break;
                                    }
                                }
                            }catch (Exception e){}
                            //Toast.makeText(sub_yazilistele.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//tıklanınca yazı işlem menüsü çıkıyor


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
