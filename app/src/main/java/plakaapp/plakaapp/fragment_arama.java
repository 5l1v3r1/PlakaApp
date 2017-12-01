package plakaapp.plakaapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 26.11.2017.
 */

public class fragment_arama extends Fragment {
    String K_ID;
    public fragment_arama() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arama, container, false);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi
        Intent intent = getActivity().getIntent();
        K_ID = intent.getStringExtra("ID"); //kullanıcı idsi
        AraButtonListener(view);
        GitButtonListener(view);
        return view;
    }
    
    int islem = 0;
    String pText="";

    private void ResetControls(View view)
    {
        islem=0;
        ((EditText) view.findViewById(R.id.et_plakaNumarasi)).setText("");
        ((TextView) view.findViewById(R.id.et_hizliyazi)).setText("");
    }

    private void GitButtonListener(final View view) {
        final Button sorgula = (Button) view.findViewById(R.id.btn_git);
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
                pText=plaka.getText().toString();
                plaka.clearFocus();
                if(islem==0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bir plaka sorgulamadınız...")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
                else if(islem==1)
                {
                    Intent intent = new Intent(getActivity(), sub_plakaEkle.class);
                    intent.putExtra("Plaka",pText);
                    getActivity().startActivity(intent);
                    ResetControls(view);
                }
                else if(islem==2)
                {
                    try {
                        JSONArray plakalar=new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());
                        String PlakaID="";
                        for (int i = 0; i < plakalar.length(); i++) {
                            JSONObject jsonChildNode = plakalar.getJSONObject(i);

                            String ID = jsonChildNode.getJSONObject("message").getString("ID");
                            String PPP = jsonChildNode.getJSONObject("message").getString("Plaka");
                            if(PPP.equals(pText))
                            {
                                PlakaID=ID;
                                break;
                            }
                        }

                        new JSONtask().execute(
                                Config.Taguncelle_URL(
                                        URLEncoder.encode(K_ID, "utf-8"),
                                        URLEncoder.encode(PlakaID, "utf-8")
                                )).get();
                        Intent intent = new Intent(getActivity(), sub_yazilistele.class);
                        intent.putExtra("Plaka",pText);
                        intent.putExtra("PlakaID",PlakaID);
                        getActivity().startActivity(intent);
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(e.getMessage())
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                    }
                    ResetControls(view);
                }
            }
        });
    }

    private void AraButtonListener(final View view) {

        //Coppied from MisafirScreen.java (Ahmet Çümen)
        final Button sorgula = (Button) view.findViewById(R.id.btn_yazigonder);
        final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
                    final TextView bulunanlar = (TextView) view.findViewById(R.id.et_hizliyazi);

                    islem=0;
                    Boolean dogrumu = Validation.plaka(plaka.getText().toString());
                    if(dogrumu == true){
                        //Api için stringleri tanımlıyoruz
                        String strURL = Config.PlakaSorgula_URL(URLEncoder.encode(plaka.getText().toString(),"utf-8"));
                        String deger = null;
                        //Api için stringleri tanımlıyoruz

                        JSONtask jst = new JSONtask();

                        deger = jst.execute(strURL).get();

                        JSONArray jsonResponse = new JSONArray(deger);

                        String hata = Validation.JsonErrorCheck(jsonResponse.getJSONObject(0));

                        JSONObject jsonResponseMessage = jsonResponse.getJSONObject(0).getJSONObject("message");

                        if(hata.equals(String.valueOf("basarili"))) {
                            String yazisayisi = jsonResponseMessage.getString("yazisayisi");
                            if(yazisayisi.equals(String.valueOf("0"))){
                                boolean plakavar=false;
                                try {
                                    JSONArray plakalar=new JSONArray(new JSONtask().execute(Config.PLISTELE_URL).get());
                                    for (int i = 0; i < plakalar.length(); i++) {
                                        JSONObject jsonChildNode = plakalar.getJSONObject(i);
                                        if(jsonChildNode.getJSONObject("message").getString("Plaka").equals(plaka.getText().toString()))
                                        {
                                            plakavar=true;
                                            break;
                                        }
                                    }
                                }
                                catch (Exception e){}
                                if(plakavar)
                                {
                                    bulunanlar.setTextColor(getResources().getColor(R.color.yazi));
                                    bulunanlar.setText("Böyle bir plaka var ama yazısı bulunmamaktadır...\n"+
                                            "Plaka sayfasına gitmek için Git'e tıklayınız.");
                                    islem=2;
                                }
                                else
                                {
                                    bulunanlar.setTextColor(getResources().getColor(R.color.red));
                                    bulunanlar.setText("Böyle bir plaka bulunmamaktadır...\n"+
                                            "Plaka bilgilerini eklemek için Git'e tıklayınız.");
                                    islem=1;
                                }
                            }else{
                                bulunanlar.setTextColor(getResources().getColor(R.color.yazi));
                                bulunanlar.setText(plaka.getText().toString() + " Plakasının toplam " + 
                                        yazisayisi + "  adet yazısı bulunmaktadır...\n"+
                                "Plaka sayfasına gitmek için Git'e tıklayınız.");
                                islem=2;
                            }
                            pText=plaka.getText().toString();

                        }else if(hata.equals(String.valueOf("8"))){
                            bulunanlar.setTextColor(getResources().getColor(R.color.red));
                            bulunanlar.setText("Kayıt Bulunamadı");

                        }else{
                            //Başka bir sorun varsa
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(" BİLİNMEDİK BİR SORUN OLUŞTU")
                                    .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                    .create()
                                    .show();
                            plaka.setText("");
                        }
                    }else{
                        //Başka bir sorun varsa
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Plaka Uygun Formatta Değil")
                                .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                .create()
                                .show();
                        plaka.setText("");
                    }
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setNegativeButton("TEKRAR DENEYİNİZ1", null)
                            .create()
                            .show();
                    plaka.setText("");
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
