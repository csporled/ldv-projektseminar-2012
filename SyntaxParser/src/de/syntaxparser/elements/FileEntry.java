package de.syntaxparser.elements;

import java.util.List;

public class FileEntry {
	// global variables
	private String topic = null;
	private String date = null;
	private String author = null;
	private String header = null;
	private String bold = null;
	private List<String> text = null;
	
	public FileEntry (String topic, String date, String author, String header, String bold, List<String> text) {
		this.setTopic(topic);
		this.setDate(date);
		this.setAuthor(author);
		this.setHeader(header);
		this.setBold(bold);
		this.setText(text);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBold() {
		return bold;
	}

	public void setBold(String bold) {
		this.bold = bold;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

}
