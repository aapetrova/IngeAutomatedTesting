package test.java.base.depositor;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.java.base.BaseTest;
import test.java.base.Genre;
import test.java.base.ItemStatus;
import main.java.pages.LoginPage;
import main.java.pages.StartPage;
import main.java.pages.homepages.DepositorHomePage;
import main.java.pages.homepages.ModeratorHomePage;
import main.java.pages.submission.FullSubmissionPage;
import main.java.pages.submission.ViewItemPage;

/**
 * TestLink UC #5
 * Tests full submission of a book in standard workflow.
 * @author apetrova
 *
 */
public class ReleaseBookFullStandardTest extends BaseTest {
	
	private String title;
	private String filepath;
	private String author;
	
	private DepositorHomePage depositorHomePage;
	private ModeratorHomePage moderatorHomePage;
	private ViewItemPage viewItemPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		title = "Released book in standard workflow: " + getTimeStamp();
		filepath = getFilepath("SamplePDFFile.pdf");
		author = "Testermeier, Testo (MPI for Social Anthropology)";
	}
	
	@Test(priority = 1)
	public void loginAsDepositor() {
		LoginPage loginPage = new StartPage(driver).goToLoginPage();
		depositorHomePage = loginPage.loginAsDepositor(depositorUsername, depositorPassword);
		Assert.assertEquals(depositorHomePage.getUsername(), depositorName, "Expected and actual name don't match.");
	}
	
	@Test(priority = 2)
	public void fullSubmissionStandardWorkflow() {
		FullSubmissionPage fullSubmissionPage = depositorHomePage.goToSubmissionPage().depositorGoToFullSubmissionPage();
		viewItemPage = fullSubmissionPage.fullSubmission(Genre.BOOK, title, author, filepath);
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.PENDING, "Item was not uploaded.");
	}
	
	@Test(priority = 3, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void depositorSubmitsItem() {
		viewItemPage = viewItemPage.submitItem();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.SUBMITTED, "Item was not submitted.");
	}
	
	@Test(priority = 4, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void logoutDepositor() {
		depositorHomePage = (DepositorHomePage) new StartPage(driver).goToHomePage(depositorHomePage);
		depositorHomePage.logout();
	}
	
	@Test(priority = 5, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void loginModerator() {
		LoginPage loginPage = new StartPage(driver).goToLoginPage();
		moderatorHomePage = loginPage.loginAsModerator(moderatorUsername, moderatorPassword);
	}
	
	@Test(priority = 6, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void moderatorSendsBackSubmission() {
		viewItemPage = moderatorHomePage.goToQAWorkspacePage().openSubmittedItemByTitle(title);
		viewItemPage = viewItemPage.editItem();
		viewItemPage = viewItemPage.sendBackForRework();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.IN_REWORK, "Item was not sent for rework.");
	}
	
	@Test(priority = 7, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void logoutModerator() {
		moderatorHomePage = (ModeratorHomePage) viewItemPage.goToHomePage(moderatorHomePage);
		moderatorHomePage.logout();
	}
	
	@Test(priority = 8, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void loginDepositor() {
		LoginPage loginPage = new StartPage(driver).goToLoginPage();
		depositorHomePage = loginPage.loginAsDepositor(depositorUsername, depositorPassword);
	}
	
	@Test(priority = 9, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void depositorRevisesItem() {
		viewItemPage = depositorHomePage.goToMyItemsPage().openItemByTitle(title);
		viewItemPage = viewItemPage.submitItem();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.SUBMITTED, "Item was not submitted.");
	}
	
	@Test(priority = 10, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void logoutDepositor2() {
		depositorHomePage = (DepositorHomePage) new StartPage(driver).goToHomePage(depositorHomePage);
		depositorHomePage.logout();
	}
	
	@Test(priority = 11, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void loginModerator2() {
		LoginPage loginPage = new StartPage(driver).goToLoginPage();
		moderatorHomePage = loginPage.loginAsModerator(moderatorUsername, moderatorPassword);
	}
	
	@Test(priority = 12, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void moderatorReleasesSubmission() {
		viewItemPage = moderatorHomePage.goToQAWorkspacePage().openSubmittedItemByTitle(title);
		viewItemPage = viewItemPage.releaseItem();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.RELEASED, "Item was not released.");
	}
	
	@Test(priority = 13, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void moderatorModifiesRelease() {
		moderatorHomePage = (ModeratorHomePage) new StartPage(driver).goToHomePage(moderatorHomePage);
		viewItemPage = moderatorHomePage.openSubmittedItemByTitle(title);
		viewItemPage = viewItemPage.modifyItem();
	}
	
	@Test(priority = 14, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void moderatorReleasesSubmissionAgain() {
		moderatorHomePage = (ModeratorHomePage) new StartPage(driver).goToHomePage(moderatorHomePage);
		viewItemPage = moderatorHomePage.goToQAWorkspacePage().openSubmittedItemByTitle(title);
		viewItemPage = viewItemPage.releaseItem();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.RELEASED, "Item was not released.");
	}
	
	@Test(priority = 15, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void moderatorDiscardsSubmission() {
		moderatorHomePage = (ModeratorHomePage) new StartPage(driver).goToHomePage(moderatorHomePage);
		viewItemPage = moderatorHomePage.openSubmittedItemByTitle(title);
		viewItemPage = viewItemPage.discardItem();
		ItemStatus itemStatus = viewItemPage.getItemStatus();
		Assert.assertEquals(itemStatus, ItemStatus.DISCARDED, "Item was not discarded.");
	}
	
	@Test(priority = 16, dependsOnMethods = { "fullSubmissionStandardWorkflow" })
	public void logoutModerator2() {
		moderatorHomePage = (ModeratorHomePage) new StartPage(driver).goToHomePage(moderatorHomePage);
		moderatorHomePage.logout();
	}
	
}
