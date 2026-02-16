package com.example.base.serializer

import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.StdDeserializer

class EmptyStringToNull: StdDeserializer<String?>(String::class.java)
{
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): String? {
        val value = p?.valueAsString
        return if (value == ""){
            null
        }else{
            value
        }
    }

}