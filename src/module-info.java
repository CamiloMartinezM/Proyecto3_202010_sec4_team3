module T7_202010
{
	requires java.desktop;
	exports test.data_structures;
	exports controller;
	exports view;
	exports model.logic;
	exports main;
	exports model.data_structures;

	requires com.google.gson;
	requires junit;
}