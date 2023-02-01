package com.github.mmakart.cftShiftTestTask.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineArgumentParser {
	public static AppSettings parse(String[] args) throws IllegalArgumentException {
		if (args.length < 3) { // for example args == { "-s", "out.txt", "in.txt" }
			throw new IllegalArgumentException();
		}
		
		List<String> argsList = Arrays.asList(args);
		if (!argsList.contains("-s") && !argsList.contains("-i")) {
			throw new IllegalArgumentException();
		}
		
		SortOrder so = SortOrder.ASCENDING;
		DataType dt = null;
		String output;
		List<String> input = new ArrayList<>();
		
		int i;
		option_args_loop:
		for (i = 0; i < args.length; i++) {	
			switch (args[i]) {
			case "-a":
				so = SortOrder.ASCENDING;
				break;
			case "-d":
				so = SortOrder.DESCENDING;
				break;	
			case "-s":
				dt = DataType.STRING;
				break;
			case "-i":
				dt = DataType.INTEGER;
				break;
			default:
				break option_args_loop;
			}
		}
		
		output = args[i++];
		
		for (; i < args.length; i++) {
			input.add(args[i]);
		}
		
		return new AppSettings(so, dt, output, input.toArray(new String[0]));		
	}
}
