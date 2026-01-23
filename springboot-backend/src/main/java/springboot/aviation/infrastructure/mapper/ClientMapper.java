package springboot.aviation.infrastructure.mapper;

import springboot.aviation.domain.client.Client;
import springboot.aviation.infrastructure.persistence.client.ClientEntity;
import springboot.aviation.domain.client.ClientStatus;

public class ClientMapper {
    
    public static Client toDomain(ClientEntity entity) {
        return Client.restore(
            entity.getId(),
            entity.getCpf(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getStatus()
        );
    }

    public static ClientEntity toEntity(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setCpf(client.getCpf());
        entity.setFirstName(client.getFirstName());
        entity.setLastName(client.getLastName());
        entity.setStatus(client.isActive()
            ? ClientStatus.ACTIVE
            : ClientStatus.INACTIVE
        );
        return entity;
    }
}
