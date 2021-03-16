package com.govtech.assignment;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.auto.support.BaseController;
import com.auto.support.TestDetails;
import com.auto.support.WebElementLibrary;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class OppenheimerGuiTest extends BaseController {

	public WebDriver driver = null;

	@Test(priority = 9)
	@TestDetails(description = "prerequisite- Rake Data base")
	public void test_rake_database() throws IOException, InterruptedException {
		RestAssured.defaultParser = Parser.JSON;
		String response = RestAssured.given()
		.headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
		.when()
		.post(Constants.RAKE_DATABASE_API)
		.then()
		.contentType(ContentType.JSON)
		.statusCode(200)
		.extract().asString();
		
		Assert.assertEquals(response, "Successfully raked DB", "Record Insertion unsuccessful" );
	}

	@Test(priority = 10)
	@TestDetails(description = "As the Clerk, I should be able to upload a csv file to a portal so\r\n" + 
			"that I can populate the database from a UI")
	public void test_upload_csv_to_portal() throws IOException, InterruptedException {
		
		this.driver = getDriver();
		Assert.assertEquals(driver.getTitle(), "Technical Challenge for CDS",
				"Page Title Mismatch for default page");
		actionUtils.waitForPageLoadingJs(driver);
		actionUtils.scrollByVisibleElement(WebElementLibrary.UPLOAD_EMP_RECORDS_FILE, driver);
		String filePath = SystemUtils.getUserDir().getCanonicalPath() + Constants.CSV_FILE_PATH;
		actionUtils.uploadFile(WebElementLibrary.UPLOAD_EMP_RECORDS_FILE, driver, filePath);
		actionUtils.click(WebElementLibrary.REFRESH_TAX_RELIEF_TABLE_BUTTON, driver);
		actionUtils.waitForPageLoadingJs(driver);
		int size = actionUtils.getListSize(WebElementLibrary.EMP_TABLE_RECORDS_LIST, driver);
		Assert.assertTrue(size>0, "Employee records are not uploaded");
		
		Response response = doGetRequest(Constants.GET_TAX_RELIEF_LIST_API);
		List<String> jsonResponse = response.jsonPath().getList("$");
		
		Assert.assertTrue(jsonResponse.size()>0, "Employee records are not updated in DB");
	}
	
	private Response doGetRequest(String endpoint) {
		RestAssured.defaultParser = Parser.JSON;
		return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get(endpoint).then().contentType(ContentType.JSON).extract().response();
	}
	
	@Test(priority = 11)
	@TestDetails(description = "As the Governor, I should be able to see a button on the screen so\r\n" + 
			"that I can dispense tax relief for my working class heroes")
	public void test_dispense_button_validation() {
		
		this.driver = getDriver();
		Assert.assertEquals(driver.getTitle(), "Technical Challenge for CDS",
				"Page Title Mismatch for default page");
		actionUtils.waitForPageLoadingJs(driver);
		actionUtils.scrollByVisibleElement(WebElementLibrary.DISPENSE_NOW_BUTTON, driver);
		
		String dispenseButtonText = actionUtils.getInputValue(WebElementLibrary.DISPENSE_NOW_BUTTON, driver);
		Assert.assertEquals(dispenseButtonText, "Dispense Now", "Dispense button text mismatch !!");
		
		actionUtils.click(WebElementLibrary.DISPENSE_NOW_BUTTON, driver);
		actionUtils.waitForPageLoadingJs(driver);
		Assert.assertEquals(driver.getTitle(), "Dispense!!",
				"Page Title Mismatch for default page");
		String cashDispenseLabelText = actionUtils.getInputValue(WebElementLibrary.CASH_DISPENSE_LABEL, driver);
		Assert.assertEquals(cashDispenseLabelText, "Cash dispensed", "Dispense button text mismatch !!");
	}

}
