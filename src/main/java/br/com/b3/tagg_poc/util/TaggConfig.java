package br.com.b3.tagg_poc.util;

import br.com.b3.tagg_poc.domain.AggregationTimeWindow;

public class TaggConfig {
	
	private Integer aggregationQuantityWindow;
	private AggregationTimeWindow aggregationTimeWindow;
	
	/**
	 * @param aggregationQuantityWindow
	 * @param aggregationTimeWindow A janela de tempo de agregação. Ela nunca poderá ser menor que a frequencia do clock pulse.
	 * Se for, a janela se fechará antes de colocar qualquer trade na mesma. Sendo assim, não haverá agrupamento.
	 * Ex: ClockPulse a cada 1 minuto e AggregationTimeWindow a cada 1 segundo. A janela se fechará a cada segundo e o serviço de compressão só poderá colocar um trade a cada 1 minuto.
	 */
	public TaggConfig(Integer aggregationQuantityWindow, AggregationTimeWindow aggregationTimeWindow) {
		this.aggregationQuantityWindow = aggregationQuantityWindow;
		this.aggregationTimeWindow = aggregationTimeWindow;
	}

	public Integer getAggregationQuantityWindow() {
		return aggregationQuantityWindow;
	}
	
	public void setAggregationQuantityWindow(Integer aggregationQuantityWindow) {
		this.aggregationQuantityWindow = aggregationQuantityWindow;
	}
	
	public AggregationTimeWindow getAggregationTimeWindow() {
		return aggregationTimeWindow;
	}
	
	public void setAggregationTimeWindow(AggregationTimeWindow aggregationTimeWindow) {
		this.aggregationTimeWindow = aggregationTimeWindow;
	}
	
}
