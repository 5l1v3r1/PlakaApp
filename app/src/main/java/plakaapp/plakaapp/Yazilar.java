package plakaapp.plakaapp;

/**
 * Created by cumen on 07.12.2017.
 */

public class Yazilar {
    // Kişi sınıfımız kişiye ait özellikler içeriyor.
    private String yazi;
    private String kadi;
    private String konum;
    private String tarih;
    private String rep;


    // Yapıcı metodumuzda bilgileri alıyoruz.
    public Yazilar(String kadi, String konum, String yazi, String rep, String tarih) {
        this.setKadi(kadi);
        this.setKonum(konum);
        this.setYazi(yazi);
        this.setRep(rep);
        this.setTarih(tarih);
    }

    // Getter setter metodlar
    public String getYazi() {
        return yazi;
    }

    public void setYazi(String yazi) {
        this.yazi = yazi;
    }

    public String getKadi() {
        return kadi;
    }

    public void setKadi(String kadi) {
        this.kadi = kadi;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }
}
