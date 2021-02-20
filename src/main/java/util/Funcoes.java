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
	
	public static String removeTexto(String TextoCompleto, String textoARemoverPrefixo, String textoARemoverSufixo) {
		TextoCompleto = TextoCompleto.replace(textoARemoverPrefixo, "");
		TextoCompleto = TextoCompleto.replace(textoARemoverSufixo, "");
		return TextoCompleto;
	}
	
//	public static String removeInformacoesAdicionasDevolveString(String nomeUsuario) {
//		nomeUsuario = nomeUsuario.replace("\nRua Brazil, 123\nDallas, Texas 12345\nUnited States", "");
//		return nomeUsuario;
//	}
	
}
