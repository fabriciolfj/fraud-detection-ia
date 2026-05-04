package com.github.fabriciolfj.fraud.controller;

import com.github.fabriciolfj.fraud.Evaluation;
import com.github.fabriciolfj.fraud.TriageService;
import com.github.fabriciolfj.fraud.dto.ChatDTO;
import com.github.fabriciolfj.fraud.dto.ResponseChatDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/triage")
public class TriageController {

    private final TriageService triageService;

    public TriageController(final TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping
    public ResponseChatDTO chat(@RequestBody final ChatDTO dto) {
        var result = triageService.triage(dto.message());

        return new ResponseChatDTO(result);
    }

}
