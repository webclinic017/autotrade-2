package entity;

import systemenum.SystemEnum;

public class Volume {

	private String scenario;
	
	private Enum<SystemEnum.Trend> trend;

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public Enum<SystemEnum.Trend> getTrend() {
		return trend;
	}

	public void setTrend(Enum<SystemEnum.Trend> trend) {
		this.trend = trend;
	}
}