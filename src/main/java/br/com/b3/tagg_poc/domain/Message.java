package br.com.b3.tagg_poc.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = 173947616927745044L;

	private Long id;
	private LocalDateTime time;

	Message() {
	}

	public Message(Long id, LocalDateTime time) {
		if(id == null){
			throw new NullPointerException("The property \"id\" is null. "
					+ "Please set the value by \"id()\". "
					+ "The properties \"id\", \"time\" are required.");
		}
		if(time == null){
			throw new NullPointerException("The property \"time\" is null. "
					+ "Please set the value by \"time()\". "
					+ "The properties \"id\", \"time\" are required.");
		}
		this.id = id;
		this.time = time;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Message(id=" + this.id + ", time=" + this.time + ")";
	}
}
