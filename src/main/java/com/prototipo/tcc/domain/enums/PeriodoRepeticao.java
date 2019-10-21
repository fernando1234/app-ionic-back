package com.prototipo.tcc.domain.enums;

public enum PeriodoRepeticao {

	UM_AO_DIA(1, "Uma vez ao dia todos os dias (9AM)"),
	DOIS_AO_DIA(2, "Duas vezes ao dia todos os dias (9AM - 9PM)"),
	UM_CADA_DOIS_DIAS(2, "Uma vez ao dia a cada dois dias (9AM)"),
	DOIS_CADA_DOIS_DIAS(3, "Duas vezes ao dia a cada dois dias (9AM - 9PM)");
	
	private int cod;
	private String descricao;
	
	PeriodoRepeticao(int cod, String descricao) {
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
