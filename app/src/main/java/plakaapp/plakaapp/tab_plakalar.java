package plakaapp.plakaapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
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
 * Created by Simulakra on 28.10.2017.
 */


public class tab_plakalar extends Activity {
    public JSONArray plakalar,cinsler,turler;
    boolean valueChanged=false;
    boolean userPulled=false;
    int selectedIndex=0;

    int redValue;
    int greenValue;
    int blueValue;
    ImageView picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_plakalar);

        SpinnerlariDoldur();
        ColorPickerCreate();
        CreateControlListenner();
        ListeDoldur();
        ButtonListenEvent();
    }

    private void ColorPickerCreate() {
        picker=findViewById(R.id.plakaRenkPalet);

        SeekBar redSeekBar= (SeekBar) findViewById(R.id.seekBarRed);
        SeekBar greenSeekBar = (SeekBar) findViewById(R.id.seekBarGreen);
        SeekBar blueSeekBar = (SeekBar) findViewById(R.id.seekBarBlue);

         redValue  = redSeekBar.getProgress();
         greenValue  = greenSeekBar.getProgress();
         blueValue  = blueSeekBar.getProgress();

        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);

        picker.setBackgroundColor(Color.rgb(redValue,greenValue,blueValue));

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                valueChanged=true;
                redValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                valueChanged=true;
                greenValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                valueChanged=true;
                blueValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });
    }

    private void SpinnerlariDoldur(){
        try {
            cinsler = new JSONArray(new JSONtask().execute(Config.CLISTELE_URL).get());
            turler = new JSONArray(new JSONtask().execute(Config.TLISTELE_URL).get());

            Spinner cinsListe = (Spinner) findViewById(R.id.plakaCinsSpin);
            Spinner turListe = (Spinner) findViewById(R.id.plakaTurSpin);

            List<String> result = new ArrayList<String>();
            for (int i = 0; i < cinsler.length(); i++) {
                JSONObject jsoncins = cinsler.getJSONObject(i);

                String soruMetni=jsoncins.getJSONObject("message").getString("AracCins");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_plakalar.this, android.R.layout.simple_spinner_dropdown_item, result);
            cinsListe.setAdapter(adapter);

            String CinsID=PullCinsSpinnerID();
            result = new ArrayList<String>();
            for (int i = 0; i < turler.length(); i++) {
                JSONObject jsontur = turler.getJSONObject(i);

                if(jsontur.getJSONObject("message").getString("CinsID").equals(CinsID)) {
                    String soruMetni = jsontur.getJSONObject("message").getString("TurAdi");
                    result.add(soruMetni);
                }
            }
            adapter = new ArrayAdapter<String>(tab_plakalar.this, android.R.layout.simple_spinner_dropdown_item, result);
            turListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private String PullCinsSpinnerID(){
        Spinner spinner = ((Spinner) findViewById(R.id.plakaCinsSpin));
        String index = "0";
        try {
            for (int i=0;i<cinsler.length();i++){

                if (spinner.getSelectedItem().toString().equals(cinsler.getJSONObject(i).getJSONObject("message").getString("AracCins"))) {
                    index = cinsler.getJSONObject(i).getJSONObject("message").getString("ID");
                    return index;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return index;}

    private String PullTurSpinnerID(){
        Spinner spinner = ((Spinner) findViewById(R.id.plakaTurSpin));
        String index = "0";
        try {
            for (int i=0;i<turler.length();i++){

                if (spinner.getSelectedItem().toString().equals(turler.getJSONObject(i).getJSONObject("message").getString("TurAdi"))) {
                    index = turler.getJSONObject(i).getJSONObject("message").getString("ID");
                    return index;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return index;}

    private void PutCinsSpinner(String ID){
        Spinner spinner = ((Spinner) findViewById(R.id.plakaCinsSpin));
        int index = 0;
        try {
            for (int i = 0; i < cinsler.length(); i++) {
                JSONObject jsoncins = cinsler.getJSONObject(i);
                if(jsoncins.getJSONObject("message").getString("ID").equals(ID)) index = i;
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        spinner.setSelection(index);
    }
    private void PutTurSpinner(String ID){
        Spinner turListe = (Spinner) findViewById(R.id.plakaTurSpin);
        int index=0;
        try{
        String CinsID=PullCinsSpinnerID();
        int sayac=0;
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < turler.length(); i++) {
            JSONObject jsontur = turler.getJSONObject(i);

            if(jsontur.getJSONObject("message").getString("CinsID").equals(CinsID)) {
                String soruMetni = jsontur.getJSONObject("message").getString("TurAdi");
                result.add(soruMetni);
                if(jsontur.getJSONObject("message").getString("ID").equals(ID))
                    index = sayac;
                sayac ++;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_plakalar.this, android.R.layout.simple_spinner_dropdown_item, result);
        turListe.setAdapter(adapter);}catch (Exception e){}
        turListe.setSelection(index);

        /*
        String CinsID=PullCinsSpinnerID();
        try {
            
            int i=0; int sayac=0;
            while(i < turListe.getCount() || sayac<turler.length())
            {
                JSONObject jsontur = turler.getJSONObject(i);
                if (jsontur.getJSONObject("message").getString("CinsID").equals(CinsID)) {
                    if(jsontur.getJSONObject("message").getString("ID").equals(ID)) {
                        index = i;
                        turListe.setSelection(index);
                        return;
                        }
                    i++;
                }
                sayac++;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        turListe.setSelection(index);
        //// TODO: 30.10.2017 tur cekmiyor*/
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void ListeDoldur(){
        ListView listemiz = (ListView) findViewById(R.id.plakaListView);
        userPulled=false;
        try{
            plakalar=new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < plakalar.length(); i++) {
                JSONObject jsonChildNode = plakalar.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("ID");
                String number = jsonChildNode.getJSONObject("message").getString("Plaka");
                String outPut = name + "-" + number;
                itemList.add(createListItem("Üyeler", outPut));
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(tab_plakalar.this, itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
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
                    JSONObject jsonChildNode = plakalar.getJSONObject(position).getJSONObject("message");
                    EditText plakaText = (EditText) findViewById(R.id.plakaText);
                    SeekBar redSeekBar= (SeekBar) findViewById(R.id.seekBarRed);
                    SeekBar greenSeekBar = (SeekBar) findViewById(R.id.seekBarGreen);
                    SeekBar blueSeekBar = (SeekBar) findViewById(R.id.seekBarBlue);

                    plakaText.setText(jsonChildNode.getString("Plaka"));
                    PutCinsSpinner(jsonChildNode.getString("CinsID"));
                    PutTurSpinner(jsonChildNode.getString("TurID"));

                    String rgbColor=jsonChildNode.getString("AracRengi");
                    if(!rgbColor.isEmpty()) {
                        redSeekBar.setProgress(Integer.parseInt(rgbColor.substring(0, 2), 16));
                        greenSeekBar.setProgress(Integer.parseInt(rgbColor.substring(2, 4), 16));
                        blueSeekBar.setProgress(Integer.parseInt(rgbColor.substring(4, 6), 16));
                    }
                    valueChanged=false;

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }

    private void ButtonListenEvent(){
        Button btn = (Button) findViewById(R.id.plaka_bt_ekle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPulled==true && valueChanged==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
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
                                        JSONObject jsonChildNode = plakalar.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText plaka = (EditText) findViewById(R.id.plakaText);
                                        SeekBar redSeek=(SeekBar) findViewById(R.id.seekBarRed);
                                        SeekBar greenSeek=(SeekBar) findViewById(R.id.seekBarGreen);
                                        SeekBar blueSeek=(SeekBar) findViewById(R.id.seekBarBlue);

                                        if(plaka.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                                            builder.setMessage("Plaka boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Pekle_URL(
                                                        URLEncoder.encode(plaka.getText().toString(),"utf-8")
                                                        ,PullCinsSpinnerID()
                                                        ,PullTurSpinnerID()
                                                        ,Integer.toHexString(redSeek.getProgress())+
                                                                Integer.toHexString(greenSeek.getProgress())+
                                                                Integer.toHexString(blueSeek.getProgress()))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }

                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage("Yeni plaka eklemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.plaka_bt_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage("Güncellenecek bir plaka seçmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                }
                else if(userPulled==true && valueChanged==false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage("Seçilen plakada hiçbir değeri değiştirmediniz")
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
                                        JSONObject jsonChildNode = plakalar.getJSONObject(selectedIndex).getJSONObject("message");
                                        EditText plaka = (EditText) findViewById(R.id.plakaText);
                                        SeekBar redSeek=(SeekBar) findViewById(R.id.seekBarRed);
                                        SeekBar greenSeek=(SeekBar) findViewById(R.id.seekBarGreen);
                                        SeekBar blueSeek=(SeekBar) findViewById(R.id.seekBarBlue);

                                        if(plaka.getText().toString().isEmpty())
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                                            builder.setMessage("Plaka boş olamaz.")
                                                    .setNegativeButton("Tamam", null).create().show();
                                            return;
                                        }

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Pguncelle_URL(jsonChildNode.getString("ID"),
                                                        URLEncoder.encode(plaka.getText().toString(),"utf-8")
                                                        ,PullCinsSpinnerID()
                                                        ,PullTurSpinnerID()
                                                        ,Integer.toHexString(redSeek.getProgress())+
                                                                Integer.toHexString(greenSeek.getProgress())+
                                                                Integer.toHexString(blueSeek.getProgress())
                                                )).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage("Plakayı güncellemek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });

        btn = (Button) findViewById(R.id.plaka_bt_sil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPulled == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage("Silmek üzere hiçbir plaka seçmediniz").setPositiveButton("Tamam", null).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        JSONObject jsonChildNode = plakalar.getJSONObject(selectedIndex).getJSONObject("message");

                                        JSONObject temp = new JSONObject(new JSONtask().execute(
                                                Config.Psil_URL(jsonChildNode.getString("ID"))).get());
                                        if (JsonErrorCheck(temp)) {
                                            Toast.makeText(getApplicationContext(), "Silme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                            ListeDoldur();
                                            return;
                                        }
                                    } catch (Exception e){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                                        builder.setMessage(e.getLocalizedMessage())
                                                .setNegativeButton("Tamam", null).create().show();
                                    }break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    String plaka="";
                    try{
                        plaka = plakalar.getJSONObject(selectedIndex).getJSONObject("message").getString("Plaka");
                    }catch (Exception e){}
                    AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
                    builder.setMessage(plaka + " plakasını silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                            .setNegativeButton("Hayır", dialogClickListener).show();
                }
            }
        });
    }

    private void CreateControlListenner() {
        ((EditText) findViewById(R.id.plakaText)).addTextChangedListener(new TextWatcher() {
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
        ((Spinner) findViewById(R.id.plakaCinsSpin)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
                try{
                Spinner turListe = (Spinner) findViewById(R.id.plakaTurSpin);
                String CinsID=PullCinsSpinnerID();
                ArrayList<String> result = new ArrayList<String>();
                for (int i = 0; i < turler.length(); i++) {
                    JSONObject jsontur = turler.getJSONObject(i);

                    if(jsontur.getJSONObject("message").getString("CinsID").equals(CinsID)) {
                        String soruMetni = jsontur.getJSONObject("message").getString("TurAdi");
                        result.add(soruMetni);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(tab_plakalar.this, android.R.layout.simple_spinner_dropdown_item, result);
                turListe.setAdapter(adapter);}catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        ((Spinner) findViewById(R.id.plakaTurSpin)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valueChanged=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });}

    private void ResetControls(){
        ((EditText) findViewById(R.id.plakaText)).setText("");
        ((Spinner) findViewById(R.id.plakaCinsSpin)).setSelection(0);
        ((Spinner) findViewById(R.id.plakaTurSpin)).setSelection(0);
        ((SeekBar)findViewById(R.id.seekBarRed)).setProgress(0);
        ((SeekBar)findViewById(R.id.seekBarGreen)).setProgress(0);
        ((SeekBar)findViewById(R.id.seekBarBlue)).setProgress(0);
        valueChanged = false;
        userPulled=false;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(tab_plakalar.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
