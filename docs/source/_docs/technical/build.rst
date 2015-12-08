Build et déploiement
____________________


Livraison des applis
~~~~~~~~~~~~~~~~~~~~

Spring-boot
~~~~~~~~~~~

Tous nos livrables peuvent être lancés avec la commande :

.. code-block:: bash

      ./gradlew :apps:webapps:backoffice:bootRun


Configuration
-------------

Les fichiers du dossier `<STARTER_ROOT>/spring-boot-config/` (dossier non versionné) sont ajoutés à la configuration spring.
On peut par exemple mettre dans ce dossier un fichier `application.yml` contenant des données de connexion à une base de données personnelle.
La conf présente dans `src/main/resources` sera utilisée pour toutes les propriétés non redéfinies dans `spring-boot-config`.

Arguments supplémentaires
-------------------------

Pour passer des paramètres supplémentaires à la jvm en utilisant gradle, utilisez la propriété ``jvmArgs``.

On peut également passer des paramètres à l'application lancée (les ``String[] args`` dans la classe ``Main``) avec ``args`` :

.. code-block:: bash

   ./gradlew :apps:webapps:backoffice:bootRun -PjvmArgs="-Dserver.port=9090"
