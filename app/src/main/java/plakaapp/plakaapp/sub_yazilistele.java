package plakaapp.plakaapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
    String P_ID,K_ID,plaka;
    JSONArray yazilar,uyeler,iller,gozuken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_yazilistele);

        //intent'ten plaka ve plakanın ID'si çekiliyor
        Intent intent = sub_yazilistele.this.getIntent();
        plaka = intent.getStringExtra("Plaka");
        P_ID=intent.getStringExtra("PlakaID");
        K_ID=intent.getStringExtra("KisiID");
        //plaka en başta yazdırılıyor
        ((TextView) findViewById(R.id.tv_plaka)).setText(plaka);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //plaka menüsü tiklanilan itemin altinda tanımlaniyor
        takipPopup = new PopupMenu(sub_yazilistele.this, ((TextView) findViewById(R.id.tv_plaka)));
        takipPopup.getMenuInflater().inflate(R.menu.plakaislem, takipPopup.getMenu());

        //plaka yazılarının bulunduğu listview uygun formatla dolduruluyor
        ListeDoldur();
        //takip ediyor ise popup'ta checked edilsin
        TakipKontrol();
        setResult(Activity.RESULT_OK,new Intent());
    }

    private void TakipKontrol() {
        try {
            JSONArray takipler = new JSONArray(new JSONtask().execute(Config.TaLISTELE_URL).get());
            for (int i = 0; i < takipler.length(); i++) {
                JSONObject jsonChildNode = takipler.getJSONObject(i);
                if(P_ID.equals(jsonChildNode.getJSONObject("message").getString("Plaka_ID"))
                        && K_ID.equals(jsonChildNode.getJSONObject("message").getString("Uye_ID")))
                {
                    //takipPopup.getMenu().findItem(R.id.takip).setChecked(true);
                    ((CheckBox)findViewById(R.id.checkBox)).setChecked(true);
                    return;
                }
            }
        } catch (Exception e){}
    }

    //plaka takip popup'u
    PopupMenu takipPopup;

    public void TakipClick(View v) {
        //takip işaretli ise takip ettiriyor, değil ise takipten çıkartıyor
        if (((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
            try {
                Log.d("TaEkle", new JSONtask().execute(Config.Taekle_URL(K_ID, P_ID)).get());
                //((CheckBox) findViewById(R.id.checkBox)).setChecked(false);
                setResult(Activity.RESULT_OK,new Intent());
            } catch (Exception e) {
            }
        } else {
            try {
                Log.d("TaSil",new JSONtask().execute(Config.Tasil_URL(K_ID, P_ID)).get());
                //((CheckBox) findViewById(R.id.checkBox)).setChecked(true);
                setResult(Activity.RESULT_OK,new Intent());
            } catch (Exception e) {
            }
        }
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


                //seçili yazının bilgileri çekiliyor
                String PlakaID = jsonChildNode.getJSONObject("message").getString("PlakaID");
                String YazarID = jsonChildNode.getJSONObject("message").getString("YazarID");
                String KonumID= jsonChildNode.getJSONObject("message").getString("KonumID");
                String Rep= jsonChildNode.getJSONObject("message").getString("Rep");
                String Yazi= jsonChildNode.getJSONObject("message").getString("Yazi");
                String Tarih= jsonChildNode.getJSONObject("message").getString("YazilmaTarih");

                //yazı plakaya ait ise işlemlere devam ediliyor
                if(PlakaID.equals(P_ID)) {
                    gozuken.put(jsonChildNode.getJSONObject("message").getString("ID"));
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

                    //eğer rep değeri -25'in altında ise yazı bulanık gözükecektir
                    if(Integer.parseInt(Rep)<=-25)
                    {
                        String temp="";
                        for (int j=0;j<Yazi.length();j++)temp+="$";
                        Yazi=temp;
                    }

                    //listview'de gösterilmek için diziye ekleniyor
                    listplakalar.add(new Yazilar(YazarID,KonumID,Yazi,Rep,Tarih));
                }
            }

            ListAdapter adapter = new ListAdapter(sub_yazilistele.this, listplakalar, "yazi");
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
                    PopupMenu popup = new PopupMenu(sub_yazilistele.this , listemiz);
                    popup.getMenuInflater().inflate(R.menu.yaziislem, popup.getMenu());

                    //yazının sahibi giriş yapan kişi olmayanlar için
                    //"yazımı sil" seçeneğinin gözükmemesi
                    String YaziID=YaziIDDondur(position);
                    if(!K_ID.equals(YaziID))
                    {
                        popup.getMenu().findItem(R.id.yazisil).setVisible(false);
                    }
                    else
                    {
                        //yazının sahibi ise rep verememesi
                        popup.getMenu().findItem(R.id.artirep).setVisible(false);
                        popup.getMenu().findItem(R.id.eksirep).setVisible(false);
                        //ve kendisini görüntüleyememesi
                        popup.getMenu().findItem(R.id.kullaniciGoruntule).setVisible(false);
                        //ve kendi yazısını şikayet edememesi
                        popup.getMenu().findItem(R.id.sikayet).setVisible(false);
                    }

                    //item click eventi için listener oluşturuluyor
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            //eğer tıklanılan eleman kullanıcı görüntüleme ise
                            //...alttaki işlemlere gitmesini engelleyen ve işlemler yapan kod öbeği
                            if(item.getItemId() == R.id.kullaniciGoruntule)
                            {
                                //şikayet etme penceresine yönlendirmek
                                Intent intent = new Intent(sub_yazilistele.this, sub_kullanicigoruntule.class);
                                intent.putExtra("KisiID",YaziIDDondur(position));
                                startActivity(intent);
                                //alttaki kod öbeklerine gitmesini engellemek
                                return true;
                            }

                            if(item.getItemId() == R.id.sikayet) {
                                try {
                                    //kullanıcı görüntüleme penceresine yönlendirmek
                                    Intent intent = new Intent(sub_yazilistele.this, sub_yaziSikayet.class);
                                    intent.putExtra("YaziID", String.valueOf(gozuken.get(position)));
                                    startActivity(intent);
                                } catch (Exception e) {
                                }

                                //alttaki kod öbeklerine gitmesini engellemek
                                return true;
                            }

                            if(item.getItemId() == R.id.yazisil)
                            {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                try {
                                                    //yazının dizi üzerindeki index'i hesaplanıyor
                                                    int index = 0;
                                                    for (int i = 0; i < yazilar.length(); i++) {
                                                        if (yazilar.getJSONObject(i).getJSONObject("message").getString("ID").equals(String.valueOf(gozuken.get(position)))) {
                                                            index = i;
                                                            break;
                                                        }
                                                    }

                                                    //yazılar dizisinden tüm bilgiler çekiliyor
                                                    //update api'si tüm bilgileri istediği için hepsi yollanıyor
                                                    //rep değeri switch koşullandırması ile 1 artıyor veya eksiliyor
                                                    JSONObject jsTemp = yazilar.getJSONObject(index).getJSONObject("message");
                                                    new JSONtask().execute(Config.Ysil_URL(jsTemp.getString("ID"))).get();
                                                    Toast.makeText(sub_yazilistele.this, "Yazınız Silindi", Toast.LENGTH_LONG).show();

                                                    ListeDoldur();
                                                }catch (Exception e){}

                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
                                builder.setMessage("Yazınızı silmek istediğinize emin misiniz?")
                                        .setPositiveButton("Evet", dialogClickListener)
                                        .setNegativeButton("Hayır", dialogClickListener).show();
                                //alttaki kod öbeklerine gitmesini engellemek
                                return true;
                            }

                            try {
                                //bu cihazdan bu yazı için rep artışı yapılıp yapılmadığı kontrol ediliyor
                                SharedPreferences sharedPref = sub_yazilistele.this.getPreferences(Context.MODE_PRIVATE);
                                int repGiris = sharedPref.getInt("yaziRep" + gozuken.get(position), 0);

                                //eğer bu cihazdan bu yazıya rep verilmiş ise hiçbir işlem yapmadan bildiriyor
                                if(repGiris!=0)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
                                    builder.setMessage("Bu yazı için bu cihazdan zaten rep girişi yapılmış.\n(Sıfırlandı)")
                                            .setNegativeButton("Tamam", null)
                                            .create()
                                            .show();
                                    //Sırf DEBUG amaçlı bildirdikten sonra sıfırlıyor
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("yaziRep" + gozuken.get(position), 0);
                                    editor.commit();
                                }
                                else {
                                    //eğer giriş olmadı ise giriş oldu bilgisi cihaza işleniyor
                                    String yaziID=String.valueOf(gozuken.get(position));
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("yaziRep" + yaziID, 1);
                                    editor.commit();

                                    //yazının dizi üzerindeki index'i hesaplanıyor
                                    int index=0;
                                    for(int i=0;i<yazilar.length();i++)
                                    {
                                        if(yazilar.getJSONObject(i).getJSONObject("message").getString("ID").equals(yaziID))
                                        {
                                            index=i;
                                            break;
                                        }
                                    }

                                    //yazılar dizisinden tüm bilgiler çekiliyor
                                    //update api'si tüm bilgileri istediği için hepsi yollanıyor
                                    //rep değeri switch koşullandırması ile 1 artıyor veya eksiliyor
                                    JSONObject jsTemp=yazilar.getJSONObject(index).getJSONObject("message");
                                    int rep=Integer.parseInt(jsTemp.getString("Rep"));
                                    switch (item.getItemId()) {
                                        case R.id.artirep:
                                            //eğer rep değeri -25'in altında ise artmasını engellemek
                                            if(rep<=-25)
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(sub_yazilistele.this);
                                                builder.setMessage("Bu yazı -25 limitinin altında, artması engellenmiştir.")
                                                        .setNegativeButton("Tamam", null)
                                                        .create()
                                                        .show();
                                                break;
                                            }
                                            else {//rep artma işlemi
                                                new JSONtask().execute(Config.Yguncelle_URL(jsTemp.getString("ID"),
                                                        jsTemp.getString("PlakaID"),
                                                        jsTemp.getString("YazarID"),
                                                        jsTemp.getString("KonumID"),
                                                        jsTemp.getString("Yazi"),
                                                        String.valueOf(rep + 1))).get();
                                                YazarRepDegistir(jsTemp.getString("YazarID"),true);
                                                Toast.makeText(sub_yazilistele.this, "Yazi rep'i arttırıldı", Toast.LENGTH_SHORT).show();
                                                ListeDoldur();
                                                break;
                                            }
                                        case R.id.eksirep:
                                            //rep azaltma işlemi
                                            new JSONtask().execute(Config.Yguncelle_URL(jsTemp.getString("ID"),
                                                    jsTemp.getString("PlakaID"),
                                                    jsTemp.getString("YazarID"),
                                                    jsTemp.getString("KonumID"),
                                                    jsTemp.getString("Yazi"),
                                                    String.valueOf(rep-1))).get();
                                            YazarRepDegistir(jsTemp.getString("YazarID"),false);
                                            Toast.makeText(sub_yazilistele.this,"Yazi rep'i azaltıldı",Toast.LENGTH_SHORT).show();
                                            ListeDoldur();
                                            break;
                                    }
                                }
                            }catch (Exception e){
                                Toast.makeText(sub_yazilistele.this,e.toString(),Toast.LENGTH_SHORT).show();
                            }
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

    private String YaziIDDondur(int position) {
        //Yazı üzerindeki yazar ID'sini döndürüyor
        String Y_ID="0";
        int index=0;
        try {
            String yaziID=String.valueOf(gozuken.get(position));
            for (int i = 0; i < yazilar.length(); i++) {
                if (yazilar.getJSONObject(i).getJSONObject("message").getString("ID").equals(yaziID)) {
                    index = i;
                    break;
                }
            }

            JSONObject jsTemp = yazilar.getJSONObject(index).getJSONObject("message");
            Y_ID = jsTemp.getString("YazarID");
        }catch (Exception e){}
        return Y_ID;
    }

    private void YazarRepDegistir(String YazarID, boolean artis)
    {
        try{
        //kişinin dizi üzerindeki index'i hesaplanıyor
        int index=0;
        for(int i=0;i<uyeler.length();i++)
        {
            if(uyeler.getJSONObject(i).getJSONObject("message").getString("ID").equals(YazarID))
            {
                index=i;
                break;
            }
        }

        //üyenin rep değerini gelen değere göre arttırmak veya azaltmak
        JSONObject jsTemp=uyeler.getJSONObject(index).getJSONObject("message");

        int repdeger=Integer.parseInt(jsTemp.getString("K_Rep"))+(artis?1:-1);

        //üye bonus rep değerine sahip ise
            if(Validation.userBonus(YazarID))
                repdeger*=2;

        new JSONtask().execute(Config.Kguncelle_URL(jsTemp.getString("ID"),
                jsTemp.getString("Admin"),
                jsTemp.getString("K_Adi"),
                "~~",
                String.valueOf(repdeger),
                jsTemp.getString("K_Mail"),
                jsTemp.getString("K_Soru"),
                jsTemp.getString("K_Cevap"))).get();
        } catch (Exception e){}
    }
}
