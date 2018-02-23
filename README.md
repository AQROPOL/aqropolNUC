# aqropolNUC

Page du projet aqropol,
Construit grâce à Maven version 2
Contient les modules :
  - aqropol-webapp :
      aqropol webapp est l'API REST mis à disposition des consumers pour récolter les données fournies par les capteurs
  - aqropol-database : à venir
      aqropol database servira à la persistance des données de l'application, des mesures faites par les capteurs notament.
      via le framework hibernate (?)
  - aqropol-mom : à venir ..
      aqropol mom est le module receiver, via rabbitmq il aura pour rôle de recevoir toute les données fournies par tout les capteurs publiant sur le sujet "measures"
  - aqropol-airMeasure : à venir
      aqropol air measure est le module qui sert à récolter les mesures du capteurs de particules via le Serial soft et les publier via amqp au module aqropol mom
