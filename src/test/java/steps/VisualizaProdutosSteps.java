package steps;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.HomePage;

public class VisualizaProdutosSteps {
	
	private static WebDriver driver;
	private HomePage homePage = new HomePage(driver);
	
	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver", "/home/dernival_liandro/.webdrivers/chromedriver/88/chromedriver");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public static void finalizar() {
		driver.quit();
	}

	@Quando("nao estou logado")
	public void nao_estou_logado() {
		assertThat(homePage.naoEstaLogado(), is(true));
	}

	@Entao("visualizo {int} produtos disponiveis")
	public void visualizo_produtos_disponiveis(Integer int1) {
		assertThat(homePage.contarProdutos(), is(int1));
	}

	@Entao("carrinho esta zerado")
	public void carrinho_esta_zerado() {
		assertThat(homePage.obterQuantidadeProdutosNoCarrinho(), is(0));
	}

}
