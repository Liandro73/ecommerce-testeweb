package steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.HomePage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

public class CompraProdutosSteps {

	private static WebDriver driver;
	private HomePage homePage = new HomePage(driver);
	private LoginPage loginPage;
	private ProdutoPage produtoPage;
	private ModalProdutoPage modalProdutoPage;

	private String nomeProduto_HomePage;
	private String precoProduto_HomePage;
	private String nomeProduto_ProdutoPage;
	private String precoProduto_ProdutoPage;

	private String nomeCliente = "Marcelo Bittencourt";
	private String emailCliente = "marcelo@teste.com";
	private String senhaCliente = "marcelo";

	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver",
				"/home/dernival_liandro/.webdrivers/chromedriver/88/chromedriver");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@After(order = 0)
	public static void finalizar() {
		driver.quit();
	}

	@Dado("que estou na pagina inicial")
	public void que_estou_na_pagina_inicial() {
		homePage.carregarPaginaIncial();
		assertThat(homePage.obterTituloPagina(), is("Loja de Teste"));
	}

	@Quando("estou logado")
	public void estou_logado() {
		// Clicar no botão Sign In na hone page
		loginPage = homePage.clicarBotaoSignIn();

		// Preencher usuário e senha
		loginPage.preencherEmail(emailCliente);
		loginPage.preencherSenha(senhaCliente);

		// Clicar no botão Sign In para logar
		loginPage.clicarBotaoSignIn();

		// Validar se o usuário está logado de fato
		assertThat(homePage.estaLogado(nomeCliente), is(true));

		homePage.carregarPaginaIncial();
	}

	@Quando("seleciono um produto na posicao {int}")
	public void seleciono_um_produto_na_posicao(Integer indice) {
		nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		precoProduto_HomePage = homePage.obterPrecoProduto(indice);

		produtoPage = homePage.clicarProduto(indice);

		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
	}

	@Quando("nome do pruduto na tela principal e na tela produto eh {string}")
	public void nome_do_pruduto_na_tela_principal_e_na_tela_produto_eh(String nomeProduto) {
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto.toUpperCase()));
		assertThat(nomeProduto_ProdutoPage.toUpperCase(), is(nomeProduto.toUpperCase()));
	}

	@Quando("preco do produto na tela principal e na tela produto eh {string}")
	public void preco_do_produto_na_tela_principal_e_na_tela_produto_eh(String precoProduto) {
		assertThat(precoProduto_HomePage.toUpperCase(), is(precoProduto.toUpperCase()));
		assertThat(precoProduto_ProdutoPage.toUpperCase(), is(precoProduto.toUpperCase()));
	}

	@Quando("adiciono o produto no carrinho com tamanho {string} cor {string} e quantidade {int}")
	public void adiciono_o_produto_no_carrinho_com_tamanho_cor_e_quantidade(String tamanhoProduto, String corProduto,
			Integer quantidadeProduto) {
		// Selecionar tamanho
		@SuppressWarnings("unused")
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);

		listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		// Selecionar cor
		if (!corProduto.equals("N/A"))
			produtoPage.selecionarCorPreta();

		// Selecionar quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);

		// Adicionar ao carrinho
		modalProdutoPage = produtoPage.cliclarBotaoAddToCart();

		// Validações
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado()
				.endsWith("Product successfully added to your shopping cart"));
	}

	@Entao("o produto aparece na confirmacao com nome {string} preco {string} tamanho {string} cor {string} e quantidade {int}")
	public void o_produto_aparece_na_confirmacao_com_nome_preco_tamanho_cor_e_quantidade(String nomeProduto,
			String precoProduto, String tamanhoProduto, String corProduto, Integer quantidadeProduto) {
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_HomePage.toUpperCase()));

		@SuppressWarnings("unused")
		Double precoProdutoDoubleEncontrado = Double.parseDouble(modalProdutoPage.obterPrecoProduto().replace("$", ""));
		Double precoProdutoDoubleEsperado = Double.parseDouble(precoProduto.replace("$", ""));

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		if (!corProduto.equals("N/A"))
			assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));

		String subTotalString = modalProdutoPage.obterSubtotal();
		subTotalString = subTotalString.replace("$", "");
		Double subTotalEncontrado = Double.parseDouble(subTotalString);

		Double subtotalCalculadoEsperado = quantidadeProduto * precoProdutoDoubleEsperado;

		assertThat(subTotalEncontrado, is(subtotalCalculadoEsperado));
	}
	
	@After(order = 1)
	public void capturarTela(Scenario scenario) {
		TakesScreenshot camera = (TakesScreenshot) driver;
		File capturarTela = camera.getScreenshotAs(OutputType.FILE);
		
		String scenarioId = scenario.getId().substring(scenario.getId().lastIndexOf(".feature:") + 9);
		String nomeArquivo = "resources/screenshots/" + scenario.getName() + "_" + scenario.getStatus() + "_" + scenarioId + ".png";
		
		try {
			Files.move(capturarTela, new File(nomeArquivo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
