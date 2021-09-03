package br.com.b3.tagg_poc.input;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.b3.tagg_poc.domain.ClockPulse;
import br.com.b3.tagg_poc.domain.Message;
import br.com.b3.tagg_poc.domain.Trade;
import br.com.b3.tagg_poc.domain.TradeSide;

public class CSVTradeParser {

	private String fileName;

	public CSVTradeParser(String fileName) {
		this.fileName = fileName;
	}

	public List<Message> parse() {
		List<Message> messages = null;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			messages = stream.skip(1).map(this::parseLineTrade).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println(e);
		}
		return messages;
	}

	private Message parseLineTrade(String line) {
		String[] array = line.split(",");
		if (array == null || array.length == 0) {
			return null;
		}
		if (array[0].startsWith("CP")) {
			return new ClockPulse(Long.valueOf(array[0].substring(2)), LocalDateTime.parse(array[1], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
		} else {
			if (array.length < 8) {
				return null;
			}
			return Trade.builder()
					.id(Long.valueOf(array[0]))
					.tradeTime(LocalDateTime.parse(array[1], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
					.participant(array[2])
					.account(array[3])
					.symbol(array[4])
					.price(new BigDecimal(array[5]))
					.tradeSide(TradeSide.valueOf(array[6]))
					.quantity(Integer.parseInt(array[7]))
					.build();
		}
	}
		

}
