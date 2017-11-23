package plakaapp.plakaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cumen on 23.11.2017.
 */

public class fragment_bildirim extends Fragment {
    public fragment_bildirim() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bildirim, container, false);
    }
}