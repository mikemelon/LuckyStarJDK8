package cn.lynu.lyq.luckystar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {

	public static void main(String[] args) throws Exception {
		File excelFile = new File(ExcelUtils.class.getResource("/students.xlsx").toURI());
        System.out.println(Arrays.toString(readNamesFromXLSX(excelFile)));
	}

	public static String[] readNamesFromXLSX(File file) throws Exception {
        return readNamesFromXLSX(new FileInputStream(file));
    }

	public static String[] readNamesFromXLSX(InputStream inputStream) throws Exception{
		Workbook wb2 = WorkbookFactory.create(inputStream);
        Sheet sheet = wb2.getSheetAt(0);
        List<String> nameList = new ArrayList<String>();
        
        int k=1;//从第2行开始读取
        while(true){
        	Row row = sheet.getRow(k); //第k+1行
        	if(row==null) break;
        	Cell cell = row.getCell(1); //第2列
        	if(cell==null) break;
        	String value = cell.getStringCellValue();
        	if(value==null || value.trim().equals("")) break;
//        	System.out.println("readValue=[" +value+"]");
        	nameList.add(value);
        	k++;
        }
        String[] names = new String[nameList.size()];
        nameList.toArray(names);
        return names;
	}

}
