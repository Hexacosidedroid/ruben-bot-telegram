package ru.cib.rubenbottelegram.service

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.cib.rubenbottelegram.Utils
import java.net.URI

private val logger = KotlinLogging.logger {}

@Component
class Processor(
    private val utils: Utils
) {

    fun processIncomingText(text: String, sessionId: String) = try {
        logger.debug("[$sessionId] Received message for request, starting to parse it: $text")
        val requestParamsArray = text.split(" ")
        val response = when (requestParamsArray[0].uppercase()) {
            "POST" -> {
                logger.debug("[$sessionId] Starting to send POST request to ${requestParamsArray[1]}")
                var postResult = RestTemplate().postForEntity(
                    URI(requestParamsArray[1]),
                    requestParamsArray[2],
                    String::class.java
                ).body
                if (!postResult.isNullOrBlank()) {
                    postResult = utils.formatReservedCharacter(postResult.trim())
                    utils.cutToMaxMessageLength(postResult)
                } else
                    throw Exception("Post request returned empty response")
            }
            "GET" -> {
                logger.debug("[$sessionId] Starting to send GET request to ${requestParamsArray[1]}")
                var getResult = RestTemplate().getForEntity(
                    URI(requestParamsArray[1]),
                    String::class.java
                ).body
                if (!getResult.isNullOrBlank()) {
                    getResult = utils.formatReservedCharacter(getResult.trim())
                    utils.cutToMaxMessageLength(getResult)
                } else
                    throw Exception("Get request returned empty response")
            }
            "DELETE" -> {
                logger.debug("[$sessionId] Starting to send DELETE request to ${requestParamsArray[1]}")
                RestTemplate().delete(
                    URI(requestParamsArray[1])
                )
                listOf("OK")
            }
            "PUT" -> {
                logger.debug("[$sessionId] Starting to send PUT request to ${requestParamsArray[1]}")
                RestTemplate().put(
                    URI(requestParamsArray[1]),
                    requestParamsArray[2]
                )
                listOf("OK")
            }
            else -> {
                throw Exception("This http method is not allowed!!!")
            }
        }
        logger.debug("[$sessionId] Request sent successfully, starting to send back response")
        response
    } catch (e: Exception) {
        logger.error("[$sessionId] Error during sending request: ${e.message}")
        val errorResponse = utils.formatReservedCharacter(e.message!!)
        utils.cutToMaxMessageLength(errorResponse)
    }

    fun buildResponseMessage(chatId: Long, messageId: Int, response: String) =
        SendMessage("$chatId", response).apply {
            enableMarkdownV2(true)
            replyToMessageId = messageId
            disableWebPagePreview = false
        }
}