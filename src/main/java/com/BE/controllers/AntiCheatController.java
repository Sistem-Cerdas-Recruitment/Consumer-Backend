package com.BE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BE.dto.antiCheat.AntiCheatEvaluationDTO;
import com.BE.dto.antiCheat.AntiCheatResultDTO;
import com.BE.services.antiCheat.AntiCheatService;

@RestController
@RequestMapping("/api/anti-cheat")
@CrossOrigin(origins = "*")
public class AntiCheatController {

    @Autowired
    private AntiCheatService antiCheatService;

    @PostMapping("/test")
    public String antiCheat(@RequestBody AntiCheatEvaluationDTO request) {
        try {
            antiCheatService.checkForCheating(request);
            return "success";
        } catch (Exception e) {
            return "Failed to process evaluation: " + e;
        }
    }

    @PatchMapping("/result")
    public ResponseEntity<String> updateResult(@RequestBody @Validated AntiCheatResultDTO request) {

        antiCheatService.updateResult(request);
        return ResponseEntity.ok("success");
    }
}
