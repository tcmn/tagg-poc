package br.com.b3.tagg_poc.service;

import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import br.com.b3.tagg_poc.domain.AggregationTimeWindow;
import br.com.b3.tagg_poc.domain.AggregationTimeWindow.AggregationTimeUnit;
import br.com.b3.tagg_poc.domain.ClockPulse;
import br.com.b3.tagg_poc.domain.CompressionType;
import br.com.b3.tagg_poc.domain.Message;
import br.com.b3.tagg_poc.domain.Trade;
import br.com.b3.tagg_poc.domain.TradeBlock;
import br.com.b3.tagg_poc.util.TaggConfig;

public class TradeCompressService {
	
	private Map<String, List<Trade>> ctrlQuantityList;
	private Map<Long, Set<String>> ctrlTimestampList;
	
	public List<TradeBlock> start(TaggConfig config, List<Message> messages) {
		ctrlQuantityList = new HashMap<>();
		ctrlTimestampList = new HashMap<>();
		List<TradeBlock> aggregatedTrades = new ArrayList<>();
		
		for (Message message : messages) {
			Long timestampKey = generateTimestampKey(message, config.getAggregationTimeWindow());
			if (message instanceof Trade) {
				Trade t = (Trade) message;
				String participantKey = generateParticipantKey(t);
				if (!ctrlQuantityList.containsKey(participantKey)) {
					ctrlQuantityList.put(participantKey, new ArrayList<>());
				}
				List<Trade> tradesToAggregation = ctrlQuantityList.get(participantKey);
				tradesToAggregation.add(t);
				if (!ctrlTimestampList.containsKey(timestampKey)) {
					ctrlTimestampList.put(timestampKey, new HashSet<>());
				}
				ctrlTimestampList.get(timestampKey).add(participantKey);
				if (tradesToAggregation.size() >= config.getAggregationQuantityWindow()) {
					aggregatedTrades.add(aggregateTrades(tradesToAggregation, CompressionType.QUANTITY));
					ctrlQuantityList.remove(participantKey);
				}
			} else if (message instanceof ClockPulse) {
				Long clockPulseTimestampKey = calculateClockPulseTimestampKey(timestampKey, config.getAggregationTimeWindow());
				if (ctrlTimestampList.containsKey(clockPulseTimestampKey)) {
					Set<String> participantsKeysToBeGrouped = ctrlTimestampList.get(clockPulseTimestampKey);
					for (String participantKey : participantsKeysToBeGrouped) {
						List<Trade> tradesToAggregation = ctrlQuantityList.get(participantKey);
						if (tradesToAggregation != null && !tradesToAggregation.isEmpty()) {
							aggregatedTrades.add(aggregateTrades(tradesToAggregation, CompressionType.TIME));
							ctrlQuantityList.remove(participantKey);
						}
					}
					ctrlTimestampList.remove(clockPulseTimestampKey);
				}
			}
		}
		return aggregatedTrades;
	}

	private Long calculateClockPulseTimestampKey(Long timestampKey, AggregationTimeWindow aggregationTimeWindow) {
		AggregationTimeUnit orderOfMagnitude = aggregationTimeWindow.getOrderOfMagnitude();
		Long clockPulseTimestampKey = null;
		if (orderOfMagnitude.equals(AggregationTimeUnit.MINUTES)) {
			clockPulseTimestampKey = timestampKey - aggregationTimeWindow.getDuration().toMinutes();
		} else if (orderOfMagnitude.equals(AggregationTimeUnit.SECONDS)) {
			clockPulseTimestampKey = timestampKey - aggregationTimeWindow.getDuration().getSeconds();
		} else {
			clockPulseTimestampKey = timestampKey - aggregationTimeWindow.getDuration().toMillis();
		}
		return clockPulseTimestampKey;
	}

	private TradeBlock aggregateTrades(List<Trade> tradesToAggregation, CompressionType compressionType) {
		// TODO discutir sobre o cenário: se existir apenas um trade na lista, criaremos um bloco com apenas 1 trade, ou retornaremos o próprio (by pass)?
		Integer quantityBlock = tradesToAggregation.stream().map(Trade::getQuantity).reduce(0, Integer::sum);
		TradeBlock tradeBlock = new TradeBlock();
		try {
			// TODO não incluir id e o timestamp do trade na copia
			BeanUtils.copyProperties(tradeBlock, tradesToAggregation.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		tradeBlock.setQuantity(quantityBlock);
		tradeBlock.setCompressionType(compressionType);
		tradeBlock.setGroupedTrades(new ArrayList<>(tradesToAggregation));
		tradesToAggregation.clear();
		return tradeBlock;
	}

	private String generateParticipantKey(Trade t) {
		return String.join("|", t.getParticipant(), t.getAccount(), t.getSymbol(), t.getTradeSide().toString(), t.getPrice().toString());
	}
	
	private Long generateTimestampKey(Message m, AggregationTimeWindow aggregationTimeWindow) {
		AggregationTimeUnit orderOfMagnitude = aggregationTimeWindow.getOrderOfMagnitude();
		if (orderOfMagnitude.equals(AggregationTimeUnit.MINUTES)) {
			return Long.valueOf(String.join("", String.valueOf(m.getTime().getHour()), String.valueOf(m.getTime().getMinute())));
		} else if (orderOfMagnitude.equals(AggregationTimeUnit.SECONDS)) {
			return Long.valueOf(String.join("", String.valueOf(m.getTime().getHour()), String.valueOf(m.getTime().getMinute()), String.format("%02d", m.getTime().getSecond())));
		} else {
			return Long.valueOf(String.join("", String.valueOf(m.getTime().getHour()), String.valueOf(m.getTime().getMinute()), String.format("%02d", m.getTime().getSecond()), String.format("%03d", m.getTime().getLong(ChronoField.MILLI_OF_SECOND))));
		}
		
	}

}
