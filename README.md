# springbootWithibmmq
	1. Pull Image

docker pull ibmcom/mq

	2. Run IBM MQ with default configuration

Refer to https://github.com/ibm-messaging/mq-container/blob/master/docs/usage.md

docker run \
--env LICENSE=accept \
--env MQ_QMGR_NAME=QM1 \
--publish 1414:1414 \
--publish 9443:9443 \
--detach \
ibmcom/mq


docker run --env LICENSE=accept  --env MQ_QMGR_NAME=QM1  --publish 1414:1414   --publish 9443:9443   --detach   ibmcom/mq

	3. Web console
https://localhost:9443/ibmmq/console

id:admin

password:passw0rd

	4. IBM MQ dev patterns and resource 
https://github.com/ibm-messaging/mq-jms-spring/tree/master/samples
https://github.com/ibm-messaging/mq-dev-patterns
