package com.example.prmproject.util;

import java.text.NumberFormat;
import java.util.Locale;

public class format {
    public static String formatPrice (int number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"));
        String formattedNumber = formatter.format(number);
        return formattedNumber + " VNĐ";
    };
}
