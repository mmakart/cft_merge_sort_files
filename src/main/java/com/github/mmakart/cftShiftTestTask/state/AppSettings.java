package com.github.mmakart.cftShiftTestTask.state;

public class AppSettings {
	private final SortOrder sortOrder;
	private final DataType dataType;
	private final String outputFilename;
	private final String[] inputFilenames;
	
	public AppSettings(SortOrder sortOrder, DataType dataType, String outputFilename, String[] inputFilenames) {
		this.sortOrder = sortOrder;
		this.dataType = dataType;
		this.outputFilename = outputFilename;
		this.inputFilenames = inputFilenames;
	}
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public String[] getInputFilenames() {
		return inputFilenames;
	}
	
}
