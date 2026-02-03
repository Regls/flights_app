package springboot.aviation.infrastructure.persistence.booking.generator;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import springboot.aviation.application.booking.port.BookingCodeGenerator;


@Component
public class RandomBookingCodeGenerator implements BookingCodeGenerator {
    
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int SIZE = 6;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(SIZE);
        for (int i = 0; i < SIZE; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
