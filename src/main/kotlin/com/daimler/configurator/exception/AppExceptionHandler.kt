package com.daimler.configurator.exception

import com.daimler.configurator.constant.AppConstants.MODEL_NOT_FOUND
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class AppExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = MODEL_NOT_FOUND)
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(request: HttpServletRequest, ex: Exception): String {
        LOGGER.error(ex.message)
        return "MODEL_NOT_FOUND"
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AppExceptionHandler::class.java)
    }
}
