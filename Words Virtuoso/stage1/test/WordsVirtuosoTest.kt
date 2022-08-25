import org.hyperskill.hstest.dynamic.DynamicTest
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testing.TestedProgram

class WordsVirtuosoTest : StageTest<Any>() {

    @DynamicTest(order = 1)
    fun noFiveLetterTest(): CheckResult {
        val inputStrings = listOf(
            "Trains",
            "word",
            "ART",
            "result"
        )

        inputStrings.forEach {input ->
            val co = CheckOutput()
            if ( !co.start("Input a 5-letter string:") )
                return CheckResult(false, "Your output should contain \"Input a 5-letter string:\"")
            if (!co.input(input, "The input isn't a 5-letter string."))
                return CheckResult(false, "Your output should contain \"The input isn't a 5-letter string.\"")
            if (!co.programIsFinished() )
                return CheckResult(false, "The application didn't exit.")
        }

        return CheckResult.correct()
    }

    @DynamicTest(order = 2)
    fun nonEnglishCharsTest(): CheckResult {
        val inputStrings = listOf(
            "ήλιος",
            "Δrash",
            "edt#r",
            "1nums",
            "wo rd"
        )

        inputStrings.forEach {input ->
            val co = CheckOutput()
            if ( !co.start("Input a 5-letter string:") )
                return CheckResult(false, "Your output should contain \"Input a 5-letter string:\"")
            if (!co.input(input, "The input has invalid characters."))
                return CheckResult(false, "Your output should contain \"The input has invalid characters.\"")
            if (!co.programIsFinished() )
                return CheckResult(false, "The application didn't exit.")
        }

        return CheckResult.correct()
    }

    @DynamicTest(order = 3)
    fun noDuplicateLettersTest(): CheckResult {
        val inputStrings = listOf(
            "Hello",
            "HELLO",
            "troop",
            "fuzzy",
            "pizza"
        )

        inputStrings.forEach {input ->
            val co = CheckOutput()
            if ( !co.start("Input a 5-letter string:") )
                return CheckResult(false, "Your output should contain \"Input a 5-letter string:\"")
            if (!co.input(input, "The input has duplicate letters."))
                return CheckResult(false, "Your output should contain \"The input has duplicate letters.\"")
            if (!co.programIsFinished() )
                return CheckResult(false, "The application didn't exit.")
        }

        return CheckResult.correct()
    }

    @DynamicTest(order = 4)
    fun correctWordsTest(): CheckResult {
        val inputStrings = listOf(
            "ovals", "SpilT", "FLARE", "crypt"
        )

        inputStrings.forEach {input ->
            val co = CheckOutput()
            if ( !co.start("Input a 5-letter string:") )
                return CheckResult(false, "Your output should contain \"Input a 5-letter string:\"")
            if (!co.input(input, "The input is a valid string."))
                return CheckResult(false, "Your output should contain \"The input is a valid string.\"")
            if (!co.programIsFinished() )
                return CheckResult(false, "The application didn't exit.")
        }

        return CheckResult.correct()
    }
}

class CheckOutput {
    private var main: TestedProgram = TestedProgram()
    var position = 0
    private var caseInsensitive = true
    private var trimOutput = true
    private val arguments= mutableListOf<String>()
    private var isStarted = false
    private var lastOutput = ""

    private fun checkOutput(outputString: String, vararg checkStr: String): Boolean {
        var searchPosition = position
        for (cStr in checkStr) {
            val str = if (caseInsensitive) cStr.lowercase() else cStr
            val findPosition = outputString.indexOf(str, searchPosition)
            if (findPosition == -1) return false
            if ( outputString.substring(searchPosition until findPosition).isNotBlank() ) return false
            searchPosition = findPosition + str.length
        }
        position = searchPosition
        return true
    }

    fun start(vararg checkStr: String): Boolean {
        return if (!isStarted) {
            var outputString = main.start(*arguments.toTypedArray())
            lastOutput = outputString
            if (trimOutput) outputString = outputString.trim()
            if (caseInsensitive) outputString = outputString.lowercase()
            isStarted = true
            checkOutput(outputString, *checkStr)
        } else false
    }

    fun stop() {
        main.stop()
    }

    fun input(input: String, vararg checkStr: String): Boolean {
        if (main.isFinished) return false
        var outputString = main.execute(input)
        lastOutput = outputString
        if (trimOutput) outputString = outputString.trim()
        if (caseInsensitive) outputString = outputString.lowercase()
        position = 0
        return checkOutput(outputString, *checkStr)
    }

    fun inputNext(vararg checkStr: String): Boolean {
        var outputString = lastOutput
        if (trimOutput) outputString = outputString.trim()
        if (caseInsensitive) outputString = outputString.lowercase()
        return checkOutput(outputString, *checkStr)
    }

    fun getNextOutput(input: String): String {
        if (main.isFinished) return ""
        val outputString = main.execute(input)
        lastOutput = outputString
        position = 0
        return  outputString
    }

    fun getLastOutput(): String { return lastOutput }
    fun programIsFinished(): Boolean  = main.isFinished
    fun setArguments(vararg args: String) { arguments.addAll(args.toMutableList()) }
    fun setCaseSensitivity(caseInsensitive: Boolean) { this.caseInsensitive = caseInsensitive }
    fun setOutputTrim(trimOutput: Boolean) { this.trimOutput = trimOutput}
}