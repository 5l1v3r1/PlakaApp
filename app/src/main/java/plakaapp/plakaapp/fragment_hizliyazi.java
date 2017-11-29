package plakaapp.plakaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cumen on 23.11.2017.
 */

public class fragment_hizliyazi extends Fragment {
    public fragment_hizliyazi() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hizliyazi, container, false);

        final Button yaziekle = (Button) view.findViewById(R.id.btn_yazigonder);


        //YazarID yi çekiyoruz
        Intent intent = getActivity().getIntent();
        final String yazarID = intent.getStringExtra("ID");
        //YazarID yi çekiyoruz

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

        final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
        Spinner konum_spnr = (Spinner) view.findViewById(R.id.spnr_konum);

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

        return view;

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
