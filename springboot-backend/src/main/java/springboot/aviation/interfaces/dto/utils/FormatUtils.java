package springboot.aviation.interfaces.dto.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FormatUtils {
    
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = 
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    private FormatUtils() {
    }

    public static String formatCpf(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null){
            return null;
        }
        return dateTime.format(DEFAULT_DATE_TIME_FORMAT);
    }
}
