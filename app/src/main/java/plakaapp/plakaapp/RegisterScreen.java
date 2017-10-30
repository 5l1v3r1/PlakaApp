package plakaapp.plakaapp;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cumen on 29.10.2017.
 */

public class RegisterScreen extends AppCompatActivity {

    public JSONArray sorular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen_activity);

        SorularSpinnerDoldur();

        final Button btn_kayit = (Button) findViewById(R.id.btn_kayit_ol);
        final EditText Kul_Adi = (EditText) findViewById(R.id.kayitol_kuladi);
        final EditText e_mail = (EditText) findViewById(R.id.kayitol_email);
        final EditText etpass = (EditText) findViewById(R.id.kayitol_password);
        final EditText cevap = (EditText) findViewById(R.id.cevap);

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validation.userValidate(Kul_Adi.getText().toString())){  //Kullanıcı adı kontrol
                    if(Validation.email(e_mail.getText().toString())){ //Email kontrol
                        if(Validation.password(etpass.getText().toString())){ //password kontrol
                            if(cevap.length()>3 && cevap.length()<50){ //cevap kontrol

                                
                                //ekleme yapılacak


                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                                builder.setMessage("cevap olmadı")
                                        .setNegativeButton("Tamam", null)
                                        .create()
                                        .show();
                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                            builder.setMessage("sifre olmadı")
                                    .setNegativeButton("Tamam", null)
                                    .create()
                                    .show();
                        }
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                        builder.setMessage("mail olmadı")
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                    builder.setMessage("kuladi olmadı")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                }
            }
        });

    }

    public void SorularSpinnerDoldur() {
        try {
            sorular=new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) findViewById(R.id.kayitol_gsorusu);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);
                String soruMetni=jsonSoru.getJSONObject("message").getString("SoruMetin");
                result.add(soruMetni);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterScreen.this, android.R.layout.simple_spinner_item, result);
            soruListe.setAdapter(adapter);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }

    }

    private String returnSpinnerID(){
        Spinner spinner = ((Spinner) findViewById(R.id.kayitol_gsorusu));
        String index = "0";
        try {
            for (int i=0;i<sorular.length();i++){

                if (spinner.getSelectedItem().toString().equals(sorular.getJSONObject(i).getJSONObject("message").getString("SoruMetin"))){
                    index = sorular.getJSONObject(i).getJSONObject("message").getString("ID");
                    return index;
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return index;
    }
}
