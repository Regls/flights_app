package springboot.aviation.infrastructure.persistence.airline;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Repository;

import springboot.aviation.domain.airline.Airline;
import springboot.aviation.infrastructure.mapper.AirlineMapper;
import springboot.aviation.domain.airline.AirlineRepository;

@Repository
public class AirlineRepositoryImpl implements AirlineRepository{
    
    private final AirlineJpaRepository jpaRepository;

    public AirlineRepositoryImpl(AirlineJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Airline> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(AirlineMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Airline> findById(Long id) {
        return jpaRepository.findById(id)
                .map(AirlineMapper::toDomain);
    }

    @Override
    public Optional<Airline> findByIataCode(String iataCode) {
        return jpaRepository.findByIataCode(iataCode)
                .map(AirlineMapper::toDomain);
    }

    @Override
    public Airline save(Airline airport) {
        AirlineEntity entity = AirlineMapper.toEntity(airport);
        AirlineEntity saved = jpaRepository.save(entity);
        return AirlineMapper.toDomain(saved);
    }

    @Override
    public boolean existsByIataCode(String iataCode) {
        return jpaRepository.existsByIataCode(iataCode);
    }
}
