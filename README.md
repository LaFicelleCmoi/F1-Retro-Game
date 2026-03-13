# 🏎️ F1 Retro Racer

Bienvenue dans **F1 Retro Racer**, un jeu de course d'arcade 2D (Endless Runner) développé en Java avec le framework [libGDX](https://libgdx.com/).

Survivez le plus longtemps possible sur la piste, esquivez le trafic et les flaques d'huile, et prenez un maximum de risques pour faire exploser votre Highscore !

## ✨ Fonctionnalités
* **Risk vs Reward :** Frôlez les adversaires pour déclencher un "Dépassement Risqué" et gagner un bonus immédiat de 500 points !
* **Séquence de Départ :** Une véritable animation de feux de départ (matrice 5x4) pour faire monter la pression avant la course.
* **Menu de Sélection :** Choisissez votre écurie (Alpine, McLaren, Ferrari, Red Bull) et l'apparence de vos rivaux.
* **Responsive Design :** L'affichage s'adapte parfaitement à n'importe quelle taille de fenêtre grâce à un système de Caméra/Viewport virtuel (480x800).
* **Highscore Persistant :** Votre meilleur score est sauvegardé localement entre chaque partie.

## 🎮 Contrôles
* **Flèches Gauche / Droite (ou Q / D)** : Se déplacer latéralement sur la piste.
* **Flèche Haut (ou Z)** : Activer le mode "Boost" (Double la vitesse de la voiture et l'augmentation du score).
* **Espace / Entrée** : Valider dans le menu principal.
* **R** : Rejouer rapidement après un crash.
* **Echap / Retour** : Quitter la course et revenir au menu principal.

## 🚀 Comment lancer le jeu (Mode Développeur)
Assurez-vous d'avoir le JDK 17 (Java) installé sur votre machine. Depuis le terminal, à la racine du projet, lancez :
```bash
./gradlew lwjgl3:run
```
## Générer l'exécutable (.jar)
Pour compiler le jeu en une version autonome et jouable par n'importe qui (Fat JAR) :
``` bash
./gradlew lwjgl3:jar
```
Le fichier exécutable généré se trouvera dans le dossier lwjgl3/build/libs/. Vous pourrez le lancer avec la commande java -jar <nom_du_fichier>.jar.


