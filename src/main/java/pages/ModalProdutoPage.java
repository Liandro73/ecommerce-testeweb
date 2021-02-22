package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ModalProdutoPage {

	private WebDriver driver;

	private By mensagemProdutoAdicionado = By.id("myModalLabel");
	private By descricaoProduto = By.className("product-name");
	private By precoProduto = By.cssSelector("div.modal-body p.product-price");
	private By listaValortesInformados = By.cssSelector("div .divide-right .col-md-6:nth-child(2) span strong");
	private By subtotal = By.cssSelector(".cart-content p:nth-child(2) span.value");
	private By botaoProceedToCheckout = By.cssSelector("div.cart-content-btn a.btn-primary");

	public ModalProdutoPage(WebDriver driver) {
		this.driver = driver;
	}

	@SuppressWarnings("unchecked")
	public String obterMensagemProdutoAdicionado() {
		@SuppressWarnings("rawtypes")
		FluentWait wait = new FluentWait(driver).withTimeout(Duration.ofSeconds(7)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemProdutoAdicionado));

		return driver.findElement(mensagemProdutoAdicionado).getText();
	}

	public String obterDescricaoProduto() {
		return driver.findElement(descricaoProduto).getText();
	}

	public String obterPrecoProduto() {
		return driver.findElement(precoProduto).getText();
	}

	public String obterTamanhoProduto() {
		return driver.findElements(listaValortesInformados).get(0).getText();
	}

	public String obterCorProduto() {
		if (driver.findElements(listaValortesInformados).size() == 3)
			return driver.findElements(listaValortesInformados).get(1).getText();
		else
			return "N/A";
	}

	public String obterQuantidadeProduto() {
		if (driver.findElements(listaValortesInformados).size() == 3)
			return driver.findElements(listaValortesInformados).get(2).getText();
		else
			return driver.findElements(listaValortesInformados).get(1).getText();
	}

	public String obterSubtotal() {
		return driver.findElement(subtotal).getText();
	}

	public CarrinhoPage clicarBotaoPreoceedToCheckout() {
		driver.findElement(botaoProceedToCheckout).click();
		return new CarrinhoPage(driver);
	}
}
