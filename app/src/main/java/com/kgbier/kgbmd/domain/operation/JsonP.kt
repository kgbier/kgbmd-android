package com.kgbier.kgbmd.domain.operation

object JsonP {
    fun toJson(input: String): String {
        val keepLower = input.indexOf("{")
        val keepUpper = input.lastIndexOf("}")
        return input.substring(keepLower, keepUpper + 1)
    }
}