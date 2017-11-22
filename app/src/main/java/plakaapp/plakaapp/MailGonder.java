package plakaapp.plakaapp;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by cumen on 21.11.2017.
 */

public class MailGonder extends Activity {

    public static boolean mgonder(String baslik, String icerik , String mailler[], Activity act){

        try {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, baslik);//Email konusu
            emailIntent.putExtra(Intent.EXTRA_TEXT, icerik);//Email içeriği
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mailler);
            act.startActivity(Intent.createChooser(emailIntent, "E-mail Göndermek için Seçiniz:")); //birden fazla email uygulaması varsa seçmek için
            //act.startActivity(emailIntent);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
