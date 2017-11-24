package plakaapp.plakaapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by cumen on 23.11.2017.
 */

public class fragment_bildirim extends Fragment {
    public fragment_bildirim() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bildirim, container, false);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //listview dolduruluyor
         String[] bildirimler =
                {"Türkiye", "Almanya", "Avusturya", "Amerika","İngiltere",
                        "Macaristan", "Yunanistan", "Rusya", "Suriye", "İran", "Irak",
                        "Şili", "Brezilya", "Japonya", "Portekiz", "İspanya",
                        "Makedonya", "Ukrayna", "İsviçre"};


        ListView listemiz=(ListView) view.findViewById(R.id.lv_bildirimler);

        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, bildirimler);

        listemiz.setAdapter(veriAdaptoru);

        //listview dolduruluyor



        return view;
    }
}