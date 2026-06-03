package com.sarthak.listeners;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class StepLogger implements ConcurrentEventListener{

	  @Override
	    public void setEventPublisher(EventPublisher publisher) {

	        publisher.registerHandlerFor(TestStepStarted.class, event -> {

	            if (event.getTestStep() instanceof PickleStepTestStep) {

	                PickleStepTestStep step =
	                        (PickleStepTestStep) event.getTestStep();

	                System.out.println("[START] " + step.getStep().getText());
	            }
	        });

	        publisher.registerHandlerFor(TestStepFinished.class, event -> {

	            if (event.getTestStep() instanceof PickleStepTestStep) {

	                Status status = event.getResult().getStatus();

	                PickleStepTestStep step =
	                        (PickleStepTestStep) event.getTestStep();

	                System.out.println("[END] " 
	                        + step.getStep().getText() 
	                        + " -> " 
	                        + status);
	            }
	        });
	    }
}
