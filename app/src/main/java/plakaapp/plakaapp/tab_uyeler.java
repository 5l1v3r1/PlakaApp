package plakaapp.plakaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

/**
 * Created by Simulakra on 27.10.2017.
 */

public class tab_uyeler extends Activity {
    public String[] uyeler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_uyeler);



        final TextView idsi = (TextView) findViewById(R.id.tv_uyeler_kuladi);
        //Admin bilgileri
        Intent inte = getIntent();
        String id = inte.getStringExtra("IDD"); //admin idsi
        idsi.setText("Admin id = " + id);
        idsi.setText("Admin id = " + id);


        //ListeDoldur();
        //ButtonListenEvent();
    }

    private HashMap<String, String> createEmployee(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    public JSONObject jsonVeri;
    private void ListeDoldur() {
        JsonObjectRequest job=new JsonObjectRequest(Request.Method.POST, Config.KLISTELE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialog progressDialog= ProgressDialog.show(tab_uyeler.this, "Okuma İşlemi",
                                "İşleniyor. Lütfen bekleyiniz...", true);
                        ListView listemiz = (ListView) findViewById(R.id.ListView);
                        jsonVeri=response;
                        try {
                            //JSONObject jsonResponse = new JSONObject(jsonString);
                            JSONObject jsonResponse = jsonVeri;
                            JSONArray jsonMainNode = jsonResponse.optJSONArray("user");
                            List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
                            for(int i = 0; i<jsonMainNode.length();i++){
                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                String name = jsonChildNode.optString("ID");
                                String number = jsonChildNode.optString("K_Adi");
                                String outPut = name + "-" +number;
                                employeeList.add(createEmployee("Üyeler", outPut));
                            }
                            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_uyeler.this, employeeList, android.R.layout.simple_list_item_1, new String[] {"Üyeler"}, new int[] {android.R.id.text1});
                            listemiz.setAdapter(simpleAdapter);
                        }
                        catch(JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                //// TODO: 28.10.2017 liste itemine basinca o itemdeki elemani textbox'lara çek
                            }
                        });
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error (Ters giden bişey var)", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        KListele_Request.getInstance(tab_uyeler.this).addToRequestque(job);
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
