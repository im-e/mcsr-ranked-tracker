package com.imi.mcsrapp.controller;

import com.imi.mcsrapp.model.Match;
import com.imi.mcsrapp.model.UserStatistics;
import com.imi.mcsrapp.model.User;
import com.imi.mcsrapp.service.MCSRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/mcsr")
public class MCSRController {

    private final MCSRService mcsrService;

    public MCSRController(MCSRService mcsrService) {
        this.mcsrService = mcsrService;
    }

    @GetMapping("/users/{identifier}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable String identifier) {
        return mcsrService.getUserByIdentifier(identifier)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/sync/{identifier}/matches")
    public ResponseEntity<List<Match>> getUserMatchesSync(
            @PathVariable String identifier,
            @RequestParam(required = false, defaultValue = "20") Integer count) {
        try {
            List<Match> matches = mcsrService.getUserMatchesSync(identifier, count);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{identifier}/stats")
    public ResponseEntity<UserStatistics> getUserStats(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "100") Integer count,
            @RequestParam(required = false) Integer type) {
        try {
            UserStatistics stats = mcsrService.getUserMatchStatistics(identifier, count, type);
            if (stats == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
