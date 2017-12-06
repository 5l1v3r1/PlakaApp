package plakaapp.plakaapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 26.11.2017.
 */

public class fragment_arama extends Fragment {
    String K_ID;
    public fragment_arama() {

    }

    public View Alprview; // OpenAlpr den gelen plaka için tanımlanmış global View Objesi

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arama, container, false);

        //Logoya yazı fontu eklendi(cumen)
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //Openalpr butonunun tanımlanması
        final ImageButton arama_alpr_btn = (ImageButton) view.findViewById(R.id.btn_arama_alpr);

        //intent'ten kullanıcı ID'si çekmek
        Intent intent = getActivity().getIntent();
        K_ID = intent.getStringExtra("ID"); //kullanıcı idsi

        //ara buttonunun işlevlerinin oluşturulması
        AraButtonListener(view);

        //git buttonunun işlevleri
        GitButtonListener(view);

        //alpr penceresine yönlendiriyoruz
        arama_alpr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), openAlpr.class);
                startActivityForResult(intent,2);
            }
        });


        //alpr penceresine yönlendiriyoruz
        Alprview = view;
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("sonuc");

                // set text view with string
                TextView textView = (TextView) Alprview.findViewById(R.id.et_plakaNumarasi);
                textView.setText(returnString);
            }
        }
    }
    
    int islem = 0;
    String pText="";

    //tüm kontrolleri sıfırlayacak olan method
    private void ResetControls(View view)
    {
        islem=0;
        ((EditText) view.findViewById(R.id.et_plakaNumarasi)).setText("");
        ((TextView) view.findViewById(R.id.et_hizliyazi)).setText("");
    }

    private void GitButtonListener(final View view) {
        //git buttonunun tanımlanması
        final Button sorgula = (Button) view.findViewById(R.id.btn_git);
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //plaka yazısını almak
                EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
                pText=plaka.getText().toString();

                //plaka editText'inden focus'u kaldırmak
                plaka.clearFocus();

                //eğer işlem 0 tanımlı ise ara tuşuna basılmamış demektir veya hatalı plaka girilmiştir
                if(islem==0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bir plaka sorgulamadınız...")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }//eğer işlem 1 ise plaka hiç bulunmamaktadır ve plaka ekleme paneline gönderilir
                else if(islem==1)
                {
                    Intent intent = new Intent(getActivity(), sub_plakaEkle.class);
                    intent.putExtra("Plaka",pText);
                    getActivity().startActivity(intent);
                    ResetControls(view);
                }//eğer işlem 2 ise plaka bulunmaktadır, plaka yazılarına yönlendirilir
                else if(islem==2)
                {
                    try {
                        //plakaların hepsi çekilir ve plakaya göre ID'si bulunur
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

                        //kullanıcının takip ettiği bir plaka ise son görülme güncellenir
                        new JSONtask().execute(
                                Config.Taguncelle_URL(
                                        URLEncoder.encode(K_ID, "utf-8"),
                                        URLEncoder.encode(PlakaID, "utf-8")
                                )).get();

                        //plaka ve plakaID gönderilerek plaka yazıları açılır
                        Intent intent = new Intent(getActivity(), sub_yazilistele.class);
                        intent.putExtra("Plaka",pText);
                        intent.putExtra("PlakaID",PlakaID);
                        intent.putExtra("KisiID",K_ID);
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
        //plaka sorgulanır, plaka için yazı olup olmadığı değeri döndürülür
        //plaka için yazı yok ise plakanın olup olmadığı kontrol edilir(berke)
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
