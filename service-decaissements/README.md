# Decaissements Service

The Decaissements service manages treasury accounts
and disbursement records. It registers with Eureka as
`decaissements` on port `8083`and uses Eureka-discovered Feign 
requests to validate loan approval and activate the repayment 
schedule in Credit.

## Run locally

```powershell
mvn -pl config-server spring-boot:run
mvn -pl discovery spring-boot:run
mvn -pl clients spring-boot:run
mvn -pl service-credit spring-boot:run
mvn -pl service-decaissements spring-boot:run
```

## HTTP API

Base path: `/api/disbursements`

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/api/disbursements` | List disbursements |
| `GET` | `/api/disbursements/{id}` | Get a disbursement |
| `GET` | `/api/disbursements/loan-application/{loanApplicationId}` | List disbursements for a loan |
| `GET` | `/api/disbursements/treasury-account/{treasuryAccountId}` | List disbursements for a treasury account |
| `GET` | `/api/disbursements/statut/{statut}` | List disbursements by status |
| `POST` | `/api/disbursements` | Validate an approved loan and create a disbursement |
| `GET` | `/api/disbursements/comptes` | List treasury accounts |
| `GET` | `/api/disbursements/comptes/{id}` | Get a treasury account |
| `POST` | `/api/disbursements/comptes` | Create a treasury account |


Swagger UI is configured at `/swagger-ui.html`
the OpenAPI document is at `/api-docs`.

! The service currently  service uses `/api/disbursements/**`.
