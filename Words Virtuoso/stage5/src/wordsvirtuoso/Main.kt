package wordsvirtuoso

import java.io.File
import kotlin.math.round
import kotlin.random.Random
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
    if (diffWords != 0) exitWithMessage("Error: $diffWords invalid words where find in the $wordsFilename file.")

    wordsStr = candidatesFile.readLines().map { it.lowercase() }
    val candidates = wordsStr.filter { isValidWord(it) }
    val diffCandidates = wordsStr.size - candidates.size
    if (diffCandidates != 0 ) exitWithMessage("Error: $diffCandidates invalid words where find in the $candidatesFilename file.")

    val candidatesNotInWords = candidates.count { it !in words }
    if (candidatesNotInWords != 0) exitWithMessage("Error: $candidatesNotInWords candidate words are not included in the $wordsFilename file.")

    println("Words Virtuoso")

    val startTime = System.currentTimeMillis()
    val hiddenWord = candidates[Random.nextInt(0, candidates.size)]
//    println(hiddenWord)

    val invalidChars = mutableSetOf<Char>()
    val history = mutableListOf<String>()

    var turns = 0
    do {
        turns++
        println("\nInput a 5-letter word: ")
        when (val input = readln().trim().lowercase()) {
            "exit" -> {
                println("The game is over.")
                break
            }
            hiddenWord -> {
                saveToHistory(input, hiddenWord, invalidChars, history)
                println()
                printHistory(history)
                println("\nCorrect!")
                val timeLapsed = round((System.currentTimeMillis() - startTime).toDouble() / 1000).toLong()
                if (turns == 1) println("Amazing luck! The solution was found at once.")
                else println("The solution was found after $turns tries in $timeLapsed seconds.")
                break
            }
            else -> {
                if (input.length != 5) {
                    println("The input isn't a 5-letter word.\n")
                    continue
                }
                if (!hasValidChars(input)) {
                    println("One or more letters of the input aren't valid.\n")
                    continue
                }
                if (!hasUniqueLetters(input)) {
                    println("The input has duplicated letters.\n")
                    continue
                }
                if (input !in words) {
                    println("The input word isn't included in my words list.\n")
                    continue
                }

                println()
                saveToHistory(input, hiddenWord, invalidChars, history)
                printHistory(history)
                println()
                printInvalidChars(invalidChars)
                println()
            }
        }
    } while(true)
}

private fun saveToHistory(
    input: String,
    hiddenWord: String,
    invalidChars: MutableSet<Char>,
    history: MutableList<String>
) {
    val str = StringBuilder()
    input.forEachIndexed { index, c ->
        if (c in hiddenWord) {
            if (input[index] == hiddenWord[index]) str.append("${c.uppercase()}")
            else str.append("${c.lowercase()}")
        } else {
            invalidChars.add(c)
            str.append("_")
        }
    }
    history.add(str.toString())
}

private fun printInvalidChars(invalidChars: MutableSet<Char>) {
    invalidChars.sorted().forEach { print(it.uppercase()) }
}

fun printHistory(history: List<String>) {
    history.forEach { println(it) }
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
