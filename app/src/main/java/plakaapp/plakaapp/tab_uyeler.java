package plakaapp.plakaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simulakra on 27.10.2017.
 */

public class tab_uyeler extends Activity {
    public JSONArray uyeler, sorular;
    boolean valueChanged=false;
    boolean userPulled=false;
    int selectedIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_uyeler);

        SorularSpinnerDoldur();
        ListeDoldur();
        ButtonListenEvent();
        CreateControlListenner();
    }

    private void CreateControlListenner() {
        ((EditText) findViewById(R.id.k_adi)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueChanged=true;
            }
        });
        ((EditText) findViewById(R.id.sifre)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueChanged=true;
            }
        });
        ((EditText) findViewById(R.id.eposta)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueChanged=true;
            }
        });
        ((EditText) findViewById(R.id.rep)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueChanged=true;
            }
        });
        ((Spinner) findViewById(R.id.soru)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        ((EditText) findViewById(R.id.cevap)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valueChanged=true;
            }
        });
        ((CheckBox) findViewById(R.id.chb_admin)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                 valueChanged=true;
             }
         }
        );
    }

    public void SorularSpinnerDoldur() {
        try {
            sorular = new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) findViewById(R.id.soru);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);

                String soruMetni=jsonSoru.getJSONObject("message").getString("SoruMetin");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_uyeler.this, android.R.layout.simple_spinner_dropdown_item, result);
            soruListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void ResetControls() {
        ((EditText) findViewById(R.id.k_adi)).setText("");
        ((EditText) findViewById(R.id.sifre)).setText("");
        ((EditText) findViewById(R.id.eposta)).setText("");
        ((EditText) findViewById(R.id.rep)).setText("0");
        ((Spinner) findViewById(R.id.soru)).setSelection(0);
        ((EditText) findViewById(R.id.cevap)).setText("");
        ((CheckBox) findViewById(R.id.chb_admin)).setChecked(false);
        valueChanged = false;
        userPulled=false;
    }

    private void ListeDoldur() {
        ListView listemiz = (ListView) findViewById(R.id.ListView);
        userPulled=false;
        try{
            uyeler=new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < uyeler.length(); i++) {
                JSONObject jsonChildNode = uyeler.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("ID");
                String number = jsonChildNode.getJSONObject("message").getString("K_Adi");
                String outPut = name + "-" + number;
                itemList.add(createListItem("Üyeler", outPut));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_uyeler.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
        ResetControls();
        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                userPulled=true;
                selectedIndex=position;
            try {
                JSONObject jsonChildNode = uyeler.getJSONObject(position).getJSONObject("message");
                EditText k_adi = (EditText) findViewById(R.id.k_adi);
                EditText sifre = (EditText) findViewById(R.id.sifre);
                EditText eposta = (EditText) findViewById(R.id.eposta);
                EditText rep = (EditText) findViewById(R.id.rep);
                Spinner soruListe = (Spinner) findViewById(R.id.soru);
                EditText cevap = (EditText) findViewById(R.id.cevap);
                CheckBox chb_admin = (CheckBox) findViewById(R.id.chb_admin);

                k_adi.setText(jsonChildNode.getString("K_Adi"));
                sifre.setText("");
                eposta.setText(jsonChildNode.getString("K_Mail"));
                rep.setText(jsonChildNode.getString("K_Rep"));
                cevap.setText(jsonChildNode.getString("K_Cevap"));
                if(jsonChildNode.getString("Admin")=="1") chb_admin.setChecked(true);
                else chb_admin.setChecked(false);

                String soru=jsonChildNode.getString("K_Soru");
                for (int jj=0;jj<sorular.length();jj++)
                {
                    JSONObject jsonSoru = sorular.getJSONObject(jj);

                    String soruID=jsonSoru.getJSONObject("message").getString("ID");
                    if(soruID==soru) {
                        setSpinnerSelect(soruListe, jsonSoru.getJSONObject("message").getString("SoruMetin"));
                        break;
                    }
                }
                valueChanged=false;

            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                builder.setMessage(e.getMessage())
                        .setNegativeButton("Tamam", null)
                        .create()
                        .show();
            }
            }
        });
    }

    private void setSpinnerSelect(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        spinner.setSelection(index);
    }

    private String returnSpinnerID(){
        Spinner spinner = ((Spinner) findViewById(R.id.soru));
        String index = "0";
        try {
        for (int i=0;i<sorular.length();i++){

                if (spinner.getSelectedItem().toString().equals(sorular.getJSONObject(i).getJSONObject("message").getString("SoruMetin"))) {
                    index = sorular.getJSONObject(i).getJSONObject("message").getString("ID");
                    return index;
                }
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return index;
    }

    private void ButtonListenEvent() {
        Button btn = (Button) findViewById(R.id.uye_bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(userPulled==true && valueChanged==false)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                builder.setMessage("Ekleme yapabilmeniz için kontrolleri boşaltıyoruz.").setPositiveButton("Tamam", null).show();
                ResetControls();
            }
            else {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    EditText k_adi = (EditText) findViewById(R.id.k_adi);
                                    EditText sifre = (EditText) findViewById(R.id.sifre);
                                    EditText eposta = (EditText) findViewById(R.id.eposta);
                                    EditText rep = (EditText) findViewById(R.id.rep);
                                    EditText cevap = (EditText) findViewById(R.id.cevap);
                                    CheckBox chb_admin = (CheckBox) findViewById(R.id.chb_admin);

                                    if (k_adi.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Kullanıcı adı boş olamaz.")
                                                .setNegativeButton("Tamam", null).create().show();
                                        return;
                                    }
                                    if (eposta.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("E-Posta boş değer olamaz.")
                                                .setNegativeButton("Tamam", null).create().show();
                                        return;
                                    }
                                    if (cevap.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Güvenlik sorusunun cevabı boş olamaz.")
                                                .setNegativeButton("Tamam", null).create().show();
                                        return;
                                    }
                                    if (sifre.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Şifre boş olamaz.")
                                                .setNegativeButton("Tamam", null).create().show();
                                        return;
                                    }


                                    /*if(!Validation.userValidate(URLEncoder.encode(k_adi.getText().toString(),"utf-8")))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Kullanıcı adı 5-28 karakter arasında olmalı" +
                                                "\nRakam ve harflerden oluşabilir." +
                                                "\n sadece '_' ve '-' karakterleri kullanılabilir.' ")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }
                                    if(!Validation.password(URLEncoder.encode(sifre.getText().toString(),"utf-8")))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                                "\nEn az bir büyük harf içermelidir." +
                                                "\nEn az bir küçük harf içermelidir."+
                                                "\nEn az bir rakam harf içermelidir." +
                                                "\nEn az bir '@$^&+=' karakter içermelidir.")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }
                                    if(!Validation.email(URLEncoder.encode(eposta.getText().toString(),"utf-8")))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Mail adresi formata uygun değildir.")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }
                                    if(URLEncoder.encode(cevap.getText().toString(),"utf-8").length()<5 ||
                                            URLEncoder.encode(cevap.getText().toString(),"utf-8").length()>50)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Cevap 5 ile 50 karakter arasında olmalıdır...")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }*/

                                    if (chb_admin.isChecked()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Yeni bir kullanıcı admin olarak tanımlanamaz,\nAdmin olarak güncellenebilir.")
                                                .setNegativeButton("Tamam", null).create().show();
                                    }
                                    if(rep.getText().toString().isEmpty())rep.setText("0");
                                    if(!rep.getText().toString().equals("0"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage("Kullanıcı rep'i ilk yeni kayıtta eklenemez")
                                                .setNegativeButton("Tamam", null).create().show();
                                    }

                                    JSONObject temp = new JSONObject(new JSONtask().execute(
                                            Config.Kekle_URL(
                                                    URLEncoder.encode(k_adi.getText().toString(),"utf-8"),
                                    URLEncoder.encode(sifre.getText().toString(),"utf-8"),
                                    URLEncoder.encode(eposta.getText().toString(),"utf-8"),
                                    URLEncoder.encode(returnSpinnerID(),"utf-8"),
                                    URLEncoder.encode(cevap.getText().toString(),"utf-8"))).get());
                                    if (JsonErrorCheck(temp)) {
                                        Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                        ListeDoldur();
                                        return;
                                    }

                                } catch (Exception e) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                    builder.setMessage(e.getMessage())
                                            .setNegativeButton("Tamam", null).create().show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                builder.setMessage("Yeni Kullanıcı eklemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }
            }
        });

        btn = (Button) findViewById(R.id.uye_bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                    builder.setMessage("Güncellenecek bir kullanıcı seçmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else if(userPulled==true && valueChanged==false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                    builder.setMessage("Seçilen kullanıcıda hiçbir değeri değiştirmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else
                 {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = uyeler.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText k_adi = (EditText) findViewById(R.id.k_adi);
                                        EditText sifre = (EditText) findViewById(R.id.sifre);
                                        EditText eposta = (EditText) findViewById(R.id.eposta);
                                        EditText rep = (EditText) findViewById(R.id.rep);
                                        EditText cevap = (EditText) findViewById(R.id.cevap);
                                        CheckBox chb_admin = (CheckBox) findViewById(R.id.chb_admin);

                                        String admin="0";
                                        if(chb_admin.isChecked()) admin="1";
                                        String ssTemp="~~";
                                        if(!sifre.getText().toString().isEmpty()) ssTemp=sifre.getText().toString();

                                        if(k_adi.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("kullanıcı adı boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }
                                        if(eposta.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("eposta boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }
                                        if(rep.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("kullanıcı rep'i boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }
                                        if(cevap.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("güvenlik sorusu cevabı boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        /*if(!Validation.userValidate(URLEncoder.encode(k_adi.getText().toString(),"utf-8")))
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("Kullanıcı adı 5-28 karakter arasında olmalı" +
                                                    "\nRakam ve harflerden oluşabilir." +
                                                    "\n sadece '_' ve '-' karakterleri kullanılabilir.' ")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            return;
                                        }
                                        if(!Validation.password(URLEncoder.encode(sifre.getText().toString(),"utf-8")))
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                                    "\nEn az bir büyük harf içermelidir." +
                                                    "\nEn az bir küçük harf içermelidir."+
                                                    "\nEn az bir rakam harf içermelidir." +
                                                    "\nEn az bir '@$^&+=' karakter içermelidir.")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            return;
                                        }
                                        if(!Validation.email(URLEncoder.encode(eposta.getText().toString(),"utf-8")))
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("Mail adresi formata uygun değildir.")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            return;
                                        }
                                        if(URLEncoder.encode(cevap.getText().toString(),"utf-8").length()<5 ||
                                                URLEncoder.encode(cevap.getText().toString(),"utf-8").length()>50)
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                            builder.setMessage("Cevap 5 ile 50 karakter arasında olmalıdır...")
                                                    .setNegativeButton("Tamam", null)
                                                    .create()
                                                    .show();
                                            return;
                                        }*/

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Kguncelle_URL(jsonChildNode.getString("ID"),
                                                        admin,
                                                        URLEncoder.encode(k_adi.getText().toString(),"utf-8")
                                                        ,ssTemp
                                                        ,URLEncoder.encode(rep.getText().toString(),"utf-8")
                                                        ,URLEncoder.encode(eposta.getText().toString(),"utf-8")
                                                        ,returnSpinnerID()
                                                        ,URLEncoder.encode(cevap.getText().toString(),"utf-8"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage(e.getMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                    builder.setMessage("Kaydı güncellemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.uye_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                    builder.setMessage("Silmek üzere hiçbir kullanıcı seçmediniz").setPositiveButton("Tamam", null).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = uyeler.getJSONObject(selectedIndex).getJSONObject("message");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Ksil_URL(jsonChildNode.getString("ID"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                                        builder.setMessage(e.getLocalizedMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    String k_adi="";
                    try{
                        k_adi = uyeler.getJSONObject(selectedIndex).getJSONObject("message").getString("K_Adi");
                    }catch (Exception e){}
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
                    builder.setMessage(k_adi + " kişisini silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });
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
                Toast.makeText(getApplicationContext(), "Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_uyeler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
