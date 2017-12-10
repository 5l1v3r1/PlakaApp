package plakaapp.plakaapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simulakra on 10.12.2017.
 */

public class tab_sikayet extends Activity {

    JSONArray sikayetler;
    boolean sPulled;
    int sPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_sikayet);

        SikayetDoldur();
        CreateButtonListener();
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void CreateButtonListener() {
    }

    private void SikayetDoldur() {
        final ListView listemiz = (ListView) findViewById(R.id.s_listview);
        try{
            sikayetler=new JSONArray(new JSONtask().execute(Config.SiLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < sikayetler.length(); i++) {
                JSONObject jsTemp = sikayetler.getJSONObject(i).getJSONObject("message");

                String goster="Şikayet ID'si: "+ jsTemp.getString("ID");
                goster += "\nYazı ID'si: "+jsTemp.getString("YaziID");
                String Tarih=jsTemp.getString("Taih");
                Tarih = Tarih.substring(0,Tarih.indexOf('T'));//+" "+Tarih.substring(Tarih.indexOf('T')+1,Tarih.indexOf(".000Z"));
                goster += "\nTarih: " + Tarih;
                itemList.add(createListItem("Üyeler", goster));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_sikayet.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
        ((TextView)findViewById(R.id.s_yazi)).setText("");
        sPulled=false;
        sPosition=-1;
        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    sPulled=true;
                    sPosition=position;
                    ((TextView)findViewById(R.id.s_yazi))
                            .setText(sikayetler.getJSONObject(sPosition).getJSONObject("message")
                                    .getString("Nedeni"));

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_sikayet.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }
}
