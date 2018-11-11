docd
====

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/61cc56dec92b41e39ca8afbb6df93bea)](https://www.codacy.com/app/eggers-julian/docd)
[![Coverage Status](https://coveralls.io/repos/julian-eggers/dockerhub-webhook-forwarder/badge.svg?branch=master&service=github)](https://coveralls.io/github/julian-eggers/dockerhub-webhook-forwarder?branch=master)
[![Build Status](https://travis-ci.com/julian-eggers/docd.svg?branch=master)](https://travis-ci.com/julian-eggers/docd)

Automatic redeploy for docker swarm services

## Docker
[Dockerhub](https://hub.docker.com/r/jeggers/docd/)

## Setup
1. Start docd on a swarm-manager
2. Choose a trigger-method (Messaging, WebHook or REST)
3. Trigger deployments for existing services

```
docker service create \
--name=docd \
--publish mode=host,target=8080 \
--mount type=bind,src=/var/run/docker.sock,dst=/var/run/docker.sock \
jeggers/docd:latest
```


## Deployments
- via Messaging (AWS SQS or RabbitMQ)
- via WebHook (Dockerhub)
- via REST

### Messaging
#### AWS SQS
| Property | Required | Default | Example |
| -------- | -------- | ------- | ---- |
| --webhookevent.queue.awssqs.access-key | yes |  | [Policy](https://github.com/julian-eggers/docd/wiki/AWS-SQS#policy) |
| --webhookevent.queue.awssqs.secret-key | yes |  |  |
| --webhookevent.queue.awssqs.region | yes |  | [Regions](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/regions/Regions.html) |
| --webhookevent.queue.awssqs.queue-name | yes |  |  |

Note: Every cluster needs its own queue-name!

#### RabbitMQ
| Property  | Required | Default | Example |
| - | - | - | - |
| --webhookevent.queue.rabbitmq.hosts  | yes |  | localhost |
| --webhookevent.queue.rabbitmq.username  | no | guest  | |
| --webhookevent.queue.rabbitmq.password | no | guest |  |
| --webhookevent.queue.rabbitmq.exchange.name | no  | io.docker |  |
| --webhookevent.queue.rabbitmq.routing-key | no  | webHookEvent |  |
| --webhookevent.queue.rabbitmq.queue-name | no | com.itelg.docker.docd.webhookevent |  |

Note: Every cluster needs its own queue-name!


### WebHook
#### Dockerhub
- POST: http://docd/webhook/dockerhub
- Body: [WebHookEvent](https://docs.docker.com/docker-hub/webhooks/)

### REST
- POST: http://docd/service/{serviceName}/redeploy


## Selection (Whitelist/Blacklist)
docd supports a white- and blacklist-mode. 

Blacklist is the default, so every service is automatically deployed.
Preferred usage: Test-systems

To disable deployments in blacklist-mode a service-label named "DAD_SELECTION_BLOCKED" must be set.
```
docker service create \
--name=docd \
jeggers/docd:latest \
--service.selection.mode=BLACKLIST

docker service create \
--name=dummy-service \
--label DAD_SELECTION_BLOCKED=true \
dummy/service:latestt
```

In whitelist-mode you have to explicitly enable every service.
Preferred usage: Staging-systems or small production-systems

To enable deployments in whitelist-mode a service-label named "DAD_SELECTION_ALLOWED" must be set.
```
docker service create \
--name=docd \
jeggers/docd:latest \
--service.selection.mode=WHITELIST

docker service create \
--name=dummy-service \
--label DAD_SELECTION_ALLOWED=true \
dummy/service:latest
```

## REST
- POST/GET: http://docd/service/{serviceName}/allow
- POST/GET: http://docd/service/{serviceName}/block
- POST/GET: http://docd/service/{serviceName}/redeploy
- DELETE: http://docd/service/{serviceName}
- GET: http://docd/service/{serviceName}



## Build & Release

### Build
```
mvn clean package dockerfile:build
```

### Release
```
mvn clean package dockerfile:build dockerfile:tag@tag-latest dockerfile:tag@tag-version dockerfile:push@push-latest dockerfile:push@push-version github-release:release
```
