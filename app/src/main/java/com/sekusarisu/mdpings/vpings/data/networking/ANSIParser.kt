package com.sekusarisu.mdpings.vpings.data.networking

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object ANSIParser {
    // ANSI color mapping
    private val basicColors = mapOf(
        30 to Color.Black,   // Black
        31 to Color.Red,     // Red
        32 to Color.Green,   // Green
        33 to Color.Yellow,  // Yellow
        34 to Color.Blue,    // Blue
        35 to Color.Magenta, // Magenta
        36 to Color.Cyan,    // Cyan
        37 to Color.White,   // White

        // Bright colors
        90 to Color.DarkGray,
        91 to Color(0xFFFF6B6B),  // Bright Red
        92 to Color(0xFF4ECD50),  // Bright Green
        93 to Color(0xFFFFA534),  // Bright Yellow
        94 to Color(0xFF5E81F7),  // Bright Blue
        95 to Color(0xFFB968C7),  // Bright Magenta
        96 to Color(0xFF4DD0E1),  // Bright Cyan
        97 to Color.White
    )

    fun parseAnsiString(input: String): AnnotatedString = buildAnnotatedString {
        var index = 0
        var currentColor = Color.White
        var isBold = false
        var isItalic = false
        var isUnderline = false

        while (index < input.length) {
            // 检查ANSI转义序列
            if (input[index] == '\u001B' && index + 1 < input.length && input[index + 1] == '[') {
                // 找到转义序列的结束
                val endIndex = findSequenceEnd(input, index)
                if (endIndex != -1) {
                    val sequence = input.substring(index + 2, endIndex)

                    // 忽略模式切换序列
                    if (sequence.startsWith("?2004") && (sequence.endsWith('h') || sequence.endsWith('l'))) {
                        index = endIndex + 1
                        continue
                    }

                    val codes = sequence.split(';').mapNotNull { it.toIntOrNull() }

                    // 重置所有样式
                    if (codes.contains(0)) {
                        currentColor = Color.White
                        isBold = false
                        isItalic = false
                        isUnderline = false
                    }

                    // 处理文本颜色
                    codes.find { it in 30..37 || it in 90..97 }?.let { colorCode ->
                        currentColor = basicColors[colorCode] ?: Color.White
                    }

                    // 处理文本格式
                    isBold = codes.contains(1)
                    isItalic = codes.contains(3)
                    isUnderline = codes.contains(4)

                    index = endIndex + 1
                    continue
                }
            }

            // 添加带样式的字符
            val char = input[index]
            pushStyle(
                SpanStyle(
                    color = currentColor,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                    fontFamily = FontFamily.Monospace
                )
            )
            append(char)
            pop()

            index++
        }
    }

    // 帮助函数查找转义序列结束
    private fun findSequenceEnd(input: String, startIndex: Int): Int {
        val endChars = setOf('m', 'h', 'l', ']', '~')

        var index = startIndex + 2 // 跳过 ESC[
        while (index < input.length) {
            if (endChars.contains(input[index])) {
                return index
            }
            index++
        }
        return -1
    }
}

// Extension function for easy use in Compose
fun String.toAnsiAnnotatedString(): AnnotatedString {
    // 移除末尾的换行符
    val trimmedInput = this.trimEnd()
    return ANSIParser.parseAnsiString(trimmedInput)
}