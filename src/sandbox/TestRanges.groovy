package sandbox

def m = [:]
def r = 10..2
r.each {m[it.toString()] = 0}
println r.toList()
println m