package com.alphadevs.web.pos.cucumber.stepdefs;

import com.alphadevs.web.pos.JPosApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = JPosApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
