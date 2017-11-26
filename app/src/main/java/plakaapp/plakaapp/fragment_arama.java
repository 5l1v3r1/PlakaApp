package plakaapp.plakaapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 26.11.2017.
 */

public class fragment_arama extends Fragment {
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
        AraButtonListener(view);
        GitButtonListener(view);
        return view;
    }
    
    int islem = 0;
    String pText="";

    private void GitButtonListener(View view) {
        final Button sorgula = (Button) view.findViewById(R.id.btn_git);
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
                    //// TODO: 26.11.2017 Plaka ekleme penceresine yönlendirme 
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Plaka ekleme ekranı")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
                else if(islem==2)
                {
                    //// TODO: 26.11.2017 Plaka yazıları penceresine yönlendirme 
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Plaka yazıları ekranı")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });
    }

    private void AraButtonListener(final View view) {
        islem=0;
        //Coppied from MisafirScreen.java (Ahmet Çümen)
        final Button sorgula = (Button) view.findViewById(R.id.btn_yazigonder);
        final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
        sorgula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
                    final TextView bulunanlar = (TextView) view.findViewById(R.id.et_hizliyazi);


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
                                bulunanlar.setTextColor(getResources().getColor(R.color.red));
                                bulunanlar.setText("Böyle bir plaka veya yazısı bulunmamaktadır...\n"+
                                "Plaka bilgilerini eklemek için Git'e tıklayınız.");
                                islem=1;
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
}
