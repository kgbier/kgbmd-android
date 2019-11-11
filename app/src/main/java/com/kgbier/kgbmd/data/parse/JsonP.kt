package com.kgbier.kgbmd.data.parse

object JsonP {
    fun toJson(input: String): String {
        val keepLower = input.indexOf("{")
        val keepUpper = input.lastIndexOf("}")
        return input.substring(keepLower, keepUpper)
    }
}