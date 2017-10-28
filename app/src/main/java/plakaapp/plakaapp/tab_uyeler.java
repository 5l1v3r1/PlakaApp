package plakaapp.plakaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Simulakra on 27.10.2017.
 */

public class tab_uyeler extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_uyeler);

        Button bttt= (Button) findViewById(R.id.bt_ekle);
        bttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog= ProgressDialog.show(tab_uyeler.this, "Ekleme İşlemi",
                        "Loading. Please wait...", true);

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ekleme İşlemi Tamamlandı", Toast.LENGTH_LONG).show();
            }
        });
    }



}
