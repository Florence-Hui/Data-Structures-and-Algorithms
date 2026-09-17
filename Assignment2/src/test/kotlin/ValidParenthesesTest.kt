import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ValidParenthesesTest {

    @Test
    fun testValidPairs() {
        assertTrue(isValid("()"))
        assertTrue(isValid("()[]{}"))
        assertTrue(isValid("{[()]}"))
    }

    @Test
    fun testInvalidMismatchedPairs() {
        assertFalse(isValid("(]"))
        assertFalse(isValid("([)]"))
    }

    @Test
    fun testUnbalancedLengths() {
        // Starts with closing bracket
        assertFalse(isValid("]"))
        assertFalse(isValid("]["))
        // Incomplete open bracket
        assertFalse(isValid("("))
        assertFalse(isValid("(()"))
    }

    @Test
    fun testEmptyString() {
        assertTrue(isValid(""))
    }
}