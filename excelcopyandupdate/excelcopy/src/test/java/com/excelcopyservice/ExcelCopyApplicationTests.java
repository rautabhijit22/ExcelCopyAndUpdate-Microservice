package com.excelcopyservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ExcelCopyApplicationTests {

	@Test
	void testMainMethod() {
		assertDoesNotThrow(() -> ExcelCopyApplication.main(new String[] {}));
	}

}
