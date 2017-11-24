package plakaapp.plakaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cumen on 23.11.2017.
 */

public class ListAdapter extends BaseAdapter{
    // Veri olarak kişi listesini kullanacak.
    private List list;
    LayoutInflater layoutInflater;
    Context context;

    public ListAdapter(Context context, List list) {
        this.context = context;
        // Layout Inflater tanımlanıyor...
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Burada inflate işlemi yapılıyor.
        // Tasarımını yaptığımız layout dosyamızı view olarak alıyoruz
        View satirView = layoutInflater.inflate(R.layout.home_satir, null);

        // Öğelerimizi satirView'dan çağırıyoruz.
        TextView tv_p = (TextView) satirView.findViewById(R.id.tv_plakalar);

        // Mevcut pozisyon için kisi nesnesi oluşturuluyor.
        Plakalar plaka = (Plakalar) list.get(position);

        // Öğelerimize verilerimizi yüklüyoruz.
        tv_p.setText(plaka.getPlaka());

        // Mevcut satır için işlem tamam ve view return ediliyor.
        return satirView;
    }

}
