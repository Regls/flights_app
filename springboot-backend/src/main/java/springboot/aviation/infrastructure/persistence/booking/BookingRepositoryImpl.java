package springboot.aviation.infrastructure.persistence.booking;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Repository;

import springboot.aviation.domain.client.Client;
import springboot.aviation.domain.flight.Flight;
import springboot.aviation.infrastructure.persistence.client.ClientEntity;
import springboot.aviation.infrastructure.persistence.flight.FlightEntity;
import springboot.aviation.domain.booking.Booking;
import springboot.aviation.infrastructure.mapper.BookingMapper;
import springboot.aviation.infrastructure.persistence.client.ClientJpaRepository;
import springboot.aviation.infrastructure.persistence.flight.FlightJpaRepository;
import springboot.aviation.domain.booking.BookingRepository;
import springboot.aviation.domain.booking.BookingStatus;


@Repository
public class BookingRepositoryImpl implements BookingRepository{
    
    private final BookingJpaRepository bookingJpaRepository;
    private final ClientJpaRepository clientJpaRepository;
    private final FlightJpaRepository flightJpaRepository;

    public BookingRepositoryImpl(BookingJpaRepository bookingJpaRepository,
            ClientJpaRepository clientJpaRepository,
            FlightJpaRepository flightJpaRepository
    ) {
        this.bookingJpaRepository = bookingJpaRepository;
        this.clientJpaRepository = clientJpaRepository;
        this.flightJpaRepository = flightJpaRepository;
    }

    @Override
    public List<Booking> findAll() {
        return bookingJpaRepository.findAll()
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingJpaRepository.findById(id)
                .map(BookingMapper::toDomain);
    }

    @Override
    public Optional<Booking> findByBookingCode(String bookingCode) {
        return bookingJpaRepository.findByBookingCode(bookingCode)
                .map(BookingMapper::toDomain);
    }

    @Override
    public List<Booking> findActiveByFlight(Long flightId) {
        FlightEntity flightEntity = flightJpaRepository.getReferenceById(flightId);
        return bookingJpaRepository.findActiveByFlight(flightEntity)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findActiveByClient(Long clientId) {
        ClientEntity clientEntity = clientJpaRepository.getReferenceById(clientId);
        return bookingJpaRepository.findActiveByClient(clientEntity)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }

    @Override
    public Booking save(Booking booking) {

        BookingEntity entity = BookingMapper.toEntity(booking, 
            clientJpaRepository.getReferenceById(booking.getClient().getId()),
            flightJpaRepository.getReferenceById(booking.getFlight().getId())
        );
        BookingEntity saved = bookingJpaRepository.save(entity);
        return BookingMapper.toDomain(saved);
    }
    
    @Override
    public boolean existsByBookingCode(String bookingCode) {
        return bookingJpaRepository.existsByBookingCode(bookingCode);
    }
    
    @Override
    public boolean existsByClientAndFlightAndStatusIn(Client client, Flight flight, List<BookingStatus> statuses) {
        ClientEntity clientEntity = clientJpaRepository.getReferenceById(client.getId());
        FlightEntity flightEntity = flightJpaRepository.getReferenceById(flight.getId());
        return bookingJpaRepository.existsByClientAndFlightAndStatusIn(clientEntity, flightEntity, statuses);
    }
}
