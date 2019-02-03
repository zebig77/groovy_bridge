package core

import junit.framework.TestCase

class TestOuvertures extends TestCase {

    void test1_1SA() {
        new Main("R V 7","A D 10","R V 9 7","R 7 4").with {
            assert ouverture() == "1 SA"
        }
    }

    void test2_1K() {
        new Main("V 4","A R V 7", "D 10 9 5", "R 7 2").with {
            assert ouverture() == "1 K"
        }
    }

    void test3_1P() {
        new Main("A R V 7 2","D 10 4", "R V 2", "9 7").with {
            assert ouverture() == "1 P"
        }
    }

    void test4_1P() {
        new Main("A D 10 4 2","R V 7 4 2","A 4","2").with {
            assert ouverture() == "1 P"
        }
    }

    void test5_Passe() {
        new Main("R D 4","A 7 4 2","R 3 2","7 6 5").with {
            assert ouverture() == "Passe"
        }
    }

    // 20DH et couleur 6ème avec gros honneurs
    void test6_2P() {
        new Main("A D 9 8 6 2", "A R 4", "A 10 2", "3").with {
            assert ouverture() == "2 P"
        }
    }

    // 15DH et longue à Pique
    void test7_1P() {
        new Main("A V 9 8 6", "3", "A V 7 6 5 2", "4").with {
            assert ouverture() == "1 P"
        }
    }

    // 17H longue peu robuste
    void test8_1SA() {
        new Main("R 8", "D 10 7 4 2", "A D 10", "R D V").with {
            assert ouverture() == "1 SA"
        }
    }

    // 26DH => 2T
    void test9_2T() {
        new Main("7", "A R D 10 8 7", "A R D 10", "A 4").with {
            assert ouverture() == "2 T"
        }
    }

    // 14H, main régulière 4-3-3-3 => 1 Trèfle
    void test10_1T() {
        new Main("9 6 2", "A D 10 4", "A D 7", "D 8 3").with {
            assert ouverture() == "1 T"
        }
    }

    // 15DH, tricolore 4-4-4-1, singleton majeur =>
    void test11_1K() {
        new Main("D 10 4 2", "4", "R V 7 2", "A R 10 4").with {
            assert ouverture() == "1 K"
        }
    }

    // 21H, main régulière 5-3-3-2 => 2 SA
    void test12_2SA() {
        new Main("R D 10", "A R", "R V 10 4 2", "A V 4").with {
            assert main_régulière()
            assert ouverture() == "2 SA"
        }
    }

    // 21DH, 6-5-X-X, honneurs concentrés dans les longues => 1 T
    void test13_1T() {
        new Main("2", "A R V 8 2", "5", "A D 10 8 7 3").with {
            assert main_bicolore()
            assert pointsDH() == 21
            assert ouverture() == "1 T"
        }
    }

    // 19DM, main régulière => 1 K
    void test14_1K() {
        new Main("A V 10", "D V 6", "R V 8 7", "A R 3").with {
            assert main_régulière()
            assert pointsDH() == 19
            assert ouverture() == "1 K"
        }
    }

    void test15_2T() {
        new Main("A R 8", "R D V", "A D 8", "A R 7 6").with {
            assert main_régulière()
            assert pointsDH() == 26
            assert ouverture() == "2 T"
        }
    }

    // 2 Fort à Pique
    void test16_2P() {
        new Main("A R D 10 8", "R V 6", "A R 8", "7 3").with {
            assert main_régulière()
            assert pointsH() == 20
            assert pointsDH() == 21
            assert ouverture() == "2 P"
        }
    }

    void test17_1SA() {
        new Main("R 3", "A D 7", "R D V 9 8", "8 7 3").with {
            assert main_régulière()
            assert pointsH() == 15
            assert pointsDH() == 16
            assert Carreaux.bien_constituée()
            assert ouverture() == "1 SA"
        }
    }

    // 17DH, bicolore majeure-mineure (Trèfles) => 1 T
    void test18_1T() {
        new Main("R V 10 7 4", "", "A D 8", "R V 8 7 6").with {
            assert !main_régulière()
            assert main_bicolore()
            assert pointsH() == 14
            assert pointsD() == 3
            assert !Piques.bien_constituée()
            assert !Trèfles.bien_constituée()
            assert ouverture() == "1 T"
        }
    }
}