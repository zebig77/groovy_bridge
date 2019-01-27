package core

import junit.framework.TestCase

class TestBridge extends TestCase {

    void testNewHand() {
        def m = new Main("A", "D 10 8 6 2", "R V 3", "10 9 8 4")
        println m
        assert m.size() == 13
        assert m.pointsH() == 10
        assert m.pointsD() == 2
        assert m.pointsDH() == 12
        assert m.main_bicolore() == false
        assert m.longue1() == m.Coeurs
        assert m.longue2() == null
        assert m.Piques.honneurs() == ["A"]
        assert m.Coeurs.honneurs() == ["D"]
        assert m.Carreaux.honneurs() == ["R","V"]
        assert m.Trêfles.honneurs() == []
        assert m.main_régulière() == false
    }

    void testNewHand2() {
        def m2 = new Main("7", "A R D 10 8 7", "A R D 10", "A 4")
        println m2
        assert m2.pointsH() == 22
        assert m2.pointsD() == 4
        assert m2.main_bicolore() == true
        assert m2.longue1() == m2.Coeurs
        assert m2.longue2() == null
    }

    void testNewHand3() {
        def m3 = new Main("10 7 6", "A R D 10", "A R D 10", "A 4")
        println m3
        assert m3.main_bicolore() == false
        assert m3.longue1() == null
        assert m3.longue2() == null
        assert m3.main_régulière()
    }

    void testNewHand4() {
        def m4 = new Main("A V 10 7 6", "D 10", "A R D 10 2", "4")
        println m4
        assert m4.main_bicolore() == true
        assert m4.longue1() == m4.Piques
        assert m4.longue2() == m4.Carreaux
        assert m4.pointsH() == 16
        assert m4.pointsD() == 4
    }

    void testPointsH() {
        new Main("R D V", "", "A D V 10 8 7 6 4 2", "A").with {
            println it
        }
    }
}