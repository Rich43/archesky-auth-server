package com.archesky.auth.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages=[
	"com.archesky.auth.server",
	"com.archesky.ssl.library.configuration"
])
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
