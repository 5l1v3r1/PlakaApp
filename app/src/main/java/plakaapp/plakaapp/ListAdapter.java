package plakaapp.plakaapp;

import android.content.Context;
import android.net.Uri;
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
    String hangisi;
    View satirView;

    public ListAdapter(Context context, List list, String hangisi) {
        this.context = context;
        // Layout Inflater tanımlanıyor...
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.hangisi = hangisi;
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


        if(hangisi == "plaka"){
            // Burada inflate işlemi yapılıyor.
            // Tasarımını yaptığımız layout dosyamızı view olarak alıyoruz
            satirView = layoutInflater.inflate(R.layout.home_satir, null);

            // Öğelerimizi satirView'dan çağırıyoruz.
            TextView tv_p = (TextView) satirView.findViewById(R.id.tv_plakalar);

            // Mevcut pozisyon için kisi nesnesi oluşturuluyor.
            Plakalar plaka = (Plakalar) list.get(position);

            // Öğelerimize verilerimizi yüklüyoruz.
            tv_p.setText(plaka.getPlaka());

            // Mevcut satır için işlem tamam ve view return ediliyor.
            return satirView;
        }
        else if(hangisi == "yazi"){
            // Burada inflate işlemi yapılıyor.
            // Tasarımını yaptığımız layout dosyamızı view olarak alıyoruz
            satirView = layoutInflater.inflate(R.layout.yazi_satir, null);

            // Öğelerimizi satirView'dan çağırıyoruz.
            TextView kadi = (TextView) satirView.findViewById(R.id.tv_yazi_kadi);
            TextView yazisi = (TextView) satirView.findViewById(R.id.tv_yazi_yazi);
            TextView konum = (TextView) satirView.findViewById(R.id.tv_yazi_konum);
            TextView tarih = (TextView) satirView.findViewById(R.id.tv_yazi_tarih);
            TextView rep = (TextView) satirView.findViewById(R.id.tv_yazi_rep);

            ImageView iv_kadi = (ImageView) satirView.findViewById(R.id.iv_yazi_kadi);
            ImageView iv_konum = (ImageView) satirView.findViewById(R.id.iv_yazi_konum);
            ImageView iv_rep = (ImageView) satirView.findViewById(R.id.iv_yazi_rep);
            ImageView iv_tarih = (ImageView) satirView.findViewById(R.id.iv_yazi_tarih);

            // Mevcut pozisyon için kisi nesnesi oluşturuluyor.
            Yazilar yazi = (Yazilar) list.get(position);

            // Öğelerimize verilerimizi yüklüyoruz.
            kadi.setText(yazi.getKadi());
            yazisi.setText(yazi.getYazi());
            konum.setText(yazi.getKonum());
            tarih.setText(yazi.getTarih());
            rep.setText(yazi.getRep());

            iv_kadi.setImageResource(R.drawable.ic_person_outline_black_24dp);
            iv_konum.setImageResource(R.drawable.ic_location_on_black_24dp);
            iv_rep.setImageResource(R.drawable.ic_favorite_black_24dp);
            iv_tarih.setImageResource(R.drawable.ic_date_range_black_24dp);

            // Mevcut satır için işlem tamam ve view return ediliyor.
            return satirView;
        }
        else{
            // Burada inflate işlemi yapılıyor.
            // Tasarımını yaptığımız layout dosyamızı view olarak alıyoruz
            satirView = layoutInflater.inflate(R.layout.home_satir, null);

            // Öğelerimizi satirView'dan çağırıyoruz.
            TextView tv_p = (TextView) satirView.findViewById(R.id.tv_plakalar);

            // Mevcut pozisyon için kisi nesnesi oluşturuluyor.
            Plakalar plaka = (Plakalar) list.get(position);

            // Öğelerimize verilerimizi yüklüyoruz.
            tv_p.setText(plaka.getPlaka());

            // Mevcut satır için işlem tamam ve view return ediliyor.
            return satirView;
        }


        //return satirView;
    }

}
