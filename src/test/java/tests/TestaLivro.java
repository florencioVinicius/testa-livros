package tests;

import static org.junit.Assert.*;

import java.awt.datatransfer.SystemFlavorMap;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

public class TestaLivro {
	private static String autor = "";
	private static String isbn = "";
	
	private WebDriver driver;
	private static ChromeDriverService service;
	
	@BeforeClass 
	public static void getBookSubmarino() throws IOException, InterruptedException {
		//Pesquisando o primeiro link de livro na submarrino
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get("http://submarino.com.br");
		Thread.sleep(1000);
		//procurar se há livro nos cards apresentados na primeira pagina
		WebElement firstBook = null;
		for( WebElement productName : driver.findElements(By.className("product-card__Shadow-asxtmt-0") )){
			if (productName.findElement(By.className("product-card__ProductName-asxtmt-11")).getText().toLowerCase().contains("livro")) {
				firstBook = productName;
				break;
			}
		}
		
		if(firstBook != null) {
			firstBook.click();
		}else {
			//Caso não ache livro na primeira pagina: ir para a sessão de livros 
			driver.findElement(By.linkText("Livros")).click();
			Thread.sleep(3000); 
			
			WebElement element = driver.findElement(By.className("product-grid-item")).findElement(By.cssSelector("a"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			element.click();
		}
		
		Thread.sleep(500);
		autor = driver.findElement(By.cssSelector("a[href*=\"author-section\"]>span")).getText();
		
		for(WebElement tableRow : driver.findElements(By.className("cNwYXF"))){
			if(tableRow.findElement(By.cssSelector("td:nth-child(1)")).getText().toLowerCase().contains("isbn")){
				isbn = tableRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
			}
		}
		
		//System.out.println(autor+titulo);
		
		driver.close();
	}

	@Before
	public void beforer(){
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@After
	public void after(){
		driver.close();
	}
	
	@Test
	public void searchOnAmazon() throws Exception, InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.get("http://amazon.com.br");
		Thread.sleep(500);
		
		//pesquisa
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(isbn.toString());
		driver.findElement(By.id("nav-search-submit-text")).click();
		Thread.sleep(1000);
		
		String otherauthor = "";
		
		try {
			//first book found 
			driver.findElement(By.cssSelector("a.a-link-normal.s-no-outline")).click();
			//comparando autores
			otherauthor = driver.findElement(By.className("contributorNameID")).getText();
		} catch (Exception e) {
			throw new Exception("ISBN não encontrado no site", e);
		}
		
		//System.out.println(otherauthor);
		assertEquals(autor, otherauthor);
	}
	
	@Test
	public void searchOnAmericanas() throws Exception, InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.get("http://americanas.com.br");
		Thread.sleep(500);
		
		//pesquisa
		driver.findElement(By.id("h_search-input")).sendKeys(isbn.toString());
		driver.findElement(By.id("h_search-btn")).click();
		Thread.sleep(1000);
		
		String otherauthor = "";
		
		try {
			//Rolara até que o livro apareça
			WebElement element = driver.findElement(By.className("src__Wrapper-sc-1di8q3f-2"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			element.click();
			
			//rolar até que o autor apareça
			element = driver.findElement(By.className("src__View-wbypax-3"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			
			//comparando autores
			for(WebElement tableRow : driver.findElements(By.className("src__View-wbypax-3"))){
				//List<WebElement> colum = tableRow.findElements(By.className("src__Text-wbypax-4:nth-child(2)"));
				if(tableRow.findElement(By.cssSelector("td:nth-child(1)")).getText().toLowerCase().contains("autor")){
					otherauthor = tableRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
				}
			}
			
		} catch (Exception e) {
			throw new Exception("ISBN não encontrado no site", e);
		}
		
		//System.out.println(otherauthor);
		assertEquals(autor, otherauthor);
	}
}