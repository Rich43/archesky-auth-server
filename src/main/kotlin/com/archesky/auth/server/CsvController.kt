package com.archesky.auth.server

import com.archesky.auth.server.types.TokenTwo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Collections.singletonList

@RestController
class CsvController {
    @GetMapping(value = ["/test"], produces = ["text/csv"])
    fun test(): ResponseEntity<List<TokenTwo>> {
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .body(
                        singletonList(
                                TokenTwo("d", "3", "2", "2", "2")
                        )
                )
    }
}
