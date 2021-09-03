package br.com.b3.tagg_poc.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class AggregationTimeWindow implements Serializable {
	
	/**
	 * Minutes per hour.
	 */
	static final int MINUTES_PER_HOUR = 60;
	/**
     * Seconds per minute.
     */
    static final int SECONDS_PER_MINUTE = 60;
    /**
     * Seconds per hour.
     */
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    
	private static final long serialVersionUID = 4041537500323012200L;

	public enum AggregationTimeUnit {
		MINUTES, SECONDS, MILLIS;
	}
	
	private Duration duration;
	
	public static AggregationTimeWindow ofMinutes(int value) {
		return new AggregationTimeWindow(value, AggregationTimeUnit.MINUTES);
	}
	
	public static AggregationTimeWindow ofSeconds(int value) {
		return new AggregationTimeWindow(value, AggregationTimeUnit.SECONDS);
	}
	
	public static AggregationTimeWindow ofMillis(int value) {
		return new AggregationTimeWindow(value, AggregationTimeUnit.MILLIS);
	}
	
	private AggregationTimeWindow(int value, AggregationTimeUnit unit) {
		Objects.requireNonNull(value);
		if (unit.equals(AggregationTimeUnit.MINUTES)) {
			this.duration = Duration.of(value, ChronoUnit.MINUTES);
		} else if (unit.equals(AggregationTimeUnit.SECONDS)) {
			this.duration = Duration.of(value, ChronoUnit.SECONDS);
		} else {
			this.duration = Duration.of(value, ChronoUnit.MILLIS);
		}
	}
	
	public AggregationTimeUnit getOrderOfMagnitude() {
		long seconds = duration.getSeconds();
		int minutes = (int) ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
		long hours = seconds / SECONDS_PER_HOUR;
		int nanos = duration.getNano();
		
		if (hours > 0 || minutes > 0) {
			return AggregationTimeUnit.MINUTES;
		} else if (seconds > 0) {
			return AggregationTimeUnit.SECONDS;
		} else if (nanos > 0) {
			return AggregationTimeUnit.MILLIS;
		} else {
			return AggregationTimeUnit.SECONDS;
		}
	}

	public Duration getDuration() {
		return duration;
	}

	public static void main(String[] args) {
		AggregationTimeWindow at = new AggregationTimeWindow(1, AggregationTimeUnit.MILLIS);
		at.getOrderOfMagnitude();
	}
	
}
