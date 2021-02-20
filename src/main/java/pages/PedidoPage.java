package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import util.Funcoes;

public class PedidoPage {
	
	private WebDriver driver;
	
	private By testePedidoConfirmado = By.cssSelector("#content-hook_order_confirmation h3");
	private By email = By.cssSelector("#content-hook_order_confirmation p");
	private By totalProdutos = By.cssSelector("div.order-confirmation-table div.order-line div.row div.bold");
	private By totalTaxIncl = By.cssSelector("div.order-confirmation-table table tr.total-value td:nth-child(2)");
	private By metodoPagamento = By.cssSelector("#order-details ul li:nth-child(2)");
	
	public PedidoPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String obter_textoPedidoConfirmado() {
		return driver.findElement(testePedidoConfirmado).getText();
	}
	
	public String obter_email() {
		String textoCompleto = driver.findElement(email).getText();
		return Funcoes.removeTexto(textoCompleto, "An email has been sent to the ", " address.");
	}
	
	public Double obter_totalProdutos() {
		return Funcoes.removeCifraoDevolveDouble(driver.findElement(totalProdutos).getText());
	}
	
	public Double obter_totalTaxIncl() {
		return Funcoes.removeCifraoDevolveDouble(driver.findElement(totalTaxIncl).getText());
	}
	
	public String obter_metodoPagamento() {
		String textoCompleto = driver.findElement(metodoPagamento).getText();
		return Funcoes.removeTexoPrefixoDevolveInt(textoCompleto, "Payment method: Payments by ");
	}
	
}
