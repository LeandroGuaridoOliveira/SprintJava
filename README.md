# Clyvo VET API - Challenge FIAP 2026

API RESTful para jornada contínua de saúde pet, desenvolvida com Java e Spring Boot como parte do Challenge FIAP 2026 em parceria com a Clyvo VET.

## Integrantes

| Nome | RM |
|------|-----|
| Gabriel Costa Solano | 562325 |
| Kaiky Pereira Rodrigues Da Silva | 564578 |
| Leandro Guarido de Oliveira | 561760 |

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA + H2 Database
- Bean Validation
- Spring Cache
- Springdoc OpenAPI (Swagger)
- Lombok

## Como executar

```bash
mvn spring-boot:run
```

A API inicia em `http://localhost:8080`

## Documentação

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs

## Endpoints

| Recurso | URL Base |
|---------|----------|
| Clínicas | `/api/clinicas` |
| Veterinários | `/api/veterinarios` |
| Tutores | `/api/tutores` |
| Pets | `/api/pets` |
| Agendamentos | `/api/agendamentos` |
| Eventos de Saúde | `/api/eventos-saude` |
| Protocolos | `/api/protocolos` |

Todos os endpoints suportam paginação (`?page=0&size=10`), ordenação (`?sort=nome,asc`) e busca por parâmetros.

## Testes

A coleção Postman está em `documentos/ClyvoVET-API.postman_collection.json`. Importe no Postman para testar todos os endpoints.
