package com.sarthak.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(

		features = "src\\test\\resources\\features", plugin = { "json:target/jsonReports/cucumber-report.json",
				"pretty" },

		glue = { "com.sarthak.stepdefinitions", "com.sarthak.hooks" }, tags = "@PlaceAPI",

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
