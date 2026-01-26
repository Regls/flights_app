package springboot.aviation.infrastructure.persistence.client;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Repository;

import springboot.aviation.domain.client.Client;
import springboot.aviation.infrastructure.mapper.ClientMapper;
import springboot.aviation.domain.client.ClientRepository;


@Repository
public class ClientRepositoryImpl implements ClientRepository {
    
    private final ClientJpaRepository jpaRepositoy;

    public ClientRepositoryImpl(ClientJpaRepository jpaRepositoy) {
        this.jpaRepositoy = jpaRepositoy;
    }

    @Override
    public List<Client> findAll() {
        return jpaRepositoy.findAll()
                .stream()
                .map(ClientMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Client> findById(Long id) {
        return jpaRepositoy.findById(id)
                .map(ClientMapper::toDomain);
    }

    @Override
    public Optional<Client> findByCpf(String cpf) {
        return jpaRepositoy.findByCpf(cpf)
                .map(ClientMapper::toDomain);
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = ClientMapper.toEntity(client);
        ClientEntity saved = jpaRepositoy.save(entity);
        return ClientMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepositoy.existsByCpf(cpf);
    }
}
