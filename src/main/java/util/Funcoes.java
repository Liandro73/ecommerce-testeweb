package util;

public class Funcoes {
	
	public static Double removeCifraoDevolveDouble(String valorComCifrao) {
		valorComCifrao = valorComCifrao.replace("$", "");
		return Double.parseDouble(valorComCifrao);
	}
	
	public static Integer removeTextItemsDevolveInt(String textoCompleto) {
		textoCompleto = textoCompleto.replace(" items", "");
		return Integer.parseInt(textoCompleto);
	}
	
}
