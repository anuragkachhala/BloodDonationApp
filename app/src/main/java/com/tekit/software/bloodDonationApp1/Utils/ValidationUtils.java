package com.tekit.software.bloodDonationApp1.Utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean mobileValidation(String mobileNo){
        if(mobileNo.isEmpty()){
            return false;
        }if(mobileNo.length()<10){
            return false;
        }
        return true;
    }

    public static boolean checkEmpty(String value) {
        if (value.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Pattern pattern;


    public static boolean validateEmail(String emailID) {

        pattern = Pattern.compile(Constant.EMAIL_VALIDATION_REGEX);
        return pattern.matcher(emailID).matches();

    }

    public static boolean passwordValidation(String password){
        if(password.length()<8){
            return false;
        }
        return true;

    }

    public static String dateToString(String date){



        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(stringToDate(date));

    }


    public static Date stringToDate(String date){
        Date date1 = null;
        try {
            date1 =new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1 ;
    }

}
