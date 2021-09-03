package br.com.b3.tagg_poc.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.b3.tagg_poc.domain.AggregationTimeWindow.AggregationTimeUnit;
import br.com.b3.tagg_poc.domain.ClockPulse;
import br.com.b3.tagg_poc.domain.Message;
import br.com.b3.tagg_poc.domain.Trade;
import br.com.b3.tagg_poc.domain.TradeSide;
import br.com.b3.tagg_poc.util.TaggUtil;

public class MessageStream {

	public List<Message> generateTradeMessagesWithClockPulse(){
		List<Message> tradesMessages = generateTrades();
		tradesMessages = generateMessagesWithClockPulse(tradesMessages);
		return tradesMessages;
	}
	
	private List<Message> generateTrades() {
		List<String> participants = Arrays.asList("114");
		List<String> accounts = Arrays.asList("1238206", "1238207", "1238208", "1238209", "1238210");
		List<String> symbols = Arrays.asList("PETR4");
		List<BigDecimal> prices = Arrays.asList(new BigDecimal("27.86"), new BigDecimal("28.00"), new BigDecimal("28.30"), new BigDecimal("28.35"));

		Integer tradeQuantity = 100;
		List<Message> messages = new ArrayList<>();
		
		for (int i = 1; i <= tradeQuantity; i++) {
			LocalDateTime tradeTime = TaggUtil.generateRandomLocalDateTime();
			List<Integer> accountIndexs = TaggUtil.generateRandomIntegers(0, accounts.size(), 2, true);
			// BigDecimal price = TaggUtil.generateRandomBigDecimalFromRange(new BigDecimal("27.86"), new BigDecimal("28.00"));
			Integer priceIndex = TaggUtil.generateRandomInteger(0, prices.size());
			Integer quantity = TaggUtil.generateRandomInteger(100, 300);
			messages.addAll(generateTrade(i, tradeTime, participants.get(0), participants.get(0), accounts.get(accountIndexs.get(0)), accounts.get(accountIndexs.get(1)), symbols.get(0), prices.get(priceIndex), quantity));
		}
		
		return messages;
	}

	private List<Trade> generateTrade(long id, LocalDateTime time, String buyerParticipant, String sellerParticipant, String buyerAccount, 
			String sellerAccount, String symbol, BigDecimal price, Integer quantity) {
		
		Trade buyTrade = Trade.builder()
				.id(id)
				.tradeTime(time)
				.participant(buyerParticipant)
				.account(buyerAccount)
				.symbol(symbol)
				.price(price)
				.tradeSide(TradeSide.BUY)
				.quantity(quantity)
				.build();

		Trade sellTrade = Trade.builder()
				.id(id)
				.tradeTime(time)
				.participant(sellerParticipant)
				.account(sellerAccount)
				.symbol(symbol)
				.price(price)
				.tradeSide(TradeSide.SELL)
				.quantity(quantity)
				.build();
		
		return Arrays.asList(buyTrade, sellTrade);
	}

	private List<Message> generateClockPulse(LocalDateTime minTime, LocalDateTime maxTime, AggregationTimeUnit aggregationTimeUnit) {
		if (minTime == null || maxTime == null) {
			return null;
		}
		long clockPulseQuantity = 0;
		if (AggregationTimeUnit.MINUTES.equals(aggregationTimeUnit)) {
			clockPulseQuantity = ChronoUnit.MINUTES.between(minTime, maxTime);
		} else if (AggregationTimeUnit.SECONDS.equals(aggregationTimeUnit)) {
			clockPulseQuantity = ChronoUnit.SECONDS.between(minTime, maxTime);
		} else {
			clockPulseQuantity = ChronoUnit.MILLIS.between(minTime, maxTime);
		}
		List<Message> clockPulses = new ArrayList<>();
		LocalDateTime current = LocalDateTime.from(minTime);
		for (int i = 0; i < clockPulseQuantity; i++) {
			if (AggregationTimeUnit.MINUTES.equals(aggregationTimeUnit)) {
				current = current.plus(1, ChronoUnit.MINUTES);
			} else if (AggregationTimeUnit.SECONDS.equals(aggregationTimeUnit)) {
				current = current.plus(1, ChronoUnit.SECONDS);
			} else {
				current = current.plus(1, ChronoUnit.MILLIS);
			}
			clockPulses.add(new ClockPulse(Long.valueOf(i), current));
		}
		return clockPulses;
	}
	
	private List<Message> generateMessagesWithClockPulse(List<Message> tradesMessages){
		LocalDateTime minTime = tradesMessages.stream().map(Message::getTime).min(LocalDateTime::compareTo).orElse(null);
		LocalDateTime maxTime = tradesMessages.stream().map(Message::getTime).max(LocalDateTime::compareTo).orElse(null);
		maxTime = maxTime.plusMinutes(2);
		List<Message> clockPulses = generateClockPulse(minTime, maxTime, AggregationTimeUnit.SECONDS);
		tradesMessages.addAll(clockPulses);
		tradesMessages = tradesMessages.stream().sorted((m1, m2) -> m1.getTime().compareTo(m2.getTime())).collect(Collectors.toList());
		return tradesMessages;
	}

	public List<Message> readMessageStream() {
		CSVTradeParser parser = new CSVTradeParser("src/main/resources/trades.csv");
		List<Message> messages = parser.parse();
		messages = generateMessagesWithClockPulse(messages);
		return messages;
	}

	public List<Message> createMessageStream() {

		Trade t1 = Trade.builder()
				.id(40L)
				.tradeTime(LocalDateTime.parse("31/08/2021 10:49:34", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				.participant("114")
				.account("1238208")
				.symbol("PETR4")
				.tradeSide(TradeSide.SELL)
				.quantity(100)
				.price(new BigDecimal("28.30"))
				.build();

		ClockPulse c1 = new ClockPulse(1L, LocalDateTime.parse("31/08/2021 10:50:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		Trade t2 = Trade.builder()
				.id(50L)
				.tradeTime(LocalDateTime.parse("31/08/2021 10:50:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				.participant("114")
				.account("1238208")
				.symbol("PETR4")
				.tradeSide(TradeSide.SELL)
				.quantity(200)
				.price(new BigDecimal("28.30"))
				.build();

		Trade t3 = Trade.builder()
				.id(60L)
				.tradeTime(LocalDateTime.parse("31/08/2021 10:50:45", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				.participant("114")
				.account("1238208")
				.symbol("PETR4")
				.tradeSide(TradeSide.SELL)
				.quantity(200)
				.price(new BigDecimal("28.30"))
				.build();

		Trade t4 = Trade.builder()
				.id(120L)
				.tradeTime(LocalDateTime.parse("31/08/2021 10:50:49", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				.participant("114")
				.account("1238208")
				.symbol("PETR4")
				.tradeSide(TradeSide.SELL)
				.quantity(100)
				.price(new BigDecimal("28.30"))
				.build();

		Trade t5 = Trade.builder()
				.id(130L)
				.tradeTime(LocalDateTime.parse("31/08/2021 10:50:57", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				.participant("114")
				.account("1238208")
				.symbol("PETR4")
				.tradeSide(TradeSide.BUY)
				.quantity(100)
				.price(new BigDecimal("27.86"))
				.build();

		ClockPulse c2 = new ClockPulse(1L, LocalDateTime.parse("31/08/2021 10:51:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		ClockPulse c3 = new ClockPulse(1L, LocalDateTime.parse("31/08/2021 10:52:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		ClockPulse c4 = new ClockPulse(1L, LocalDateTime.parse("31/08/2021 10:53:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		return Arrays.asList(t1, c1, t2, t3, t4, t5, c2, c3, c4);
	}

}
