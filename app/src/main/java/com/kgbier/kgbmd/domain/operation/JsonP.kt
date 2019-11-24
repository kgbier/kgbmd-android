package com.kgbier.kgbmd.domain.operation

object JsonP {
    fun toJson(input: String): String? {
        val keepLower = input.indexOf("{")
        val keepUpper = input.lastIndexOf("}")

        if (keepLower == -1 && keepUpper == -1) return null
        return input.substring(keepLower, keepUpper + 1)
    }
}