import json
import time
import urllib.error
import urllib.request


checks = [
    ("Config Server: clients", "http://localhost:8888/clients/default", "config", "clients"),
    ("Config Server: credit", "http://localhost:8888/credit/default", "config", "credit"),
    ("Config Server: decaissements", "http://localhost:8888/decaissements/default", "config", "decaissements"),
    ("Config Server: gateway", "http://localhost:8888/api-gateway/default", "config", "api-gateway"),
    ("Eureka", "http://localhost:8761/eureka/apps", "http", None),
    ("Clients API", "http://localhost:8081/api/clients", "http", None),
    ("Credit API", "http://localhost:8082/api/loan-applications", "http", None),
    ("Decaissements API", "http://localhost:8083/api/disbursements", "http", None),
    ("Gateway to Clients", "http://localhost:8080/api/clients", "http", None),
]


def check(url, kind, app):
    request = urllib.request.Request(url, headers={"Accept": "application/json"})
    with urllib.request.urlopen(request, timeout=5) as response:
        payload = response.read()
    if kind == "config":
        config = json.loads(payload)
        sources = config.get("propertySources", [])
        if not sources:
            raise RuntimeError("Config Server returned no property sources")
        if not any(source.get("source", {}).get("spring.application.name") == app for source in sources):
            raise RuntimeError(f"No property source matched application name '{app}'")


failures = []
for name, url, kind, app in checks:
    last_error = None
    for attempt in range(30):
        try:
            check(url, kind, app)
            print(f"PASS {name}")
            break
        except (urllib.error.URLError, TimeoutError, RuntimeError, json.JSONDecodeError) as error:
            last_error = error
            if attempt < 29:
                time.sleep(2)
    else:
        failures.append((name, url, last_error))
        print(f"FAIL {name}: {last_error}")

if failures:
    raise SystemExit(1)
