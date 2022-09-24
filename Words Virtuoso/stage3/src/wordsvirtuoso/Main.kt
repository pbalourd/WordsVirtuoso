package wordsvirtuoso

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) exitWithMessage("Error: Wrong number of arguments.")
    val wordsFilename = args[0]
    val candidatesFilename = args[1]

    val wordsFile = File(wordsFilename)
    if (!wordsFile.exists()) exitWithMessage("Error: The words file $wordsFilename doesn't exist.")

    val candidatesFile = File(candidatesFilename)
    if (!candidatesFile.exists()) exitWithMessage("Error: The candidate words file $candidatesFilename doesn't exist.")

    var wordsStr = wordsFile.readLines().map { it.lowercase() }
    val words = wordsStr.filter { isValidWord(it) }
    val diffWords = wordsStr.size - words.size
    if (diffWords != 0) exitWithMessage("Error: $diffWords invalid words were found in the $wordsFilename file.")

    wordsStr = candidatesFile.readLines().map { it.lowercase() }
    val candidates = wordsStr.filter { isValidWord(it) }
    val diffCandidates = wordsStr.size - candidates.size
    if (diffCandidates != 0 ) exitWithMessage("Error: $diffCandidates invalid words were found in the $candidatesFilename file.")

    val candidatesNotInWords = candidates.count { it !in words }
    if (candidatesNotInWords != 0) exitWithMessage("Error: $candidatesNotInWords candidate words are not included in the $wordsFilename file.")

    println("Words Virtuoso")

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

fun exitWithMessage (message: String) {
    println(message)
    exitProcess(1)
}
