package plakaapp.plakaapp;

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
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cumen on 23.11.2017.
 * Codded by berke on 25.11.2017.
 */

public class fragment_profil extends Fragment {
    String K_ID, K_Ad;
    Boolean e1=false,e2=false,e3=false,e4=false,e5=false;
    public JSONArray uyeler, sorular;
    public fragment_profil() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profil, container, false);

        Intent intent = getActivity().getIntent();
        K_Ad = intent.getStringExtra("K_Adi");//kullanıcı adı
        K_ID = intent.getStringExtra("ID"); //kullanıcı id

        SorularSpinnerDoldur(view);
        UyeBilgiDoldur(view);
        CreateControlListener(view);
        e1=e2=e3=e4=e5=false;
        CreateButtonListener(view);

        return view;
    }

    private void CreateButtonListener(View view) {

    }

    private void CreateControlListener(View view) {
        ((EditText) view.findViewById(R.id.et_profil_kuladi)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                e1=true;
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
                e2=true;
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
                e3=true;
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
                e5=true;
            }
        });
        ((Spinner) view.findViewById(R.id.sp_profil_gsorusu)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                e4=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void UyeBilgiDoldur(View view) {
        try {
            uyeler= new JSONArray(new JSONtask().execute(Config.KLISTELE_URL).get());
            for (int i = 0; i < uyeler.length(); i++) {
                JSONObject jsonSoru = uyeler.getJSONObject(i).getJSONObject("message");

                ((EditText) view.findViewById(R.id.et_profil_kuladi)).setText(jsonSoru.getString("K_Adi"));
                ((EditText) view.findViewById(R.id.et_profil_email)).setText(jsonSoru.getString("K_Mail"));
                ((EditText) view.findViewById(R.id.et_profil_parola)).setText("");
                ((EditText) view.findViewById(R.id.et_profil_gcevap)).setText(jsonSoru.getString("K_Cevap"));
                SpinnerPush(jsonSoru.getString("K_Soru"), view);
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    private void SpinnerPush(String ID,View view) {
        try{
            if(ID.isEmpty()) return;

            Spinner soru = ((Spinner) view.findViewById(R.id.sp_profil_gsorusu));

            int arrayPos=0;
            for (int i=0;i<sorular.length();i++)
            {
                if(sorular.getJSONObject(i).getJSONObject("message").getString("ID")==ID)
                {
                    arrayPos=i;
                    break;
                }
            }

            for (int i=0;i<soru.getCount();i++)
            {
                if(soru.getItemAtPosition(i).toString()==
                        sorular.getJSONObject(arrayPos).getJSONObject("message").getString("SoruMetin"))
                {
                    soru.setSelection(i);
                    return;
                }
            }
        }catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(e.getMessage())
                    .setNegativeButton("Tamam", null)
                    .create()
                    .show();
        }
    }

    public void SorularSpinnerDoldur(View view) {
        try {
            sorular = new JSONArray(new JSONtask().execute(Config.SORULISTELE).get());
            Spinner soruListe = (Spinner) view.findViewById(R.id.sp_profil_gsorusu);
            List<String> result = new ArrayList<String>();

            for (int i = 0; i < sorular.length(); i++) {
                JSONObject jsonSoru = sorular.getJSONObject(i);

                String soruMetni=jsonSoru.getJSONObject("message").getString("SoruMetin");
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
}