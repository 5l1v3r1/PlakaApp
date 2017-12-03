package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simulakra on 26.11.2017.
 */

public class sub_plakaEkle extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_plakaekle);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //eklenecek plakanın bilgisi intent'ten çekiliyor
        Intent intent = sub_plakaEkle.this.getIntent();
        String plaka = intent.getStringExtra("Plaka");
        ((EditText) findViewById(R.id.plakaText)).setText(plaka);

        //renk seçici oluşturuluyor
        ColorPickerCreate();
        //tür ve cins spinnerları dolduruluyor
        SpinnerlariDoldur();
        //spinnerlar için listener oluşturuluyor
        //cins seçildiği zaman ona göre türlerin çekilmesi için
        CreateSpinnerCinsListener();
        //ana işlem buttonunun listeneri oluşturuluyor
        CreateButtonListener();
    }

    private void CreateSpinnerCinsListener() {
        //cins seçildiği zaman türlerin ona göre listelenmesi
        ((Spinner) findViewById(R.id.plakaCinsSpin)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(sub_plakaEkle.this, android.R.layout.simple_spinner_dropdown_item, result);
                    turListe.setAdapter(adapter);}catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void CreateButtonListener() {
        //plaka eklemek için tanımlanan button
        final Button giris = (Button) findViewById(R.id.bt_giris);
        giris.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    //değerler dışarıdan alınıyor
                    EditText plaka = (EditText) findViewById(R.id.plakaText);
                    SeekBar redSeek=(SeekBar) findViewById(R.id.seekBarRed);
                    SeekBar greenSeek=(SeekBar) findViewById(R.id.seekBarGreen);
                    SeekBar blueSeek=(SeekBar) findViewById(R.id.seekBarBlue);

                    //api yardımı ile plaka ekleniyor
                    JSONObject temp = new JSONObject(new JSONtask().execute(
                            Config.Pekle_URL(
                                    URLEncoder.encode(plaka.getText().toString(),"utf-8")
                                    ,PullCinsSpinnerID()
                                    ,PullTurSpinnerID()
                                    ,Integer.toHexString(redSeek.getProgress())+
                                            Integer.toHexString(greenSeek.getProgress())+
                                            Integer.toHexString(blueSeek.getProgress()))).get());

                    //başarılı verisi alınıyor
                    if (JsonErrorCheck(temp)) {
                        Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sub_plakaEkle.this);
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null).create().show();
                }
            }
        });
    }

    JSONArray cinsler,turler;

    int redValue;
    int greenValue;
    int blueValue;
    ImageView picker;
    private void ColorPickerCreate() {
        //renk seçici tanımlanıyor
        picker=findViewById(R.id.plakaRenkPalet);

        SeekBar redSeekBar= (SeekBar) findViewById(R.id.seekBarRed);
        SeekBar greenSeekBar = (SeekBar) findViewById(R.id.seekBarGreen);
        SeekBar blueSeekBar = (SeekBar) findViewById(R.id.seekBarBlue);

        redValue  = redSeekBar.getProgress();
        greenValue  = greenSeekBar.getProgress();
        blueValue  = blueSeekBar.getProgress();

        //değer çubuklarının maksimum değerleri 255'e atanıyor
        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);

        //arkaplan rengi değerlere göre değiştiriliyor
        picker.setBackgroundColor(Color.rgb(redValue,greenValue,blueValue));

        //ve her değer değişiminde arkaplan rengi değişmeye devam ediyor
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
                blueValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });
    }

    private void SpinnerlariDoldur(){
        try {
            //doldurulacak spinnerlar tanımlanıyor
            cinsler = new JSONArray(new JSONtask().execute(Config.CLISTELE_URL).get());
            turler = new JSONArray(new JSONtask().execute(Config.TLISTELE_URL).get());

            Spinner cinsListe = (Spinner) findViewById(R.id.plakaCinsSpin);
            Spinner turListe = (Spinner) findViewById(R.id.plakaTurSpin);

            //cinslerin tamamı dolduruluyor
            List<String> result = new ArrayList<String>();
            for (int i = 0; i < cinsler.length(); i++) {
                JSONObject jsoncins = cinsler.getJSONObject(i);

                String soruMetni=jsoncins.getJSONObject("message").getString("AracCins");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(sub_plakaEkle.this, android.R.layout.simple_spinner_dropdown_item, result);
            cinsListe.setAdapter(adapter);

            //o an seçili olan cinse göre türler dolduruluyor
            String CinsID=PullCinsSpinnerID();
            result = new ArrayList<String>();
            for (int i = 0; i < turler.length(); i++) {
                JSONObject jsontur = turler.getJSONObject(i);

                if(jsontur.getJSONObject("message").getString("CinsID").equals(CinsID)) {
                    String soruMetni = jsontur.getJSONObject("message").getString("TurAdi");
                    result.add(soruMetni);
                }
            }
            adapter = new ArrayAdapter<String>(sub_plakaEkle.this, android.R.layout.simple_spinner_dropdown_item, result);
            turListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(sub_plakaEkle.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private String PullCinsSpinnerID(){
        //cins spinner'indan seçili cins'in CinsID'si çekiliyor
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
        //tür spinner'indan seçili olan türün ID'si çekiliyor
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
                Toast.makeText(getApplicationContext(), "Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(sub_plakaEkle.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}
