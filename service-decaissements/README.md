# Decaissements Service

The Decaissements service manages treasury accounts
and disbursement records. It registers with Eureka as
`decaissements` on port `8083`and uses Eureka-discovered Feign 
requests to validate loan approval and activate the repayment 
schedule in Credit.

## Run locally

```powershell
mvn -f discovery/pom.xml spring-boot:run
mvn -f service-credit/pom.xml spring-boot:run
mvn -f service-decaissements/pom.xml spring-boot:run
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
| `GET` | `/api/disbursements/comptes` | List treasury accounts |
| `GET` | `/api/disbursements/comptes/{id}` | Get a treasury account |
| `POST` | `/api/disbursements/comptes` | Create a treasury account |


Swagger UI is configured at `/swagger-ui.html`
the OpenAPI document is at `/api-docs`.

! The service currently  service uses `/api/disbursements/**`.