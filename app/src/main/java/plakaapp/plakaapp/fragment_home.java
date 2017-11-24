package plakaapp.plakaapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cumen on 23.11.2017.
 */

public class fragment_home extends Fragment {





    public fragment_home() {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        Intent intent = getActivity().getIntent();
        String kuladi = intent.getStringExtra("K_Adi");//kullanıcı adı
        String id = intent.getStringExtra("ID"); //kullanıcı idsi
        final TextView hosgeldiniz = (TextView) view.findViewById(R.id.tv_hosgeldiniz);
        hosgeldiniz.setText("Hoşgeldiniz " + kuladi);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi


        ListView liste = (ListView) view.findViewById(R.id.lv_plakalar);

        List plakalar = new ArrayList<>();
        plakalar.add(new Plakalar("35 DJF 01"));
        plakalar.add(new Plakalar("35 DJF 02"));
        plakalar.add(new Plakalar("35 DJF 03"));
        plakalar.add(new Plakalar("35 DJF 04"));
        plakalar.add(new Plakalar("35 DJF 05"));
        plakalar.add(new Plakalar("35 DJF 06"));
        plakalar.add(new Plakalar("35 DJF 07"));
        plakalar.add(new Plakalar("35 DJF 08"));
        plakalar.add(new Plakalar("35 DJF 09"));

        ListAdapter adapter = new ListAdapter(getActivity(), plakalar);
        liste.setAdapter(adapter);

        //bu kodun hep sonda olması gerekli
        return view;
    }
}
