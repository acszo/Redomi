package com.acszo.redomi.data

import androidx.datastore.core.Serializer
import com.acszo.redomi.model.AllApps
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AllAppSerializable: Serializer<AllApps> {

    override val defaultValue: AllApps
        get() = AllApps()

    override suspend fun readFrom(input: InputStream): AllApps {
        return try {
            Json.decodeFromString(
                deserializer = AllApps.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AllApps, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AllApps.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}