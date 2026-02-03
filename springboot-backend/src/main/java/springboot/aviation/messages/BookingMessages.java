package springboot.aviation.messages;

import static springboot.aviation.messages.CommonMessages.*;


public final class BookingMessages {
    public static final String CLIENT_REQUIRED = "Client" + REQUIRED;
    public static final String FLIGHT_REQUIRED = "Flight" + REQUIRED;
    public static final String BOOKING_CODE_REQUIRED = "Booking code" + REQUIRED;
    public static final String BOOKING_CODE_INVALID = "Booking code format invalid";
    public static final String CLIENT_ACTIVE = "Client must be active to make a booking";
    public static final String FLIGHT_SCHEDULED = "Can only book flights that are scheduled";
    public static final String CONFIRM_ONLY_CREATED = "Only created bookings can be confirmed";
    public static final String CONFIRM_ONLY_FLIGHT_SCHEDULED = "Cannot confirm booking for a flight that is not scheduled";
    public static final String CANCELLED_CANNOT_CHANGE = "Cancelled bookings cannot be changed";
    public static final String CANCEL_ONLY_CREATED_OR_CONFIRMED = "Only created or confirmed bookings can be cancelled";
    public static final String CANCEL_ONLY_FLIGHT_SCHEDULED = "Cannot cancel booking for a flight that is not scheduled";
}
