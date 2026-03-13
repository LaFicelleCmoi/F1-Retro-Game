🏎️ Game Design Document : F1 Retro Racer

1. Informations Générales

Titre du jeu : F1 Retro Racer

Genre : Endless Runner / Course d'Arcade 2D

Plateforme : PC (Desktop - Java/libGDX)

Public cible : Joueurs occasionnels, fans de retro-gaming et de Formule 1.

1. Concept et Pitch

Le Pitch : "Survivez le plus longtemps possible sur une autoroute à grande vitesse en esquivant le trafic et les flaques d'huile. Prenez des risques frôlant vos adversaires pour faire exploser votre score !"

Le jeu se concentre sur les réflexes purs, une mécanique de "Risk vs Reward" (Risque contre Récompense) et l'envie de battre son propre meilleur score (Highscore).

1. Gameplay et Mécaniques
    1. Contrôles

Le jeu est conçu pour être simple à prendre en main avec un clavier :

Flèches Gauche / Droite (ou Q / D) : Déplacer la F1 latéralement.

Flèche Haut (ou Z) : Activer le mode "Boost" (double la vitesse de la voiture et du défilement).

Espace / Entrée : Valider dans les menus.

R : Relancer la partie après un Crash.

Echap : Retour au menu principal.

1. Règles du jeu

La route défile verticalement de haut en bas.

Le joueur reste en bas de l'écran et doit esquiver tout ce qui arrive d'en haut.

Le score augmente continuellement en fonction de la distance parcourue et de la vitesse (le mode Boost fait monter le score plus vite).

Collision = Game Over immédiat.

1. Mécanique Avancée : "Risk vs Reward" (Dépassement Risqué)

Pour encourager la prise de risque plutôt qu'une conduite trop passive sur les bords de la piste :

Si le joueur dépasse une voiture adverse de très près (moins de 85 pixels d'écart sur l'axe X).

Le joueur est récompensé par un bonus instantané de +500 Points.

Un feedback visuel textuel ("DEPASSEMENT RISQUE ! +500") flotte au-dessus du joueur pour valider l'action.

1. Éléments du Jeu

Le Joueur : Choix entre plusieurs écuries au menu principal (Alpine, McLaren, Ferrari, Red Bull).

Adversaires (Rival Cars) : Voitures générées aléatoirement qui descendent à une vitesse inférieure à celle du joueur.

Obstacles (Flaques d'huile) : Objets statiques sur la route. Si le joueur roule dessus, c'est le crash.

1. Interface et Expérience Utilisateur (UI/UX)

Menu Principal : Menu carrousel permettant de sélectionner l'apparence de sa voiture et celle des adversaires principaux. Fenêtre "Responsive" s'adaptant à toutes les résolutions via un système de Caméra/Viewport.

Séquence de Départ : Procédure d'immersion avec un portique de feux (matrice 5x4). 5 feux rouges s'allument progressivement, suivis des feux verts marquant le début du scrolling.

HUD (Heads-Up Display) en jeu : Affichage en temps réel du Score actuel et du Meilleur Score absolu (sauvegardé sur la machine du joueur via Preferences).

Écran de Game Over : Apparition d'un voile sombre, affichage du score final et des options pour rejouer ou quitter.

1. Direction Artistique

Visuel : Style 2D "Top-Down" (Vue de dessus). Pixel Art / Vectoriel propre et lisible.

Couleurs : Asphalte sombre contrastant avec les couleurs vives des F1 (Rouge, Bleu, Vert) et les feux du portique.
