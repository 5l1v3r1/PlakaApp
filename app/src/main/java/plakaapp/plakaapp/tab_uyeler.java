package plakaapp.plakaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_uyeler);

        SorularSpinnerDoldur();
        ListeDoldur();
        ButtonListenEvent();
    }

    private void SorularSpinnerDoldur() {
        try {
            sorular=new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) findViewById(R.id.soru);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);

                String soruMetni=jsonSoru.getJSONObject("message").getString("SoruMetin");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_uyeler.this, android.R.layout.simple_spinner_item, result);
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

    private void ListeDoldur() {
        ListView listemiz = (ListView) findViewById(R.id.ListView);

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

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            //// TODO: 28.10.2017 liste itemine basinca o itemdeki elemani textbox'lara çek
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

    private void ButtonListenEvent()
    {
        Button btn= (Button) findViewById(R.id.bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog= ProgressDialog.show(tab_uyeler.this, "Ekleme İşlemi",
                        "İşleniyor. Lütfen bekleyiniz...", true);
                //// TODO: 28.10.2017 üye Ekleme sistemi
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
            }
        });

        btn= (Button) findViewById(R.id.bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog= ProgressDialog.show(tab_uyeler.this, "Güncelleme İşlemi",
                        "İşleniyor. Lütfen bekleyiniz...", true);
                //// TODO: 28.10.2017 üye Güncelleme sistemi
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
            }
        });

        btn= (Button) findViewById(R.id.bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog= ProgressDialog.show(tab_uyeler.this, "Silme İşlemi",
                        "İşleniyor. Lütfen bekleyiniz...", true);
                //// TODO: 28.10.2017 üye Silme sistemi
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
            }
        });
    }



}
