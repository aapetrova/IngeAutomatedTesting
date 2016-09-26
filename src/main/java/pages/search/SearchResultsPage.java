package main.java.pages.search;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import main.java.pages.BasePage;
import main.java.pages.submission.ViewItemPage;
import main.java.pages.tools.RestExamplePage;

public class SearchResultsPage extends BasePage {

	@FindBy(id = "j_idt105:lnkRestServiceExamplePage")
	private WebElement queryInRestLink;
	
	@FindBy(id = "j_idt105:lnkList_lblExportOptions")
	private WebElement exportLink;
	
	@FindBy(id = "j_idt105:selEXPORTFORMAT_OPTIONS")
	private WebElement exportFormatDropdown;
	
	@FindBy(className = "allCheckBox")
	private WebElement allCheckBox;
	
	@FindBy(id = "j_idt105:btnExportDownload")
	private WebElement downloadLink;
	
	private List<WebElement> searchResults;
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		
		searchResults = driver.findElements(By.className("listItem"));
		
		PageFactory.initElements(driver, this);
	}
	
	public ViewItemPage openFirstResult() {
		WebElement titleLink = searchResults.get(0).findElement(By.className("itemHeadline"));
		titleLink.click();
		
		return PageFactory.initElements(driver, ViewItemPage.class);
	}
	
	public List<WebElement> getResults() {
		return searchResults;
	}
	
	public boolean allResultsReleased() {
		for (WebElement result : searchResults) {
			String uploadInfo = result.findElement(By.cssSelector(".listItemLine:nth-of-type(2)>span:nth-of-type(2)")).getText();
			if (!uploadInfo.contains("Published"))
				return false;
		}
		return true;
	}
	
	public RestExamplePage insertQueryREST() {
		queryInRestLink.click();
		//TODO switch frames?
		return PageFactory.initElements(driver, RestExamplePage.class);
	}
	
	public SearchResultsPage goToExport() {
		exportLink.click();
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
	}
	
	public boolean allResultsExported(String format, String fileFormat) {
		Select exportFormat = new Select(exportFormatDropdown);
		exportFormat.selectByValue(format);
		
		if (fileFormat != null) {
			WebElement fileFormatDropdown = driver.findElement(By.id("j_idt105:selFILEFORMAT_OPTIONS"));
			Select fileFormatSelect = new Select(fileFormatDropdown);
			fileFormatSelect.selectByValue(fileFormat);
		}
		
		makeVisible(allCheckBox);
		selectAllItems();
		return downloadLink.isDisplayed() && downloadLink.isEnabled();
	}
	
	private void selectAllItems() {
		allCheckBox.click();
	}
	
	private void makeVisible(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].style.display='block';", element);
	}
}
