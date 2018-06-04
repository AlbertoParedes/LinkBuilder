package Classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFactura {
	
	
	
	public ReadFactura() {
		super();
	}

	public ArrayList<String> readExcel(HttpServletRequest request) throws IOException, ServletException {
		ArrayList<String> ls = new ArrayList<String>();
		Part archivo = request.getPart("excelFactura");
		InputStream is = archivo.getInputStream();
		
		
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = sheet.iterator();
		Row row;

		// Recorremos todas las filas para mostrar el contenido de cada celda
		while (rowIterator.hasNext()){

			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			Cell celda;

			while (cellIterator.hasNext()){
				celda = cellIterator.next();
				//ls.add(celda.getRichStringCellValue().getString());
				
				if(row.getRowNum()>1) {
					if(celda.getColumnIndex()==0) {
						System.out.print(celda.getStringCellValue());
					}else if(celda.getColumnIndex()==1) {
						System.out.print(" | "+celda.getStringCellValue());
					}else if(celda.getColumnIndex()==2) {
						System.out.print(" | "+celda.getStringCellValue());
					}else if(celda.getColumnIndex()==3) {
						System.out.print(" | "+celda.getStringCellValue());
					}else if(celda.getColumnIndex()==4) {
						System.out.print(" | "+celda.getDateCellValue());
					}else if(celda.getColumnIndex()==5) {
						System.out.print(" | "+celda.getStringCellValue());
					}else if(celda.getColumnIndex()==6) {
						try {
							System.out.print(" | "+celda.getNumericCellValue());
						} catch (Exception e) {
							System.out.print(" | "+celda.getStringCellValue());
						}
					}else if(celda.getColumnIndex()==7) {
						try {
							System.out.print(" | "+celda.getNumericCellValue());
						} catch (Exception e) {
							System.out.print(" | "+celda.getStringCellValue());
						}
					}else if(celda.getColumnIndex()==8) {
						try {System.out.print(" | "+celda.getNumericCellValue());} catch (Exception e) {}
					}else if(celda.getColumnIndex()==9) {
						try {
							System.out.print(" | "+celda.getNumericCellValue());
						} catch (Exception e) {
							System.out.print(" | "+celda.getStringCellValue());
						}
						
					}else if(celda.getColumnIndex()==10) {
						try {
							System.out.println(" | "+celda.getNumericCellValue());
						} catch (Exception e) {
							System.out.println(" | "+celda.getStringCellValue());
						}
					}
					
				}
				

			}
			

		}
		workbook.close();
		// cerramos el libro excel
		is.close();
		return ls;
	}

}
