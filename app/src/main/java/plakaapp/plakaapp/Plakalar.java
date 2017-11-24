package plakaapp.plakaapp;

/**
 * Created by cumen on 23.11.2017.
 */

public class Plakalar {
    // Kişi sınıfımız kişiye ait özellikler içeriyor.
    private String plaka;

    // Yapıcı metodumuzda bilgileri alıyoruz.
    public Plakalar(String plaka) {
        this.setPlaka(plaka);
    }

    // Getter setter metodlar
    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String ad) {
        this.plaka = ad;
    }
}
