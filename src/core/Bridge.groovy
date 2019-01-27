package core

/** Toutes les cartes d'une même couleur */
class Couleur {
    def cartes = []
    static cartes_valides = ["A", "R", "D", "V"]
    static points_honneur = ["A":4, 'R':3, 'D':2, 'V':1]
    static {
        (10..2).each {
            cartes_valides << it.toString()
            points_honneur[it.toString()] = 0
        }
    }

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

    int pointsH() {
        if (cartes.size() == 0) { return 0 }
        return cartes.collect( {c-> points_honneur[c]} ).sum()
    }

    String toString() {
        return cartes.toString()
    }

    List<String> honneurs() {
        return cartes.findAll { points_honneur[it] > 0 }
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
${if (this.bicolore()) {"Main bicolore"} else {"Main non-bicolore"}}
"""
    }

    int size() {
        return couleurs*.size().sum()
    }

    int pointsH() {
        return couleurs*.pointsH().sum()
    }

    int pointsDH() {
        return pointsD()+pointsH()
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
        if (bicolore()) {
            if ((longue1().honneurs().size() >= 2)
                  && (longue2()?.honneurs()?.size() >= 2)) {
                switch(longue1().size()+longue2().size()) {
                    case 10: d+= 1; break
                    case 11: d+= 2; break
                    case 12: d+= 3; break
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
    boolean bicolore() {
        def big1 = couleurs*.size().sort { -it }[0] // plus longue
        def big2 = couleurs*.size().sort { -it }[1] // 2ème plus longue
        return (big1 == 6 && big2 >= 4) || (big1 == 5 && big2 == 5)
    }
}


