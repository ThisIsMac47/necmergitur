# Hackaton "Nec Mergitur" du 15/01 au 17/01

Le hackaton Nec Mergitur a été organisé après les attentats du 13/11/15 pour trouver des solutions pour renforcer la sécurité et améliorer les process dans des moments de crise.

## Croix Rouge
Nous nous sommes investis dans la proposition de la croix rouge francaise, qui consistait à réfléchir autour des problèmes actuels et ceux survenus pendant des crises précédentes tout en respectant les process interne déjà en place.

## Le but

Process a amélioré :
* responsable nationale / departementale qui fait descendre l'information au regulation locale
* la régulation (personne qui régule les bénévoles, qui les mobilisent etc)
  *  Appelle 1 par 1 les bénévoles pour avoir des informations (disponibilité, matériel)
  *  Constitue une équipe
  *  Leur renvoie des informations 1 par 1 (ou ce rejoindre, quel matériel prendre, lieu de l'evenement etc)
* les bénévoles se rejoignent au point de rdv et vont sur l'evenement

Notre idée :  
* tous les bénévoles ont une application mobile :
  * Permet de donner une disponibilité (dispo, pas dispo, en cas d'urgence)
  * Permet de recevoir des notifications du backend et de répondre avec des forms pré-remplies etc
  * Envoyer la localisation du bénévole pour calculer le temps d'intervention et de monter une équipe en fonction
  * Faire remonter des informations (changement de situation sur intervention, besoin de renfort etc)
* La régulation a un backoffice qui permet de récupérer/renvoyer sur le backoffice.
* Un backend qui s'interface entre le backoffice et l'application.

Process avec l'idée :
* responsable nationale / departementale qui fait descendre l'information au regulation locale
* la régulation crée un evenement sur le backoffice
* le backend envoie des notifications (en sachant qui est dispo ou pas) pour avoir des réponses valides.
* le backend remonte les informations en proposant des bénévoles pertinents (diplomes, localisation)
* la régulation modifie (ou pas) les équipes et lance l'intervention
* le backend envoie les informations au bénévoles (ou aller, comment, avec quoi)
* intervention des bénévoles
* si besoin une remonter d'information des bénévoles vers la régulation
* intervention terminé, l'application demande des infos au bénévoles (qu'est ce qui c'est passé)
* le backend crée un rapport d'intervention automatiquement (horaire, mobilisation, localisation, information)
* enregistrement de l'intervention et fin

## Realisation

Une partie du groupe a réalisé des mockups (cf. Crowd Rescue.pdf), vtarreau et moi même ayant pour but personnel de réaliser un prototype pour pouvoir en faire une demo.

vtarreau s'est donc lancé dans une application android (cf. /android_app/) qui serait dans l'idée utilisé par tous les bénévoles de la croix rouge pour pouvoir avoir des réponses rapides et précises lors des modibilisations.

Personellement, je me suis occupé du back-end pour intéragir avec l'application (cf. /backend/)

# Resultat

L'application se connecte et recupere des informations, permet de définir sa disponibilité, recois les notifications, fait répondre l'utilisateur a des questions simples puis renvoie les infos au backend + la geolocalisation.

![alt text](https://github.com/ThisIsMac47/necmergitur/blob/master/android_app_rendu.png "Rendu app")

Le backend permet d'envoyer la notification au bénévoles disponibles et forcément gérer les requetes de l'application, permet aussi de lister les utilisateurs en mission etc.

Ce n'est qu'une partie du process repenser, mais sa nous a quand même pris une bonne nuit blanche à mettre en place, sachant que ne connaissions presque pas les technologies que nous avons utilisé (c'était mon cas).

La croix rouge est intéréssé par l'idée et nous avons un rendez-vous avec la croix rouge national pour présenter un prototype pour envisager une réalisation.

# Coté technique

- Le backend
      - framework web : spark
      - framework orm : ormlite
      - base de données sql (cf. dump data.sql)
      - serialization + deserialization avec gson (lib json de google)
      - lib pour notifs : https://developers.google.com/cloud-messaging/
- L'application
      - android natif
      - serialization + deserialization avec gson (lib json de google)
