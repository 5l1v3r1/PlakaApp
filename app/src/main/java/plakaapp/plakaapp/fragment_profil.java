package plakaapp.plakaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 25.11.2017.
 */

public class fragment_profil extends Fragment {
    View vvv;
    String K_ID, K_Ad;
    public Boolean e1, e2 , e3 , e4 , e5;
    public JSONArray uyeler, sorular;

    public fragment_profil() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        vvv = view;

        //giriş yapan kullanıcı verileri çekiliyor
        Intent intent = getActivity().getIntent();
        K_Ad = intent.getStringExtra("K_Adi");//kullanıcı adı
        K_ID = intent.getStringExtra("ID"); //kullanıcı id

        //soruların bulunduğu spinner dolduruluyor
        SorularSpinnerDoldur(view);
        //üyelerin bilgileri gerekli alanlara dolduruluyor
        UyeBilgiDoldur(view);
        //tüm kontrolleri dinleyen listenerlar oluşturuluyor
        //ki hangi verilerin değiştiği veya hangilerine dokunulmadığı belli olsun diye
        CreateControlListener(view);
        //buttton için bir listener oluşturuluyor
        CreateButtonListener(view);
        //tüm verilere hiç dokunulmadı verisi giriliyor
        e1 = e2 = e3 = e4 = e5 = false;

        return view;
    }

    private void CreateButtonListener(View view) {
        Button btn = (Button) view.findViewById(R.id.et_profil_guncelle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eğer tüm veriler için değer "dokunulmadı" yani "false" ise
                if (!e1 & !e2 & !e3 & !e4 & !e5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Hiçbir değeri değiştirmediniz")
                            .setNegativeButton("Tamam", null).create().show();
                    //alt satırlardaki hiçbir koda gitmemesi için engelleniyor
                    return;
                }
                //değiştirmek ister misiniz sorusu için evet-hayır cevabı alınması
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    //üye ID'sinden üyenin bilgilerinin olduğu obje kümesi indexi
                                    int index = 0;
                                    for (int i = 0; i < uyeler.length(); i++) {
                                        if (uyeler.getJSONObject(i).getJSONObject("message").getString("ID").equals(K_ID)) {
                                            index = i;
                                            break;
                                        }
                                    }
                                    //değişken alınan kontroller tanımlanıyor
                                    JSONObject jsonChildNode = uyeler.getJSONObject(index).getJSONObject("message");
                                    EditText k_adi = (EditText) vvv.findViewById(R.id.et_profil_kuladi);
                                    EditText sifre = (EditText) vvv.findViewById(R.id.et_profil_email);
                                    EditText eposta = (EditText) vvv.findViewById(R.id.et_profil_parola);
                                    EditText rep = (EditText) vvv.findViewById(R.id.et_profil_gcevap);

                                    //geri gönderilecek string verileri tanımlanıyor
                                    String v1, v2, v3, v4, v5;

                                    //değişmiş veriler için yeni veriler,
                                    //dokunulmamışlar için ise bellekteki veriler çekiliyor
                                    if (e1) v1 = k_adi.getText().toString();
                                    else v1 = jsonChildNode.getString("K_Adi");
                                    if (e2) v2 = eposta.getText().toString();
                                    else v2 = jsonChildNode.getString("K_Mail");
                                    if (e3) v3 = sifre.getText().toString();
                                    else v3 = "~~";
                                    if (e4) v4 = SpinnerPull(vvv);
                                    else v4 = jsonChildNode.getString("K_Soru");
                                    if (e5) v5 = rep.getText().toString();
                                    else v5 = jsonChildNode.getString("K_Cevap");

                                    //girilen değerler Validation class'ında kontrol ediliyor
                                    //usulsüzlik olduğu takdirde işlemler return ile disband ediliyor
                                    if(!Validation.userValidate(v1))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Kullanıcı adı 5-28 karakter arasında olmalı" +
                                                "\nRakam ve harflerden oluşabilir." +
                                                "\n sadece '_' ve '-' karakterleri kullanılabilir.' ")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }
                                    if(!Validation.email(v2))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Mail adresi formata uygun değildir.")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }
                                    if(!Validation.password(v3) && e3)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                                "\nEn az bir büyük harf içermelidir." +
                                                "\nEn az bir küçük harf içermelidir."+
                                                "\nEn az bir rakam harf içermelidir." +
                                                "\nEn az bir '@$^&+=' karakter içermelidir.")
                                                .setNegativeButton("Tamam", null)
                                                .create()
                                                .show();
                                        return;
                                    }


                                    //verilerin sisteme girişi api ile sağlanıyor
                                    JSONObject temp = new JSONObject(new JSONtask().execute(
                                            Config.Kguncelle_URL(jsonChildNode.getString("ID"),
                                                    jsonChildNode.getString("Admin"),
                                                    URLEncoder.encode(v1, "utf-8"),
                                                    URLEncoder.encode(v3, "utf-8"),
                                                    jsonChildNode.getString("K_Rep"),
                                                    URLEncoder.encode(v2, "utf-8"),
                                                    URLEncoder.encode(v4, "utf-8"),
                                                    URLEncoder.encode(v5, "utf-8"))).get());
                                    //başarılı mesajı alınıyor
                                    if (JsonErrorCheck(temp)) {
                                        Toast.makeText(getContext(), "Güncelleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
                                        UyeBilgiDoldur(vvv);
                                        return;
                                    }
                                } catch (Exception e) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                //ekranda gösterilecek evet-hayır mesajı tanımlanıyor
                String mesaj = "Değiştirilecek Bilgiler;\n";
                if (e1) mesaj += "Kullanıcı Adı,\n";
                if (e2) mesaj += "Mail,\n";
                if (e3) mesaj += "Parola,\n";
                if (e4) mesaj += "Güvenlik Sorusu,\n";
                if (e5) mesaj += "Soru Cevabı,\n";
                mesaj += "\nBu bilgileri değiştirmek istediğinze emin misiniz?";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mesaj).setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();

            }
        });
    }

    private void CreateControlListener(View view) {
        //değişen her bir veri için ayrı bir boolean değeri true oluyor
        ((EditText) view.findViewById(R.id.et_profil_kuladi)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                e1 = true;
            }
        });
        ((EditText) view.findViewById(R.id.et_profil_email)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                e2 = true;
            }
        });
        ((EditText) view.findViewById(R.id.et_profil_parola)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                e3 = true;
            }
        });
        ((EditText) view.findViewById(R.id.et_profil_gcevap)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                e5 = true;
            }
        });
        ((Spinner) view.findViewById(R.id.sp_profil_gsorusu)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                e4 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void UyeBilgiDoldur(View view) {
        try {
            //Üye bilgilerini çek
            uyeler = new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            for (int i = 0; i < uyeler.length(); i++) {
                JSONObject jsonSoru = uyeler.getJSONObject(i).getJSONObject("message");

                //üyelerin bilgilerini uygun alanlara gir
                if(jsonSoru.getString("ID").equals(K_ID)) {
                    ((EditText) view.findViewById(R.id.et_profil_kuladi)).setText(jsonSoru.getString("K_Adi"));
                    ((EditText) view.findViewById(R.id.et_profil_email)).setText(jsonSoru.getString("K_Mail"));
                    ((EditText) view.findViewById(R.id.et_profil_parola)).setText("");
                    ((EditText) view.findViewById(R.id.et_profil_gcevap)).setText(jsonSoru.getString("K_Cevap"));
                    SpinnerPush(jsonSoru.getString("K_Soru"), view);
                    return;
                }
            }

            //kullanıcı bilgileri el ile değişmemiş olarak tanımla
            e1 = e2 = e3 = e4 = e5 = false;
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private String SpinnerPull(View view) {
        //spinner tanımla
        Spinner soru = ((Spinner) view.findViewById(R.id.sp_profil_gsorusu));
            String index = "0";
            try {
                for (int i=0;i<sorular.length();i++){

                    //soru metnine göre sorunun ID'sini çekip geri gönder
                    if (soru.getSelectedItem().toString().equals(sorular.getJSONObject(i).getJSONObject("message").getString("SoruMetin"))) {
                        index = sorular.getJSONObject(i).getJSONObject("message").getString("ID");
                        return index;
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            //bulunamaması ihtimali ile sıfır gönder
            return index;
    }

    private void SpinnerPush(String ID, View view) {
        try {
            //girilen ID değeri boş ise durdur
            if (ID.isEmpty()) return;

            Spinner soru = ((Spinner) view.findViewById(R.id.sp_profil_gsorusu));

            //gelen ID ile soru ID'lerini kıyasla, hangi soru olduğunu bul
            int arrayPos = 0;
            for (int i = 0; i < sorular.length(); i++) {
                if (sorular.getJSONObject(i).getJSONObject("message").getString("ID") == ID) {
                    arrayPos = i;
                    break;
                }
            }

            //belirlenen ID'deki sorunun spinnerdaki sırasını bulup seçtir
            for (int i = 0; i < soru.getCount(); i++) {
                if (soru.getItemAtPosition(i).toString() ==
                        sorular.getJSONObject(arrayPos).getJSONObject("message").getString("SoruMetin")) {
                    soru.setSelection(i);
                    //bu işlem el ile seçim olmadığından değeri false ata
                    e4=false;
                    return;
                }
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    public void SorularSpinnerDoldur(View view) {
        try {
            //spinner ve doldurulacak list tanımla
            sorular = new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) view.findViewById(R.id.sp_profil_gsorusu);
            List<String> result = new ArrayList<String>();

            //soruları listeleniyor
            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);

                String soruMetni = jsonSoru.getJSONObject("message").getString("SoruMetin");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, result);
            soruListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

    }

    private boolean JsonErrorCheck(JSONObject temp) {
        try {
            //JSONObject js1 =temp.getJSONObject(0);
            JSONObject js2 = temp.getJSONObject("message");
            String durum = js2.getString("durum");
            if (durum.equals(String.valueOf("basarili")))
                return true;
            if (durum.equals(String.valueOf("99")))
                Toast.makeText(getContext(), "SQL Hatası alındı", Toast.LENGTH_LONG).show();
            if (durum.equals(String.valueOf("8")))
                Toast.makeText(getContext(), "Kayıt Bulunamadı", Toast.LENGTH_LONG).show();
            if (durum.equals(String.valueOf("1")))
                Toast.makeText(getContext(), "Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}