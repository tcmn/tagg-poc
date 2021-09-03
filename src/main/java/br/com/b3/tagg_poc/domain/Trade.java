package br.com.b3.tagg_poc.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trade extends Message {

	private static final long serialVersionUID = 8946364703192013755L;
	
	private String participant;
	private String account;
	private String symbol;
	private Integer quantity;
	private BigDecimal price;
	private TradeSide tradeSide;
	
	Trade() {
		
	}

	Trade(Long id, LocalDateTime tradeTime, String participant, String account, String symbol, Integer quantity, BigDecimal price, TradeSide tradeSide) {
		super(id, tradeTime);
		
		if(participant == null){
			throw new NullPointerException("The property \"participant\" is null. "
					+ "Please set the value by \"participant()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		if(account == null){
			throw new NullPointerException("The property \"account\" is null. "
					+ "Please set the value by \"account()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		if(symbol == null){
			throw new NullPointerException("The property \"symbol\" is null. "
					+ "Please set the value by \"symbol()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		if(quantity == null){
			throw new NullPointerException("The property \"quantity\" is null. "
					+ "Please set the value by \"quantity()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		if(price == null){
			throw new NullPointerException("The property \"price\" is null. "
					+ "Please set the value by \"price()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		if(tradeSide == null){
			throw new NullPointerException("The property \"tradeSide\" is null. "
					+ "Please set the value by \"tradeSide()\". "
					+ "The properties \"id\", \"tradeTime\", \"participant\", \"account\", \"symbol\", \"quantity\", \"price\" and \"tradeSide\" are required.");
		}
		
		this.participant = participant; 
		this.account = account; 
		this.symbol = symbol; 
		this.quantity = quantity; 
		this.price = price; 
		this.tradeSide = tradeSide; 
	}

	public static TradeBuilder builder(){
		return new TradeBuilder();
	}  
	public static class TradeBuilder {

		private Long id;
		private LocalDateTime tradeTime;
		private String participant;
		private String account;
		private String symbol;
		private Integer quantity;
		private BigDecimal price;
		private TradeSide tradeSide;

		TradeBuilder() {    
		}

		public TradeBuilder id(Long id){
			this.id = id;
			return TradeBuilder.this;
		}

		public TradeBuilder tradeTime(LocalDateTime tradeTime){
			this.tradeTime = tradeTime;
			return TradeBuilder.this;
		}

		public TradeBuilder participant(String participant){
			this.participant = participant;
			return TradeBuilder.this;
		}

		public TradeBuilder account(String account){
			this.account = account;
			return TradeBuilder.this;
		}

		public TradeBuilder symbol(String symbol){
			this.symbol = symbol;
			return TradeBuilder.this;
		}

		public TradeBuilder quantity(Integer quantity){ 
			this.quantity = quantity;
			return TradeBuilder.this;
		}

		public TradeBuilder price(BigDecimal price){
			this.price = price;
			return TradeBuilder.this;
		}

		public TradeBuilder tradeSide(TradeSide tradeSide){
			this.tradeSide = tradeSide;
			return TradeBuilder.this;
		}

		public Trade build() {
			return new Trade(this.id, this.tradeTime, this.participant, this.account, this.symbol, this.quantity, this.price, this.tradeSide);
		}
		@Override
		public String toString() {
			return "Trade.TradeBuilder(id=" + this.id + ", tradeTime=" + this.tradeTime + ", participant=" + this.participant + ", account=" + this.account + ", symbol=" + this.symbol + ", quantity=" + this.quantity + ", price=" + this.price + ", tradeSide=" + this.tradeSide + ")";
		}
	}

	@Override
	public String toString() {
		return "Trade(id=" + super.getId() + ", tradeTime=" + super.getTime() + ", participant=" + this.participant + ", account=" + this.account + ", symbol=" + this.symbol + ", quantity=" + this.quantity + ", price=" + this.price + ", tradeSide=" + this.tradeSide + ")";
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public TradeSide getTradeSide() {
		return tradeSide;
	}

	public void setTradeSide(TradeSide tradeSide) {
		this.tradeSide = tradeSide;
	}
	
}
