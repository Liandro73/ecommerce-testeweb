package util;

public class Funcoes {
	
	public static Double removeCifraoDevolveDouble(String valorComCifrao) {
		valorComCifrao = valorComCifrao.replace("$", "");
		return Double.parseDouble(valorComCifrao);
	}
	
	public static String removeTexoPrefixoDevolveInt(String textoCompleto, String textoARemover) {
		textoCompleto = textoCompleto.replace(textoARemover, "");
		return textoCompleto;
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
