# flights_app

Sistema de gerenciamento de clientes, aeroportos, companhias aéreas, voos e reservas.

---

## Tecnologias

| Camada | Tecnologia |
|--------|------------|
| Backend | Spring Boot, Hibernate, JPA |
| Frontend | Angular |
| API | REST |
| Banco | H2 / PostgreSQL |
| Testes | JUnit, Jasmine/Karma |

---

## Como rodar

### Backend
```bash
cd springboot-backend
mvn clean install
mvn spring-boot:run
```
### Frontend
```bash
cd angular-frontend
npm install
npx ng serve