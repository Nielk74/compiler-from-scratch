# Projet Génie Logiciel, Ensimag.
gl10, 25/04/2022.

## Sommaire

 - [Implémentation effective](##implementation-effective)
 - [Choix d'implémentation](##choix-d-implementation)
 - [Utilisation de decac](##utilisation-de-decac)
 - [Tests](##tests)

---
## Implémentation effective
### Rendu initial
Récapitulatif des fonctionnalités :
| Fonctionnalité | Types compatibles |
| ------ | ------ |
| Déclaration de variable | int, float, identifier, int converti en float (convFloat) |
| Affectation de variable | int, float, identifier, convFloat |
| print(), println() | int, float, string, identifier |

### Rendu intermédiaire 1
Récapitulatif des fonctionnalités :
| Fonctionnalité | Détails |
| ------ | ------ |
| readInt(), readFloat() | Lit un int ou un float |
| Comparaison '==', '!=', '<', '<=', '>', '>=' | Comparaison litéral/litéral, identifier/identifier, litéral/identifier de type int ou float.<br />Possibilité de comparer un int et un float et le résultat d'un readInt() à un litéral ou un identifier.|
| Expression booléenne '!', '&&', '\|\|' | Les termes ne peuvent être que des comparaisons.<br />Les opérateurs peuvent être combinés.<br />Le nombre d'opérateur n'est pas limité.<br />Des parenthèses peuvent être utilisées pour définir des ordres de priorité. |
| Boucle while | - Condition : une comparaison ou une expression booléenne <br/> - Corps : liste d'instructions qui font partie de celles implémentées |
| If/then/else | - Condition : idem que précedemment<br/> - Corps then/else : idem que précedemment |

<br/>

---
## Choix d'implémentation

### ErrorCatcher
Cette classe génère les gestionnaires d'erreur lors de la phase codegen. Cela comprend pour le moment :
- io_error (erreur d'entrée/sortie)
- so_error (débordement de pile)
### LabelManager
Il s'agit d'un dictionnaire associant le nom d'un label (string) à un objet Label. Elle gère la création et la récupération de label pour des sauts, conditions et boucles.

### StackManager
Cette classe gère l'initialisation de la pile. Elle permet de compter le nombre de variables utilisées lors de la génération du code des déclarations et alloue la taille nécéssaire en début de code

---
## Utilisation de decac
### Options
| Option | Nom | Description |
| ------ | --- | ---------|
| -p | parse | arrête decac après l'étape de construction de l'arbre, et affiche la décompilation de ce dernier (i.e. s'il n'y a qu'un fichier source à compiler, la sortie doit être un programme deca syntaxiquement correct)
| -v | verification | arrête decac après l'étape de vérifications (ne produit aucune sortie en l'absence d'erreur) |
> **N.B :** -p et -v sont incompatibles. 

### Arguments
Le premier (et seul argument pour l’instant) doit être un chemin vers un fichier ayant pour extension .deca.

> ```decac demonstration.deca```

---
## Tests

Les tests du renduInitial passent à 100 %.
Nous avons également écrit des tests (src/test/deca/codegen/valid/intermediaire/) pour le rendu intermédiaire qui peuvent être exécutés avec le script src/test/script/intermediaire.sh.

- Programme

```c
{
    int i, j = 0;
    float k;

    println("Entrez un nombre entier :");
    i = readInt();
    println("Vous avez entré le nombre : ", i);

    i = 1;
    println("i = ", i);
    k = 10;
    println("k = ", k);

    if(!(i > 10 || i <= 0)){
        println("!(i > 10 || i <= 0) <=> !(",i," > 10 || ",i," <= 0) -> VRAI");
        i = 2;
        println("i = ", i);
    }
    else{
        println("ERROR");
    }

    while((i > j) && (i <= k)){
        println("---- (i > j) && (i <= k) <=> (",i," > ",j,") && (",i," <= ",k,") -> VRAI");
        println("----> Entrée dans la boucle");
        if(i > 2 && k == 10){ 
            // Not expected
            println("ERROR");
        }
        else {
            println("-------- i > 2 <=> ",i," > 2 -> FAUX");
        }
        if(!(i != 3)){
           println("-------- !(i != 3) <=> !(",i," != 3) -> VRAI");
           i = 100;
           println("-------- i = ", i);
           println("----> Sortie prévue");
        }
        else {
           println("-------- !(i != 3) <=> !(",i," != 3) -> FAUX");
        }
        if((i == 2 || i == 1) && k == 10){
            println("-------- (i == 2 || i == 1) && k == 10 <=> (",i," == 2 || ",i," == 1) && ",k," == 10 -> VRAI");
            println("-------- i E {1,2}");
            i = 3;
            k = 9;
        }
        else { 
            // Not expected
            println("-------- (i == 2 || i == 1) && k == 10 <=> (",i," == 2 || ",i," == 1) && ",k," == 10 -> FAUX");
        }
    }
    println("----(i > j) && (i <= k) <=> (",i," > ",j,") && (",i," <= ",k,") -> FAUX");
    println("Fin de boucle.");
}
```
- Sortie :
```
Entrez un nombre entier :
5
Vous avez entré le nombre : 5
i = 1
k = 1.00000e+01
!(i > 10 || i <= 0) <=> !(1 > 10 || 1 <= 0) -> VRAI
i = 2
---- (i > j) && (i <= k) <=> (2 > 0) && (2 <= 1.00000e+01) -> VRAI
----> Entrée dans la boucle
-------- i > 2 <=> 2 > 2 -> FAUX
-------- !(i != 3) <=> !(2 != 3) -> FAUX
-------- (i == 2 || i == 1) && k == 10 <=> (2 == 2 || 2 == 1) && 1.00000e+01 == 10 -> VRAI
-------- i E {1,2}
---- (i > j) && (i <= k) <=> (3 > 0) && (3 <= 9.00000e+00) -> VRAI
----> Entrée dans la boucle
-------- i > 2 <=> 3 > 2 -> FAUX
-------- !(i != 3) <=> !(3 != 3) -> VRAI
-------- i = 100
----> Sortie prévue
-------- (i == 2 || i == 1) && k == 10 <=> (100 == 2 || 100 == 1) && 9.00000e+00 == 10 -> FAUX
----(i > j) && (i <= k) <=> (100 > 0) && (100 <= 9.00000e+00) -> FAUX
Fin de boucle.
```