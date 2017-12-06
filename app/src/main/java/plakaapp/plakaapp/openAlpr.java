package plakaapp.plakaapp;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.openalpr.OpenALPR;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by cumen on 03.12.2017.
 */

public class openAlpr extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 100;
    private static final int STORAGE=1;
    private String ANDROID_DATA_DIR;
    private static File destination;
    private TextView resultTextView;
    private ImageView imageView;
    String sonuc;
    private Button cevap_dondur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openalpr);

        cevap_dondur = (Button) findViewById(R.id.cevap_dondur);




        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        resultTextView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        resultTextView.setText("Plaka Taraması Yapmak İçin Lütfen Butona Tıklayınız...");

        cevap_dondur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO: fragment_hizliyazi ya yönlendirelecek ve sonuc gönderilecek

            }
        });

    }
    Context context = this;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            final ProgressDialog progress = ProgressDialog.show(this, "Yükleniyor", "İşlem Sonuçlanıyor...", true);
            final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            // Picasso requires permission.WRITE_EXTERNAL_STORAGE
            Picasso.with(openAlpr.this).load(destination).fit().centerCrop().into(imageView);
            resultTextView.setText("İşleniyor");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    String result = OpenALPR.Factory.create(openAlpr.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("eu", "", destination.getAbsolutePath(), openAlprConfFile, 10);

                    Log.d("OPEN ALPR", result);

                    try {
                        final Results results = new Gson().fromJson(result, Results.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (results == null || results.getResults() == null || results.getResults().size() == 0) {


                                    Toast.makeText(openAlpr.this, "Plaka Algılanamadı...", Toast.LENGTH_LONG).show();
                                    resultTextView.setText("Plaka Algılanamadı...");
                                } else {

                                    cevap_dondur.setVisibility(View.VISIBLE);

                                    sonuc = boslukDoldurma(results.getResults().get(0).getPlate());
                                    resultTextView.setText("Plaka: " + sonuc
                                            // Trim confidence to two decimal places
                                            + " Başarı Oranı: " + String.format("%.2f", results.getResults().get(0).getConfidence()) + "%"
                                            // Convert processing time to seconds and trim to two decimal places
                                            + " İşlem Süresi: " + String.format("%.2f", ((results.getProcessingTimeMs() / 1000.0) % 60)) + " saniye");

                                    SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("var1", sonuc);
                                    editor.commit();



                                }
                            }
                        });

                    } catch (JsonSyntaxException exception) {
                        final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTextView.setText(resultsError.getMsg());
                            }
                        });
                    }

                    progress.dismiss();
                }
            });
        }
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Resmin Analizi İçin Depolama Alanına Erişim Gerekmektedir.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, STORAGE);
        } else { // We already have permissions, so handle as normal
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE:{
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // permission was granted, yay!
                    takePicture();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Resmin Analizi İçin Depolama Alanına Erişim Gerekmektedir.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public void takePicture() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (destination != null) {// Picasso does not seem to have an issue with a null value, but to be safe
            Picasso.with(openAlpr.this).load(destination).fit().centerCrop().into(imageView);
        }


    }

    public String boslukDoldurma(String plaka){
        String orta = "";
        for (int i = 0; i < plaka.length(); i++)
        {
            String karakter = Character.toString(plaka.charAt(i));
            if (!karakter.matches("\\d+(?:\\.\\d+)?"))//sayı değilse
            {
                orta = orta + karakter;
            }
        }
        String yeniOrta = " " + orta + " ";
        plaka = plaka.replace(orta,yeniOrta);
        return plaka;
    }

}
