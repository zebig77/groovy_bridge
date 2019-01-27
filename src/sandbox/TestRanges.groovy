package sandbox

def m = [:]
def r = 10..2
r.each {m[it.toString()] = 0}
println r.toList()
println m

def r2 = "9".."2"
println r2.toList()
def l = ["A"] + r2
println l