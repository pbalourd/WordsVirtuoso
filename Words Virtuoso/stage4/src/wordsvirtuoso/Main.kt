package wordsvirtuoso

import java.io.File
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
    if (diffWords != 0) exitWithMessage("Error: $diffWords invalid words were found in the $wordsFilename file.")

    wordsStr = candidatesFile.readLines().map { it.lowercase() }
    val candidates = wordsStr.filter { isValidWord(it) }
    val diffCandidates = wordsStr.size - candidates.size
    if (diffCandidates != 0 ) exitWithMessage("Error: $diffCandidates invalid words were found in the $candidatesFilename file.")

    val candidatesNotInWords = candidates.count { it !in words }
    if (candidatesNotInWords != 0) exitWithMessage("Error: $candidatesNotInWords candidate words are not included in the $wordsFilename file.")

    println("Words Virtuoso\n")

    val startTime = System.currentTimeMillis()
    val hiddenWord = candidates[Random.nextInt(0, candidates.size)]
//    println(hiddenWord)

    var turns = 0
    do {
        turns++
        println("Input a 5-letter word: ")
        when (val input = readln().trim().lowercase()) {
            "exit" -> {
                println("\nThe game is over.")
                break
            }
            hiddenWord -> {
                println("\nCorrect!")
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
                    println("The input has duplicate letters.\n")
                    continue
                }
                if (input !in words) {
                    println("The input word isn't included in my words list.\n")
                    continue
                }

                input.forEachIndexed { index, c ->
                    if (c in hiddenWord) {
                        if (input[index] == hiddenWord[index]) print(c.uppercaseChar())
                        else print(c)
                    } else print("_")
                }
                println("\n")
            }
        }
    } while(true)
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
