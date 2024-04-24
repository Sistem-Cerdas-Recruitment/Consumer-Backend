package com.BE.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @GetMapping("/all/{page}/{size}")
    public String getAllJobs(@PathVariable int page, @PathVariable int size) {
        return "All jobs";
    }

    
}
