package ru.cib.rubenbottelegram

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class Utils {

    fun formatReservedCharacter(text: String) =
        text.replace("!", "\\!")
            .replace("_", "\\_")
            .replace(".", "\\.")
            .replace("}", "\\}")
            .replace("{", "\\{")
            .replace("|", "\\|")
            .replace("=", "\\=")
            .replace("-", "\\-")
            .replace("+", "\\+")
            .replace("#", "\\#")
            .replace(">", "\\>")
            .replace("`", "\\`")
            .replace("~", "\\~")
            .replace(")", "\\)")
            .replace("(", "\\(")
            .replace("]", "\\]")
            .replace("[", "\\[")
            .replace("*", "\\*")

    fun cutToMaxMessageLength(text: String) =
        if (text.length > 4096)
            text.chunked(4096)
        else
            listOf(text)
}