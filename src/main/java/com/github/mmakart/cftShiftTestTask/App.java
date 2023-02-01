package com.github.mmakart.cftShiftTestTask;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.github.mmakart.cftShiftTestTask.state.AppSettings;
import com.github.mmakart.cftShiftTestTask.state.CommandLineArgumentParser;

public class App {
	
	private static final String usage =
			"Usage: java App [-a|-d] [-s|-i] <output file> <input files>...\n" +
			"    -a    sort in ascending order (not required, default value)\n" +
			"    -d    sort in descending order (not required)\n" +
			"    -s    treat and sort input files content as strings\n" +
			"    -i    treat and sort input files as containing integers only";
	
	private static AppSettings settings;

	public static void main(String[] args) {
		
		settings = readSettings(args);
		
		List<BufferedReader> readers = null;
		PrintWriter writer = null;
		try {
			readers = openInputFiles();
			writer = openOutputFile();

			processStringFiles(readers, writer);
		} finally {
			for (Reader reader : readers) {
				try {
					reader.close();
				} catch (IOException ignored) {}
			}
			writer.close();
		}
	}

	private static AppSettings readSettings(String[] args) {
		AppSettings settings = null;
		try {
			settings = CommandLineArgumentParser.parse(args);
		} catch (IllegalArgumentException ignored) {
			System.err.println(usage);
			System.exit(1);
		}
		return settings;
	}

	private static int pickUpComparingOrder() {
		switch (settings.getSortOrder()) {
		case ASCENDING:
			return +1;
		case DESCENDING:
			return -1;
		default:
			throw new AssertionError();
		}
	}

	private static List<BufferedReader> openInputFiles() {
		List<BufferedReader> inputReaders = new ArrayList<>();
		for (String filename : settings.getInputFilenames()) {
			try {
				inputReaders.add(
						new BufferedReader(new FileReader(filename)));
			} catch (FileNotFoundException e) {
				System.err.println("Warning: cannot open " + filename);
			}
		}
		
		if (inputReaders.isEmpty()) {
			System.err.println("Error: none of given input files can be open or exist");
			System.exit(2);
		}
		
		return inputReaders;
	}

	private static PrintWriter openOutputFile() {
		PrintWriter outputWriter = null;
		try {
			outputWriter = new PrintWriter(
					new FileWriter(settings.getOutputFilename()));
		} catch (IOException e) {
			System.err.println("Error: cannot open or create output file " +
					settings.getOutputFilename());
			System.exit(2);
		}
		return outputWriter;
	}

	private static void processStringFiles(List<BufferedReader> readers,
			PrintWriter writer) {
		
		Comparator<String> comparator = null;
		Comparator<String> checkOrderComparator = null;
		Predicate<String> isStringValid = null;
		
		switch (settings.getSortOrder()) {
		case ASCENDING:
			comparator = nullsLast(naturalOrder());
			checkOrderComparator = nullsFirst(naturalOrder());
			break;
		case DESCENDING:
			comparator = nullsLast(reverseOrder());
			checkOrderComparator = nullsFirst(reverseOrder());
			break;
		default:
			throw new AssertionError();
		}
		
		switch (settings.getDataType()) {
		case STRING:
			isStringValid = str -> !str.matches(".*\\s.*");
			break;
		case INTEGER:
			isStringValid = str -> str.matches("[-+]?\\d+");
			break;
		default:
			throw new AssertionError();
		}
		
		// TODO add Integer support
		String[] lastLines = new String[readers.size()];
		String previousPushed = null;
		boolean isMoreLines;
		
		do {
			int index = 0;
			
			for (int i = 0; i < readers.size(); i++) {
				BufferedReader reader = readers.get(i);
				try {
					if (lastLines[i] == null && reader.ready()) {
						String line = reader.readLine();
						
						// ignore invalid lines
						while (!isStringValid.test(line)) {
							line = reader.readLine();
							// end of file
							if (line == null) {
								break;
							}
						}
						lastLines[i] = line;
					}
					if (Objects.compare(lastLines[i], lastLines[index], comparator) <= 0) {
						index = i;
					}
				} catch (IOException e) {
					System.err.println("Warning: IOException while attempting reading an open file");
				}
			}
			
			// ignore wrong order lines
			if (Objects.compare(lastLines[index], previousPushed, checkOrderComparator) >= 0) {
				writer.println(lastLines[index]);
				previousPushed = lastLines[index];
			}
			lastLines[index] = null;
			
			isMoreLines = Arrays.stream(lastLines).anyMatch(Objects::nonNull);
			
		} while (isMoreLines);
	}
}
