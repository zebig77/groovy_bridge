package sandbox

def l1 = [5,5,2,1]
def l2 = [6,4,2,1]
def l3 = [6,5,2,0]
def l4 = [6,6,1,0]
def l5 = [7,6,0,0]
def l6 = [5,4,2,2]
def all = [l1, l2, l3, l4, l5, l6]

all.each { liste ->
    print liste
    def big1 = liste.sort { -it }[0]
    def big2 = liste.sort { -it }[1]
    boolean bicolore = ((big1 == 6 && big2 >= 4) || (big1 == 5 && big2 == 5))
    println " big1 = $big1 big2 = $big2 bicolore : $bicolore"

}
