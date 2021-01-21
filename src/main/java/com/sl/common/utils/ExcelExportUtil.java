package com.sl.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 导出Excel
 * @author liuyazhuang
 *
 * @param <T>
 */
public class ExcelExportUtil<T>{
	
		// 2007 版本以上 最大支持1048576行
		// 2003 版本 最大支持65536 行
	public  final static String  EXCEL_FILE_2003 = "2003";
	public  final static String  EXCEL_FILE_2007 = "2007";


	/**
	 * 导出Excel文件  下载Excel文件  小标题+数据
	 * @param dataList  列表数据
	 * @param titleName  第一行 标题名称
	 * @param rowKey   标题 对应的数据的key
	 * @param rowWidth   行宽
	 * @param sheetName  表格名称
	 * @return
	 * @throws Exception
	 */
	public static XSSFWorkbook exportTitleAndDataExcel(List<Map> dataList,String [] titleName,String [] rowKey,int rowWidth,String sheetName){
		XSSFWorkbook workbook = new XSSFWorkbook();  //新的Excel工作簿
		XSSFSheet sheet = workbook.createSheet(sheetName); // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
		XSSFRow row  = sheet.createRow(0); //第一行  标题列数据
		XSSFCell cell =null; //单元格
		XSSFCellStyle titleCellStyle =createTitleCellStyle(workbook);  //标题样式
		rowWidth=256*rowWidth+184;  //单元格真实列宽
		for(int i=0;i<titleName.length;i++){
			cell = row.createCell(i);  //在 第一列 创建单元格
			cell.setCellValue(titleName[i]);  //往单元格中放入数据
			cell.setCellStyle(titleCellStyle);
			sheet.setColumnWidth(i,rowWidth); //列宽
		}
		//新增数据  从第二行 到结束
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);  //list中的数据
			row = sheet.createRow(i+1);  //第二行开始--最后结束

			for(int j=0;j<rowKey.length;j++){   //每一行的数据
				String name=map.get(rowKey[j])==null?"":String.valueOf(map.get(rowKey[j]));
				row.createCell(j).setCellValue(name);
			}
		}
		return workbook;
	 }

	/**
	 * 标题样式
	 * @param workbook
	 * @return
	 */
	public static XSSFCellStyle createTitleCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle(); //设置样式
		cellStyle.setFillForegroundColor((short) 13);// 设置背景色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置背景色
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		XSSFFont font = workbook.createFont();  //设置字体
		font.setFontName("黑体");  //设置字体格式
		font.setFontHeightInPoints((short) 16);//设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示 如果需要加上
		cellStyle.setFont(font); //往样式中放入字体
		return cellStyle;
	}




	//创建下拉框
	private static void creatDropDownList(Sheet taskInfoSheet, DataValidationHelper helper, String[] list,
										  Integer firstRow, Integer lastRow, Integer firstCol, Integer lastCol) {
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		//设置下拉框数据
		DataValidationConstraint constraint = helper.createExplicitListConstraint(list);
		DataValidation dataValidation = helper.createValidation(constraint, addressList);
		//处理Excel兼容性问题
		if (dataValidation instanceof XSSFDataValidation) {
			dataValidation.setSuppressDropDownArrow(true);
			dataValidation.setShowErrorBox(true);
		} else {
			dataValidation.setSuppressDropDownArrow(false);
		}
		taskInfoSheet.addValidationData(dataValidation);
	}


	/**
	 * 给shell 添加日期格式限制   表格中数据有效性
	 * @param sheet  表格
	 * @param firstRow	开始行
	 * @param lastRow   结束行
	 * @param firstCol   开始列
	 * @param lastCol   结束列
	 * @return
	 */
	public static DataValidation setDate(XSSFSheet sheet,int firstRow,int lastRow,int firstCol,int lastCol ) {
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		//限制范围  以及格式
		XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createDateConstraint(XSSFDataValidationConstraint.OperatorType.BETWEEN,"1900-01-01",
				"5000-01-01", "yyyy-mm-dd");
		XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
		dataValidation.setSuppressDropDownArrow(false);
		//提示信息
		dataValidation.createPromptBox("输入提示", "请填写日期格式");
		// 设置输入错误提示信息
		dataValidation.createErrorBox("日期格式错误提示", "你输入的日期格式不符合'yyyy-mm-dd'格式规范，请重新输入！");
		//是否展示提示信息
		dataValidation.setShowPromptBox(true);
		//输入错误 不允许保存
		dataValidation.setShowErrorBox(true);

		return dataValidation;
	}
	//sheet.addValidationData(setDate(sheet,2,20,2,2));

}

