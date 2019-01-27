package core

import junit.framework.TestCase

class TestOuvertures extends TestCase {

    void test1_1SA() {
        new Main("R V 7","A D 10","R V 9 7","R 7 4").with {
            assert ouverture() == "1 SA"
        }
    }

    void test4_1P() {
        new Main("A D 10 4 2","R V 7 4 2","A 4","2").with {
            assert ouverture() == "1 P"
        }
    }
}