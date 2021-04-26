package e.roman.nm_lab3

import kotlin.math.ln

fun triDiagonalSolve(a: MutableList<MutableList<Float>>): MutableList<Float> {
    val x = mutableListOf<Float>()
    val p = mutableListOf<Float>()
    val q = mutableListOf<Float>()
    p.add(-a[0][2] / a[0][1])
    q.add(a[0][3] / a[0][1])
    for (i in 1 .. a.lastIndex){
        p.add(-a[i][2] / (a[i][1] + a[i][0] * p[i - 1]))
        q.add((a[i][3] - a[i][0] * q[i - 1]) / (a[i][1] + a[i][0] * p[i - 1]))
    }
    x.add(q.last())
    for (i in a.lastIndex - 1 downTo 0)
        x.add(p[i] * x.last() + q[i])
    x.reverse()
    return x
}

fun luSolve(a: Matrix, b: MutableList<Float>): MutableList<Float> {
    var l = Matrix(a.data.size, 1f)
    val u = Matrix.copy(a) //Matrix(a.data.size, a.data.size, 0f)
    var m: Matrix
    for (i in a.data.indices) {
        m = Matrix(a.data.size, 1f)
        for (j in i + 1 until a.data.size) {
            m.data[j][i] = u.data[j][i] / u.data[i][i]
            for (k in a.data.indices) {
                u.data[j][k] -= m.data[j][i] * u.data[i][k]
            }
        }
        l = Matrix.multiply(l, m)
    }
    val z = solveZ(b, l)
    return solveX(z, u)
}

private fun solveX(z: MutableList<Float>, u: Matrix): MutableList<Float> {
    val x = mutableListOf<Float>()
    for (i in z.indices)
        x.add(0f)
    x[x.lastIndex] = z.last() / u.data.last().last()
    for (i in x.lastIndex - 1 downTo 0) {
        x[i] = z[i]
        for (j in i + 1..x.lastIndex)
            x[i] -= u.data[i][j] * x[j]
        x[i] /= u.data[i][i]
    }
    return x
}

private fun solveZ(b: MutableList<Float>, l: Matrix): MutableList<Float> {
    val z = mutableListOf<Float>()
    z.add(b[0])
    for (i in 1 until b.size) {
        z.add(b[i])
        for (j in 0 until i)
            z[z.lastIndex] -= l.data[i][j] * z[j]
    }
    return z
}

fun f(x: Float, a: MutableList<Float>): Float {
    if (a.size == 2)
        return a[0] + a[1] * x
    return a[0] + a[1] * x + a[2] * x * x
}

fun f(x: Float): Float {
    return ln(x)
}

fun f(x: MutableList<Float>): Float {
    if (x.size == 2)
        return (f(x[0]) - f(x[1])) / (x[0] - x[1])
    val tmp1 = x.toMutableList()
    val tmp2 = x.toMutableList()
    tmp1.removeAt(tmp1.lastIndex)
    tmp2.removeAt(0)
    return (f(tmp1) - f(tmp2)) / (x[0] - x.last())
}

fun s(x: Float, xPrev: Float, a: Float, b: Float, c: Float, d: Float): Float {
    return a + b * (x - xPrev) + c * (x - xPrev) * (x - xPrev) + d * (x - xPrev) * (x - xPrev) * (x - xPrev)
}

fun h(x: MutableList<Float>, i: Int): Float {
    return x[i] - x[i - 1]
}