package plakaapp.plakaapp;

/**
 * Created by Simulakra on 28.10.2017.
 */

public class Config {

    public static final String BASE_URL = "http://plakaapp.cf:3000";
    public static final String KLISTELE_URL = BASE_URL + "/Klistele";
    public static final String GIRIS_URL = BASE_URL + "/Kgiris";
    public static final String SORULISTELE = BASE_URL + "/Sorulistele";

    public static String Kekle_URL(String K_Adi,String K_Parola,String K_Mail,String K_Soru,String K_Cevap)
    {
        return (BASE_URL+"/Kekle"+"/"+K_Adi+"/"+K_Parola+"/"+K_Mail+"/"+K_Soru+"/"+K_Cevap).replaceAll("\\+","%20");
    }
    public static String Kguncelle_URL(String ID,String Admin,String K_Adi,String K_Parola,
                                       String K_Rep,String K_Mail,String K_Soru,String K_Cevap)
    {
        return (BASE_URL+"/Kguncelle"+"/"+ID+"/"+Admin+"/"+K_Adi+"/"+K_Parola
                +"/"+K_Rep+"/"+K_Mail+"/"+K_Soru+"/"+K_Cevap).replaceAll("\\+","%20");
    }
    public static String Ksil_URL(String ID)
    {
        return BASE_URL+"/Ksil"+"/"+ID;
    }

    public static String Soruekle_URL(String SoruMetin)
    {
        return BASE_URL+"/Soruekle"+"/"+SoruMetin.replaceAll("\\+","%20");
    }
    public static String Soruguncelle_URL(String ID, String SoruMetin)
    {
        return BASE_URL+"/Soruguncelle"+"/"+ID+"/"+SoruMetin.replaceAll("\\+","%20");
    }
    public static String Sorusil_URL(String ID)
    {
        return BASE_URL+"/Sorusil"+"/"+ID;
    }
}
