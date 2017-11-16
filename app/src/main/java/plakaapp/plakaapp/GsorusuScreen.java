package plakaapp.plakaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cumen on 30.10.2017.
 */

public class GsorusuScreen extends AppCompatActivity {

    String gsid,gsadmin,gsadi,gsrep,gsmail,gssoru,gscevap,gspassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gsorusu_screen_activity);

        SharedPreferences sharedPref = GsorusuScreen.this.getPreferences(Context.MODE_PRIVATE);
        int denemeSayisi = sharedPref.getInt("denemeSayisi", 0);
        if(denemeSayisi>=3)
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            SharedPreferences sharedPref = GsorusuScreen.this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("denemeSayisi", 0);
                            editor.commit();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            Toast.makeText(getApplicationContext(), "Şifre değiştirme işlemin BLOKLANDI!.", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
            builder.setMessage("DEBUG MODE\n\nBu kullanıcı BLOKLANDI.\nBlok kaldırmak ister misiniz?").setPositiveButton("Evet Kaldır", dialogClickListener)
                    .setNegativeButton("Hayır Çık", dialogClickListener).show();
        }

        Intent intent = getIntent();
        gsid = intent.getStringExtra("K_id"); //kullanıcı idsi
        gsadmin = intent.getStringExtra("K_admin"); //kullanıcı admin
        gsadi = intent.getStringExtra("K_adi"); //kullanıcı ad
        gsrep = intent.getStringExtra("K_rep"); //kullanıcı Rep
        gsmail = intent.getStringExtra("K_email"); //kullanıcı Mail
        gssoru = intent.getStringExtra("K_soru"); //kullanıcı soru
        gscevap = intent.getStringExtra("K_cevap");//kullanıcı cevap

        final TextView tv_gsoru = (TextView) findViewById(R.id.tv_gsoru);
        final Button btn_gs_kontrol = (Button) findViewById(R.id.gs_kontrol);
        final EditText et_password = (EditText) findViewById(R.id.gs_password);
        final EditText et_cevap = (EditText) findViewById(R.id.gs_cevap);


        String soru = sorugetir(gssoru);

        tv_gsoru.setText(soru);

        btn_gs_kontrol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(gscevap.toString().equals(et_cevap.getText().toString())){
                    if(Validation.password(et_password.getText().toString())){

                        try {
                            gspassword = et_password.getText().toString();
                            String url = Config.Kguncelle_URL(gsid,gsadmin,gsadi,gspassword,gsrep,gsmail,gssoru,gscevap);

                            JSONObject temp = new JSONObject(new JSONtask().execute(url).get());
                            if(JsonErrorCheck(temp)){
                                SharedPreferences sharedPref = GsorusuScreen.this.getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("denemeSayisi", 0);
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Şifreniniz başarıyla değiştirilmiştir.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
                            builder.setMessage(e.getMessage())
                                    .setNegativeButton("Tamam ahmet", null)
                                    .create()
                                    .show();
                        }

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
                        builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                "\nEn az bir büyük harf içermelidir." +
                                "\nEn az bir küçük harf içermelidir."+
                                "\nEn az bir rakam harf içermelidir." +
                                "\nEn az bir '@$^&+=' karakter içermelidir.")
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                    }
                }
                else if(et_cevap.getText().length()>20)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
                    builder.setMessage("Cevap Limiti Aşıldı (20 karakter)")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
                    builder.setMessage("Cevap Doğru Değil")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();

                    SharedPreferences sharedPref = GsorusuScreen.this.getPreferences(Context.MODE_PRIVATE);
                    int denemeSayisi = sharedPref.getInt("denemeSayisi", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("denemeSayisi", denemeSayisi + 1);
                    editor.commit();
                    if((denemeSayisi+1)>=3)
                    {
                        Toast.makeText(getApplicationContext(), "Şifre değiştirme işlemin BLOKLANDI!.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Yanlış cevap denemesi: "+(denemeSayisi+1), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private String sorugetir(String soru){

        String kul_soru = "Soru Bulunamadı";

        try{
            JSONArray sorular=new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            int sayac = 0;
            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonChildNode = sorular.getJSONObject(i);
                String soruid = jsonChildNode.getJSONObject("message").getString("ID");
                if(soruid.equals(String.valueOf(soru.toString()))){

                    kul_soru = jsonChildNode.getJSONObject("message").getString("SoruMetin");
                }
            }

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

        return kul_soru;
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
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
                Toast.makeText(getApplicationContext(), "Kullanıcı Adı veya Email Adresi Aynı Olan Bir Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(GsorusuScreen.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
