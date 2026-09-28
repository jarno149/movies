package com.movies.movieservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
@Tag(name = "Ping", description = "Service ping endpoint")
public class PingController {

    @GetMapping
    @Operation(summary = "Pings server")
    @ApiResponse(responseCode = "200", description = "Service is available")
    public String ping() {
        return "pong";
    }

}
