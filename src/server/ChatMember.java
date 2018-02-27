package server;

import java.io.OutputStream;

public class ChatMember {
		private String name;
		private OutputStream output;
		
		
		public ChatMember(String name, OutputStream o) {
			this.name = name;
			this.output = o;
		}
		public String getName() {
			return name;
		}
		
		public OutputStream getOutput() {
			return output;
		}
		
		@Override
		public boolean equals(Object chatM) {
			return ((ChatMember)chatM).getName().equals(name);
		}
		
		
		
}
