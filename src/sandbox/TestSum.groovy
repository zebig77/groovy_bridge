package sandbox

class Bidule {
    int i = 1
    int size() { return i }
}

def b1 = new Bidule()
def b2 = new Bidule()
def b3 = new Bidule()
def bidules = [b1, b2, b3]
assert bidules*.size().sum() == 3
