package br.com.b3.tagg_poc.domain;

import java.time.LocalDateTime;

public class ClockPulse extends Message {

	private static final long serialVersionUID = -4963827873763858809L;

	public ClockPulse(Long id, LocalDateTime time) {
		super(id, time);
	}

	@Override
	public String toString() {
		return "ClockPulse(id=" + this.getId() + ", time=" + this.getTime() + ")";
	}

}
