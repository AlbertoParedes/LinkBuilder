package Classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Objects.Enlace;
import Objects.Oferta;

public class ReadFactura {
	
	
	
	public ReadFactura() {
		super();
	}

	public ArrayList<Oferta> readExcel(HttpServletRequest request) throws IOException, ServletException {
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
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

			String enlace = "";
			int precio=0;
			String numFactura = "";
			Date fecha = null;
			String medio = "";
			int cantidad = 0;
			String iva = "";
			double total = 0;
			double precioUnidad=0;
			boolean lineaAcabada = false;
			
			while (cellIterator.hasNext()){
				celda = cellIterator.next();
				
				
				
				if(row.getRowNum()>0) {
					if(celda.getColumnIndex()==0) {
						enlace = celda.getStringCellValue();
						//System.out.print(celda.getStringCellValue());
						System.out.print(enlace);
					}else if(celda.getColumnIndex()==1) {
						//System.out.print(" | "+celda.getStringCellValue());
						precio = (int) celda.getNumericCellValue(); 
						System.out.println("   ->   "+precio);
						lineaAcabada=true;
					}/*else if(celda.getColumnIndex()==2) {//enlace
						enlace = celda.getStringCellValue();
					}else if(celda.getColumnIndex()==3) {//numero de factura
						numFactura = celda.getStringCellValue();
					}else if(celda.getColumnIndex()==4) {//fecha
						fecha = new java.sql.Date(celda.getDateCellValue().getTime());
					}else if(celda.getColumnIndex()==5) {//url
						medio = celda.getStringCellValue();
					}else if(celda.getColumnIndex()==6) {//cantidad
						try {
							Double d = celda.getNumericCellValue();
							cantidad = d.intValue();
						} catch (Exception e) {
							Double d = Double.parseDouble(celda.getStringCellValue().replace(",", "."));
							cantidad = d.intValue();
						}
					}else if(celda.getColumnIndex()==7) {//precio por unidad	
					}else if(celda.getColumnIndex()==8) {//descuento 
						//try {System.out.print(" | "+celda.getNumericCellValue());} catch (Exception e) {}
					}else if(celda.getColumnIndex()==9) {
						try {
							iva = celda.getNumericCellValue()+"";
						} catch (Exception e) {
							iva = celda.getStringCellValue();
						}
						
					}else if(celda.getColumnIndex()==10) {
						try {
							total = celda.getNumericCellValue();
						} catch (Exception e) {
							total = Double.parseDouble(celda.getStringCellValue().replace(",", "."));
						}
						lineaAcabada=true;
					}*/
					
				}
				if(lineaAcabada) {
					//precioUnidad = total / cantidad;
					
					
					
					ofertas.add(new Oferta(enlace, precio));
					enlace = ""; numFactura = ""; fecha = null; medio = ""; cantidad = 0; iva = ""; total = 0; precioUnidad=0; lineaAcabada=false;
				
				}
				

			}
			

		}
		workbook.close();
		// cerramos el libro excel
		is.close();
		return ofertas;
	}

}
