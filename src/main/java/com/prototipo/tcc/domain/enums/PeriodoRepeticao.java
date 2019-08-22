package com.prototipo.tcc.domain.enums;

public enum PeriodoRepeticao {
	
	UM_AO_DIA(1, "Uma vez ao dia"),
	TRES_POR_SEMANA(2, "Três vezes por semana"),
	UM_POR_SEMANA(3, "Uma vez por semana");
	
	private int cod;
	private String descricao;
	
	private PeriodoRepeticao(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static PeriodoRepeticao toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		for (PeriodoRepeticao x : PeriodoRepeticao.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}

}
