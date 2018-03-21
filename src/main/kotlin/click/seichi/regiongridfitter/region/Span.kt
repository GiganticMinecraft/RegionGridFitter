package click.seichi.regiongridfitter.region

import kotlin.math.ceil
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
 */
fun RealSpan.getQuantizedEnclosure(unitSize: Int): RealSpan {
    val newLargeEnd = ceil(largeEnd / unitSize.toDouble()) * unitSize
    val newSmallEnd = floor(smallEnd / unitSize.toDouble()) * unitSize

    return RealSpan(newSmallEnd, newLargeEnd)
}
