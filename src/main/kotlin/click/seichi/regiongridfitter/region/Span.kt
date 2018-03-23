package click.seichi.regiongridfitter.region

import kotlin.math.floor

/**
 * 抽象的な「区間」のクラス。
 */
data class Span<T: Comparable<T>>(private val end1: T, private val end2: T) {
    val largeEnd = maxOf(end1, end2)
    val smallEnd = minOf(end1, end2)
}

typealias RealSpan = Span<Double>

/**
 * レシーバの区間を含むかつ[unitSize]で量子化された最小の区間を計算する。
 * 量子化された区間は、とある整数k, a, b(a < b)に対し、`[ka, kb - 1]`となるものである。
 * とある[Double] aを中心とする空の区間`[a, a]`は、aが属する量子化された区間に変換される。
 */
fun RealSpan.getQuantizedEnclosure(unitSize: Int): RealSpan {
    fun quantizedBlockNumber(position: Double) = floor(position / unitSize.toDouble())

    val newLargeEnd = (quantizedBlockNumber(largeEnd) + 1) * unitSize - 1.0
    val newSmallEnd = quantizedBlockNumber(smallEnd) * unitSize

    return RealSpan(newSmallEnd, newLargeEnd)
}
