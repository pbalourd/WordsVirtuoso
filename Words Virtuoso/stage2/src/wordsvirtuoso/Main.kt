package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("Input the words file:")
    val filename = readLine()!!

    val file = File(filename)

    if (!file.exists()) {
        println("Error: The words file $filename doesn't exist.")
        exitProcess(1)
    }

    val words = file.readLines().map { it.lowercase() }

    val validWords = mutableListOf<String>()
    words.forEach { if (isValidWord(it)) validWords.add(it) }

    val diff = words.size - validWords.size
    if (diff == 0) println("All words are valid!")
    else println ("Warning: $diff invalid words were found in the $filename file.")

//    val index = Random.nextInt(0, words.size)
//    println(words[index])
}

fun hasValidChars(str: String): Boolean {
    return str.all { c: Char -> c in 'a'..'z'}
}

fun hasUniqueLetters(str: String): Boolean {
    return str.all { c: Char ->
        str.count { it == c } == 1
    }
}

fun isValidWord(word: String): Boolean {
    return word.length == 5 && hasValidChars(word) && hasUniqueLetters(word)
}
