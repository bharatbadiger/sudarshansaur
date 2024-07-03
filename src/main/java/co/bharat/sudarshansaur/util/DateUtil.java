package co.bharat.sudarshansaur.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatDate(Date date){
        return dateFormat.format(date);
    }
}
