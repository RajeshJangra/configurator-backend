package com.daimler.configuratorbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ConfiguratorBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(ConfiguratorBackendApplication::class.java, *args)
}
