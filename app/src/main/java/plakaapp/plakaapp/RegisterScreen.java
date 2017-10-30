package plakaapp.plakaapp;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cumen on 29.10.2017.
 */

public class RegisterScreen extends AppCompatActivity {

    public JSONArray sorular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen_activity);

        SorularSpinnerDoldur();

        final Button btn_kayit = (Button) findViewById(R.id.btn_kayit_ol);
        final EditText Kul_Adi = (EditText) findViewById(R.id.kayitol_kuladi);
        final EditText e_mail = (EditText) findViewById(R.id.kayitol_email);
        final EditText etpass = (EditText) findViewById(R.id.kayitol_password);
        final EditText cevap = (EditText) findViewById(R.id.Kayitol_gcevap);

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validation.userValidate(Kul_Adi.getText().toString())){  //Kullanıcı adı kontrol
                    if(Validation.email(e_mail.getText().toString())){ //Email kontrol
                        if(Validation.password(etpass.getText().toString())){ //password kontrol
                            if(cevap.length()>3 && cevap.length()<50){ //cevap kontrol

                                try{
                                    //ekleme yapılacak
                                    JSONObject temp = new JSONObject(new JSONtask().execute(
                                            Config.Kekle_URL(
                                                    URLEncoder.encode(Kul_Adi.getText().toString(),"utf-8"),
                                                    URLEncoder.encode(etpass.getText().toString(),"utf-8"),
                                                    URLEncoder.encode(e_mail.getText().toString(),"utf-8"),
                                                    URLEncoder.encode(returnSpinnerID(),"utf-8"),
                                                    URLEncoder.encode(cevap.getText().toString(),"utf-8"))).get());
                                    if (JsonErrorCheck(temp)) {
                                        Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                        finish();
                                        return;
                                    }
                                }
                                catch (Exception e){

                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                                builder.setMessage("Cevap 5 ile 50 karakter arasında olmalıdır...")
                                        .setNegativeButton("Tamam", null)
                                        .create()
                                        .show();
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                            builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                    "\nEn az bir büyük harf içermelidir." +
                                    "\nEn az bir küçük harf içermelidir."+
                                    "\nEn az bir rakam harf içermelidir." +
                                    "\nEn az bir özel karakter içermelidir.")
                                    .setNegativeButton("Tamam", null)
                                    .create()
                                    .show();
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                        builder.setMessage("Mail adresi formata uygun değildir.")
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                    builder.setMessage("Kullanıcı adı 5-28 karakter arasında olmalı" +
                            "\nRakam ve harflerden oluşabilir." +
                            "\n sadece '_' ve '-' karakterleri kullanılabilir.' ")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });

    }

    public void SorularSpinnerDoldur() {
        try {
            sorular=new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) findViewById(R.id.kayitol_gsorusu);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);
                String soruMetni=jsonSoru.getJSONObject("message").getString("SoruMetin");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterScreen.this, android.R.layout.simple_spinner_item, result);
            soruListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

    }

    private String returnSpinnerID(){
        Spinner spinner = ((Spinner) findViewById(R.id.kayitol_gsorusu));
        String index = "0";
        try {
            for (int i=0;i<sorular.length();i++){

                if (spinner.getSelectedItem().toString().equals(sorular.getJSONObject(i).getJSONObject("message").getString("SoruMetin"))){
                    index = sorular.getJSONObject(i).getJSONObject("message").getString("ID");
                    return index;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return index;
    }
    private boolean JsonErrorCheck(JSONObject temp) {
        try {
            //JSONObject js1 =temp.getJSONObject(0);
            JSONObject js2=temp.getJSONObject("message");
            String durum=js2.getString("durum");
            if(durum.equals(String.valueOf("basarili")))
                return true;
            if(durum.equals(String.valueOf("99")))
                Toast.makeText(getApplicationContext(), "SQL Hatası alındı", Toast.LENGTH_LONG).show();
            if(durum.equals(String.valueOf("8")))
                Toast.makeText(getApplicationContext(), "Kayıt Bulunamadı", Toast.LENGTH_LONG).show();
            if(durum.equals(String.valueOf("1")))
                Toast.makeText(getApplicationContext(), "Kullanıcı Adı veya Eposyası Aynı Olan Bir Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
