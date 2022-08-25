package wordsvirtuoso

fun main(args: Array<String>) {
    println("Input a 5-letter string:" )
    val input = readln().trim().lowercase()

    if (input.length != 5) println("The input isn't a 5-letter string.")
    else if (!hasValidChars(input)) println("The input has invalid characters.")
    else if (!hasUniqueLetters(input)) println("The input has duplicate letters.")
    else println("The input is a valid string.")
}

fun hasValidChars(str: String): Boolean {
    return str.all { c: Char -> c in 'a'..'z'}
}

fun hasUniqueLetters(str: String): Boolean {
    return str.all { c: Char ->
        str.count { it == c } == 1
    }
}
