# Projet Génie Logiciel, Ensimag.
gl10, 25/04/2022.

![https://gitlab.ensimag.fr/glapp2022/gl10/-/jobs/artifacts/develop/raw/cover.svg?job=coverage](https://gitlab.ensimag.fr/glapp2022/gl10/-/jobs/artifacts/develop/raw/cover.svg?job=coverage)
## Sommaire

 - [Implémentation effective](#implémentation-effective)
 - [Choix d'implémentation](#choix-dimplémentation)
 - [Utilisation de decac](#utilisation-de-decac)
 - [Tests](#tests)

---
## Implémentation effective
### Rendu initial
Récapitulatif des fonctionnalités :
| Fonctionnalité | Types compatibles |
| ------ | ------ |
| Déclaration de variable | int, float, identifier, int converti en float (convFloat) |
| Affectation de variable | int, float, identifier, convFloat |
| print(), println() | int, float, string, identifier |


### Rendu intermédiaire
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
Nous avons également écrit des tests (```src/test/deca/codegen/valid/intermediaire/```) pour le rendu intermédiaire qui peuvent être exécutés avec le script ```src/test/script/intermediaire.sh```.

- Programme

```c
{
    int rayon = 15;
    int N = rayon * 2 + 1;
    int x, y, i, j;
    i = 0;
    while (i < N + 4) {
        j = 0;
        while (j < N * 2) {
            x = i - rayon - 4;
            y = j - rayon * 2 + 8;
            if (((i <= N /2 && x * x/2 + y * y/2 <= rayon * rayon - 40 ) || x * x + y * y/2 <= rayon * rayon )
                && ((i <= N /2 && x * x/2 + y * y/2 > rayon * rayon - 70  ) || ( i >N/2 && x * x + y * y/2 > rayon * rayon - 50) )) {
                print(".");
            }
            else if (((i <= N /2 && x * x/2 + y * y/2 <= rayon * rayon - 50 ) || x * x + y * y/2 <= rayon * rayon - 50)
                && (x - 4) * (x - 4) + y * y/4 >= rayon * rayon - 200 ){
                print("o");
            }
            else {
                print(" ");
            }
            j = j + 1;
        }
        println();
        i = i + 1;
    }
}

```
- Sortie :
```
                   .......                                    
                .............                                 
             .....ooooooooo.....                              
            ...ooooooooooooooo...                             
          ...ooooooooooooooooooo...                           
         ...ooooooooooooooooooooo...                          
        ...ooooooooooooooooooooooo...                         
       ...ooooooooooooooooooooooooo...                        
       ..ooooooooooooooooooooooooooo..                        
      ..ooooooooooooooooooooooooooooo..                       
     ..ooooooooooooooooooooooooooooooo..                      
     ..ooooooooooooooooooooooooooooooo..                      
    ..ooooooooooooooooooooooooooooooooo..                     
   ...ooooooooooooooooooooooooooooooooo...                    
  ....ooooooooooooooooooooooooooooooooo....                   
  ...ooooooooooooooooooooooooooooooooooo...                   
  ..ooooooooooooooooooooooooooooooooooooo..                   
 ...ooooooooooooooooooooooooooooooooooooo...                  
 ...ooooooooooooooooooooooooooooooooooooo...                  
 ...ooooooooooooo           ooooooooooooo...                  
 ...ooooooooooo               ooooooooooo...                  
 ...ooooooooo                   ooooooooo...                  
  ..ooooooooo                   ooooooooo..                   
  ...oooooooo                   oooooooo...                   
  ...oooooooo                   oooooooo...                   
   ...ooooooo                   ooooooo...                    
    ...oooooooo               oooooooo...                     
     ...ooooooooo           ooooooooo...                      
      ...ooooooooooooooooooooooooooo...                       
       ...ooooooooooooooooooooooooo...                        
        ....ooooooooooooooooooooo....                         
          .....ooooooooooooooo.....                           
            .......ooooooo.......                             
               ...............                                
                      .
```
