package com.monitor.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sensor")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SensorController {
}
