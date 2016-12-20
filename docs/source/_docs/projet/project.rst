SOS-BANK
________

Ce projet signifie Simple Open Source Bank.

Ce projet a pour but de d'apprendre les technologies suivantes :

    * Micro-service avec Spring Cloud
    * Angular 2
    * Ionic 2
    * Spring-session
    * Docker
    * Test acceptance avec geb, groovy, spookframework


Ce progiciel est découpé en un seul repository. Ce qui apporte les avantages suivants :

    * Pouvoir déceller les problèmes au build et non au runtime
    * Pouvoir avoir tous les sources sous les yeux pour ne pas réécrire plusieurs fois du code
    * Pouvoir relivrer en une seuls fois l'inégralité du progiciel
    * Centralisation sur les outils comme SONAR ou jenkins
    * Pourvoir refactorer facilement car tous les sources sont au même endroit

Inconvénient :

    * Temps de compilation de l'intégralité du projet plus long


docs
====

Ce projet est la documentation technique du progiciel


tools
=====

Ce sont une multitude de modules utilitaire


common
~~~~~~

Ce projet sera découper quand le projet grossirra.

Pour le moment, il contient :

    * Un utilitaire d'encodage
    * Un utilitaire de Date
    * Un utilitaire pour simplifier l'instantiation du Logger


domain
~~~~~~

Ce projet contient :

    * Des converteurs hibernate
    * Des Listner
    * Un objet de base pour toutes les entités


repository
~~~~~~~~~~

Ce projet contient :

    * Des utilitaires de filtrage générique
    * La base de tous les repositories


script
~~~~~~

Script de déploiement


service
~~~~~~~

Ce projet contient la base de tous les services


core
====

Ce sont une multitude de modules de base pour le progiciel


application-services
~~~~~~~~~~~~~~~~~~~~

Ce sont des modules indépandant du projet pour lui-même.

Il contient des modules comme :

    * Envoie de mail
    * Envoie de SMS
    * Génération de PDF
    * Inclusion d'un CMS
    * ...

domains
~~~~~~~

Ce module représent le model métier common à tout le progiciel

repositories
~~~~~~~~~~~~

Ce module sert de repository pour le model common

services
~~~~~~~~

Ce module sert de service pour les repositories commun



apps
====

Ce modules contient toutes les parties du progiciel applicatif


batches
~~~~~~~

Module de batch


daemons
~~~~~~~

Module en mode daemon


distrib
~~~~~~~

Script et option des démmarrages des applications


frontapps
~~~~~~~~~

Applications frontales qui non pas besoin de serveurs d'applications

Ce module est divisé est deux :

    * application web
    * application mobile


restapps
~~~~~~~~

Application pour le developement pour éviter les micro-services en local


webapps
~~~~~~~

Applications qui nécessitent un serveur d'application.
Les applications utilisent spring-session pour pouvoir utiliser le principe de microservice.
Cela va permettre de relivrer qu'une partie d'une application.

Il contient :

    * Une application backoffice
    * Une application frontoffice
    * Une application bouchon du core banking


modules
~~~~~~~

Ces sont tous modules du progiciel découpé en sillo quasiment indépendant.

Chaque module contient :

    * Son modèle métier
    * Ses repositories
    * Ses services
    * Ses api-rest en microservice
    * Ses briques frontal web pour chaque application web en microservice
