package core

/** Toutes les cartes d'une même couleur */
class Couleur {
    def cartes = []
    static final cartes_valides = ["A", "R", "D", "V", "10"] + ("9".."2")

    // constructeur avec couleur structurée comme par ex "A R V 2"
    Couleur(String lc) {
        lc.split().each {
            if (cartes_valides.contains(it)) {
                cartes << it
            } else {
                throw new Exception("La carte $it est invalide")
            }
        }
    }

    int size() {
        cartes.size()
    }

    static int val_honneur(String c) {
        switch (c) {
            case 'A': return 4
            case 'R': return 3
            case 'D': return 2
            case 'V': return 1
        }
        return 0
    }

    int pointsH() {
        if (cartes.size() == 0) {
            return 0
        }
        cartes.sum { val_honneur(it as String) } as int
    }

    String toString() {
        cartes.toString()
    }

    List<String> honneurs() {
        cartes.findAll { val_honneur(it as String) > 0 }
    }

    boolean un_as() {
        cartes.contains("A")
    }

    // TODO demander définition à un expert
    boolean robuste() {
        // en attendant longue avec AS ou 3 honneurs
        if (size() >= 5 // au moins 5 cartes
                && (un_as() || honneurs().size() >= 3)) {
            return true
        }
        return false
    }

    // TODO demander à un expert
    boolean bien_constituée() {
        // au moins 5 cartes, au moins 6H, au moins 2 non-honneurs élevés (10, 9, 8)
        return size() >= 5 && pointsH() >= 6 && cartes.minus(2..6).size() >= 4
    }

}

/** Main d'un joueur */
class Main {

    Couleur Piques
    Couleur Coeurs
    Couleur Carreaux
    Couleur Trèfles
    List<Couleur> couleurs
    def distrib = [] // exemple 5-5-2-1

    Main(String p, String c, String k, String t) {
        Piques = new Couleur(p)
        Coeurs = new Couleur(c)
        Carreaux = new Couleur(k)
        Trèfles = new Couleur(t)
        couleurs = [Piques, Coeurs, Carreaux, Trèfles]
        distrib = couleurs*.size().sort { -it }
    }

    String toString() {
        return """\
Piques   $Piques
Coeurs   $Coeurs
Carreaux $Carreaux
Trèfles  $Trèfles
${this.pointsH()} points d'honneur
${this.pointsD()} points de distribution
${
            if (this.main_bicolore()) {
                "Main bicolore"
            } else {
                "Main non-bicolore"
            }
        }
"""
    }

    int size() {
        return couleurs*.size().sum() as int
    }

    int pointsH() {
        return couleurs*.pointsH().sum() as int
    }

    boolean H_entre(int min, int max) {
        return pointsH() >= min && pointsH() <= max
    }

    int pointsDH() {
        return pointsD() + pointsH()
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
                case 6..13: d += (it.size() - 5); break // +1D à partir de la 6ème
            }
        }
        // si la main est bicolore et qu'une au moins est bien constituée
        if (main_bicolore() && couleurs.any({ it.bien_constituée() })) {
            switch (longue1().size() + longue2().size()) {
                case 10: d += 1; break // distrib 5-5 -> +1
                case 11: d += 2; break // distrib 6-5 -> +2
                case 12: d += 3; break // distrib 6-6 ou 7-5 -> +3
                case 13: d += 3; break // distrib 7-6 -> +3
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

    // distributions 5-5-X-X, 6-4-X-X, 6-5-X-X, 6-6-X-X
    boolean main_bicolore() {
        def big1 = distrib[0]
        def big2 = distrib[1]
        return (big1 == 6 && big2 >= 4) || (big1 == 5 && big2 == 5)
    }

    // distributions 6-6-X-X, 6-5-X-X, 5-5-X-X
    boolean main_bicolore_complète() {
        def big1 = distrib[0]
        def big2 = distrib[1]
        return (big1 >= 5 && big2 >= 5)
    }

    // distributions 4-3-3-3, 4-4-3-2, 5-3-3-2
    boolean main_régulière() {
        if (distrib == [4, 3, 3, 3]) {
            return true
        }
        if (distrib == [4, 4, 3, 2]) {
            return true
        }
        if (distrib == [5, 3, 3, 2]) {
            return true
        }
        return false
    }

    // Dans le cas des mains bicolores, la plus longue
    Couleur longue1() {
        if (!main_bicolore()) {
            return null
        }
        return couleurs.sort({ -it.size() })[0]
    }

    // Dans le cas des mains bicolores, la 2ème plus longue
    Couleur longue2() {
        if (!main_bicolore()) {
            return null
        }
        return couleurs.sort({ -it.size() })[1]
    }

    boolean est_majeure(Couleur c) {
        return c.equals(Piques) || c.equals(Coeurs)
    }

    boolean est_mineure(Couleur c) {
        return !est_majeure(c)
    }

    String ouverture() {
        if (pointsH() < 13 && pointsDH() < 14) {
            return "Passe"
        }

        // 1 SA : main régulière, entre 16 et 18H ou 15H mais au moins 1 main bien constituée
        if (main_régulière() && (
                H_entre(16, 18) ||
                        (pointsH() == 15 && couleurs.any({ it.bien_constituée() }))
        )) {
            return "1 SA"
        }

        // 1 Pique
        if (Piques.size() >= 5
                && Coeurs.size() < 7
                && Trèfles.size() < 5
                && pointsDH() <= 19) {
            return "1 P"
        }

        // 1 Coeur
        if (Coeurs.size() >= 5 && pointsDH() <= 19) {
            return "1 C"
        }

        // 1 Carreau
        if (Carreaux.size() >= 4
                && Carreaux.size() >= Trèfles.size()
                && pointsDH() <= 19) {
            return "1 K"
        }

        // 1 Carreau cas particulier distrib 4432 (jeu tricolore)
        if (distrib == [4, 4, 3, 2]) {
            return "1 K"
        }

        // 1 Trèfle
        if (Carreaux.size() == 3
                && Trèfles.size() == 3
                && pointsDH() <= 19) {
            return "1 T"
        }

        // 1 Trèfle conventionnel pour faciliter les enchères
        if (Piques.size() == 5
                && Trèfles.size() >= 5
                && pointsDH() <= 19) {
            return "1 T"
        }

        // bicolore majeure/mineure mais mineure > majeure => ouvrir 1 mineure
        if (main_bicolore() &&
                longue1().size() > longue2().size() &&
                est_mineure(longue1()) &&
                est_majeure(longue2()) &&
                longue1().pointsH() >= 6 && // honneurs concentrés dans les longues
                longue2().pointsH() >= 6
        ) {
            if (longue1() == Carreaux) {
                return "1 K"
            } else {
                return "1 T"
            }
        }

        // 2 SA
        if (main_régulière() && H_entre(21, 22)) {
            return "2 SA"
        }

        // 2 Pique
        if (Piques.size() >= 5
                && Piques.pointsH() >= 6
                && DH_entre(20, 23)) {
            // cas d'exclusions
            if (!Piques.robuste()) {
                return "1 P"    // Pique pas assez robuste
            }
            if (main_bicolore_complète()) {
                return "1 P"    // Autre couleur au moins 5ème
            }
            return "2 P"
        }

        // 2 Coeur
        if (Coeurs.size() >= 5
                && Coeurs.pointsH() >= 6
                && DH_entre(20, 23)) {
            // cas d'exclusions
            if (!Coeurs.robuste()) {
                return "1 C"    // Pique pas assez robuste
            }
            if (main_bicolore_complète()) {
                return "1 C"    // Autre couleur au moins 5ème
            }
            return "2 C"
        }

        // 2 Carreau
        if (Carreaux.size() >= 5
                && Carreaux.pointsH() >= 6
                && DH_entre(20, 23)) {
            // cas d'exclusions
            if (!Carreaux.robuste()) {
                return "1 K"    // Pique pas assez robuste
            }
            if (main_bicolore_complète()) {
                return "1 K"    // Autre couleur au moins 5ème
            }

            return "2 K"
        }

        // 1 Trèfle cas particulier Trèfle fort
        if (Trèfles.size() >= 5
                && DH_entre(20, 23)) {
            return "1 T"
        }

        // 2 Trèfle conventionnel
        if (pointsDH() >= 24 || pointsH() >= 23) {
            return "2 T"
        }

        return "Je sais pas"
    }
}


