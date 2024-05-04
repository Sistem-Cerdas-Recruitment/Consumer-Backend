package com.BE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.BE.dto.antiCheat.AntiCheatEvaluationDTO;
import com.BE.dto.antiCheat.AntiCheatResultDTO;
import com.BE.services.AntiCheatService;

@Controller
@RequestMapping("/api/anti-cheat")

public class AntiCheatController {

    @Autowired
    private AntiCheatService antiCheatService;

    @PostMapping("/test")
    public String antiCheat(@RequestBody AntiCheatEvaluationDTO body) {
        try {
            antiCheatService.checkForCheating(body);
            return "success";
        } catch (Exception e) {
            return "Failed to process evaluation: " + e;
        }
    }

    @PatchMapping("/result")
    public ResponseEntity<?> updateResult(@RequestBody @Validated AntiCheatResultDTO body) {

        antiCheatService.updateResult(body);
        return ResponseEntity.ok("success");
    }
}