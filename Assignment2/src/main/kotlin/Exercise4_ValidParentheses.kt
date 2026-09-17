/**
 * Determines whether an input string containing bracket characters is valid.
 *
 * An input string is valid if:
 * Open brackets must be closed by the same type of brackets.
 * Open brackets must be closed in the correct order.
 * Every close bracket has a corresponding open bracket of the same type.
 *
 * ### Algorithm Overview:
 * The Valid Parentheses problem comes down to one structural rule: brackets must close in reverse order of how they were opened
 * Which is, most recently opened must be resolved first
 * This rule meets the definition/ property of stack (Last in, First out)
 * So, Stack is used to solve this problem
 *
 * - Opening brackets (`(`, `{`, `[`) are pushed onto the stack.
 * - Closing brackets (`)`, `}`, `]`) pop the most recent opening bracket and verify that the types match.
 * - If `pop()` returns `null` (empty stack) or the wrong opening bracket, the string is immediately invalid.
 *
 * @param s The string containing bracket characters to validate.
 * @return True if all brackets are properly matched and closed, false otherwise.
 */
fun isValid(s: String): Boolean {
    val stack = LinkedListStack<Char>()

    for (char in s) {
        when (char) {
            // Opening brackets: push to stack
            '(', '{', '[' -> stack.push(char)

            // Closing brackets: check match against top of stack
            ')' -> if (stack.pop() != '(') return false
            '}' -> if (stack.pop() != '{') return false
            ']' -> if (stack.pop() != '[') return false
        }
    }

    // Must be empty to ensure all open brackets were closed
    return stack.isEmpty()
}