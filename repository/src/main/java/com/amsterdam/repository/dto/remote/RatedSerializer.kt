package com.amsterdam.repository.dto.remote

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object RatedSerializer : KSerializer<Rated> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Rated")

    override fun deserialize(decoder: Decoder): Rated {
        val input = decoder as? JsonDecoder ?: error("Expected JsonDecoder")
        val element = input.decodeJsonElement()

        return when {
            // Case: rated: { "value": 8.0 }
            element is JsonObject && "value" in element -> {
                val value = element["value"]?.jsonPrimitive?.floatOrNull
                    ?: throw IllegalArgumentException("Invalid rated value")
                Rated.RatedValue(value)
            }

            // Case: rated: false
            element is JsonPrimitive && element.booleanOrNull == false -> Rated.NotRated

            else -> Rated.NotRated
        }
    }

    override fun serialize(encoder: Encoder, value: Rated) {
        val output = encoder as? JsonEncoder ?: error("Expected JsonEncoder")

        val element = when (value) {
            is Rated.RatedValue -> buildJsonObject {
                put("value", JsonPrimitive(value.value))
            }

            Rated.NotRated -> JsonPrimitive(false)
        }

        output.encodeJsonElement(element)
    }
}
