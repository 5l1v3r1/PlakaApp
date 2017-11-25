package plakaapp.plakaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Typeface;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        final EditText etMail = (EditText) findViewById(R.id.login_email);
        final EditText etpassword = (EditText) findViewById(R.id.login_password);
        final Button bLogin = (Button) findViewById(R.id.giris_yap);
        final Button bRegister = (Button) findViewById(R.id.kayit_ol);
        final Button misafirgiris = (Button) findViewById(R.id.misafir_girisi);
        final Button sifremiunuttum = (Button) findViewById(R.id.sifremi_unuttum);

        //debug mod admin ve kullanıcı girişi
            final Button admin_giris = (Button) findViewById(R.id.btn_admin_girisi);
            final Button kullanici_giris = (Button) findViewById(R.id.btn_kullanici_girisi);

            admin_giris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etMail.setText("ahmet@gmail.com");
                    etpassword.setText("asdASD123$");
                }
            });

            kullanici_giris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etMail.setText("cumen@gmail.com");
                    etpassword.setText("asdASD123$");
                }
            });


        //debug mod admin ve kullanıcı girişi


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String kul_email = etMail.getText().toString();
                final String password = etpassword.getText().toString();
                String strURL = Config.GIRIS_URL + "/" + kul_email + "/" + password;
                String deger = null;



                if(Validation.email(kul_email)){
                    if(Validation.password(password)){
                        try{
                            deger = new JSONtask().execute(strURL).get();
                            //System.out.println(deger);
                            //deger değişkenimizde Api' dan gelen JSON değerimiz var

                            JSONObject jsonResponse = new JSONObject(deger);

                            String hata = Validation.JsonErrorCheck(jsonResponse);


                            JSONObject jsonResponseMessage = jsonResponse.getJSONObject("message");

                            if(hata.equals(String.valueOf("basarili"))){
                                //kullanıcı girisi basarılı
                                String admin = jsonResponseMessage.getString("admin"); // admin bilgisini aldık
                                if(admin.equals(String.valueOf("0"))){//Kullanıcı ise (admin değilse)
                                    String KulAdi = jsonResponseMessage.getString("adi"); //Kullanıcı adını aldık
                                    String id = jsonResponseMessage.getString("id"); //id sini aldık
                                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                                    intent.putExtra("K_Adi",KulAdi);
                                    intent.putExtra("ID",id);
                                    MainActivity.this.startActivity(intent);

                                }else if(admin.equals(String.valueOf("1"))){                                    //Admin ise
                                    String KulAdi = jsonResponseMessage.getString("adi");//Admin adını adlık
                                    String id = jsonResponseMessage.getString("id");//ID bilgisini aldık
                                    Intent intent = new Intent(MainActivity.this, admin_screen.class);
                                    intent.putExtra("ID",id);
                                    MainActivity.this.startActivity(intent);
                                }
                            }else if(hata.equals(String.valueOf("8"))){
                                //giriş Başarısızsa
                                //Hatalı bilgiler
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Kayıt Bulunamadı")
                                        .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                        .create()
                                        .show();
                                etMail.setText("");
                                etpassword.setText("");

                            }else{
                                //Başka bir sorun varsa
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(" BİLİNMEDİK BİR SORUN OLUŞTU")
                                        .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                        .create()
                                        .show();
                                etMail.setText("");
                                etpassword.setText("");
                            }
                        }catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(e.getMessage())
                                    .setNegativeButton("TEKRAR DENEYİNİZ", null)
                                    .create()
                                    .show();
                            etMail.setText("");
                            etpassword.setText("");
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Şifre 8-29 karakter arasında olmalır." +
                                "\nEn az bir büyük harf içermelidir." +
                                "\nEn az bir küçük harf içermelidir."+
                                "\nEn az bir rakam harf içermelidir." +
                                "\nEn az bir '@$^&+=' karakter içermelidir.")
                                .setNegativeButton("Tamam", null)
                                .create()
                                .show();
                        etpassword.setText("");
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Mail adresi formata uygun değildir.")
                            .setNegativeButton("Tamam", null)
                            .create()
                            .show();
                    etMail.setText("");
                }
            }
        });

        // Kayıt ol ekranına yönlendiriliyor
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterScreen.class);
                startActivity(intent);
            }
        });

        // Şifremi unuttum ekranına yönlendiriliyor
        sifremiunuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SunuttumScreen.class);
                startActivity(intent);
            }
        });

        // Misafir girişi ekranına yönlendiriliyor
        misafirgiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MisafirScreen.class);
                startActivity(intent);
                /*Intent intent = new Intent(MainActivity.this, MisafirScreen.class);
                startActivity(intent);*/
            }
        });

    }
}

