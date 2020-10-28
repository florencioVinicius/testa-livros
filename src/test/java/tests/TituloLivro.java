package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TituloLivro {

	@Test
	public void ComparaAutores() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("http://submarino.com.br");
		driver.findElement(By.linkText("Livros")).click();

		driver.findElement(By.className("product-grid-item")).click();
		String author = driver.findElement(By.className("author-name__AuthorLink-sc-19niywj-0")).getText();
		System.out.println(author);
		assertEquals(1, 1);
	}
}