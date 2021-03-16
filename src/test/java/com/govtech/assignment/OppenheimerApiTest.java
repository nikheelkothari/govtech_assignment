package com.govtech.assignment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.auto.support.TestDetails;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class OppenheimerApiTest {
	
	public WebDriver driver = null;
	
	@Test(dataProvider="getHeroDetails", enabled = true, priority = 11)
	@TestDetails(description = "As the Clerk, I should be able to insert a single record of\r\n" + 
			"working class hero into database via an API")
	public void test_insert_single_record_of_working_class_hero(final HeroDetails heroDetails) {
		
		Map <String, String> map = new HashMap<String, String>();
		map.put("birthday", heroDetails.getBirthday());
		map.put("gender", heroDetails.getGender());
		map.put("name", heroDetails.getName());
		map.put("natid", heroDetails.getNatid());
		map.put("salary", heroDetails.getSalary());
		map.put("tax", heroDetails.getTax());
		
		JSONObject request = new JSONObject(map);
		
		String response = RestAssured.given().
		header("Content-Type", "application/json").
		contentType(ContentType.JSON).accept(ContentType.JSON).
		body(request.toJSONString()).
		when().
		post(Constants.INSERT_SINGLE_RECORD_API).
		then().
		statusCode(202).extract().asString();
		
		Assert.assertEquals(response, "Alright", "Record Insertion unsuccessful" );
		
	}
	
	// Data provider for different hero details
	@DataProvider (name = "getHeroDetails")
	public static synchronized Object[][] getHeroDetails() {

		HeroDetails hd1 = new HeroDetails();
		hd1.setBirthday("12121950");
		hd1.setGender("M");
		hd1.setName("Jon");
		hd1.setNatid("1000001");
		hd1.setSalary("5000");
		hd1.setTax("50");

		return new Object[][] { { hd1 } };
	}
	
	@Test(priority = 12, enabled = true)
	@TestDetails(description = "As the Clerk, I should be able to insert more than one working\r\n" + 
			"class hero into database via an API")
	public void test_insert_multiple_record_of_working_class_hero() {
		
		Map <String, String> map1 = new HashMap<String, String>();
		map1.put("birthday", "12121950");
		map1.put("gender", "M");
		map1.put("name", "Jon");
		map1.put("natid", "1000002");
		map1.put("salary", "5000");
		map1.put("tax", "50");
		JSONObject jsonObj1 = new JSONObject(map1);
		
		Map <String, String> map2 = new HashMap<String, String>();
		map2.put("birthday", "12121956");
		map2.put("gender", "F");
		map2.put("name", "Bon");
		map2.put("natid", "1000003");
		map2.put("salary", "3500");
		map2.put("tax", "35");
		JSONObject jsonObj2 = new JSONObject(map2);
		
		List<JSONObject> jsonArrayPayload = new ArrayList<>();
		jsonArrayPayload.add(jsonObj1);
		jsonArrayPayload.add(jsonObj2);
		
		String response = RestAssured.given().
		header("Content-Type", "application/json").
		contentType(ContentType.JSON).accept(ContentType.JSON).
		body(jsonArrayPayload).
		when().
		post(Constants.INSERT_MULTIPLE_RECORD_API).
		then().
		statusCode(202).extract().asString();
		
		Assert.assertEquals(response, "Alright", "Record Insertion unsuccessful" );
		
	}
	
	@Test(priority = 13)
	@TestDetails(description = "As the Bookkeeper, I should be able to query the amount of tax\r\n" + 
			"relief for each person in the database so that I can report the\r\n" + 
			"figures to my Bookkeeping Manager")
	public void test_query_amount_of_tax_relief_for_each_person() {
		
		// GET endpoint which returns a list consist of natid, tax relief amount and name
		Response response = doGetRequest(Constants.GET_TAX_RELIEF_LIST_API);
		List<String> jsonResponse = response.jsonPath().getList("$");
		
		ArrayList<Double> hdTxReliefList = calculateTaxRelief();
		
		for (int i = 0 ; i < jsonResponse.size() ; i ++) {
			String natid = response.jsonPath().getString("natid["+i+"]");
			String name = response.jsonPath().getString("name["+i+"]");
			String actualTaxRelief = response.jsonPath().getString("relief["+i+"]");
			
			// natid field must be masked from the 5th character onwards with dollar sign ‘$’
			String fifthCharOnwards = natid.substring(4, natid.length());
			Assert.assertTrue(allCharactersSame(fifthCharOnwards), "All fields in natId from the 5th character onwards are not masked with dollar sign ‘$’");
			
			// computation of the tax relief is using the formula
			// ((salary-taxpaid)*variable)+genderBonus;
			double expectedTaxRelief = 0000; // Initialize with default value
			if(i < hdTxReliefList.size()) {
				expectedTaxRelief = hdTxReliefList.get(i);
			}
			String expectedTaxReliefRoundOffVal = round(expectedTaxRelief);
			Assert.assertEquals(actualTaxRelief, expectedTaxReliefRoundOffVal, "1. Tax relief is wrongly calculated for- " + name);
			
			//If the calculated tax relief amount after subjecting to normal
			//rounding rule is more than 0.00 but less than 50.00, the final tax
			//relief amount should be 50.00
			if(expectedTaxRelief > 0.00 && expectedTaxRelief < 50.00) {
				expectedTaxRelief = (int) 50.00;
			}
			expectedTaxReliefRoundOffVal = round(expectedTaxRelief);
			Assert.assertEquals(actualTaxRelief, expectedTaxReliefRoundOffVal, "2. Tax relief is wrongly calculated for- " + name);
			
			
			//If the calculated tax relief amount before applying the normal
			//rounding rule gives a value with more than 2 decimal places, it should
			//be truncated at the second decimal place and then subjected to normal
			//rounding rule
			String roundOffTaxRelief = round(expectedTaxRelief);
			Assert.assertEquals(actualTaxRelief, roundOffTaxRelief, "3. Tax relief is wrongly calculated for- " + name);
		}
	}
	
	private ArrayList<Double> calculateTaxRelief() {
		ArrayList<Double> hdTaxReliefList = new ArrayList<>();
		String filePath = null;
		try {
			filePath = SystemUtils.getUserDir().getCanonicalPath() + Constants.CSV_FILE_PATH;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
			String[] lineInArray;
			int i = 0;
			while ((lineInArray = reader.readNext()) != null) {
				if(i > 0) {
					int salary = Integer.valueOf(lineInArray[3]);
					int taxPaid = Integer.valueOf(lineInArray[5]);
					// ((salary-taxpaid)*variable)+genderBonus;
					Double taxRelief = ((salary - taxPaid) * getVariableBasedOnAge(lineInArray[4])+getGenderBonus(lineInArray[2]));
					hdTaxReliefList.add(taxRelief);
				}
				i ++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return hdTaxReliefList;
	}
	
	private double getVariableBasedOnAge(String dob) {
		double variable = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("DDMMYYYY");
		Date d = null;
		try {
			d = sdf.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		LocalDate l1 = LocalDate.of(year, month, date);
		LocalDate now1 = LocalDate.now();
		Period diff1 = Period.between(l1, now1);
		
		if(diff1.getYears() <= 18) {
			variable = 1;
		}else if (diff1.getYears() <= 35) {
			variable = 0.8;
		}else if (diff1.getYears() <= 50) {
			variable = 0.5;
		}else if (diff1.getYears() <= 75) {
			variable = 0.367;
		}else if (diff1.getYears() <= 76) {
			variable = 0.05;
		}
		
		return variable;
	}
	
	private int getGenderBonus(String gender) {
		int bonus = 0;
		if(gender.equals("F")) {
			bonus = 500;
		}
		return bonus;
	}
	
	private Response doGetRequest(String endpoint) {
		RestAssured.defaultParser = Parser.JSON;
		return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
				.get(endpoint).then().contentType(ContentType.JSON).extract().response();
	}
	
	private boolean allCharactersSame(String s) {
		int n = s.length();
		for (int i = 1; i < n; i++)
			if (s.charAt(i) != '$') {
				return false;
			}
		return true;
	}
	
	private String round(double expectedTaxRelief) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(expectedTaxRelief);
	}
}
