package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.columns.values
import org.jetbrains.kotlinx.dataframe.impl.api.concatImpl
import org.jetbrains.kotlinx.dataframe.impl.asList

// region DataColumn

public fun <T> DataColumn<T>.concat(vararg other: DataColumn<T>): DataColumn<T> = concatImpl(name, listOf(this) + other)

public fun <T> DataColumn<DataFrame<T>>.concat(): DataFrame<T> = values.concat()

public fun <T> DataColumn<Collection<T>>.concat(): List<T> = values.flatten()

// endregion

// region DataRow

public fun <T> DataRow<T>.concat(vararg other: DataRow<T>): DataFrame<T> = (listOf(this) + other).concat()

// endregion

// region DataFrame

public fun <T> DataFrame<T>.concat(vararg other: DataFrame<T>): DataFrame<T> = concatImpl(listOf(this) + other)

// endregion

// region GroupBy

public fun <T, G> GroupBy<T, G>.concat(): DataFrame<G> = groups.concat()

// endregion

// region Iterable

public fun <T> Iterable<DataFrame<T>>.concat(): DataFrame<T> {
    return concatImpl(asList())
}

public fun <T> Iterable<DataColumn<T>>.concat(): DataColumn<T> {
    val list = asList()
    if (list.isEmpty()) return DataColumn.empty().cast()
    return concatImpl(list[0].name(), list)
}

@JvmName("concatRows")
public fun <T> Iterable<DataRow<T>?>.concat(): DataFrame<T> = concatImpl(map { it?.toDataFrame() ?: emptyDataFrame(1).cast() })

// endregion
