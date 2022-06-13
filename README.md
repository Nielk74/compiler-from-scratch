# Projet Génie Logiciel, Ensimag.
gl10 (Donkey Kong), 25/04/2022.

![https://gitlab.ensimag.fr/glapp2022/gl10/-/jobs/artifacts/develop/raw/cover.svg?job=coverage](https://gitlab.ensimag.fr/glapp2022/gl10/-/jobs/artifacts/develop/raw/cover.svg?job=coverage)

---
## Implémentation effective
Le deca sans objet a été implémenté en totalité.

## Compiler le projet
```
mvn clean
mvn compile
```

## Lancer les tests
```
mvn clean
mvn test
```

## Utilisation de decac
Voir la documentation du Projet GL.

---
## Programme de démonstration

```c
{
    int rayon = 15;
    int N = rayon * 2 + 1;
    int x, y, z,i, j, k;
    boolean front;
    boolean back;
    i = 0;
    while (i < N) {
        j = 0;
        while (j < N + 50 ) {
            front = false;
            back = false;
            k = 0;
            while(k < N){
                x = i - rayon;
                y = j - rayon - 20;
                z = k - rayon;
                if (x * x + y * y/5 + z * z <= rayon * rayon + 15
                && x * x + y * y/5 + z * z >= rayon * rayon - 15
                && ((y + z + x*2)%14 == 0 || (y + z +x*2)%14 == 1)) {
                    if(k < N/2){
                        front = true;
                    }
                    else{
                        back = true;
                    }
                }
                k = k + 1;
            }
            if (front){
                print("o");
            }else if(back){
                print(".");
            }
            else{
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
<img src="https://cdn.discordapp.com/attachments/896722660027953162/985791297895931924/unknown.png" width=250>
