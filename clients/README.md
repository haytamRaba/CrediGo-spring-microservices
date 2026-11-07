# Clients Service

The Clients service manages client records and their collateral.
Credit calls this service through Eureka-discovered Feign
requests to look up a client and retrieve collateral details.

## Run locally

Run each command in a separate terminal, in order:

```powershell
mvn -f discovery/pom.xml spring-boot:run
mvn -f clients/pom.xml spring-boot:run
```

The service registers as `clients` on port `8081`. It uses Spring Data JPA and H2. With the datasource properties currently commented out, Spring Boot uses its embedded in-memory H2 database defaults.

## HTTP API

Base path: `/api/clients`

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/clients` | Create a client |
| `GET` | `/api/clients` | List clients |
| `GET` | `/api/clients/{id}` | Get a client by ID |
| `GET` | `/api/clients/email/{email}` | Get a client by email |
| `PUT` | `/api/clients/{id}` | Update a client |
| `DELETE` | `/api/clients/{id}` | Delete a client |
| `POST` | `/api/clients/{clientId}/collaterals` | Add collateral to a client |
| `GET` | `/api/clients/collaterals` | List collateral |
| `GET` | `/api/clients/collaterals/{id}` | Get collateral by ID |
| `GET` | `/api/clients/collaterals/client/{clientId}` | List collateral for a client |
| `PUT` | `/api/clients/collaterals/{id}` | Update collateral |
| `DELETE` | `/api/clients/collaterals/{id}` | Delete collateral |

Swagger UI is configured at `/swagger-ui.html` the OpenAPI document is at `/api-docs`.
