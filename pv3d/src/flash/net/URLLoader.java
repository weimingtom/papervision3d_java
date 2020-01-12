package flash.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import flash.events.Event;
import flash.events.EventDispatcher;

public class URLLoader extends EventDispatcher {
	public Object data;
	
	public void load(URLRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(request._url);
			InputStream input = new FileInputStream(file);
			Reader reader = new InputStreamReader(input, "UTF-8");
			BufferedReader buffer = new BufferedReader(reader);
			
			String line;
			while (null != (line = buffer.readLine())) {
				sb.append(line);
				sb.append("\n");
			}
			buffer.close();
			reader.close();
			input.close();
		} catch (IOException e) {
			throw new RuntimeException("URLLoader");
		}
		
		this.data = sb.toString();
		Event event = new Event(Event.COMPLETE, false, false);
		event._target = this;
		this.dispatchEvent(event);
	}
}
