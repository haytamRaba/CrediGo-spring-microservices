# CreditGo SPRING Microservices

CreditGo is a Spring Boot microservices project built from a set of independently
deployable modules. The workspace is organized around:

- **Template module** (`clients`) a Maven archetype-style copy used to bootstrap
  new modules (e.g., `credit`).
- **Service modules** (`service-credit`, `service-decaissements`, …)  domain services
  exposing REST endpoints and domain repositories.

---

## Module layout

```
.
├── clients/                  # archetype module
├── service-credit/           # Loan-application service
├── service-decaissements/    # Decaissement handling service
├── api-gateway/              # Spring Cloud Gateway 
├── discovery/                # Service registry "eureka"
└── README.md                 
```

---

