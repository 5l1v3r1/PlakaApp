package plakaapp.plakaapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simulakra on 28.10.2017.
 */

public class tab_turler extends Activity {
    public JSONArray cinsler,turler;
    boolean valueChanged=false;
    boolean userPulled=false;
    int selectedIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_turler);
        CinsSpinnerDoldur();
        ListeDoldur();
        ButtonListenEvent();
        CreateControlListenner();
    }

    private void CreateControlListenner(){
        ((EditText) findViewById(R.id.turText)).addTextChangedListener(new TextWatcher() {
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
        ((Spinner) findViewById(R.id.cinsSpin)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
                ListeDoldur();
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

    private void ResetControls() {
        ((EditText) findViewById(R.id.turText)).setText("");
        valueChanged = false;
        userPulled=false;
    }

    private String ReturnCinsAdi(String ID)    {
        String temp="";
        try{
        for(int i=0; i<cinsler.length();i++)
        {
            if(cinsler.getJSONObject(i).getJSONObject("message").getString("ID")==ID)
                temp=cinsler.getJSONObject(i).getJSONObject("message").getString("AracCins");
        }}catch (Exception e){}
        return temp;
    }
    private String ReturnCinsID()
    {
        String temp="";
        Spinner cinsSpin = (Spinner) findViewById(R.id.cinsSpin);
        try{
            for(int i=0; i<cinsler.length();i++)
            {
                if(cinsSpin.getSelectedItem().toString().equals(cinsler.getJSONObject(i).getJSONObject("message").getString("AracCins")))
                {temp=cinsler.getJSONObject(i).getJSONObject("message").getString("ID");
                    return temp;}
            }}catch (Exception e){}
        return temp;
    }

    private void ListeDoldur() {
        final ListView listemiz = (ListView) findViewById(R.id.turListView);
        userPulled=false;
        try{
            turler=new JSONArray(new JSONtask().execute(Config.TLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            String cinsID=ReturnCinsID();
            for (int i = 0; i < turler.length(); i++) {
                JSONObject jsonChildNode = turler.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("ID");
                String number = jsonChildNode.getJSONObject("message").getString("CinsID");
                String number2 = jsonChildNode.getJSONObject("message").getString("TurAdi");
                String outPut = name + "-" + number2;
                if(cinsID == number)
                {
                    itemList.add(createListItem("Üyeler", outPut));
                }
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_turler.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
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
                    EditText turAdi = (EditText) findViewById(R.id.turText);
                    turAdi.setText(String.valueOf(listemiz.getItemAtPosition(position)));

                    valueChanged=false;

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }
    private void CinsSpinnerDoldur()    {
        try {
            cinsler = new JSONArray(new JSONtask().execute(Config.CLISTELE_URL).get());
            Spinner cinsSpin = (Spinner) findViewById(R.id.cinsSpin);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < cinsler.length(); i++) {
                JSONObject jsonSoru = cinsler.getJSONObject(i);

                String cinsMetni=jsonSoru.getJSONObject("message").getString("AracCins");
                result.add(cinsMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_turler.this, android.R.layout.simple_spinner_dropdown_item, result);
            cinsSpin.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private void ButtonListenEvent() {
        Button btn = (Button) findViewById(R.id.tur_bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPulled==true && valueChanged==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
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
                                        EditText turAdi = (EditText) findViewById(R.id.turText);

                                        if (turAdi.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                                            builder.setMessage("Tür adı metni boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Tekle_URL(ReturnCinsID(),
                                                        URLEncoder.encode(turAdi.getText().toString(),"utf-8"))).get());

                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }

                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("Yeni tür eklemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.tur_bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("Güncellenecek bir tür seçmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else if(userPulled==true && valueChanged==false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("Seçilen tür değerini değiştirmediniz")
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
                                        JSONObject jsonChildNode = turler.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText turAdi = (EditText) findViewById(R.id.turText);

                                        if(turAdi.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                                            builder.setMessage("Tür adı boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }
                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Tguncelle_URL(jsonChildNode.getString("ID"),
                                                        ReturnCinsID()
                                                        ,URLEncoder.encode(turAdi.getText().toString(),"utf-8"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("Türü güncellemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.tur_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("Silmek üzere hiçbir cins seçmediniz").setPositiveButton("Tamam", null).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = turler.getJSONObject(selectedIndex).getJSONObject("message");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Tsil_URL(jsonChildNode.getString("ID"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                                        builder.setMessage(e.getLocalizedMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    String turadi="";
                    try{
                        turadi = turler.getJSONObject(selectedIndex).getJSONObject("message").getString("TurAdi");
                    }catch (Exception e){}
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
                    builder.setMessage("\""+turadi + "\"\n\n tür adını silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
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
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_turler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
