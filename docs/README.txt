Afin de pouvoir utilser Sphinx, il faut installer au préalable les outils suivant :

 * pyhon
 * sphinx
 * graphviz

 Pour ce faire on peut utiliser les commande suivantes sous les distributions debian-like :

sudo apt-get install python-sphinx python-pip graphviz

La documentation utilise le add-on plantuml qui permet de faire toutes sorte de diagramme UML :
 * sudo pip install sphinxcontrib-plantuml

Pour rendre la génération html plus sexy j'utilise un theme cloud_sptheme
 * sudo pip install cloud_sptheme


 * sudo pip install sphinxcontrib-httpdomain

 Ce dernier n'est qu'un choix personel et n'est qu'une possiblité. Car Sphinx
 permet l'utilisation de son propre template.

