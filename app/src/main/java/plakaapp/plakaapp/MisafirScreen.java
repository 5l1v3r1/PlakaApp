package plakaapp.plakaapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * Created by cumen on 29.10.2017.
 */

public class MisafirScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.misafir_screen_activity);


        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //değerleri çekiyoruz.
        final Button sorgula = (Button) findViewById(R.id.btn_misafir_sorgula);
        final EditText plaka = (EditText) findViewById(R.id.et_misafir_pnumarasi);
        final TextView bulunanlar = (TextView) findViewById(R.id.tv_misafir_sonuclar);
        //değerleri çekiyoruz.

        //plakaya yazıya sınır koymak
        int maxLength_hizliyazi = 10;
        InputFilter[] FilterArray_hizliyazi = new InputFilter[1];
        FilterArray_hizliyazi[0] = new InputFilter.LengthFilter(maxLength_hizliyazi);
        plaka.setFilters(FilterArray_hizliyazi);
        //plakaya yazıya sınır koymak


        //Sorgulama yapıldığı zaman
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    final EditText plaka = (EditText) findViewById(R.id.et_misafir_pnumarasi);
                    final TextView bulunanlar = (TextView) findViewById(R.id.tv_misafir_sonuclar);


                    Boolean dogrumu = Validation.plaka(plaka.getText().toString());
                    if(dogrumu == true){
                        //Api için stringleri tanımlıyoruz
                        String strURL = Config.PlakaSorgula_URL(URLEncoder.encode(plaka.getText().toString(),"utf-8"));
                        String deger = null;
                        //Api için stringleri tanımlıyoruz

                        JSONtask jst = new JSONtask();

                        deger = jst.execute(strURL).get();

                        JSONArray jsonResponse = new JSONArray(deger);

                        String hata = Validation.JsonErrorCheck(jsonResponse.getJSONObject(0));

                        JSONObject jsonResponseMessage = jsonResponse.getJSONObject(0).getJSONObject("message");

                        if(hata.equals(String.valueOf("basarili"))) {
                            String yazisayisi = jsonResponseMessage.getString("yazisayisi");
                            if(yazisayisi.equals(String.valueOf("0"))){
                                bulunanlar.setTextColor(getResources().getColor(R.color.red));
                                bulunanlar.setText("Böyle bir plaka veya yazısı bulunmamaktadır...");
                            }else{
                                bulunanlar.setTextColor(getResources().getColor(R.color.yazi));
                                bulunanlar.setText(plaka.getText().toString() + " Plakasının toplam " + yazisayisi + "  adet yazısı bulunmaktadır");
                            }

                        }else if(hata.equals(String.valueOf("8"))){
                            bulunanlar.setTextColor(getResources().getColor(R.color.red));
                            bulunanlar.setText("Kayıt Bulunamadı");

                        }else{
                            //Başka bir sorun varsa
                            AlertDialog.Builder builder = new AlertDialog.Builder(MisafirScreen.this);
                            builder.setMessage(" BİLİNMEDİK BİR SORUN OLUŞTU")
                                    .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                    .create()
                                    .show();
                            plaka.setText("");
                        }
                    }else{
                        //Başka bir sorun varsa
                        AlertDialog.Builder builder = new AlertDialog.Builder(MisafirScreen.this);
                        builder.setMessage("Plaka Uygun Formatta Değil")
                                .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                .create()
                                .show();
                        plaka.setText("");
                    }







                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MisafirScreen.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("TEKRAR DENEYİNİZ1", null)
                            .create()
                            .show();
                    plaka.setText("");

                }
            }
        });
        //Sorgulama yapıldığı zaman

    }
}
