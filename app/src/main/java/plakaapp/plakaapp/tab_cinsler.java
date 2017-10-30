package plakaapp.plakaapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class tab_cinsler extends Activity {
    public JSONArray cinsler;
    boolean valueChanged=false;
    boolean userPulled=false;
    int selectedIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_cinsler);
        ListeDoldur();
        ButtonListenEvent();
        CreateControlListenner();
    }

    private void CreateControlListenner(){
        ((EditText) findViewById(R.id.cinsText)).addTextChangedListener(new TextWatcher() {
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
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void ResetControls() {
        ((EditText) findViewById(R.id.cinsText)).setText("");
        valueChanged = false;
        userPulled=false;
    }

    private void ListeDoldur() {
        ListView listemiz = (ListView) findViewById(R.id.cinsListView);
        userPulled=false;
        try{
            cinsler=new JSONArray(new JSONtask().execute(Config.CLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < cinsler.length(); i++) {
                JSONObject jsonChildNode = cinsler.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("ID");
                String number = jsonChildNode.getJSONObject("message").getString("AracCins");
                String outPut = name + "-" + number;
                itemList.add(createListItem("Üyeler", outPut));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_cinsler.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
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
                    JSONObject jsonChildNode = cinsler.getJSONObject(position).getJSONObject("message");
                    EditText k_adi = (EditText) findViewById(R.id.cinsText);

                    k_adi.setText(jsonChildNode.getString("AracCins"));

                    valueChanged=false;

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }

    private void ButtonListenEvent() {
        Button btn = (Button) findViewById(R.id.cins_bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPulled==true && valueChanged==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
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
                                        EditText cinsadi = (EditText) findViewById(R.id.cinsText);

                                        if (cinsadi.getText().toString().isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                                            builder.setMessage("Cins adı metni boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Cekle_URL(
                                                        URLEncoder.encode(cinsadi.getText().toString(),"utf-8"))).get());

                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }

                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("Yeni cins eklemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.cins_bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("Güncellenecek bir cins seçmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else if(userPulled==true && valueChanged==false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("Seçilen cins değerini değiştirmediniz")
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
                                        JSONObject jsonChildNode = cinsler.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText cinsadi = (EditText) findViewById(R.id.cinsText);

                                        if(cinsadi.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                                            builder.setMessage("cins metni boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Cguncelle_URL(jsonChildNode.getString("ID")
                                                        ,URLEncoder.encode(cinsadi.getText().toString(),"utf-8"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("Cinsi güncellemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.cins_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("Silmek üzere hiçbir cins seçmediniz").setPositiveButton("Tamam", null).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = cinsler.getJSONObject(selectedIndex).getJSONObject("message");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Csil_URL(jsonChildNode.getString("ID"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                                        builder.setMessage(e.getLocalizedMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    String cinsadi="";
                    try{
                        cinsadi = cinsler.getJSONObject(selectedIndex).getJSONObject("message").getString("AracCins");
                    }catch (Exception e){}
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
                    builder.setMessage("\""+cinsadi + "\"\n\n cins adını silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
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
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_cinsler.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
