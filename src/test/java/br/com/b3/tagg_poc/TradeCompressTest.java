package br.com.b3.tagg_poc;

import java.util.List;

import org.junit.Test;

import br.com.b3.tagg_poc.domain.AggregationTimeWindow;
import br.com.b3.tagg_poc.domain.CompressionType;
import br.com.b3.tagg_poc.domain.Message;
import br.com.b3.tagg_poc.domain.Trade;
import br.com.b3.tagg_poc.domain.TradeBlock;
import br.com.b3.tagg_poc.input.MessageStream;
import br.com.b3.tagg_poc.service.TradeCompressService;
import br.com.b3.tagg_poc.util.TaggConfig;

public class TradeCompressTest {
	
	@Test
	public void compressTest() {
		TradeCompressService tcs = new TradeCompressService();
		TaggConfig config = new TaggConfig(5, AggregationTimeWindow.ofMinutes(2));
		List<Message> messages = new MessageStream().generateTradeMessagesWithClockPulse();
		List<TradeBlock> aggregatedTrades = tcs.start(config, messages);
		
		// Result
		System.out.println("Aggregated Trades: ");
		for (TradeBlock tradeBlock : aggregatedTrades) {
			System.out.println(tradeBlock);
		}
		
		// Metrics
		int aggregatedTradesQuantity = aggregatedTrades.size();
		long tradesQuantity = messages.stream().filter(Trade.class::isInstance).count();
		long tradesQuantityAfterAggregation = aggregatedTrades.stream().map(TradeBlock::getGroupedTrades).flatMap(List::stream).count();
		double compressionRate = (1 - ((double) aggregatedTradesQuantity / tradesQuantity)) * 100;
		long aggregatedTradesByTime = aggregatedTrades.stream().filter(tb -> CompressionType.TIME.equals(tb.getCompressionType())).count();
		long aggregatedTradesByQuantity = aggregatedTradesQuantity - aggregatedTradesByTime;
		
		System.out.println();
		System.out.println("Trades quantity: " + tradesQuantity);
		System.out.println("Trades quantity after aggregation: " + tradesQuantityAfterAggregation);
		System.out.println("Aggregated trades quantity: " + aggregatedTradesQuantity);
		System.out.printf("Compression rate: %.2f%%\n", compressionRate);
		System.out.printf("Aggregated trades by time: %d (%.2f%%)\n", aggregatedTradesByTime,  ((double) aggregatedTradesByTime * 100) / aggregatedTradesQuantity);
		System.out.printf("Aggregated trades by quantity: %d (%.2f%%)\n", aggregatedTradesByQuantity, ((double) aggregatedTradesByQuantity * 100) / aggregatedTradesQuantity);
	}

}
