package ru.cib.rubenbottelegram.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.facilities.TelegramHttpClientBuilder
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class RubenBotService(
    private val processor: Processor
) : TelegramLongPollingBot() {
    @Value("\${bot.name}")
    val name: String? = null

    @Value("\${bot.token}")
    val token: String? = null

    override fun getBotToken() = token

    override fun getBotUsername() = name

    override fun onUpdateReceived(update: Update) {
        val sessionGuid = UUID.randomUUID().toString()
        logger.debug("[$sessionGuid] Initialize session with bot")
        try {
            if (update.hasMessage()) {
                val message = update.message
                val chatId = message.chatId
                val messageId = message.messageId
                logger.debug("[$sessionGuid] Chat started for id: $chatId")
                val response = when (val text = message.text) {
                    "/start" -> "Welcome\\! This bot designed for send http requests from telegram"
                    "/help" -> "[Post/Get/Delete/Put] [URL] [Request]"
                    else -> processor.processIncomingText(text, sessionGuid)
                }
                logger.debug("[$sessionGuid] Forming response message and sending back")
                execute(processor.buildResponseMessage(chatId, messageId, response))
            } else
                throw Exception("Message is empty!!!")
        } catch (e: Exception) {
            logger.error("[$sessionGuid]: Error during receiving message: ${e.message}")
        }
    }
}