package com.acszo.redomi.data

import androidx.datastore.core.Serializer
import com.acszo.redomi.model.AppsConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AllAppSerializable: Serializer<AppsConfig> {

    override val defaultValue: AppsConfig
        get() = AppsConfig()

    override suspend fun readFrom(input: InputStream): AppsConfig {
        return try {
            Json.decodeFromString(
                deserializer = AppsConfig.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppsConfig, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = AppsConfig.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}