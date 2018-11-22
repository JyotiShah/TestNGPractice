package testScriptTestNG;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import testBaseTestNG.testBaseTestNG;

public class testScriptTestNG extends testBaseTestNG {
  

	/*
	 * exercise 1 -
		Go to https://manipal.edu/mu.html
		Mouseover to Academics
		Mouseover to Institution List at the left side menu
		Mouseover to Engineering and then mouseover/click on Dept & Faculty
		once the department and facult page opens up
		Print all the Quick Facts (Actual Value)
		Print sum of starting two quick facts
		Take screenshot of the page 
	 */
	
	@Test
	public void manipalTest() throws IOException {

//		Locators
		WebElement academics = driver.findElement(By.xpath("//*[@id='primary-sticky']/div/ul/li[2]/a"));

//		verifying title
		
		wait.until(ExpectedConditions.titleIs(driver.getTitle()));
		System.out.println(driver.getTitle());
		
//		Navigating to Dep and Faculty
		
		moveToElement(academics);
		WebElement instlink = driver.findElement(By.xpath("//*[@id='fat-menu']/div/ul/li[3]/a"));
		moveToElement(instlink);
		moveToElement(driver.findElement(By.xpath("//*[@id='fat-menu']/div/div/ul[1]/li[7]/a[1]/span")));
		moveToElementAndClick(driver.findElement(By.xpath("//*[@id='fat-menu']/div/div/ul[2]/li[1]/ul[1]/li[3]/a/span")));
		System.out.println("Navigating");
		
		
//		Navigation to dep and faculty page
		String expDepnFactPage = "Faculty";
		wait.until(ExpectedConditions.titleContains(expDepnFactPage));
		String actualDepnFactPage = driver.getTitle();		

//		Printing Quick Facts
		
		List<WebElement> list = driver.findElements(By.xpath("//div[contains(@class,'fact-no col-xs-12 col-md-12 col-lg-12')]"));
//		List<WebElement> list2 = driver.findElements(By.xpath("//div[contains(@class,'fact-text col-xs-12 col-md-12 col-lg-12')]"));
		int sum=0;
		for(int i=0; i<list.size(); i++) {
			String sublist = list.get(i).getText();
			System.out.println("Quick Facts no "+(i+1)+" :"+ sublist);
			sum = Integer.parseInt(list.get(0).getText()) + Integer.parseInt(list.get(1).getText());
		}
		System.out.println("Sum : "+sum);
		captureScreenShot(driver);
	}
/*
 * exercise 2 - done
		1. Open site http://www.crossword.in/
		2. Chose "Books" from tabs visible
		3. Display number of books found in the IDE Log
		4. Click on Price: Sort by Price: high to low icon
		5. Ensure that the books are indeed sorted on price high to low
 */
	@Test
	public void crossWordtest() throws IOException, InterruptedException {

		wait.until(ExpectedConditions.titleIs(driver.getTitle()));
//		Selecting book from home page
		WebElement booklink = driver.findElement(By.xpath("//li[2]/a[contains(text(),'Books')]"));
		booklink.click();
		
//		Navigating to books page
		String expTitle = "Books";
		wait.until(ExpectedConditions.titleContains(expTitle));
		
//		Selecting high to low in sort
		String visibletext = "High to Low";
		WebElement element = driver.findElement(By.xpath("//div/select"));
		selectByVisibleText(element, visibletext);
		
		wait.until(ExpectedConditions.urlContains("sort_type=price_in_desc"));
		System.out.println("Navigated to books page and sorted books by Price - High to low");
		captureScreenShot(driver);
		Thread.sleep(2000);
		
//		Validating prices
		int i =0;
		List<WebElement> list = driver.findElements(By.xpath("//span[@class='price']//span[@class='m-w']"));
		List<Integer> stringList = new ArrayList<Integer>();
		System.out.println("Total Elements found: "+list.size());
		for(i=0; i<list.size(); i++) {
			
			String price = list.get(i).getAttribute("innerText");
//			String price = list.get(i).getAttribute("textContent");

			int cost=Integer.parseInt(price.substring(price.indexOf(".")+1).replace(",", ""));
			System.out.println("Price: "+cost);
			stringList.add(cost);
		}
		System.out.println("Total elements inserted: "+stringList.size());
		List<Integer> tmp = new ArrayList(stringList);
		Collections.sort(tmp);
		Collections.reverse(tmp);
		boolean sorted=true;
		for(i=0;i<stringList.size();i++)
		{
			System.out.println(tmp.get(i)+">>>"+stringList.get(i));
			 if(tmp.get(i)!=stringList.get(i))
			 {
					 sorted=false;
					 break;
			 }
			
		}
		Assert.assertTrue(sorted, "Elements are not sorted in descending order");
	}
	/*
	 * exercise 4 - done
		1. go to http://apollocabs.in/contact-us.html
		2. FIND all the links on the website and check whether they are working fine or broken
		3. use Junit framework
	 */
	@Test
	public void findBrokenLinksApollocabs() throws Exception {
		
		List<WebElement> allLinks = findAllLinks(driver); 
		System.out.println("Total number of elements found: "+allLinks.size());
		for(WebElement element :allLinks) {

			try {
			String href = element.getAttribute("href");
			URL url = new URL(href);
			System.out.println("URL : "+ href+ " returned : "+verifyBrokenLinks(url));
			}catch(Exception e) {
				System.out.println("At " + element.getAttribute("innerHTML") + " Exception occured -&gt; " + e.getMessage());
			}
		}
		
	}
	/*
	exercise 5 - done
	1. Go to the below URL: https://www.google.com/maps/
	2. Type and search for the below Address:
	Harvard Business School, Boston, MA 02163, United States
	3. Verify the Text Present “Harvard Business School” in the header of location card
	4. Assert phone number +(617) 495-6000 present within the inline popup window.
	5. Display the rating number in console
	6. Capture the screen at the start and end of Test
	*/
	@Test
	public void googleMapsTest() throws IOException {

		captureScreenShot(driver);
//		2. Type and search for the below Address:
//		Harvard Business School, Boston, MA 02163, United States
		String searchText = "Harvard Business School, Boston, MA 02163, United States";
		
		WebElement searchInput = driver.findElement(By.id("searchboxinput"));
		searchInput.clear();
		searchInput.sendKeys(searchText);
		searchInput.sendKeys(Keys.ENTER);

//			3. Verify the Text Present Harvard Business School in the header of location card

		String expTitle = "Harvard Business School";
		wait.until(ExpectedConditions.titleContains(expTitle));
		
		WebElement headerOfSearchResult = driver.findElement(By.tagName("h1"));
		String exptHeader = "Harvard Business School";
		String actualHeader = headerOfSearchResult.getText();
		
		Assert.assertEquals(exptHeader, actualHeader);
		
//			4. Assert phone number +(617) 495-6000 present within the inline popup window.

		WebElement phoneNumber = driver.findElement(By.xpath("//span[@class='section-info-icon maps-sprite-pane-info-phone']/parent::div/span[@class='section-info-text']"));
		System.out.println(phoneNumber.getText());
		String expPhoneNo = "(617) 495-6000";
		String actPhoneNo = phoneNumber.getText();
		Assert.assertEquals(expPhoneNo, actPhoneNo);
		
//			5. Display the rating number in console

		WebElement Rating = driver.findElement(By.xpath("//div[@class='section-reviewchart-right']/span/span"));
		System.out.println("Rating :"+Rating.getText());
		
//			6. Capture the screen at the start and end of Test
		captureScreenShot(driver);
	}
	
//	Helper methods------------------------------------------------------------------
	
//	Find all links on a web page:
	public List findAllLinks(WebDriver driver) {
		List<WebElement> list = driver.findElements(By.tagName("a"));
		list.addAll(driver.findElements(By.tagName("img")));
		
		List<WebElement> finalList = new ArrayList();
		for(WebElement element : list) {
			if(element.getAttribute("href")!=null) {
				finalList.add(element);
			}
		}
		return finalList;
	}
//	Method to verify Broken Link
	public String verifyBrokenLinks(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
		connection.connect();
		String response = connection.getResponseMessage();
		connection.disconnect();
		return response;
		}catch(Exception e) {
			return e.getMessage();
		}
	}
//	Select by visible text
	public void selectByVisibleText(WebElement element, String visibletext) {
		Select select = new Select(element);
		select.selectByVisibleText(visibletext);
	}
//	Move to element using actions class
	public void moveToElement(WebElement element) {

		Actions action = new Actions(driver);
		
//		wait.until(ExpectedConditions.visibilityOf(element))
		action.moveToElement(element).build().perform();
	}

//	Move to element and click using Actions class
	public void moveToElementAndClick(WebElement element) {

		Actions action = new Actions(driver);
		wait.until(ExpectedConditions.visibilityOf(element));
		action.moveToElement(element).build().perform();
		action.click(element).build().perform();;
		
	}
	
//	Class to take screenshot
	public void captureScreenShot(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in d drive with name "screenshot.png"
           FileUtils.copyFile(scrFile, new File("C:\\Jyoti\\workspaces\\Practice\\src\\Output\\screenshot"+System.currentTimeMillis()+".png"));
	}
}
