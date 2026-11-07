$ErrorActionPreference = 'Stop'

$configurations = @(
    'http://localhost:8888/clients/default',
    'http://localhost:8888/credit/default',
    'http://localhost:8888/decaissements/default',
    'http://localhost:8888/api-gateway/default'
)

foreach ($url in $configurations) {
    try {
        $configuration = Invoke-RestMethod -Uri $url -TimeoutSec 5
        if (-not $configuration.propertySources) {
            throw "Config Server returned no property sources for $url"
        }
    } catch {
        Write-Error "Configuration check failed for $url. Start Config Server first. $($_.Exception.Message)"
        exit 1
    }
}

mvn --batch-mode --no-transfer-progress verify
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
