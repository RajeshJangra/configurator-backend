package com.daimler.configurator

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ConfiguratorApp

fun main(args: Array<String>) {
    SpringApplication.run(ConfiguratorApp::class.java, *args)
}
