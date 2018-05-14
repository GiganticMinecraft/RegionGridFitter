package click.seichi.regiongridfitter.extensions

import click.seichi.regiongridfitter.region.RealSpan
import com.sk89q.worldedit.bukkit.selections.Selection
import org.bukkit.Location

fun Selection.toSpans(): Triple<RealSpan, RealSpan, RealSpan> {
    fun Location.components() = Triple(x, y, z)
    fun <T> Pair<Triple<T, T, T>, Triple<T, T, T>>.transpose() = Triple(
            first.first to second.first,
            first.second to second.second,
            first.third to second.third
    )

    val xyzSpans = Pair(maximumPoint.components(), minimumPoint.components()).transpose()
            .toList()
            .map { RealSpan(it.first, it.second) }

    return Triple(xyzSpans[0], xyzSpans[1], xyzSpans[2])
}