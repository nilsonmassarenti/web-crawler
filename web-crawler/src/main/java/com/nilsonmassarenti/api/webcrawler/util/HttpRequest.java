package com.nilsonmassarenti.api.webcrawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class HttpRequest {
	
	public String getHtml(String link) {
		URL url;
		try {
			url = new URL(link);
			InputStream is = url.openStream();
			int ptr = 0;
			StringBuffer buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
			return buffer.toString();
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
	}
}
