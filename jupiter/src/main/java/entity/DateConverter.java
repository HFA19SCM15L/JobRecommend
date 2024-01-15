package entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConverter {
    public static String convertDateToNewFormat(String originalDateStr) {
        // Define the new and old date formats
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");

        // Set the original format to parse dates in GMT timezone
        originalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            // Parse the original string to Date object
            Date originalDate = originalFormat.parse(originalDateStr);

            // Format the Date object to the new date string
            return targetFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String originalDateStr = "Thu, 14 Dec 2023 08:55:53 GMT";
        String newDateStr = convertDateToNewFormat(originalDateStr);
        if (newDateStr != null) {
            System.out.println("Converted Date: " + newDateStr);
        }
    }
}
