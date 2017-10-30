package plakaapp.plakaapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cumen on 30.10.2017.
 */

public class Validation {


    public static boolean userValidate(final String username){

        Pattern pattern;
        Matcher matcher;
        String USERNAME_PATTERN = "^[A-Za-z][A-Za-z0-9_-]{5,29}$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();

    }

    public static boolean email(final String mail){

        Pattern pattern;
        Matcher matcher;
        String USERNAME_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(mail);
        return matcher.matches();

    }

    public static boolean password(final String pass){


        Pattern pattern;
        Matcher matcher;
        String USERNAME_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,29}$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(pass);
        return matcher.matches();

    }
}
