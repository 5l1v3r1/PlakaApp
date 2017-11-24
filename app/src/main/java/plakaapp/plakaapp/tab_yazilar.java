package plakaapp.plakaapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simulakra on 21.11.2017.
 */

public class tab_yazilar extends Activity {
    public JSONArray yazilar,uyeler,plaka,iller;
    boolean valueChanged=false;
    boolean userPulled=false;
    int selectedIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_yazilar);

        SpinnerDoldur();
        ListeDoldur();
        ButtonListenEvent();
        CreateControlListenner();
    }

    private void CreateControlListenner() {
        ((EditText) findViewById(R.id.y_tarih)).addTextChangedListener(new TextWatcher() {
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
        ((EditText) findViewById(R.id.y_rep)).addTextChangedListener(new TextWatcher() {
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
        ((EditText) findViewById(R.id.y_yazi)).addTextChangedListener(new TextWatcher() {
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
        ((Spinner) findViewById(R.id.y_yazar)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        ((Spinner) findViewById(R.id.y_plaka)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        ((Spinner) findViewById(R.id.y_konum)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void ListeDoldur() {
        ListView listemiz = (ListView) findViewById(R.id.y_listview);
        userPulled=false;
        try {
            yazilar = new JSONArray(new JSONtask().execute(Config.YLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < yazilar.length(); i++) {
                JSONObject jsonChildNode = yazilar.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("PlakaID");
                String number = jsonChildNode.getJSONObject("message").getString("YazarID");
                String outPut = name + "-" + number;
                itemList.add(createListItem("Üyeler", outPut));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_yazilar.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
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
                    JSONObject jsonChildNode = yazilar.getJSONObject(position).getJSONObject("message");
                    EditText tarih = ((EditText) findViewById(R.id.y_tarih));
                    EditText rep = ((EditText) findViewById(R.id.y_rep));
                    EditText yazi = ((EditText) findViewById(R.id.y_yazi));

                    tarih.setText(jsonChildNode.getString("YazilmaTarih"));
                    rep.setText(jsonChildNode.getString("Rep"));
                    yazi.setText(jsonChildNode.getString("Yazi"));

                    PushSpinnerYazar(jsonChildNode.getString("YazarID"));
                    PushSpinnerPlaka(jsonChildNode.getString("PlakaID"));
                    PushSpinnerKonum(jsonChildNode.getString("KonumID"));

                    valueChanged=false;

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }

    private void PushSpinnerKonum(String ID){
        try{
            if(ID.isEmpty()) return;

            Spinner konum = ((Spinner) findViewById(R.id.y_konum));

            int arrayPos=0;
            for (int i=0;i<iller.length();i++)
            {
                if(iller.getJSONObject(i).getJSONObject("message").getString("il_kodu")==ID)
                {
                    arrayPos=i;
                    break;
                }
            }

            for (int i=0;i<konum.getCount();i++)
            {
                if(konum.getItemAtPosition(i).toString()==
                        iller.getJSONObject(arrayPos).getJSONObject("message").getString("il_adi"))
                {
                    konum.setSelection(i);
                    return;
                }
            }
        }catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private void PushSpinnerPlaka(String ID){
        try{
            Spinner splaka = ((Spinner) findViewById(R.id.y_plaka));

            int arrayPos=0;
            for (int i=0;i<plaka.length();i++)
            {
                if(plaka.getJSONObject(i).getJSONObject("message").getString("ID")==ID)
                {
                    arrayPos=i;
                    break;
                }
            }

            for (int i=0;i<splaka.getCount();i++)
            {
                if(splaka.getItemAtPosition(i).toString()==
                        plaka.getJSONObject(arrayPos).getJSONObject("message").getString("Plaka"))
                {
                    splaka.setSelection(i);
                    return;
                }
            }
        }catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private void PushSpinnerYazar(String ID){
        try{
            Spinner yazar = ((Spinner) findViewById(R.id.y_yazar));

            int arrayPos=0;
            for (int i=0;i<uyeler.length();i++)
            {
                if(uyeler.getJSONObject(i).getJSONObject("message").getString("ID")==ID)
                {
                    arrayPos=i;
                    break;
                }
            }

            for (int i=0;i<yazar.getCount();i++)
            {
                if(yazar.getItemAtPosition(i).toString()==
                        uyeler.getJSONObject(arrayPos).getJSONObject("message").getString("K_Adi"))
                {
                    yazar.setSelection(i);
                    return;
                }
            }
        }catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private String PullSpinnerKonum(){
        try
        {
            Spinner konum = ((Spinner) findViewById(R.id.y_konum));
            for (int i=0;i<iller.length();i++)
            {
                if(iller.getJSONObject(i).getJSONObject("message").getString("il_adi")==
                        konum.getSelectedItem())
                {
                    return iller.getJSONObject(i).getJSONObject("message").getString("il_kodu");
                }
            }
        }catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
        return "";
    }

    private String PullSpinnerPlaka(){
        try
        {
            Spinner splaka = ((Spinner) findViewById(R.id.y_plaka));
            for (int i=0;i<plaka.length();i++)
            {
                if(plaka.getJSONObject(i).getJSONObject("message").getString("Plaka")==
                        splaka.getSelectedItem())
                {
                    return plaka.getJSONObject(i).getJSONObject("message").getString("ID");
                }
            }
        }catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
        return "";
    }

    private String PullSpinnerYazar(){
        try
        {
            Spinner yazar = ((Spinner) findViewById(R.id.y_yazar));
            for (int i=0;i<uyeler.length();i++)
            {
                if(uyeler.getJSONObject(i).getJSONObject("message").getString("K_Adi")==
                        yazar.getSelectedItem())
                {
                    return uyeler.getJSONObject(i).getJSONObject("message").getString("ID");
                }
            }
        }catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
        return "";
    }

    private void ResetControls() {
        ((EditText) findViewById(R.id.y_tarih)).setText("");
        ((EditText) findViewById(R.id.y_rep)).setText("0");
        ((Spinner) findViewById(R.id.y_yazar)).setSelection(0);
        ((Spinner) findViewById(R.id.y_plaka)).setSelection(0);
        ((Spinner) findViewById(R.id.y_konum)).setSelection(0);
        ((EditText) findViewById(R.id.y_yazi)).setText("");
        valueChanged = false;
        userPulled=false;
    }

    private void SpinnerDoldur() {
        try {
            uyeler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            plaka = new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());
            iller = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());
            Spinner uyeSpin = (Spinner) findViewById(R.id.y_yazar);
            Spinner plakaSpin = (Spinner) findViewById(R.id.y_plaka);
            Spinner ilSpin = (Spinner) findViewById(R.id.y_konum);

            List<String> result;
            ArrayAdapter<String> adapter;

            result = new ArrayList<String>();
            for (int i = 0; i < uyeler.length(); i++) {
                JSONObject jsonSoru = uyeler.getJSONObject(i);
                String metin=jsonSoru.getJSONObject("message").getString("K_Adi");
                result.add(metin);
            }
            adapter = new ArrayAdapter<String>(tab_yazilar.this, android.R.layout.simple_spinner_dropdown_item, result);
            uyeSpin.setAdapter(adapter);

            result = new ArrayList<String>();
            for (int i = 0; i < plaka.length(); i++) {
                JSONObject jsonSoru = plaka.getJSONObject(i);
                String metin=jsonSoru.getJSONObject("message").getString("Plaka");
                result.add(metin);
            }
            adapter = new ArrayAdapter<String>(tab_yazilar.this, android.R.layout.simple_spinner_dropdown_item, result);
            plakaSpin.setAdapter(adapter);

            result = new ArrayList<String>();
            for (int i = 0; i < iller.length(); i++) {
                JSONObject jsonSoru = iller.getJSONObject(i);
                String metin=jsonSoru.getJSONObject("message").getString("il_adi");
                result.add(metin);
            }
            adapter = new ArrayAdapter<String>(tab_yazilar.this, android.R.layout.simple_spinner_dropdown_item, result);
            ilSpin.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private void ButtonListenEvent() {
        Button btn = (Button) findViewById(R.id.y_bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPulled==true && valueChanged==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
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
                                        EditText yTarih = (EditText) findViewById(R.id.y_tarih);
                                        EditText yRep = (EditText) findViewById(R.id.y_rep);
                                        EditText yYazi = (EditText) findViewById(R.id.y_yazi);

                                        if (yYazi.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                            builder.setMessage("Yazı boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        if (!yTarih.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                            builder.setMessage("İlk eklemede el ile tarih girilemez, bügünü alır.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                        }

                                        if(yRep.getText().toString().isEmpty())yRep.setText("0");
                                        if(!yRep.getText().toString().equals("0"))
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                            builder.setMessage("Yazı rep'i ilk yeni kayıtta eklenemez")
                                                    .setNegativeButton("Tamam", null).create().show();
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Yekle_URL(
                                                        URLEncoder.encode(PullSpinnerPlaka(),"utf-8"),
                                                        URLEncoder.encode(PullSpinnerYazar(),"utf-8"),
                                                        URLEncoder.encode(PullSpinnerKonum(),"utf-8"),

                                                        URLEncoder.encode(yYazi.getText().toString(),"utf-8")
                                                )).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }

                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Yeni Yazı eklemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.y_bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Güncellenecek bir yazı seçmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else if(userPulled==true && valueChanged==false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Seçilen yazıda hiçbir değeri değiştirmediniz")
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
                                        JSONObject jsonChildNode = yazilar.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText yTarih = (EditText) findViewById(R.id.y_tarih);
                                        EditText yRep = (EditText) findViewById(R.id.y_rep);
                                        EditText yYazi = (EditText) findViewById(R.id.y_yazi);

                                        if (yYazi.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                            builder.setMessage("Yazı boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        if (yTarih.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                            builder.setMessage("Tarih boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                        }
                                        if(yRep.getText().toString().isEmpty())yRep.setText("0");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Yguncelle_URL(jsonChildNode.getString("ID"),
                                                        URLEncoder.encode(PullSpinnerPlaka(),"utf-8"),
                                                        URLEncoder.encode(PullSpinnerYazar(),"utf-8"),
                                                        URLEncoder.encode(PullSpinnerKonum(),"utf-8"),

                                                        URLEncoder.encode(yYazi.getText().toString(),"utf-8"),
                                                        URLEncoder.encode(yRep.getText().toString(),"utf-8"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Kaydı güncellemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.y_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Silmek üzere hiçbir yazı seçmediniz").setPositiveButton("Tamam", null).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = yazilar.getJSONObject(selectedIndex).getJSONObject("message");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Ksil_URL(jsonChildNode.getString("ID"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                                        builder.setMessage(e.getLocalizedMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
                    builder.setMessage("Yazıyı silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
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
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_yazilar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
