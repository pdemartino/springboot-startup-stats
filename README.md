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
    https://<service-hostname>/actuator/startup  \
    --stats --offendingThreshold 0.5

```
Note: tree roots are printed even if non offender.

## How to get details on an offending nodes
Suppose you want to know more about a specific bean:
```
springboot-startup-stats \
    https://<service-hostname>/actuator/startup \
    --mode tree --treeRoot <bean-name> --offendingThreshold 0.5 

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
