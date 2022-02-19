package com.guilherme.marvelcharacters.remote.util

import java.io.FileNotFoundException

fun readFile(path: String): String {
    val content = ClassLoader.getSystemResource(path)

    return content?.readText() ?: throw FileNotFoundException("File was not found in $path")
}