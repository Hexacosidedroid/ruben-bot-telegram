package ru.cib.rubenbottelegram

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
        if (text.length >= 4096)
            text.substring(0, 4095)
        else
            text
}