package com.sarthak.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(

		features = "src\\test\\resources\\features", plugin = {"json:target/jsonReports/cucumber.json",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"html:target/cucumber-report.html", "com.sarthak.listeners.StepLogger" },

		glue = { "com.sarthak.stepdefinitions", "com.sarthak.hooks" }, tags = "@Regression",

		// ── Report ─────────────────────────────────────────────────

		// ── Show snippets for unimplemented steps ──────────────────
		snippets = CucumberOptions.SnippetType.CAMELCASE,

		// ── Dry run ────────────────────────────────────────────────
		dryRun = false,

		// ── Show monochrome console output ─────────────────────────
		monochrome = true

)
public class TestRunner {

}
