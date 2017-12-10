package plakaapp.plakaapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openalpr.OpenALPR;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by cumen on 23.11.2017.
 */





public class fragment_hizliyazi extends Fragment {
    String yazarID = "bos";
    public fragment_hizliyazi() {

    }

    public View Alprview; // OpenAlpr den gelen plaka için tanımlanmış global View Objesi
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_hizliyazi, container, false);
        final Button yaziekle = (Button) view.findViewById(R.id.btn_yazigonder);
        final ImageButton alpr = (ImageButton) view.findViewById(R.id.btn_alpr_git);
        String foto_plaka = "boş";

        Session session = new Session(getActivity());

        if(session.loggedin()){ //Uygulama cihazda açıksa
            yazarID = session.Sid();
        }else{ // uygulama cihazda ilk defa açılıyorsa
            //YazarID yi çekiyoruz
            Intent intent = getActivity().getIntent();
            yazarID = intent.getStringExtra("ID");
            //YazarID yi çekiyoruz
        }

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        final EditText hizliyazi = (EditText) view.findViewById(R.id.et_hizliyazi);

        //hizli yazıya sınır koymak
        int maxLength_hizliyazi = 250;
        InputFilter[] FilterArray_hizliyazi = new InputFilter[1];
        FilterArray_hizliyazi[0] = new InputFilter.LengthFilter(maxLength_hizliyazi);
        hizliyazi.setFilters(FilterArray_hizliyazi);
        //hizli yazıya sınır koymak


        Spinner konum_spnr = (Spinner) view.findViewById(R.id.spnr_konum);



        final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);

        //plakaya sınır koymak
        int maxLength_plaka = 10;
        InputFilter[] FilterArray_plaka = new InputFilter[1];
        FilterArray_plaka[0] = new InputFilter.LengthFilter(maxLength_plaka);
        plaka.setFilters(FilterArray_plaka);
        //plaka yazıya sınır koymak

        //Konumlar dolduruluyor
        konum_spnr = (Spinner) view.findViewById(R.id.spnr_konum);
        KonumSpinnerDoldur(konum_spnr);
        //Konumlar dolduruluyor

        final Spinner finalKonum_spnr = konum_spnr;
        yaziekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // boş olup olmadığının kontrolü

                //edittextten gelen yazıları bir stringe atadık
                final String gelenplaka = plaka.getText().toString();
                String gelenyazi = hizliyazi.getText().toString();
                //edittextten gelen yazıları bir stringe atadık

                //hangi konum seçiliyse alınıyor
                String str = finalKonum_spnr.getSelectedItem().toString();
                //hangi konum seçiliyse alınıyor

                if (gelenplaka.isEmpty() || gelenyazi.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Lütfen Tüm Alanları Doldurunuz")
                            .setNegativeButton("TEKRAR DENEYİNİZ", null)
                            .create()
                            .show();
                    plaka.setText("");
                    hizliyazi.setText("");
                } else { //Boş değilse
                    if (Validation.plaka(gelenplaka)) { //Plaka doğru formattaysa

                        //Plakanın idsini çekiyoruz
                        String plakaID = PlakaIDSorgula(gelenplaka);
                        //Plakanın idsini çekiyoruz

                        //Konumun ID si çekiliyor
                        String konumID = KonumIDSorgula(str);
                        //Konumun ID si çekiliyor

                        //Plaka Ekleme Ekranına yönlendirme
                        if (plakaID == "bos") {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Böyle bir plaka yok. Eklemek İster misiniz ?")
                                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getActivity(), sub_plakaEkle.class);
                                            intent.putExtra("Plaka",gelenplaka);
                                            getActivity().startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Hayır", null)
                                    .create()
                                    .show();
                            //Plaka Ekleme Ekranına yönlendirme

                        } else if (plakaID == "hata") {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Bir hata oluştu")
                                    .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                    .create()
                                    .show();
                            plaka.setText("");
                        } else {
                            //kullanıcının yazı yazma yetkisi yok ise engelleme
                            if(!Validation.userWrite(yazarID))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Sizin yazı yazma yetkiniz bulunmamaktadır.\nRep değeriniz sınırın altındadır.")
                                        .setNegativeButton("Tamam Anladım", null)
                                        .create()
                                        .show();
                                return;
                            }

                            //Sisteme yazının eklenmesi
                            try{
                                JSONObject temp = new JSONObject(new JSONtask().execute(
                                        Config.Yekle_URL(
                                                URLEncoder.encode(plakaID,"utf-8"),
                                                URLEncoder.encode(yazarID,"utf-8"),
                                                URLEncoder.encode(konumID,"utf-8"),
                                                URLEncoder.encode(gelenyazi,"utf-8"))).get());
                                if (Validation.JsonErrorCheck(temp) == "basarili") {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Yazınız Başarıyla Eklenmiştir.")
                                            .setNegativeButton("Tamam", null)
                                            .create()
                                            .show();
                                    plaka.setText("");
                                    hizliyazi.setText("");
                                }
                            }
                            catch (Exception e){

                            }
                            //Sisteme yazının eklenmesi
                        }
                    } else { //Plaka yanlış formattaysa
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Plaka Uygun Formatta Değil")
                                .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                .create()
                                .show();
                        plaka.setText("");
                    }
                }

            }
        });



        //alpr penceresine yönlendiriyoruz
        alpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), openAlpr.class);
                startActivityForResult(intent,1);
            }
        });
        //alpr penceresine yönlendiriyoruz

        Alprview=view; //OnActivityResult için  Alprview içine view imizi atıyoruz.
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("sonuc");

                // set text view with string
                TextView textView = (TextView) Alprview.findViewById(R.id.et_plakaNumarasi);
                textView.setText(returnString);
            }
        }
    }

    private String PlakaIDSorgula(String plaka) {
        try {
            JSONArray plakalar = new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());

            for (int i = 0; i < plakalar.length(); i++) {
                JSONObject jsonChildNode = plakalar.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("ID");
                String number = jsonChildNode.getJSONObject("message").getString("Plaka");
                if (plaka.equals(number)) {
                    return name;
                }
            }
            return "bos";

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
            return "hata";
        }
    }

    private void KonumSpinnerDoldur(Spinner konum_spnr)    {
        try {
             JSONArray konumlar = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());


            List<String> result = new ArrayList<String>();

            for (int i = 0; i < konumlar.length(); i++) {
                JSONObject jsonSoru = konumlar.getJSONObject(i);
                String konumadi=jsonSoru.getJSONObject("message").getString("il_adi");
                result.add(konumadi);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, result);
            konum_spnr.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private String KonumIDSorgula(String konum)    {
        try {
            JSONArray konumlar = new JSONArray(new JSONtask().execute(Config.ILLISTELE_URL).get());

            for (int i = 0; i < konumlar.length(); i++) {
                JSONObject jsonSoru = konumlar.getJSONObject(i);
                String konumadi=jsonSoru.getJSONObject("message").getString("il_adi");
                String konum_id=jsonSoru.getJSONObject("message").getString("il_kodu");
                if (konum.equals(konumadi)) {
                    return konum_id;
                }
            }
            return "bos";

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
            return "hata";
        }
    }


}
