package br.com.b3.tagg_poc.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TradeBlock extends Trade {

	private static final long serialVersionUID = 2165340181324269530L;
	
	public TradeBlock() {
	}
	
	public TradeBlock(Long id, LocalDateTime tradeTime, String participant, String account, String symbol, Integer quantity,
			BigDecimal price, TradeSide tradeSide) {
		super(id, tradeTime, participant, account, symbol, quantity, price, tradeSide);
	}

	private CompressionType compressionType;
	private List<Trade> groupedTrades;
	
	public CompressionType getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public List<Trade> getGroupedTrades() {
		return groupedTrades;
	}
	
	public void setGroupedTrades(List<Trade> groupedTrades) {
		this.groupedTrades = groupedTrades;
	}
	
	@Override
	  public String toString() {
	    return "TradeBlock(compressionType=" + this.compressionType + ", groupedTrades=" + this.groupedTrades + ")";
	  }

}
