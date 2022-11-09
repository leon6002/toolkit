package com.leo.toolkit.utils;


import com.microsoft.schemas.office.visio.x2012.main.CellType;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class Doc2DocxKit {

    //    /**
//     * 根据格式类型转换doc文件
//     *
//     * @param srcPath  doc path 源文件
//     * @param descPath the docx path 目标文件
//     * @param fmt      fmt 所转格式
//     * @return the file
//     * @throws Exception the exception
//     */
//    public static File convertDocFmt(String srcPath, String descPath) throws Exception {
//        long start = System.currentTimeMillis();
//
//        // 实例化ComThread线程与ActiveXComponent
//        ComThread.InitSTA();
//        ActiveXComponent app = new ActiveXComponent("Word.Application");
//        try {
//            // 文档隐藏时进行应用操作
//            app.setProperty("Visible", new Variant(false));
//            app.setProperty("DisplayAlerts", new Variant(false));
//            // 实例化模板Document对象
//            Dispatch document = app.getProperty("Documents").toDispatch();
//            // 打开Document进行另存为操作
//            Dispatch doc = Dispatch.invoke(document, "Open", Dispatch.Method,
//                    new Object[]{srcPath, new Variant(true), new Variant(true)}, new int[1]).toDispatch();
//
//            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{descPath, new Variant(12)}, new int[1]);
//            Dispatch.call(doc, "Close", new Variant(false));
//            return new File(descPath);
//        } catch (Exception e){
//            throw e;
//        } finally {
//            long end = System.currentTimeMillis();
//            System.out.println("转换完成，用时："+ (end - start) +"ms");
//            // 释放线程与ActiveXComponent
//            app.invoke("Quit", new Variant[]{});
//            ComThread.Release();
//        }
//    }
//    public static void convert(String filePath) throws Docx4JException {
//        //将docx转换为符合doc格式规范的xml文档，再由xml更改后缀名为doc的方式达到docx转换doc格式的目的
//
//        File templateFile = new File(filePath);
//
//        WordprocessingMLPackage wordMLPackage = Docx4J.load(templateFile);
//
//        File outFile = new File(templateFile.getParent() + "/outxml/", "我的增量补丁整理软件.xml");
//
//        Docx4J.save(wordMLPackage, outFile, Docx4J.FLAG_SAVE_FLAT_XML);//FLAG_SAVE_ZIP_FILE
//
//        outFile.renameTo(new File(outFile.getParentFile(), "我的增量补丁整理软件.docx"));
//    }
//
//    public static void convert1(String filePath) throws IOException {
//        InputStream input = new FileInputStream(filePath);
//
//        HWPFDocument wordDocument = new HWPFDocument(input);
//        WordToHtmlConverter converter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
//
//    }


//    public static void html2docx(String htmlPath) throws JAXBException {
//        // XHTML to docx
//        String stringFromFile = FileUtils.readFileToString(new File(htmlPath), "UTF-8");
//
//
//        WordprocessingMLPackage docxOut = WordprocessingMLPackage.createPackage();
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        docxOut.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
//
//        XHTMLImporterImpl XHTMLImporterImpl = new XHTMLImporterImpl(docxOut);
//        XHTMLImporter.setHyperlinkStyle("Hyperlink");
//
//        docxOut.getMainDocumentPart().getContent().addAll(
//                XHTMLImporter.convert(stringFromFile, baseURL) );
//
//        docxOut.save(new java.io.File(dir + "/DocxToXhtmlAndBack.docx") );
//
//    }

    public static void xls2xlsx(String outFn) throws IOException {
//        String inpFn = "/Users/apple/Documents/test_resources/海康入场人员信息test.xls";
        String inpFn = "/Users/apple/Documents/test_resources/工作簿3.xls";


        FileInputStream in = new FileInputStream(inpFn);
        try {
            Workbook wbIn = new HSSFWorkbook(in);
            File outF = new File(outFn);
            if (outF.exists()) {
                outF.delete();
            }
            HashMap<Integer, XSSFCellStyle> styleMap = new HashMap<>();
            Workbook wbOut = new XSSFWorkbook();
            int sheetCnt = wbIn.getNumberOfSheets();
            for (int i = 0; i < sheetCnt; i++) {
                Sheet sIn = wbIn.getSheetAt(i);
                Sheet sOut = wbOut.createSheet(sIn.getSheetName());
                sOut.setDefaultRowHeight(sIn.getDefaultRowHeight());
                sOut.setDefaultColumnWidth(sIn.getDefaultColumnWidth());
                copyPicture((HSSFSheet) sIn, (XSSFSheet) sOut);
                Iterator<Row> rowIt = sIn.rowIterator();
                while (rowIt.hasNext()) {
                    Row rowIn = rowIt.next();
                    Row rowOut = sOut.createRow(rowIn.getRowNum());
                    rowOut.setHeight(rowIn.getHeight());
                    Iterator<Cell> cellIt = rowIn.cellIterator();
                    while (cellIt.hasNext()) {
                        Cell cellIn = cellIt.next();
                        Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), cellIn.getCellTypeEnum());
                        if (cellIn.getRowIndex() == 0) {
                            sOut.setColumnWidth(cellIn.getColumnIndex(), sIn.getColumnWidth(cellIn.getColumnIndex()));
                        }
                        switch (cellIn.getCellTypeEnum()) {
                            case BLANK:
                                break;
                            case BOOLEAN:
                                cellOut.setCellValue(cellIn.getBooleanCellValue());
                                break;
                            case ERROR:
                                cellOut.setCellValue(cellIn.getErrorCellValue());
                                break;
                            case FORMULA:
                                cellOut.setCellFormula(cellIn.getCellFormula());
                                break;
                            case NUMERIC:
                                cellOut.setCellValue(cellIn.getNumericCellValue());
                                break;
                            case STRING:
                                cellOut.setCellValue(cellIn.getStringCellValue());
                                break;
                        }
                        CellStyle styleIn = cellIn.getCellStyle();
                        CellStyle styleOut = cellOut.getCellStyle();
                        Font fontIn = wbIn.getFontAt(styleIn.getFontIndex());
                        styleOut.setDataFormat(styleIn.getDataFormat());
                        cellOut.setCellComment(cellIn.getCellComment());
                        Integer hash = cellIn.getCellStyle().hashCode();
                        XSSFCellStyle cellStyle = styleMap.get(hash);
                        if (cellStyle == null) {
                            cellStyle = transformCellStyle(cellIn, wbOut, fontIn);
                            styleMap.put(hash, cellStyle);
                        }
                        cellOut.setCellStyle(cellStyle);
                    }
                }
            }
            FileOutputStream out = new FileOutputStream(outF);
            try {
                wbOut.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }

    }

    // 拷贝图片
    public static void copyPicture(HSSFSheet source, XSSFSheet destination) {
        // 获取sheet中的图片信息
        List<Map<String, Object>> mapList = getPicturesFromHSSFSheet(source);
        if (CollectionUtils.isEmpty(mapList)) {
            return;
        }
        XSSFDrawing drawing = destination.createDrawingPatriarch();

        for (Map<String, Object> pictureMap : mapList) {

            HSSFClientAnchor hssfClientAnchor = (HSSFClientAnchor) pictureMap.get("pictureAnchor");

            HSSFRow startRow = source.getRow(hssfClientAnchor.getRow1());
            float startRowHeight = startRow == null ? source.getDefaultRowHeightInPoints() : startRow.getHeightInPoints();

            HSSFRow endRow = source.getRow(hssfClientAnchor.getRow1());

            float endRowHeight = endRow == null ? source.getDefaultRowHeightInPoints() : endRow.getHeightInPoints();

            // hssf的单元格，每个单元格无论宽高，都被分为 宽 1024个单位 高 256个单位。
            // 32.00f 为默认的单元格单位宽度 单元格宽度 / 默认宽度 为像素宽度
            XSSFClientAnchor xssfClientAnchor = drawing.createAnchor(
                    (int) (source.getColumnWidth(hssfClientAnchor.getCol1()) / 32.00f
                            / 1024 * hssfClientAnchor.getDx1() * Units.EMU_PER_PIXEL),
                    (int) (startRowHeight / 256 * hssfClientAnchor.getDy1() * Units.EMU_PER_POINT),
                    (int) (source.getColumnWidth(hssfClientAnchor.getCol2()) / 32.00f
                            / 1024 * hssfClientAnchor.getDx2() * Units.EMU_PER_PIXEL),
                    (int) (endRowHeight / 256 * hssfClientAnchor.getDy2() * Units.EMU_PER_POINT),
                    hssfClientAnchor.getCol1(),
                    hssfClientAnchor.getRow1(),
                    hssfClientAnchor.getCol2(),
                    hssfClientAnchor.getRow2());
            xssfClientAnchor.setAnchorType(hssfClientAnchor.getAnchorType());

            drawing.createPicture(xssfClientAnchor,
                    destination.getWorkbook().addPicture((byte[]) pictureMap.get("pictureByteArray"),
                            Integer.parseInt(pictureMap.get("pictureType").toString())));

            System.out.println("imageInsert");
        }
    }

    /**
     * 获取图片和位置 (xls)
     */
    public static List<Map<String, Object>> getPicturesFromHSSFSheet(HSSFSheet sheet) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        HSSFPatriarch drawingPatriarch = sheet.getDrawingPatriarch();
        if (drawingPatriarch == null) {
            return null;
        }
        List<HSSFShape> list = drawingPatriarch.getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                Map<String, Object> map = new HashMap<>();
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = picture.getClientAnchor();
                HSSFPictureData pdata = picture.getPictureData();
                map.put("pictureAnchor", cAnchor);
                map.put("pictureByteArray", pdata.getData());
                map.put("pictureType", pdata.getPictureType());
                map.put("pictureSize", picture.getImageDimension());
                mapList.add(map);
            }
        }
        return mapList;
    }


    private static XSSFCellStyle transformCellStyle(Cell cellHssf, Workbook wbOut, Font fontIn) {
        XSSFCellStyle styleNew = (XSSFCellStyle) wbOut.createCellStyle();
        Font fontNew = wbOut.createFont();
        CellStyle styleOld = cellHssf.getCellStyle();
        styleNew.setAlignment(styleOld.getAlignmentEnum());
        styleNew.setBorderBottom(styleOld.getBorderBottomEnum());
        styleNew.setBorderLeft(styleOld.getBorderLeftEnum());
        styleNew.setBorderRight(styleOld.getBorderRightEnum());
        styleNew.setBorderTop(styleOld.getBorderTopEnum());
        styleNew.setDataFormat(styleOld.getDataFormat());
        styleNew.setFillPattern(styleOld.getFillPatternEnum());
        styleNew.setFillBackgroundColor(styleOld.getFillBackgroundColor());
        styleNew.setFillForegroundColor(styleOld.getFillForegroundColor());
        styleNew.setFont(transformFont(fontIn, fontNew));
        styleNew.setHidden(styleOld.getHidden());
        styleNew.setIndention(styleOld.getIndention());
        styleNew.setLocked(styleOld.getLocked());
        styleNew.setVerticalAlignment(styleOld.getVerticalAlignmentEnum());
        styleNew.setWrapText(styleOld.getWrapText());
        return styleNew;
    }

    private static Font transformFont(Font fontOld, Font fontNew) {
        fontNew.setBold(fontOld.getBold());
        fontNew.setCharSet(fontOld.getCharSet());
        fontNew.setColor(fontOld.getColor());
        fontNew.setFontName(fontOld.getFontName());
        fontNew.setFontHeight(fontOld.getFontHeight());
        fontNew.setItalic(fontOld.getItalic());
        fontNew.setStrikeout(fontOld.getStrikeout());
        fontNew.setTypeOffset(fontOld.getTypeOffset());
        fontNew.setUnderline(fontOld.getUnderline());
        return fontNew;
    }


    public static void main(String[] args) throws Exception {
        xls2xlsx("/Users/apple/Documents/outputs/converted_report33.xlsx");
    }
}
