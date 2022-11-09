package com.leo.toolkit.utils;

import org.apache.poi.hssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * apache poi 对excel的相关操作
 * 需导入的依赖如下
 * <dependency>
 * <groupId>org.apache.poi</groupId>
 * <artifactId>poi-ooxml</artifactId>
 * <version>${poi.version}</version>
 * </dependency>
 */
public class ExcelKit {
    /**
     * @param fileName 完整的文件名
     * @param headers  标题字符串数组
     * @param dataList 数据对象数组
     */
    public static void exportDataToExcel(String fileName, String[] headers, List<Object[]> dataList) {
        // 创建一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("表单导出");
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        //循环将标题放入到标题行对应的单元格
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生数据行
        Iterator<Object[]> it = dataList.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            // 从第1行开始创建
            row = sheet.createRow(index);
            // 获取到即将插入的这一行数据
            Object[] obj = it.next();
            for (short i = 0; i < obj.length; i++) {
                HSSFCell cell = row.createCell(i);
                Object value = obj[i];
                String textValue;
                //根据不同的数据类型设置单元格数据
                if (!"".equals(value) && value != null) {
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        textValue = sdf.format(date);
                        cell.setCellValue(textValue);
//                    } else if (value instanceof List) {
//                        cell.setCellValue(Joiner.on(",").join((List) value));
                    } else {
                        textValue = value.toString();
                        cell.setCellValue(textValue);
                    }
                } else {
                    cell.setCellValue("");
                }
            }
        }
        //设置单元格宽度
        for (int colNum = 0; colNum < headers.length; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue() != null
                                ? currentCell.getStringCellValue().getBytes().length : 10;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
        //输出文件
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            workbook.write(baos);
            workbook.close();// HSSFWorkbook关闭
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void testExport() {
        String fileName = "/Users/apple/Documents/test_" + System.currentTimeMillis() + ".xlsx";
        String[] headers = {
                "id", "标题", "sku"
        };
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{1, "测试标题1", "2022110111111"});
        data.add(new Object[]{2, "测试标题2", "2022110111112"});
        data.add(new Object[]{3, "测试标题3", "2022110111113"});
        data.add(new Object[]{3, "测试标题4", "2022110111113"});
        data.add(new Object[]{4, "测试标题5", "2022110111113"});
        data.add(new Object[]{5, "测试标题5", "2022110111113"});
        data.add(new Object[]{6, "测试标题5", "2022110111113"});
        data.add(new Object[]{7, "测试标题5", "2022110111113"});
        exportDataToExcel(fileName, headers, data);
    }


}
