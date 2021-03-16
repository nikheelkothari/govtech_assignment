package com.auto.reporting;

import java.io.File;

import org.openqa.selenium.Platform;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * @author nikheelkumar
 * Extent report operations are done by this class
 * it also contains some other functionalities/methods like to create a report folder and support multiple OS etc.
 */

public class ExtentManager {

	private static ExtentReports extent;
	private static Platform platform;
	private static String reportFileName = "test-report.html";
	private static String macPath = System.getProperty("user.dir") + "/test-report";
	private static String windowsPath = System.getProperty("user.dir") + "\\test-report";
	private static String macReportFileLoc = macPath + "/" + reportFileName;
	private static String winReportFileLoc = windowsPath + "\\" + reportFileName;

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance();
		return extent;
	}

	// Create an extent report instance
	public static ExtentReports createInstance() {
		platform = getCurrentPlatform();
		String fileName = getReportFileLocation(platform);
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("Test Automation Report");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Test Automation Report");
		htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "\\report-config\\extent-config.xml");

		extent = new ExtentReports();
		extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
		extent.setSystemInfo("Excercise", "Java Automation Assignment");
		extent.attachReporter(htmlReporter);

		return extent;
	}

	// Select the extent report file location based on platform
	private static String getReportFileLocation(Platform platform) {
		String reportFileLocation = null;
		switch (platform) {
		case MAC:
			reportFileLocation = macReportFileLoc;
			createReportPath(macPath);
			System.out.println("ExtentReport Path for MAC: " + macPath + "\n");
			break;
		case WINDOWS:
			reportFileLocation = winReportFileLoc;
			createReportPath(windowsPath);
			System.out.println("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
			break;
		default:
			System.out.println("ExtentReport path has not been set! There is a problem!\n");
			break;
		}
		return reportFileLocation;
	}

	// Create the report path if it does not exist
	private static void createReportPath(String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
			} else {
			}
		} else {
		}
	}

	// Get current platform
	private static Platform getCurrentPlatform() {
		if (platform == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				platform = Platform.LINUX;
			} else if (operSys.contains("mac")) {
				platform = Platform.MAC;
			}
		}
		return platform;
	}

}
