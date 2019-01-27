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

    void test6_2P() {
        new Main("A D 9 8 6 2", "A R 4", "A 10 2", "3").with {
            assert ouverture() == "2 P"
        }
    }

}