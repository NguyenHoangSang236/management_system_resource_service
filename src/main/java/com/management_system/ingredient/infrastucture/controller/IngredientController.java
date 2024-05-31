package com.management_system.ingredient.infrastucture.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
}
