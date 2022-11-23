package com.findmecore.findmecore;

import com.findmecore.findmecore.entity.Ability;
import com.findmecore.findmecore.entity.Employee;
import com.findmecore.findmecore.repo.AbilityRepository;
import com.findmecore.findmecore.repo.EmployeeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class FindMeCoreApplicationTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AbilityRepository abilityRepository;

	@Test
	void contextLoads() {
	}

	List<String> getPdfContentLines() {
		return Arrays.asList(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit",
				"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco",
				"laboris nisi ut aliquip ex ea commodo consequat.",
				"Duis aute irure dolor in reprehenderit in voluptate velit esse",
				"cillum dolore eu fugiat nulla pariatur.",
				"Excepteur sint occaecat cupidatat non proident",
				"sunt in culpa qui officia deserunt mollit anim id est laborum."
		);
	}

	@Test
	void TestPdf() throws IOException {

	}

}
