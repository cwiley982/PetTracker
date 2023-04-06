package com.caitlynwiley.pettracker.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type

class DocumentTypeAdapterFactory(private val gson: Gson) : TypeAdapterFactory {
    override fun <T : Any?> create(p0: Gson?, p1: TypeToken<T>?): TypeAdapter<T> {
        p1?.type?.let { return DocumentTypeAdapter(gson, p1.type) }
    }

    class DocumentTypeAdapter<T>(private val gson: Gson, private val type: Type) : TypeAdapter<T>() {

        override fun write(p0: JsonWriter?, p1: T) {
            TODO("Not yet implemented")
        }

        override fun read(reader: JsonReader?): T {
            var result: T? = null
            reader?.beginObject()
            val key: String = reader?.nextString() ?: ""
            if (key == "name") {
                Log.d("DocumentTypeAdapter", "name: " + reader?.nextString())
            } else if (key == "fields") {
                result = gson.fromJson(reader, type)
            }

            return result ?: T()
        }
/*
        {
            "name": "projects/pet-tracker-1530373031875/databases/(default)/documents/users/uUXGp7mk7MhzCbpO5imC",
            "fields": {
                "pets": {
                    "arrayValue": {
                        "values": [
                            {
                                "referenceValue": "projects/pet-tracker-1530373031875/databases/(default)/documents/pets/I323LBlhBPp39ESiplmv"
                            }
                        ]
                    }
                },
                "uid": {
                    "stringValue": "123"
                }
            },
            "createTime": "2021-07-30T02:00:42.710838Z",
            "updateTime": "2021-07-30T17:11:08.803645Z"
        }
*/
    }
}