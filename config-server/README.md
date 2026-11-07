# Config Server

Central configuration service for CreditGo.
It reads configuration from the `main` branch of `https://github.com/haytamRaba/Credi-Go-.git`,
under the root `config-repo/` directory.
That same working-tree directory is configured as a local fallback if GitHub cannot be reached.

## Run locally

 Config Server then Eureka and the apps services.

```powershell
mvn -f config-server/pom.xml spring-boot:run
```


| Application name | Config file | Port |
| --- | --- | ---: |
| Shared settings | `application.properties` | — |
| `clients` | `clients.properties` | 8081 |
| `credit` | `credit.properties` | 8082 |
| `decaissements` | `decaissements.properties` | 8083 |
| `api-gateway` | `api-gateway.properties` | 8080 |
Eureka is intentionally not a Config Client and keeps its configuration in `discovery/src/main/resources/application.properties`.

`http://localhost:8888/credit/default` returns shared `application.properties` plus `credit.properties`.
Client modules use a required `spring.config.import=configserver:http://localhost:8888` so they do not silently start without centralized configuration. If GitHub is unavailable, clients receive the root `config-repo/` fallback. Commit `config-repo/` to `main` for the GitHub source to contain the same configuration.


```text
http://localhost:8888/clients/default
http://localhost:8888/credit/default
http://localhost:8888/decaissements/default
http://localhost:8888/api-gateway/default
```
