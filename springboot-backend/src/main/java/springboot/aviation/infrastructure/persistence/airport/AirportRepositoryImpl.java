package springboot.aviation.infrastructure.persistence.airport;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Repository;

import springboot.aviation.domain.airport.Airport;
import springboot.aviation.infrastructure.mapper.AirportMapper;
import springboot.aviation.domain.airport.AirportRepository;

@Repository
public class AirportRepositoryImpl implements AirportRepository{
    
    private final AirportJpaRepository jpaRepository;

    public AirportRepositoryImpl(AirportJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Airport> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(AirportMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Airport> findById(Long id) {
        return jpaRepository.findById(id)
                .map(AirportMapper::toDomain);
    }

    @Override
    public Optional<Airport> findByIataCode(String iataCode) {
        return jpaRepository.findByIataCode(iataCode)
                .map(AirportMapper::toDomain);
    }

    @Override
    public Airport save(Airport airport) {
        AirportEntity entity = AirportMapper.toEntity(airport);
        AirportEntity saved = jpaRepository.save(entity);
        return AirportMapper.toDomain(saved);
    }

    @Override
    public boolean existsByIataCode(String iataCode) {
        return jpaRepository.existsByIataCode(iataCode);
    }
}
