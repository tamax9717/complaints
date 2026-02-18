package com.example.complaint

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ComplaintApplication

fun main(args: Array<String>) {
	runApplication<ComplaintApplication>(*args)
}
