module proyecto3
{
	exports test.data_structures;
	exports controller;
	exports view;
	exports model.logic;
	exports main;
	exports model.data_structures;

	requires java.desktop;
	requires junit;
	
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;

	opens model.logic;
}