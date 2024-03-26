package com.acszo.redomi.data

import androidx.datastore.core.Serializer
import com.acszo.redomi.model.AppsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AllAppSerializable: Serializer<AppsPreferences> {

    override val defaultValue: AppsPreferences
        get() = AppsPreferences()

    override suspend fun readFrom(input: InputStream): AppsPreferences {
        return try {
            Json.decodeFromString(
                deserializer = AppsPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppsPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = AppsPreferences.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}