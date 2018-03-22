package click.seichi.regiongridfitter.util

/**
 * Returns a [Map] containing all mappings whose values are not null.
 *
 * The returned map preserves the entry iteration order of the original map.
 */
@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterNotNullValues() = filterValues { it != null } as Map<K, V>
