package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController
class MessageResource(val service: MessageService) {
	@GetMapping
	fun index() : List<Message> = listOf(
		Message("1", "Hello!"),
		Message("2", "Areeba"),
		Message("3", "Fatima")
	)

	@GetMapping("/messages")
	fun getMessages() : List<Message> = service.findMessages()

	@PostMapping
	fun postMessage(@RequestBody message: Message) {
		service.postMessage(message)
	}
}

@Table("MESSAGES")
data class Message(
	@Id val id: String?,
		val text:String)

interface MessageRepository : CrudRepository<Message, String> {

	@Query("Select * from messages")
	fun findMessages() : List<Message>
}

@Service
class MessageService(val db: MessageRepository) {
	fun findMessages() : List<Message> = db.findMessages()
	fun postMessage(message : Message) {
		db.save(message)
	}
}