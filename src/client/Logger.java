package client;

import java.io.PrintStream;

import javafx.scene.control.TextArea;

public class Logger {
	private static PrintStream output = System.out;
	private static TextArea inputMessages;

	public static void setOutput(PrintStream newOutput, TextArea input) {
		output = newOutput;
		inputMessages = input;
	}

	public static void log(String logEntry) {
		if(logEntry.equals("delete oldest")) {
			String tmp = inputMessages.getText(0, inputMessages.getLength());

			
			inputMessages.clear();
			String toShow = "";
			String[] split = tmp.split("\n");
			for(int i = 1; i< split.length; i++) {
				toShow += split[i];
				if(i+1 < split.length) {
					toShow += "\n";
				}
			}
			if(!toShow.equals("")) {
				output.println(toShow);
				
			}
			
		}else {
			String toShow = logEntry;
			if(logEntry.contains("*")) {
				String[] split = logEntry.split("[*]");
				toShow = split[0] + ": " + split[1] + " (" + split[2] + ")";
			}
			output.println(toShow);
		}
	}

}
