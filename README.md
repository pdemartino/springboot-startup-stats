SpringBoot Startup Stats
=====

*This is Free Software and it's distributed under [GPLv3](./LICENSE)*

Utility to extract statistics from Spring Boot Startup 
actuator and identify bottlenecks and slow beans during the start-up procedure.

# Hot to install

## Homebrew on MacOS
```
brew tap pdemartino/tap
brew install springboot-startup-stats
```

## Other installation methods

You can rely on Gradle:
```
INSTALL_DIR=$HOME/springboot-startup-stats
./gradlew clean -PinstallPrefix=$INSTALL_DIR installDist

ls $INSTALL_DIR
```

# How to run

```
springboot-startup-stats -h

Usage: springboot-startup-stats options_list
Arguments: 
    source -> Startup Actuator source, it can be a JSON file or an URL { String }
Options: 
    --stats [true] -> Print Statistics 
    --offendingThreshold [1.7976931348623157E308] -> Threshold in seconds after which a startup step is considered too slow { Double }
    --printTree [false] -> Print full tree (or subtree if root is specified) 
    --root -> When printing tree, limit to the subtree identified by this root { String }
    --help, -h -> Usage info 

```

## Get statistics and identify offenders (slow steps)
```
springboot-startup-stats \
    https://experience-api-lodging-property.rcp.us-west-2.partnerexperiences.test.exp-aws.net/actuator/startup  \
    --stats --offendingThreshold 0.5
Number of nodes: 816
Number of roots: 7
Number of leaves: 544
Global duration: 97.18283569675987
Offending nodes: 13
[1]spring.boot.application.environment-prepared - t:0.9781982
[9]spring.context.config-classes.parse - t:4.527874
[72]spring.beans.instantiate:yodaConfigFactory - t:6.8859005 - ∑t:8.413338s
[78]spring.beans.instantiate:asyncConfigClientKotlin - t:0.5614208 - ∑t:1.4911664s
[197]spring.beans.instantiate:productEntityApi - t:0.78408253
[341]spring.beans.instantiate:experimentApplication - t:2.6806433 - ∑t:2.829927s
[345]spring.beans.instantiate:uisPrimeApplicationEnvironment - t:28.488194 - ∑t:28.50423s
[410]spring.beans.instantiate:siteResolver - t:1.1516571 - ∑t:1.1627022s
[470]spring.beans.instantiate:userManager - t:0.6743718 - ∑t:0.7255849s
[482]spring.beans.instantiate:schema - t:6.6427255 - ∑t:6.901356s
[492]spring.beans.instantiate:travelAdsConfigRetriever - t:0.69220394 - ∑t:0.7058539s
[506]...tantiate:org.springframework.security.config.annotation.web.reactive.ServerHttpSecurityConfiguration - t:1.0289042 - ∑t:1.2269076s
[528]spring.beans.instantiate:uisPrimeApplication - t:28.936798

```
Note: tree roots are printed even if non offender.

## How to get details on an offending nodes
Suppose you want to know more about _spring.beans.instantiate:yodaConfigFactory_
```
springboot-startup-stats \
    https://experience-api-lodging-property.rcp.us-west-2.partnerexperiences.test.exp-aws.net/actuator/startup \
    --printTree --root spring.beans.instantiate:yodaConfigFactory --offendingThreshold 0.5 
Number of nodes: 816
Number of roots: 7
Number of leaves: 544
Global duration: 100.19436100753956
Offending nodes: 15
[1]spring.boot.application.environment-prepared - t:0.9601233
[9]spring.context.config-classes.parse - t:5.161605
[72]spring.beans.instantiate:yodaConfigFactory - t:4.7618237 - ∑t:6.3810496s
[78]spring.beans.instantiate:asyncConfigClientKotlin - t:0.5623821 - ∑t:1.5465084s
[140]spring.beans.instantiate:reactorClientHttpConnector - t:0.5021843 - ∑t:0.5049456s
[197]spring.beans.instantiate:productEntityApi - t:0.87301534
[341]spring.beans.instantiate:experimentApplication - t:2.7227573 - ∑t:2.9071972s
[345]spring.beans.instantiate:uisPrimeApplicationEnvironment - t:30.54371 - ∑t:30.561623s
[410]spring.beans.instantiate:siteResolver - t:1.326255 - ∑t:1.363262s
[470]spring.beans.instantiate:userManager - t:0.5238574 - ∑t:0.55435574s
[482]spring.beans.instantiate:schema - t:6.5935407 - ∑t:6.88559s
[492]spring.beans.instantiate:travelAdsConfigRetriever - t:0.5726996 - ∑t:0.6203504s
[506]...tantiate:org.springframework.security.config.annotation.web.reactive.ServerHttpSecurityConfiguration - t:1.183091 - ∑t:1.3912022s
[528]spring.beans.instantiate:uisPrimeApplication - t:29.667406
[573]spring.beans.instantiate:defaultValidator - t:0.51279485

[72]spring.beans.instantiate:yodaConfigFactory - t:4.7618237 - ∑t:6.3810496s
├──[73]spring.beans.instantiate:yoda-config-eg.platform.config.client.impl.yoda.config.YodaConfig - t:0.004939556
├──[74]spring.beans.instantiate:appConfigExceptionLogger - t:0.008830033 - ∑t:0.0258259s
│   └──[75]spring.beans.instantiate:applicationConfiguration - t:0.016995866
└──[76]spring.beans.instantiate:configClient - t:0.034334064 - ∑t:1.5884604s
    ├──[77]spring.beans.instantiate:com.expediagroup.platform.config.PlatformConfigAutoconfiguration - t:0.007617929
    └──[78]spring.beans.instantiate:asyncConfigClientKotlin - t:0.5623821 - ∑t:1.5465084s
        └──[79]spring.beans.instantiate:configClientBuilder - t:0.04404193 - ∑t:0.9841263s
            └──[80]spring.beans.instantiate:compositeMeterRegistry - t:0.058414638 - ∑t:0.9400844s
                ├──[81]...stantiate:org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryConfiguration - t:0.002556411
                ├──[82]spring.beans.instantiate:micrometerClock - t:0.0038972169 - ∑t:0.006401538s
                │   └──[83]...ng.beans.instantiate:org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration - t:0.002504321
                ├──[84]spring.beans.instantiate:statsdMeterRegistry - t:0.46330136 - ∑t:0.5922626s
                │   ├──[85]spring.beans.instantiate:metricsConfiguration - t:0.007686172
                │   ├──[86]spring.beans.instantiate:statsdConfig - t:0.042181022 - ∑t:0.057123616s
                │   │   ├──[87]...pringframework.boot.actuate.autoconfigure.metrics.export.statsd.StatsdMetricsExportAutoConfiguration - t:0.002941859
                │   │   └──[88]....export.statsd-org.springframework.boot.actuate.autoconfigure.metrics.export.statsd.StatsdProperties - t:0.012000735
                │   ├──[89]spring.beans.instantiate:nameMapper - t:0.004789524 - ∑t:0.018990649s
                │   │   └──[90]spring.beans.instantiate:hostnameProvider - t:0.008753535 - ∑t:0.014201125s
                │   │       └──[91]spring.beans.instantiate:ecsMetadata - t:0.00544759
                │   ├──[92]...tantiate:management.metrics-org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties - t:0.018161828
                │   ├──[93]spring.beans.instantiate:propertiesMeterFilter - t:0.007162472
                │   ├──[94]spring.beans.instantiate:metricsHttpClientUriTagFilter - t:0.008690685 - ∑t:0.011722643s
                │   │   └──[95]...org.springframework.boot.actuate.autoconfigure.metrics.web.client.HttpClientMetricsAutoConfiguration - t:0.003031958
                │   └──[96]spring.beans.instantiate:metricsHttpServerUriTagFilter - t:0.0021315236 - ∑t:0.008113896s
                │       └──[97]...:org.springframework.boot.actuate.autoconfigure.metrics.web.reactive.WebFluxMetricsAutoConfiguration - t:0.005982372
                ├──[98]spring.beans.instantiate:jmxMeterRegistry - t:0.020001449 - ∑t:0.112137474s
                │   ├──[99]...:org.springframework.boot.actuate.autoconfigure.metrics.export.jmx.JmxMetricsExportAutoConfiguration - t:0.004325212
                │   └──[100]spring.beans.instantiate:jmxConfig - t:0.013349891 - ∑t:0.087810814s
                │       └──[101]...t.metrics.export.jmx-org.springframework.boot.actuate.autoconfigure.metrics.export.jmx.JmxProperties - t:0.07446092
                ├──[102]spring.beans.instantiate:prometheusMeterRegistry - t:0.015886925 - ∑t:0.08062903s
                │   ├──[103]...mework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration - t:0.002751518
                │   ├──[104]spring.beans.instantiate:prometheusConfig - t:0.008823592 - ∑t:0.013365702s
                │   │   └──[105]...etheus-org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusProperties - t:0.004542109
                │   └──[106]spring.beans.instantiate:collectorRegistry - t:0.048624884
                ├──[107]spring.beans.instantiate:jvmGcMetrics - t:0.007705474 - ∑t:0.01006349s
                │   └──[108]...beans.instantiate:org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration - t:0.002358016
                ├──[109]spring.beans.instantiate:jvmHeapPressureMetrics - t:0.005749047
                ├──[110]spring.beans.instantiate:jvmMemoryMetrics - t:0.004209114
                ├──[111]spring.beans.instantiate:jvmThreadMetrics - t:0.00379157
                ├──[112]spring.beans.instantiate:classLoaderMetrics - t:0.00338787
                ├──[113]spring.beans.instantiate:log4j2Metrics - t:0.00412617 - ∑t:0.006463039s
                │   └──[114]...ns.instantiate:org.springframework.boot.actuate.autoconfigure.metrics.Log4J2MetricsAutoConfiguration - t:0.002336869
                ├──[115]spring.beans.instantiate:uptimeMetrics - t:0.0035075932 - ∑t:0.005868106s
                │   └──[116]...ns.instantiate:org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration - t:0.002360513
                ├──[117]spring.beans.instantiate:processorMetrics - t:0.03966712
                ├──[118]spring.beans.instantiate:fileDescriptorMetrics - t:0.003879857
                └──[119]spring.beans.instantiate:diskSpaceMetrics - t:0.004603499
```

Note:
- Offending nodes will be red on the console
- Long step names are shortened _"...ns.instantiate:org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration"_

### How to read step duration
#### Step duration ('t')
For every step, its duration is indicated by the **t**. Note that's net duration, children's duration sum is already subtracted.
#### Subtree duration ('∑t')
For every intermediate (non leaf) nodes also the duration of its subtree (root included) is provided
```
[86]spring.beans.instantiate:statsdConfig - t:0.042181022 - ∑t:0.057123616s
├──[87]...pringframework.boot.actuate.autoconfigure.metrics.export.statsd.StatsdMetricsExportAutoConfiguration - t:0.002941859
└──[88]....export.statsd-org.springframework.boot.actuate.autoconfigure.metrics.export.statsd.StatsdProperties - t:0.012000735
```
In the example above, subtree duration  ∑t:0.057123616s is the sum of children duration (0.002941859 + 0.012000735) plus root duration (0.042181022)
