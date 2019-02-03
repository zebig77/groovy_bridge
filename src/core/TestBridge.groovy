package core

import junit.framework.TestCase

class TestBridge extends TestCase {

    void testValHonneurs() {
        assert Couleur.val_honneur("A") == 4
        assert Couleur.val_honneur("R") == 3
        assert Couleur.val_honneur("D") == 2
        assert Couleur.val_honneur("V") == 1
        assert Couleur.val_honneur("10") == 0
        assert Couleur.val_honneur("2") == 0
    }

    void testNewHand() {
        new Main("A", "D 10 8 6 2", "R V 3", "10 9 8 4").with {
            assert size() == 13
            assert pointsH() == 10
            assert pointsD() == 2
            assert pointsDH() == 12
            assert !main_bicolore()
            assert longue1() == null
            assert longue2() == null
            assert Piques.honneurs() == ["A"]
            assert Coeurs.honneurs() == ["D"]
            assert Carreaux.honneurs() == ["R","V"]
            assert Trèfles.honneurs() == []
            assert !main_régulière()
            assert est_majeure(Piques)
            assert est_majeure(Coeurs)
            assert est_mineure(Carreaux)
            assert est_mineure(Trèfles)
            assert !Coeurs.bien_constituée()
        }
    }

    void testNewHand2() {
        new Main("7", "A R D 10 8 7", "A R D 10", "A 4").with {
            assert pointsH() == 22
            assert pointsD() == 5
            assert main_bicolore()
            assert longue1() == Coeurs
            assert longue2() == Carreaux
            assert Coeurs.bien_constituée()
        }
    }

    void testNewHand3() {
        new Main("10 7 6", "A R D 10", "A R D 10", "A 4").with {
            assert !main_bicolore()
            assert longue1() == null
            assert longue2() == null
            assert main_régulière()
        }
    }

    void testNewHand4() {
        new Main("A V 10 7 6", "D 10", "A R D 10 2", "4").with {
            assert main_bicolore()
            assert longue1() == Piques
            assert longue2() == Carreaux
            assert pointsH() == 16
            assert pointsD() == 4
            assert Carreaux.bien_constituée()
        }
    }

}