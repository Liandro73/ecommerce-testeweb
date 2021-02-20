package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests extends BaseTests {
	
	LoginPage loginPage;
	ProdutoPage produtoPage;
	ModalProdutoPage modalProdutoPage;
	CarrinhoPage carrinhoPage;
	CheckoutPage checkoutPage;
	PedidoPage pedidoPage;
	
	String nomeProduto_HomePage;
	String nomeCliente = "Marcelo Bittencourt";
	String emailCliente = "marcelo@teste.com";
	String senhaCliente = "marcelo";
	
	@Test
	public void testContarProdutos_oitoProdutosDiferentes( ) {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		assertThat(produtosNoCarrinho, is(0));
	}
	
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguas() {
		int indice = 0;
		nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		produtoPage = homePage.clicarProduto(indice);
		
		String nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		//Clicar no botão Sign In na hone page
		loginPage = homePage.clicarBotaoSignIn();
		
		//Preencher usuário e senha
		loginPage.preencherEmail(emailCliente);
		loginPage.preencherSenha(senhaCliente);
		
		//Clicar no botão Sign In para logar
		loginPage.clicarBotaoSignIn();
		
		//Validar se o usuário está logado de fato
		assertThat(homePage.estaLogado(nomeCliente), is(true));
		
		carregarPaginaInicial();
	}
	
	@Test
	public void testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
		
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		
		
		//Usuário logado
		if (!homePage.estaLogado(nomeCliente)) {
			testLoginComSucesso_UsuarioLogado();
		}
		
		//Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguas();
		
		//Selecionar tamanho
		@SuppressWarnings("unused")
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
		//Selecionar cor
		produtoPage.selecionarCorPreta();
		
		//Selecionar quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);
		
		//Adicionar ao carrinho
		modalProdutoPage = produtoPage.cliclarBotaoAddToCart();
		
		//Validações
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_HomePage.toUpperCase()));
		
		Double precoProduto = Funcoes.removeCifraoDevolveDouble(modalProdutoPage.obterPrecoProduto());
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		
		Double subtotal = Funcoes.removeCifraoDevolveDouble(modalProdutoPage.obterSubtotal());
		
		Double subtotalCalculado = quantidadeProduto * precoProduto;
		
		assertThat(subtotal, is(subtotalCalculado));
		
	}
	
	//Valores esperados
	String esperado_nomeProduto = "Hummingbird printed t-shirt";
	Double esperado_precoProduto = 19.12;
	String esperado_tamanhoProduto = "M";
	String esperado_corProduto = "Black";
	Integer esperado_input_quantidadeProduto = 2;
	Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;
	
	Integer esperado_numeroItensTotal = esperado_input_quantidadeProduto;
	Double esperado_subtotalTotal = esperado_subtotalProduto;
	Double esperado_freteTotal = 7.00;
	Double esperado_totalTaxExclTotal = esperado_subtotalTotal + esperado_freteTotal;
	Double esperado_totalTaxIncTotal = esperado_totalTaxExclTotal;
	Double esperado_taxasTotal = 0.00;
	
	@Test
	public void testIrParaCarrinho_InformacoesPersistidas() {
		//Produto incluído na tela ModalProdutoPage
		testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso();
		
		carrinhoPage = modalProdutoPage.clicarBotaoPreoceedToCheckout();
		
		//Teste
		//Validar todos elementos da tela
		
		//Asserções Hamcrest
		assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
		assertThat(carrinhoPage.obter_tamanhoProduto(), is(esperado_tamanhoProduto));
		assertThat(carrinhoPage.obter_corProduto(), is(esperado_corProduto));
		assertThat(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()), is(esperado_input_quantidadeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()), is(esperado_subtotalProduto));
		
		assertThat(Integer.parseInt(Funcoes.removeTexoPrefixoDevolveInt(carrinhoPage.obter_numeroItensTotal(), " items")), is(esperado_numeroItensTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is(esperado_subtotalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_freteTotal()), is(esperado_freteTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()), is(esperado_totalTaxExclTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()), is(esperado_totalTaxIncTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxasTotal()), is(esperado_taxasTotal));
		
		//Asserções JUnit
		/*
		assertEquals(carrinhoPage.obter_nomeProduto(), esperado_nomeProduto);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), esperado_precoProduto);
		assertEquals(carrinhoPage.obter_tamanhoProduto(), esperado_tamanhoProduto);
		assertEquals(carrinhoPage.obter_corProduto(), esperado_corProduto);
		assertEquals(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()), esperado_input_quantidadeProduto);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalProduto()), esperado_subtotalProduto);
		
		assertEquals(Funcoes.removeTextItemsDevolveInt(carrinhoPage.obter_numeroItensTotal()), esperado_numeroItensTotal);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), esperado_subtotalTotal);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_freteTotal()), esperado_freteTotal);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()), esperado_totalTaxExclTotal);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()), esperado_totalTaxIncTotal);
		assertEquals(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxasTotal()), esperado_taxasTotal);
		*/
		
	}
	
	@Test
	public void testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk() {
		//Produto disponível no carrinho de compras
		testIrParaCarrinho_InformacoesPersistidas();
		
		//Teste
		//Clicar no botão
		checkoutPage = carrinhoPage.clicarBotaoProceedToCheckout();
		
		//Preencher informações
		
		//Validar informações na tela
		assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()), is(esperado_totalTaxIncTotal));
//		assertThat(Funcoes.removeInformacoesAdicionasDevolveString(checkoutPage.obter_nomeCliente()), is(nomeCliente));
		assertTrue(checkoutPage.obter_nomeCliente().startsWith(nomeCliente));
		
		checkoutPage.clicarBotaoContinueAddress();
		
		assertThat(Double.parseDouble(Funcoes.removeTexto(checkoutPage.obter_valorFrete(), "$", " tax excl.")), is(esperado_freteTotal));
		
		checkoutPage.clicarBotaoContinueShipping();
		
		//Selecionar opção "Pay By Check"
		checkoutPage.selecionarRadioPayByCheck();
		//Validar valor do cheque (amount)
		assertThat(Double.parseDouble(Funcoes.removeTexto(checkoutPage.obter_amountPayByCheck(), "$", " (tax incl.)")), is(esperado_totalTaxIncTotal));
		//Clicar na opção "I agree"
		checkoutPage.selecionarCheckboxIAgree();
		checkoutPage.checkboxIAgreeSelecionado();
	}
	
	@Test
	public void finalizarPedido_pedidoFinalizadoComSucesso() {
		//Checkout completamente concluído
		testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk();
		
		//Teste
		//Clicar no botão para confirmar o pedido
		pedidoPage = checkoutPage.clicarBotaoConfirmaPedido();
		
		//Validar valores na tela
		assertTrue(pedidoPage.obter_textoPedidoConfirmado().endsWith("YOUR ORDER IS CONFIRMED"));
		assertThat(pedidoPage.obter_email(), is(emailCliente));
		assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalProduto));
		assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
		
	}

}
