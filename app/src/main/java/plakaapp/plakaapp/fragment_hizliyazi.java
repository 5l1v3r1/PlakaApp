package plakaapp.plakaapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by cumen on 23.11.2017.
 */

public class fragment_hizliyazi extends Fragment {
    public fragment_hizliyazi() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hizliyazi, container, false);

        //Logoya yazı fontu eklendi
        TextView Logo = (TextView) view.findViewById(R.id.Logo);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/daysoneregular.ttf");
        Logo.setTypeface(typeface);
        //Logoya yazı fontu eklendi

        //hizli yazıya sınır koymak
        final EditText hizliyazi = (EditText) view.findViewById(R.id.et_hizliyazi);
        int maxLength_hizliyazi = 250;
        InputFilter[] FilterArray_hizliyazi = new InputFilter[1];
        FilterArray_hizliyazi[0] = new InputFilter.LengthFilter(maxLength_hizliyazi);
        hizliyazi.setFilters(FilterArray_hizliyazi);
        //hizli yazıya sınır koymak

        //plakaya sınır koymak
        final EditText plaka = (EditText) view.findViewById(R.id.et_plakaNumarasi);
        int maxLength_plaka = 10;
        InputFilter[] FilterArray_plaka = new InputFilter[1];
        FilterArray_plaka[0] = new InputFilter.LengthFilter(maxLength_plaka);
        plaka.setFilters(FilterArray_plaka);
        //plaka yazıya sınır koymak





        return view;

    }
}
