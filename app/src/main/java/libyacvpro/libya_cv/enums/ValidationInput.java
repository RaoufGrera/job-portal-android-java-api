package libyacvpro.libya_cv.enums;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Asasna on 12/1/2017.
 */

public  class ValidationInput {

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidNOT_EMPTY(String item) {
        String NOT_EMPTY = "(?m)^\\s*\\S+[\\s\\S]*$";

        Pattern pattern = Pattern.compile(NOT_EMPTY);
        Matcher matcher = pattern.matcher(item);
        return matcher.matches();
    }

    public static boolean isValidYear(String year) {
        int yearInt =0;
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        try{
            yearInt =Integer.parseInt(year);
        }catch (NumberFormatException e){
            yearInt=0;
        }
        if (yearInt != 0 && year.length() == 4 && (yearInt >= 1960 && yearInt <=  thisYear)) {
            return true;
        }
        return false;
    }


    public static boolean isValidPhone(String year) {
        int yearInt =0;

        try{
            yearInt =Integer.parseInt(year);
        }catch (NumberFormatException e){
            yearInt=0;
        }
        if (yearInt != 0 && year.length() == 10) { //092 722 30 01
            return true;
        }
        return false;
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
    public static boolean isValidWebSite(String web) {
        String WEB_PATTERN =  "^(https?|http?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern pattern = Pattern.compile(WEB_PATTERN);
        Matcher matcher = pattern.matcher(web);
        return matcher.matches();
    }


}
