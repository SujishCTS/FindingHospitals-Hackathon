package pages;


import java.io.FileReader;

import java.time.Duration;

import java.util.*;

import java.util.concurrent.TimeUnit;

import org.json.simple.parser.JSONParser;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.json.simple.*;

import base.Base;

public class HospitalNames extends Base {

	public static JSONArray data = null;
	

	List<WebElement> lis = null;

	By Location = By.xpath("//input[@placeholder='Search location']");

	By place = By.xpath("//div[text()='Bangalore']");

	By hospital = By.xpath("//input[@data-input-box-id='omni-searchbox-keyword']");

	By search = By.xpath("//div[text()='Hospital']");

	By CB24x7 = By.xpath("//div[@data-qa-id='Open_24X7_checkbox']");

	By Filters = By.xpath("//span[text()='All Filters']");

	By Parking = By.xpath("//span[text()='Has Parking']");

	By Ratings = By.xpath("//span[@class='common__star-rating__value']");

	By HNames = By.xpath("//*[@data-qa-id='hospital_name']");

	By total = By.xpath("//span[@data-qa-id='results_count']");

	public JSONArray getDataFromJSON() {

		try {

			JSONParser parser = new JSONParser();

			FileReader reader = new FileReader(

					System.getProperty("user.dir") + "\\src\\test\\resources\\InputDetails.json");

			Object obj = parser.parse(reader);

			JSONObject obj1 = (JSONObject) obj;

			JSONArray data1 = (JSONArray) obj1.get("Data");

			data = data1;

		} catch (Exception e) {

			reportFail("Error reading JSON file");

		}

		return data;

	}

	public void getHospitalNames() throws InterruptedException {

		logger = report.createTest("Finding Hospital Names");

// get data from JSON

		data = getDataFromJSON();

// To select the place as Banglore

		try {

			WebElement location = findElement(Location);

			location.clear();

// to get location from json file

			JSONObject data1 = (JSONObject) data.get(0);

			String locationFromJSON = (String) data1.get("Location");

			location.sendKeys(locationFromJSON);

			wait = new WebDriverWait(driver, 15);

			wait.until(ExpectedConditions.visibilityOfElementLocated(place));

			findElement(place).click();

			reportPass("Bangalore is selected Successfully");

		} catch (Exception e) {

			reportFail(e.getMessage());

		}

// To find Hospitals

		try {

// to get search data from json file

			JSONObject data2 = (JSONObject) data.get(0);

			String searchFromJSON = (String) data2.get("Search");

			findElement(hospital).sendKeys(searchFromJSON);

			wait.until(ExpectedConditions.visibilityOfElementLocated(search));

			findElement(search).click();

			reportPass("Hospital is selected Successfully");

		} catch (Exception e) {

			reportFail(e.getMessage());

		}


// To find the names of the hospitals with rating > 3.5

		try {

			System.out.println("*************************************************************");

			System.out.println(" Hospitals which are open for 24/7 :");

			System.out.println("*************************************************************");

			Screenshot("Hospitals");
			for (int p = 1; p <= 4; p++) {

				String s = String.format(

						"(//div[@class=\'c-estb-card\'])[%d]/div[2]/div[1]/a", p * 10);

				WebDriverWait wait = new WebDriverWait(driver, 30);

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(s)));

				JavascriptExecutor js = (JavascriptExecutor) driver;

				js.executeScript("window.scrollBy(0,1000)","");
				
				js.executeScript("window.scrollBy(0,1700)","");

			}
		} catch (Exception e) {

			reportFail(e.getMessage());

		}
		

		try {

			for (int p = 1; p < 30; p++) {

				String time = String.format("(//div[@class='c-estb-card'])[%d]/div[2]/div[1]/div/span[2]/span", p);

				WebElement ab = driver.findElement(By.xpath(time));

				String str2 = ab.getText();

				String str1 = "MON - SUN 00:00AM - 11:59PM";

				boolean retval1 = str2.equals(str1);

				if (retval1) {

					
					String ra = String.format("(//div[@class='c-estb-card'])[%d]/div[2]/div[1]/a", p);

					WebElement s = driver.findElement(By.xpath(ra));

					System.out.println(s.getText());

				}

			}
			reportPass("Open 24X7 is Successful"); 
			reportPass("All Hospitals are obtained successfully");
		}

		catch (Exception e) {

			reportFail(e.getMessage());

		}

	}

}
