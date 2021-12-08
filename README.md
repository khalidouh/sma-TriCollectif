# sma-TriCollectif version 2

Nous avons programe notre system base sur une programmation oriente objets

Notre programmation d’un agent consiste en une boucle perception (), action(). La
perception, permet à l’agent de récupérer les informations de l’environnement et l’action,
dans le cas du sujet est soit : le déplacement (aléatoire) d’un pas, la prise (suivant probabilité)
d’un objet ou le dépôt (probabiliste) d’un objet et ça illustre dans le code suivant.

## Architecture Voyelle

A, Environnement
- Grille où se déplace les agents et où sont placés les objets.
- Objets A, B, C, ces objets font partis de l’environnement et leurs méthodes sont gérées par l’environnement.
 B, Agents
- Agents réactifs : agents qui agissent se déplacent au hasard ou suivent le gradient de phéromone si la trace est assez forte.
Ils sont munis de fonctions de perception (regard au sol) déplacement, de prise et de dépot d’un objet. Ces seules fonctionnalités
leurs permettent de triés efficacement les tas d’objets ne nécessitant pas d’interaction (A et B). Pour trié les objets C d’autres
fonctionnalités leurs seront implémentés (cf partie IV).
C, Interactions
- Pas d’interaction dans la première partie.D, Organisation
- Organisation émergente : Sans que ces comportements soient codés, les agents vont s’auto-organiser pour triés les objets.

![alt text](https://github.com/khalidouh/sma-TriCollectif/blob/master/images/vvv.JPG)
![alt text](https://github.com/khalidouh/sma-TriCollectif/blob/master/images/v33.png)



