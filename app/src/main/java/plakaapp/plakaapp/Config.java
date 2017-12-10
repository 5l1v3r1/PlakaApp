package plakaapp.plakaapp;

/**
 * Created by Simulakra on 28.10.2017.
 * Coded by berke & cumen
 */

public class Config {

    public static final String BASE_URL = "http://plakaapp.cf:3000";
    public static final String KLISTELE_URL = BASE_URL + "/Klistele";
    public static final String GIRIS_URL = BASE_URL + "/Kgiris";
    public static final String SORULISTELE = BASE_URL + "/Sorulistele";
    public static final String CLISTELE_URL = BASE_URL + "/Clistele";
    public static final String TLISTELE_URL = BASE_URL + "/Tlistele";
    public static final String PLISTELE_URL = BASE_URL + "/Plistele";
    public static final String YLISTELE_URL = BASE_URL + "/Ylistele";
    public static final String ILLISTELE_URL = BASE_URL + "/Ilistele";
    public static final String TaLISTELE_URL = BASE_URL + "/Takiplistele";
    public static final String SiLISTELE_URL = BASE_URL + "/Sikayetlistele";
    //////////////////////////////////////////////////////////////
    public static String Kekle_URL(String K_Adi, String K_Parola, String K_Mail, String K_Soru, String K_Cevap) {
        return (BASE_URL + "/Kekle" + "/" + K_Adi + "/" + K_Parola + "/" + K_Mail + "/" + K_Soru + "/" + K_Cevap).replaceAll("\\+", "%20");
    }

    public static String Kguncelle_URL(String ID, String Admin, String K_Adi, String K_Parola, String K_Rep, String K_Mail, String K_Soru, String K_Cevap) {
        return (BASE_URL + "/Kguncelle" + "/" + ID + "/" + Admin + "/" + K_Adi + "/" + K_Parola + "/" + K_Rep + "/" + K_Mail + "/" + K_Soru + "/" + K_Cevap).replaceAll("\\+", "%20");
    }

    public static String Ksil_URL(String ID) {
        return BASE_URL + "/Ksil" + "/" + ID;
    }
    //////////////////////////////////////////////////////////////
    public static String Soruekle_URL(String SoruMetin) {
        return BASE_URL + "/Soruekle" + "/" + SoruMetin.replaceAll("\\+", "%20");
    }

    public static String Soruguncelle_URL(String ID, String SoruMetin) {
        return BASE_URL + "/Soruguncelle" + "/" + ID + "/" + SoruMetin.replaceAll("\\+", "%20");
    }

    public static String Sorusil_URL(String ID) {

        return BASE_URL + "/Sorusil" + "/" + ID;
    }
    //////////////////////////////////////////////////////////////
    public static String Cekle_URL(String AracCins) {
        return (BASE_URL + "/Cekle" + "/" + AracCins).replaceAll("\\+", "%20");
    }

    public static String Cguncelle_URL(String ID, String AracCins) {
        return (BASE_URL + "/Cguncelle" + "/" + ID + "/" + AracCins).replaceAll("\\+", "%20");
    }

    public static String Csil_URL(String ID) {
        return (BASE_URL + "/Csil" + "/" + ID).replaceAll("\\+", "%20");
    }
    //////////////////////////////////////////////////////////////
    public static String Tekle_URL(String CinsID, String TurAdi) {
        return (BASE_URL + "/Tekle" + "/" + CinsID + "/" + TurAdi).replaceAll("\\+", "%20");
    }

    public static String Tguncelle_URL(String ID, String CinsID, String TurAdi) {
        return (BASE_URL + "/Tguncelle" + "/" + ID + "/" + CinsID + "/" + TurAdi).replaceAll("\\+", "%20");
    }

    public static String Tsil_URL(String ID) {
        return (BASE_URL + "/Tsil" + "/" + ID).replaceAll("\\+", "%20");
    }
    //////////////////////////////////////////////////////////////
    public static String Pekle_URL(String Plaka, String CinsID, String TurID, String AracRengi) {
        return (BASE_URL + "/Pekle" + "/" + Plaka + "/" + CinsID + "/" + TurID + "/" + AracRengi).replaceAll("\\+", "%20");
    }

    public static String Pguncelle_URL(String ID, String Plaka, String CinsID, String TurID, String AracRengi) {
        return (BASE_URL + "/Pguncelle" + "/" + ID + "/" + Plaka + "/" + CinsID + "/" + TurID + "/" + AracRengi).replaceAll("\\+", "%20");
    }

    public static String Psil_URL(String ID) {
        return (BASE_URL + "/Psil" + "/" + ID).replaceAll("\\+", "%20");
    }
    //////////////////////////////////////////////////////////////
    public static String Yekle_URL(String PlakaID, String YazarID, String KonumID, String Yazi) {
        return (BASE_URL + "/Yekle" + "/" + PlakaID + "/" + YazarID + "/" + KonumID + "/" + Yazi).replaceAll("\\+", "%20");
    }

    public static String Yguncelle_URL(String ID, String PlakaID, String YazarID, String KonumID, String Yazi, String Rep) {
        return (BASE_URL + "/Yguncelle" + "/" + ID + "/" + PlakaID + "/" + YazarID + "/" + KonumID + "/" + Yazi + "/" + Rep).replaceAll("\\+", "%20");
    }

    public static String Ysil_URL(String ID) {
        return (BASE_URL + "/Ysil" + "/" + ID).replaceAll("\\+", "%20");
    }
    //////////////////////////////////////////////////////////////
    public static String Taekle_URL(String UyeID,String PlakaID) {
        return (BASE_URL + "/Takipekle" + "/" + UyeID+ "/" + PlakaID).replaceAll("\\+", "%20");
    }
    public static String Taguncelle_URL(String UyeID,String PlakaID) {
        return (BASE_URL + "/Takipguncelle" + "/" + UyeID+ "/" + PlakaID).replaceAll("\\+", "%20");
    }
    public static String Tasil_URL(String UyeID,String PlakaID) {
        return (BASE_URL + "/Takipsil" + "/" + UyeID+ "/" + PlakaID).replaceAll("\\+", "%20");
    }
    //////////////////////////////////////////////////////////////
    public static String PlakaSorgula_URL(String Plaka)
    {
        return BASE_URL+"/Psorgula"+"/"+Plaka.replaceAll("\\+","%20");
    }
    //////////////////////////////////////////////////////////////

    public static String SikayetSil(String SikayetID) {
        return (BASE_URL + "/Sikayetsil" + "/" + SikayetID).replaceAll("\\+", "%20");
    }
    public static String Sikayetban(String SikayetID, String Rep) {
        return (BASE_URL + "/Sikayetban" + "/" + SikayetID+ "/" + Rep).replaceAll("\\+", "%20");
    }
}
