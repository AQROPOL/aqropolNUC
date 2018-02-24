https://www.rabbitmq.com/configure.html#config-location

Voici un exemple de configuration du broker RabbitMQ

Placez les fichiers :
	- rabbitmq-env.conf
	- rabbitmq.conf 
dans le dossier :
	- [MAC OS] : /usr/local/etc/rabbitmq/

Si vous ne lancez pas le broker pour la première fois, il se peut que ces modifications ne soient pas toute prise en compte.
Pour remédier à cela, une fois serveur lancé (rabbitmq-server), exécutez ces commandes :

rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app

