package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;

import static plakaapp.plakaapp.Validation.JsonErrorCheck;

/**
 * Created by cumen on 10.12.2017.
 */

public class sub_yaziSikayet extends Activity {

    String yaziID,yazi_ID,nedeni,kul_id;
    JSONArray yazilar,uyeler,iller;
    EditText et_nedeni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_yazisikayet);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        yaziID = "6"; //Todo: buraya 7 yerine activityden gelen yaziid olacak
        yaziDoldur(yaziID);

        //Buton tanımlanıyor
        Button btn_gonder = (Button) findViewById(R.id.btn_s_gonder);


        //Edittext tanımlanıyor
        et_nedeni = (EditText) findViewById(R.id.et_s_nedeni);

        //hizli yazıya sınır koymak
        int maxLength_nedeni = 120;
        InputFilter[] FilterArray_hizliyazi = new InputFilter[1];
        FilterArray_hizliyazi[0] = new InputFilter.LengthFilter(maxLength_nedeni);
        et_nedeni.setFilters(FilterArray_hizliyazi);
        //hizli yazıya sınır koymak

        //Buton olayı
        btn_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    nedeni = et_nedeni.getText().toString();
                    System.out.println("Nedeni = " + nedeni);
                    JSONObject temp = new JSONObject(new JSONtask().execute(
                            Config.Sikayetekle_URL(
                                    URLEncoder.encode(yaziID,"utf-8"),
                                    URLEncoder.encode(nedeni,"utf-8"))).get());
                    if (Validation.JsonErrorCheck(temp).equals("basarili")) {
                        Toast.makeText(getApplicationContext(), "Şikayetiniz Gönderildi", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(), "hata oluştu", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                catch (Exception e){

                }
            }
        });


    }

    protected void yaziDoldur(String yaziID){
        try{

            //tasarım bilgilerini tanımlıyoruz.
            final TextView tv_yazaradi = (TextView) findViewById(R.id.tv_s_yazi_kadi);
            final TextView tv_konumadi = (TextView) findViewById(R.id.tv_s_yazi_konum);
            final TextView tv_yazi = (TextView) findViewById(R.id.tv_s_yazi_yazi);
            final TextView tv_rep = (TextView) findViewById(R.id.tv_s_yazi_rep);
            final TextView tv_tarih = (TextView) findViewById(R.id.tv_s_yazi_tarih);

            //JSON nesneleri çekiliyor.
            yazilar = new JSONArray(new JSONtask().execute(Config.YLISTELE_URL).get());
            uyeler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            iller = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());
            String uye_adi = " ", konumadi = " ";

            //Seçilen yazi bulunuyor
            for (int i = 0; i < yazilar.length(); i++){
                JSONObject jsonChildNode = yazilar.getJSONObject(i);
                yazi_ID = jsonChildNode.getJSONObject("message").getString("ID");

                if(yaziID == yazi_ID.toString()){

                    //Yazi bilgileri çekiliyor
                    String YazarID = jsonChildNode.getJSONObject("message").getString("YazarID");
                    String KonumID = jsonChildNode.getJSONObject("message").getString("KonumID");
                    String Yazi = jsonChildNode.getJSONObject("message").getString("Yazi");
                    String Rep = jsonChildNode.getJSONObject("message").getString("Rep");
                    String YazilmaTarih = jsonChildNode.getJSONObject("message").getString("YazilmaTarih");

                    //YazarID sine göre Yazar isminin çekilmesi
                    for (int j = 0; j < uyeler.length(); j ++){
                        String tahmini_uye_id = uyeler.getJSONObject(j).getJSONObject("message").getString("ID");
                        if(tahmini_uye_id.equals(YazarID)){
                            uye_adi = uyeler.getJSONObject(j).getJSONObject("message").getString("K_Adi");
                            break;
                        }

                    }

                    //KonumID sine göre Konum isminin çekilmesi
                    for (int j = 0; j < iller.length(); j ++){
                        String tahmini_konum_id = iller.getJSONObject(j).getJSONObject("message").getString("il_kodu");
                        if(tahmini_konum_id.equals(YazarID)){
                            konumadi = iller.getJSONObject(j).getJSONObject("message").getString("il_adi");
                            break;
                        }

                    }

                    //tarih göze şık gelecek formata değiştiriliyor
                    YazilmaTarih = YazilmaTarih.substring(0,YazilmaTarih.indexOf('T'))+" "+YazilmaTarih.substring(YazilmaTarih.indexOf('T')+1,YazilmaTarih.indexOf(".000Z"));

                    //eğer rep değeri -25'in altında ise yazı bulanık gözükecektir
                    if(Integer.parseInt(Rep)<=-25)
                    {
                        String temp="";
                        for (int j=0;j<Yazi.length();j++)temp+="$";
                        Yazi=temp;
                    }

                    //Yazı dolduruluyor
                    tv_yazaradi.setText(uye_adi);
                    tv_konumadi.setText(konumadi);
                    tv_yazi.setText(Yazi);
                    tv_rep.setText(Rep);
                    tv_tarih.setText(YazilmaTarih);
                    return;
                }
                else{
                    tv_yazi.setText("Yazi Bulunamadı");
                }
            }
        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(sub_yaziSikayet.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }
}
