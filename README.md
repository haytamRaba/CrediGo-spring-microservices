# CreditGo SPRING Microservices

CreditGo is a Spring Boot microservices project built from independently deployable services. Eureka provides service discovery, and the API Gateway is the intended entry point for external requests.

---

## Architecture

```mermaid
flowchart LR
    User[Web or API client] --> Gateway[API Gateway :8080]
    Gateway -->|service discovery| Eureka[Eureka :8761]
    Eureka -. registered instances .-> Clients[Clients service :8081]
    Eureka -. registered instances .-> Credit[Credit service :8082]
    Eureka -. registered instances .-> Disbursements[Decaissements service :8083]
    Gateway --> Clients
    Gateway --> Credit
    Gateway --> Disbursements
    Credit -->|Feign: client and collateral data| Clients
    Credit -->|Feign: disbursement summaries| Disbursements
    Disbursements -->|Feign: loan validation and schedule activation| Credit
    Clients --> ClientsDB[(Clients data)]
    Credit --> CreditDB[(Credit H2 database)]
    Disbursements --> DisbursementsDB[(Disbursements H2 database)]
```

Each service owns its data and exposes HTTP endpoints. The service-to-service calls use Feign and Eureka discovery; the services do not share Java modules.

## Loan and disbursement flow

```mermaid
sequenceDiagram
    actor User
    participant Gateway as API Gateway
    participant Credit as Credit service
    participant Clients as Clients service
    participant Disbursements as Decaissements service
    participant Treasury as Treasury account repository
    User->>Gateway: Create loan application
    Gateway->>Credit: Forward loan request
    Credit->>Clients: Find client by ID
    Clients-->>Credit: Client data
    Credit-->>User: Loan application response
    User->>Gateway: Request disbursement
    Gateway->>Disbursements: Forward disbursement request
    Disbursements->>Treasury: Check available balance
    Disbursements->>Credit: Validate approval and amount
    Credit-->>Disbursements: Validation result
    Disbursements->>Credit: Activate repayment schedule
    Credit-->>Disbursements: Schedule activation result
    Disbursements->>Treasury: Deduct disbursement amount
    Disbursements-->>User: Disbursement response
```

Credit enrichment also requests client, collateral, and disbursement data from the owning services.

## Module layout

```
.
├── clients/                  # Client and collateral service
├── service-credit/           # Loan-application service
├── service-decaissements/    # Decaissement handling service
├── api-gateway/              # Spring Cloud Gateway 
├── discovery/                # Service registry "eureka"
└── README.md
```

The gateway route prefixes currently configured in `api-gateway` do not match all service controller paths. For example, the credit controller uses `/api/loan-applications`, while the configured gateway route matches `/api/credit/**`; decaissements has the equivalent mismatch. Configure route rewrites or align the route prefixes before relying on those gateway routes.

---

