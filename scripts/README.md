# Developer checks

These scripts are the local entry points for the same checks used by CI.
Run commands from the repository root

## Maven tests

`test-local.ps1` first checks that Config Server returns configuration for `clients`, `credit`, `decaissements`, and `api-gateway`. It then runs `mvn --batch-mode --no-transfer-progress verify` across the reactor. The configuration check is required because the Clients and Gateway Spring context tests use a mandatory Config Server import.

Start Config Server in its own terminal:

```powershell
mvn -pl config-server spring-boot:run
```

Then run the test script from another terminal at the repository root:

```powershell
.\scripts\test-local.ps1
```

The script exits nonzero if a configuration response is empty or Maven fails. It does not start or stop application processes.

## Runtime smoke checks

`smoke-test.py` makes read-only HTTP requests and retries each target for up to 60 seconds. It checks:

| Target | Request | Assertion |
| --- | --- | --- |
| Config Server | `GET /{application}/default` for each Config Client | A property source is returned with the matching `spring.application.name` |
| Eureka | `GET /eureka/apps` | HTTP success |
| Clients | `GET /api/clients` | HTTP success |
| Credit | `GET /api/loan-applications` | HTTP success |
| Decaissements | `GET /api/disbursements` | HTTP success |
| Gateway to Clients | `GET /api/clients` through port `8080` | HTTP success |


```powershell
mvn -pl config-server spring-boot:run
mvn -pl discovery spring-boot:run
mvn -pl clients spring-boot:run
mvn -pl service-credit spring-boot:run
mvn -pl service-decaissements spring-boot:run
mvn -pl api-gateway spring-boot:run
```

Run the smoke script after starting the applications:

```powershell
py -3 scripts/smoke-test.py
```

The smoke script only issues `GET` requests

## Postman integration collection

`postman/collections/creditgo` is the saved end-to-end request sequence.


```powershell
postman collection run postman/collections/creditgo --environment postman/environments/local.environment.yaml --no-report-events
```

The collection writes test data to the running service databases. 
The configured H2 databases are in-memory and reset when their services restart.
CI runs this same collection after starting the modules.

## CI execution

`.github/workflows/ci.yml` builds Config Server first,
starts it, 
 verifies all four Config Client responses.
then runs `mvn verify`,
starts Eureka and the application services
from their packaged JARs, 
and runs  smoke script.

