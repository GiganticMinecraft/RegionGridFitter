package click.seichi.regiongridfitter.region

import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.bukkit.selections.Selection
import org.bukkit.Location
import org.bukkit.World


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

infix fun CuboidSelection?.equalsWith(another: CuboidSelection?) =
    when (this) {
        null -> another == null
        else -> another != null &&
                maximumPoint == another.maximumPoint &&
                minimumPoint == another.minimumPoint &&
                world == another.world
    }

fun CuboidSelection.clone() = CuboidSelection(world, minimumPoint, maximumPoint)

private fun toVertExtendedSelection(world: World, xSpan: RealSpan, zSpan: RealSpan): CuboidSelection {
    val (minX, maxX) = Pair(xSpan.smallEnd, xSpan.largeEnd)
    val (minZ, maxZ) = Pair(zSpan.smallEnd, zSpan.largeEnd)

    val start = Vector(minX, 0.0, minZ)
    val end = Vector(maxX, 256.0, maxZ)

    return CuboidSelection(world, start, end)
}

fun Selection.fitToGrid(gridSize: Int): CuboidSelection {
    val (xSpan, _, zSpan) = toSpans()
    val (xGridSpan, zGridSpan) = listOf(xSpan, zSpan).map { it.getQuantizedEnclosure(gridSize) }

    return toVertExtendedSelection(world!!, xGridSpan, zGridSpan)
}
