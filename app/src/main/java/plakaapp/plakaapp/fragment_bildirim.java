package plakaapp.plakaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 25.11.2017.
 */

public class fragment_bildirim extends Fragment {
    public JSONArray takipler,plakalar,yazilar;
    String K_ID, K_Ad;

    public fragment_bildirim() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bildirim, container, false);

        Intent intent = getActivity().getIntent();
        K_Ad = intent.getStringExtra("K_Adi");//kullanıcı adı
        K_ID = intent.getStringExtra("ID"); //kullanıcı idsi

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        ListeDoldur(view);
        TemizleListener(view);

        return view;
    }

    private void TemizleListener(final View view) {
        Button btn = (Button) view.findViewById(R.id.btn_temizle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    for (int i = 0; i < takipler.length(); i++) {
                                        JSONObject jsonChildNode = takipler.getJSONObject(i).getJSONObject("message");
                                        String ID=jsonChildNode.getString("Uye_ID");
                                        if(K_ID.equals(ID)) {
                                            JSONObject temp = new JSONObject(new JSONtask().execute(
                                                    Config.Taguncelle_URL(
                                                            URLEncoder.encode(jsonChildNode.getString("Uye_ID"), "utf-8"),
                                                            URLEncoder.encode(jsonChildNode.getString("Plaka_ID"), "utf-8")
                                                    )).get());
                                            if (!JsonErrorCheck(temp)) {
                                                return;
                                            }
                                        }

                                    }
                                    Toast.makeText(getContext(), "Bildirimler temizlendi", Toast.LENGTH_LONG).show();
                                    ListeDoldur(view);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Tüm bildirimlerinizi kaldırmak istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();

            }
        });
    }

    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    private void ListeDoldur(View view) {
        ListView listemiz = (ListView) view.findViewById(R.id.lv_bildirimler);
        try {
            takipler = new JSONArray(new JSONtask().execute(Config.TaLISTELE_URL).get());
            plakalar = new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());
            yazilar = new JSONArray(new JSONtask().execute(Config.YLISTELE_URL).get());

            List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < takipler.length(); i++) {
                JSONObject jsonChildNode = takipler.getJSONObject(i);

                String name = jsonChildNode.getJSONObject("message").getString("Plaka_ID");
                String number = jsonChildNode.getJSONObject("message").getString("Uye_ID");
                String tarih=jsonChildNode.getJSONObject("message").getString("SonBakma");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
                try {
                    boolean oku=false;
                    Date date = format.parse(tarih);
                    for (int j = 0; j < yazilar.length(); j++)
                    {
                        if(yazilar.getJSONObject(j).getJSONObject("message").getString("PlakaID")==name) {
                            Date date2 = format.parse(yazilar.getJSONObject(j).getJSONObject("message").getString("YazilmaTarih"));
                            if (date.before(date2)) {
                                oku = true;
                                break;
                            }
                        }
                    }
                    if(!oku) number="-1";
                } catch (Exception e) {   }

                if(K_ID.equals(number)) {
                    for (int j = 0; j < plakalar.length(); j++) {
                        if (plakalar.getJSONObject(j).getJSONObject("message").getString("ID") == name) {
                            name = plakalar.getJSONObject(j).getJSONObject("message").getString("Plaka");
                        }
                    }

                    String outPut = name+" plakasına yeni yazı eklendi.";
                    itemList.add(createListItem("Üyeler", outPut));
            }
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), itemList, android.R.layout.simple_list_item_1, new String[]{"Üyeler"}, new int[]{android.R.id.text1});
            listemiz.setAdapter(simpleAdapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //// TODO: 25.11.2017 Plaka yazıları penceresine yönlendirme

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
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
                Toast.makeText(getContext(), "SQL Hatası alındı", Toast.LENGTH_LONG).show();
            if(durum.equals(String.valueOf("8")))
                Toast.makeText(getContext(), "Kayıt Bulunamadı", Toast.LENGTH_LONG).show();
            if(durum.equals(String.valueOf("1")))
                Toast.makeText(getContext(), "Kayıt Mevcut", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null).create().show();
        }
        return false;
    }
}