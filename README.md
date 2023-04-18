### Features
- [X] Possibilité de changer le niveau des logs actuels à chaud
- [X] Possibilité d'ajouter des loggers à chaud
- [X] Possibilité de monitorer les performances depuis le l'entrée dans le controller REST jusqu'a sa sortie avec toutes les méthodes intermédaires
- [X] Possibilité de gérer les aspects RGPD
- [X] Possibilité en ajoutant une ligne "@LogMe" sur les méthodes publiques de loggers l'ensemble du module 
- [X] Possibilité de tracer avec une même UUID parent l'ensemble des logs d'une même requête
- [X] Possibilité de rejouer une requête avec un header supplémentaire et d'obtenir l'ensemble des logs au niveau "TRACE"
-------------------------------
### Ongoing
- [X] Refactor du DynamicThreasholdFilter : ne doit pas être porté par le module java final mais par la lib

-------------------------------
### Todos
- [ ] IHM pour monitorer les logs
- [ ] Revoir les infos / niveau de log pour une meilleure cohérence 
-------------------------------
### Maybe
- [ ] Plutot que de réaliser de l'introspection pour les champs confidentiels, voir si possibilité de générer du bytecode via javaagent pour créer les méthodes de log
- [ ] Ajouter une propriété de configuration "confidential.ready=false" qui permet lorsque le module cible n'a pas fait l'exercice RGPD d'avoir un mode de log moins intrusif
- [ ] Voir la possibilité d'ajouter des "LogFeature" au niveau du formater log4j (notamment pour l'UUID de requête)
- [ ] Possibilité de passer l'ensemble d'une CATAGORY (DAO/SERVICE/ETC) sur un LEVEL different

