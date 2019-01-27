package core

/** Toutes les cartes d'une même couleur */
class Couleur {
    def cartes = []
    static cartes_valides = ["A", "R", "D", "V", "10"] + ("9".."2")

    // constructeur avec couleur structurée comme par ex "A R V 2"
    Couleur(String lc) {
        lc.split().each {
            if (cartes_valides.contains(it)) {
                cartes << it
            }
            else {
                throw new Exception("La carte $it est invalide")
            }
        }
    }

    int size() {
        return cartes.size()
    }

    static int val_honneur(String c) {
        switch (c) {
            case 'A': return 4;
            case 'R': return 3;
            case 'D': return 2;
            case 'V': return 1;
        }
        return 0
    }

    int pointsH() {
        if (cartes.size() == 0) { return 0 }
        return cartes.collect( { val_honneur(it) } ).sum()
    }

    String toString() {
        return cartes.toString()
    }

    List<String> honneurs() {
        return cartes.findAll { val_honneur(it) > 0 }
    }
}

/** Main d'un joueur */
class Main {

    Couleur Piques
    Couleur Coeurs
    Couleur Carreaux
    Couleur Trêfles
    List<Couleur> couleurs

    Main(String p, String c, String k, String t) {
        Piques = new Couleur(p)
        Coeurs = new Couleur(c)
        Carreaux = new Couleur(k)
        Trêfles = new Couleur(t)
        couleurs = [Piques, Coeurs, Carreaux, Trêfles]
    }

    String toString() {
        return """\
Piques   $Piques
Coeurs   $Coeurs
Carreaux $Carreaux
Trêfles  $Trêfles
${this.pointsH()} points d'honneur
${this.pointsD()} points de distribution
${if (this.main_bicolore()) {"Main bicolore"} else {"Main non-bicolore"}}
"""
    }

    int size() {
        return couleurs*.size().sum()
    }

    int pointsH() {
        return couleurs*.pointsH().sum()
    }

    boolean H_entre(int min, int max) {
        return pointsH() >= min && pointsH() <= max
    }

    int pointsDH() {
        return pointsD()+pointsH()
    }

    boolean DH_entre(int min, int max) {
        return pointsDH() >= min && pointsDH() <= max
    }

    int pointsD() {
        int d = 0
        couleurs.each {
            switch (it.size()) {
                case 0: d += 3; break // chicane
                case 1: d += 2; break // singleton
                case 2: d += 1; break // doubleton
                case 6..13: d += (it.size()-5); break // +1D à partir de la 6ème
            }
        }
        if (main_bicolore()) {
            if ((longue1().honneurs().size() >= 2)
                  && (longue2()?.honneurs()?.size() >= 2)) {
                switch(longue1().size()+longue2().size()) {
                    case 10: d+= 1; break
                    case 11: d+= 2; break
                    case 12: d+= 3; break
                }
            }
        }
        // honneurs secs < AS => réduire les points D
        couleurs.each {
            if (it.size() == 1) {
                switch (it.cartes[0]) {
                    case 'R': d -= 2; break // enlever 2 points
                    case 'D': d -= 2; break // enlever 2 points
                    case 'V': d -= 1; break // enlever 1 point
                }
            }
        }
        return d
    }

    Couleur longue1() {
        def l = couleurs.findAll { it.size() > 4 }
        if (l.size() == 0) { return null }          // pas de longue => null
        if (l.size() == 1) { return l[0] }          // 1 longue
        return l[0].size() >= l[1].size()? l[0] : l[1] // 2 longues => on renvoie la plus grande

    }

    Couleur longue2() {
        def l = couleurs.findAll { it.size() > 4 }
        if (l.size() == 0) { return null }          // pas de longue => null
        if (l.size() == 1) { return null }          // 1 longue => longue2 = null
        return l[0].size() >= l[1].size()? l[1] : l[0] // 2 longues => on renvoie la plus petite
    }

    // distributions 5-5-X-X, 6-4-X-X, 6-5-X-X, 6-6-X-X
    boolean main_bicolore() {
        def big1 = couleurs*.size().sort { -it }[0] // plus longue
        def big2 = couleurs*.size().sort { -it }[1] // 2ème plus longue
        return (big1 == 6 && big2 >= 4) || (big1 == 5 && big2 == 5)
    }

    // distributions 4-3-3-3, 4-4-3-2, 5-3-3-2
    boolean main_régulière() {
        def distrib = couleurs*.size().sort { -it }
        if (distrib == [4,3,3,3]) { return true }
        if (distrib == [4,4,3,2]) { return true }
        if (distrib == [5,3,3,2]) { return true }
        return false
    }

    String ouverture() {
        if (pointsH() < 13) { return "Passe" }
        if (pointsDH() < 14) { return "Passe" }

        // 1 SA
        if (main_régulière() && pointsH() >= 16 && pointsH() <= 18) {
            return "1 SA"
        }

        // 1 Pique
        if (Piques.size() >= 5
                && Coeurs.size() < 7
                && Trêfles.size() < 5
                && pointsDH() <= 19) {
            return "1 P"
        }

        // 1 Coeur
        if (Coeurs.size() >= 5 && pointsDH() <= 19) {
            return "1 C"
        }

        // 1 Carreau
        if (Carreaux.size() >= 4
                && Carreaux.size() >= Trêfles.size()
                && pointsDH() <= 19) {
            return "1 K"
        }

        // 1 Carreau cas particulier distrib 4432 (jeu tricolore)
        def distrib = couleurs*.size()
        if (distrib == [4,4,3,2]) {
            return "1 K"
        }

        // 1 Trèfle
        if (Carreaux.size() == 3
                && Trêfles.size() == 3
                && pointsDH() <= 19) {
            return "1 T"
        }

        // 1 Trèfle conventionnel pour faciliter les enchères
        if (Piques.size() == 5
                && Trêfles.size() >= 5
                && pointsDH() <= 19) {
            return "1 T"
        }

        // 2 SA
        if (main_régulière() && H_entre(21,22)) {
            return "2 SA"
        }

        // 2 Pique
        if (Piques.size() >= 5
                && Piques.pointsH() >= 6
                && DH_entre(20,23)) {
            return "2 P"
        }

        // 2 Coeur
        if (Coeurs.size() >= 5
                && Coeurs.pointsH() >= 6
                && DH_entre(20,23)) {
            return "2 C"
        }

        // 2 Carreau
        if (Carreaux.size() >= 5
                && Carreaux.pointsH() >= 6
                && DH_entre(20,23)) {
            return "2 K"
        }

        // 2 Trèfle conventionnel


        return "Je sais pas"
    }
}


