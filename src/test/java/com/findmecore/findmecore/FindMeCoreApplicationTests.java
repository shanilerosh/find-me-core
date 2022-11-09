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
		int topHeadingFont = 35;
		int mediumFont = 15;
		int smallFont = 8;

		try (PDDocument doc = new PDDocument()) {
			PDPage pdPage = new PDPage();
			doc.addPage(pdPage);

			try (PDPageContentStream contentStream = new PDPageContentStream(doc, pdPage)) {

				Employee employee = employeeRepository.findById(1L)
						.get();

				List<Ability> employeeSkills = abilityRepository.findAllByEmployee(employee);


				//left colored section
				contentStream.setNonStrokingColor(Color.DARK_GRAY);
				contentStream.addRect(0, 0, 200, 900);
				contentStream.fill();

				contentStream.beginText();


				//Side Bar Text Generation
				contentStream.setNonStrokingColor(Color.WHITE);
				contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
				contentStream.setLeading(14.5f);
				contentStream.newLineAtOffset(90, 700);

				contentStream.showText(employee.getName());

				contentStream.newLine();
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
				contentStream.showText("Contact Me At");

				contentStream.newLine();
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
				contentStream.setLeading(25f);

				//personal Data
				contentStream.showText(employee.getMobile());
				contentStream.newLine();
				contentStream.showText(employee.getAddress());
				contentStream.newLine();
				contentStream.showText(employee.getAddress());
				contentStream.newLine();
				contentStream.showText(employee.getEmail());
				contentStream.newLine();
				contentStream.showText(employee.getLinkedinProfile());

				//skills section
				contentStream.newLine();
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
				contentStream.showText("Skills");

				contentStream.endText();

				//Generate Skills Sections
//
				PDImageXObject pdImage = PDImageXObject.createFromFile("C:\\Users\\Admin\\Desktop\\C#\\star.png",doc);

				int originalStartInitX = 40;
				int originalStartInitY = 450;

				int starInitX = originalStartInitX;
				int starInitY = originalStartInitY;
				int horizontalIncrementor = 15;
				int startSpace = 15;

				for (Ability employeeSkill : employeeSkills) {
					//Side Bar Text Generation
					for (int i = 0; i < employeeSkill.getRating(); i++) {
						contentStream.drawImage(pdImage,starInitX, starInitY,10,10);
						starInitX += startSpace;
					}
					starInitX = originalStartInitX;
					starInitY-=horizontalIncrementor;
				}

				//Skill title set
				int skillIncrementorX = originalStartInitX+60;
				int skillIncrementorY = originalStartInitY;

				contentStream.beginText();

				contentStream.setNonStrokingColor(Color.WHITE);
				contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
				contentStream.newLineAtOffset(skillIncrementorX, skillIncrementorY);
				contentStream.setLeading(horizontalIncrementor);

				for (Ability employeeSkill : employeeSkills) {
					contentStream.showText(employeeSkill.getSkill().getSkillName());
					contentStream.newLine();
					skillIncrementorY-=horizontalIncrementor;
				}

				contentStream.newLine();
				contentStream.newLine();

				contentStream.moveTextPositionByAmount(-60, 0);


				contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
				contentStream.showText("Non Related Refrees");

				contentStream.newLine();
				contentStream.newLine();


				contentStream.showText(employee.getRef1());
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
				contentStream.showText(employee.getRef1Position());
				contentStream.newLine();
				contentStream.showText(employee.getRef1Address());
				contentStream.newLine();
				contentStream.showText(employee.getRef1Mobile());

				contentStream.newLine();
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
				contentStream.showText(employee.getRef1());
				contentStream.newLine();

				contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
				contentStream.showText(employee.getRef1Position());
				contentStream.newLine();
				contentStream.showText(employee.getRef1Address());
				contentStream.newLine();
				contentStream.showText(employee.getRef1Mobile());

				contentStream.endText();

				//Side Bar End


				//Profile Section
				contentStream.beginText();
				int startingProfileCordinateX = 220;
				int startingProfileCordinateY = 720;


				contentStream.setNonStrokingColor(Color.GRAY);
				contentStream.setFont(PDType1Font.TIMES_BOLD, 35);
				contentStream.setLeading(35f);
				contentStream.newLineAtOffset(startingProfileCordinateX, startingProfileCordinateY);

				//Main Heading Name
				contentStream.showText("Shanil Miranda");

				contentStream.newLine();

				contentStream.setNonStrokingColor(Color.GRAY);
				contentStream.setFont(PDType1Font.TIMES_BOLD, mediumFont);
				contentStream.showText("Personal Profile");

				contentStream.newLine();

				contentStream.setNonStrokingColor(Color.BLACK);
				contentStream.setFont(PDType1Font.COURIER, smallFont);
				contentStream.showText("I am hard working, methodical person who bears a good moral character. I can carry out the duties entrusted to me efficiently without any supervision.");



				contentStream.endText();

			}

			doc.save("C:\\Users\\Admin\\Desktop\\C#\\doc.pdf");
		} catch (IOException ioException) {
			System.out.println("Handling IOException...");
		}
	}

}
