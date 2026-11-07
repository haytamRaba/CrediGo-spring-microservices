# Credit Service

The Credit service manages loan applications, their approval state, and repayment schedules. It registers with Eureka as `credit` on port `8082`.

## Run locally

Run each command in a separate terminal, in order:

```powershell
mvn -pl config-server spring-boot:run
mvn -pl discovery spring-boot:run
mvn -pl clients spring-boot:run
mvn -pl service-credit spring-boot:run
```

Credit uses Spring Data JPA with an in-memory H2 database named `creditdb`. Data is lost when the process stops. H2 Console is enabled at `/h2-console-credit`.

## Service calls

- Calls `CLIENTS` to look up clients and collateral.
- Calls `DECAISSEMENTS` to include disbursement data in an enriched loan response.
- Exposes loan validation and schedule activation endpoints used by Decaissements.

These calls use Feign and Eureka; Credit has no Maven dependency on the other services.

## HTTP API

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/loan-applications` | Create a loan application |
| `GET` | `/api/loan-applications` | List loan applications |
| `GET` | `/api/loan-applications/{id}` | Get a loan application |
| `PUT` | `/api/loan-applications/{id}/etudier` | Move an application into study |
| `PUT` | `/api/loan-applications/{id}/accorder` | Approve an application |
| `PUT` | `/api/loan-applications/{id}/refuser` | Reject an application |
| `PUT` | `/api/loan-applications/{id}/bloquer` | Block an approved application |
| `PUT` | `/api/loan-applications/{id}/retirer` | Withdraw an approved application |
| `GET` | `/api/loan-applications/statut/{statut}` | List by status |
| `POST` | `/api/loan-applications/{id}/remarque` | Generate a repayment schedule |
| `POST` | `/api/loan-applications/{id}/disbursement-validation?montant={amount}` | Validate approval and amount for a disbursement |
| `POST` | `/api/loan-applications/{id}/activate-schedule` | Activate the repayment schedule |
| `GET` | `/api/enriched/loan-applications/{id}` | Get the loan with client, collateral, schedule, and disbursement data |

OpenAPI document is at `/api-docs`.

