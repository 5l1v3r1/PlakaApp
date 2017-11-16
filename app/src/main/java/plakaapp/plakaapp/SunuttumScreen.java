package plakaapp.plakaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cumen on 29.10.2017.
 */

public class SunuttumScreen extends AppCompatActivity {

    String su_id = "";
    String su_admin ="";
    String su_adi ="";
    String su_rep ="";
    String su_email ="";
    String su_cevap = "";
    String su_gsoru ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sunuttum_screen_activity);

        final Button btn_kontrol = (Button) findViewById(R.id.mail_kontrol);
        final EditText su_mail = (EditText) findViewById(R.id.su_mail);


        btn_kontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validation.email(su_mail.getText().toString())) { //Email kontrol
                    try{
                        JSONArray uyeler=new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
                        int sayac = 0;
                        List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();
                        for (int i = 0; i < uyeler.length(); i++) {
                            JSONObject jsonChildNode = uyeler.getJSONObject(i);
                            String vt_mail = jsonChildNode.getJSONObject("message").getString("K_Mail");
                            if(vt_mail.equals(String.valueOf(su_mail.getText().toString()))){
                                sayac = sayac + 1;
                                su_id = jsonChildNode.getJSONObject("message").getString("ID");
                                su_admin = jsonChildNode.getJSONObject("message").getString("Admin");
                                su_adi = jsonChildNode.getJSONObject("message").getString("K_Adi");
                                su_rep = jsonChildNode.getJSONObject("message").getString("K_Rep");
                                su_email = jsonChildNode.getJSONObject("message").getString("K_Mail");
                                su_gsoru = jsonChildNode.getJSONObject("message").getString("K_Soru");
                                su_cevap = jsonChildNode.getJSONObject("message").getString("K_Cevap");
                            }
                        }
                        if(sayac != 0){

                            Intent intent = new Intent(SunuttumScreen.this, GsorusuScreen.class);
                            intent.putExtra("K_id",su_id);
                            intent.putExtra("K_admin",su_admin);
                            intent.putExtra("K_adi",su_adi);
                            intent.putExtra("K_rep",su_rep);
                            intent.putExtra("K_email",su_email);
                            intent.putExtra("K_soru",su_gsoru);
                            intent.putExtra("K_cevap",su_cevap);
                            SunuttumScreen.this.startActivity(intent);

                            finish();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SunuttumScreen.this);
                            builder.setMessage("Mail adresi sisteme kayıtlı değil")
                                    .setNegativeButton("Tamam", null)
                                    .create()
                                    .show();
                        }
                    }catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SunuttumScreen.this);
                        builder.setMessage(e.getMessage())
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SunuttumScreen.this);
                    builder.setMessage("Mail adresi formata uygun değildir.")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });


    }
    private HashMap<String, String> createListItem(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }
}
