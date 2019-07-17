package com.nfdw.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelUtil<T> {

    private final static String excel2003 = ".xls"; // 2003- 版本的excel
    private final static String excel2007 = ".xlsx"; // 2007+ 版本的excel

    public static List<List<Object>> getDataHeaderByExcel(InputStream in,
                                                          String fileName) throws Exception {
        List<List<Object>> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    List<Object> li = new ArrayList<Object>();
                    for (int y = row.getFirstCellNum(); y < row
                            .getLastCellNum(); y++) {
                        cell = row.getCell(y);
                        if (cell != null) {
                            li.add(getCellValue(cell));
                        }
                    }
                    list.add(li);
                    break;
                }
            }
        }
        work.close();
        return list;
    }

    /**
     * Excel格式校验
     *
     * @return
     * @throws Exception
     */
    public static String checkExcelFormat(String fileName) throws Exception {
        String message = "";
        if (StringUtils.isBlank(fileName)) {
            message = "error:导入文档为空!";
        } else if (fileName.toLowerCase().endsWith("xls")) {
            message = "success";
        } else if (fileName.toLowerCase().endsWith("xlsx")) {
            message = "success";
        } else {
            message = "error:文档格式不正确!";
        }
        return message;
    }

    /**
     *
     * @param in
     * @param fileName
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @return
     * @throws Exception
     */
    public static List<Object> getDataHeaderByHeaderNum(InputStream in,
                                                        String fileName, int headerNum) throws Exception {
        List<Object> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        sheet = work.getSheetAt(0);
        if (null != sheet) {
            row = sheet.getRow(headerNum);
            list = new ArrayList<Object>();
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                if (cell != null) {
                    list.add(getCellValue(cell));
                }
            }
        }
        work.close();
        return list;
    }

    /**
     * @Desc Excel数据存储格式转换
     * @param headerData
     *            标题数据 数据列表格式：List<Object>
     * @param excelDatas
     *            Excel数据列表 格式为: List<Map<key=行号,value=List<Object>>>
     * @return 返回格式为：Map<key=行号,value=Map<key=标题,value=单元格数据>>
     * @throws Exception
     */
    public static Map<String, Map<String, String>> excelDataConvert(
            List<Object> headerData, List<Map> excelDatas) throws Exception {
        Map<String, Map<String, String>> resultMap = null;
        if ((null != excelDatas) && (excelDatas.size() > 0)
                && (null != headerData) && (headerData.size() > 0)) {
            resultMap = new LinkedHashMap<String, Map<String, String>>();

            for (Map<Integer, List> curMap : excelDatas) {
                if (null != curMap) {
                    for (Map.Entry<Integer, List> curEntry : curMap.entrySet()) {
                        String rowNum = String.valueOf(curEntry.getKey());
                        Map<String, String> dataMap = null;
                        List<Object> dataLists = curEntry.getValue();
                        if ((null != dataLists) && (dataLists.size() > 0)
                                && (headerData.size() == dataLists.size())) {
                            dataMap = new LinkedHashMap<String, String>();
                            for (int i = 0; i < dataLists.size(); i++) {
                                String cellName = String.valueOf(headerData
                                        .get(i));
                                String cellValue = String.valueOf(dataLists
                                        .get(i));
                                dataMap.put(cellName, cellValue);
                            }
                        }
                        if (null != dataMap) {
                            resultMap.put(rowNum, dataMap);
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    public static List<List<Object>> getDataListByExcel(InputStream in,
                                                        String fileName, int columnNum) throws Exception {
        List<List<Object>> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 遍历当前sheet中的所有行
            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                row = sheet.getRow(j);
                if (row == null || j == 0) {
                    continue;
                }
                // 遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = 0; y < columnNum; y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        li.add("");
                    } else {
                        li.add(getCellValue(cell));
                    }
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    /**
     *
     * @param in
     * @param fileName
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param columnNum
     * @return
     * @throws Exception
     */
    public static List<Map> getDataListByExcelHeaderNum(InputStream in,
                                                        String fileName, int headerNum, int columnNum) throws Exception {
        List<Map> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<Map>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            Map workTabMap = new LinkedHashMap();
            // 遍历当前sheet中的所有行
            for (int j = (headerNum + 1); j < (sheet.getLastRowNum() + 1); j++) {
                row = sheet.getRow(j);
                if (row == null || j == 0) {
                    continue;
                }

                // 遍历列
                List<Object> li = new ArrayList<Object>();
                for (int y = 0; y < columnNum; y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        li.add("");
                    } else {
                        li.add(getCellValue(cell));
                    }
                }
                workTabMap.put((j + 1), li);// map<k=行号,v=每行结果列表>
            }
            list.add(workTabMap);
        }
        work.close();
        return list;
    }

    public static boolean isEmptyRow(Row row) {
        boolean isEmptyRow = true;
        for (int cellNum = row.getFirstCellNum(); cellNum < row
                .getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
                    && StringUtils.isNotBlank(cell.toString())) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }

    /**
     *
     * @param in
     * @param fileName
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @param columnNum
     * @return
     * @throws Exception
     */
    public static List<Map> getDataListByExcelHeaderNum2(InputStream in,
                                                         String fileName, int headerNum, int columnNum) throws Exception {
        List<Map> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = work.getSheetAt(0);
        Row row = null;
        Cell cell = null;
        list = new ArrayList<Map>();
        Map workTabMap = new LinkedHashMap();
        int beginRowNum = (headerNum + 1);
        int endRowNum = (headerNum + 2);
        for (int j = (sheet.getLastRowNum() + 1); j > beginRowNum; j--) {
            row = sheet.getRow(j);
            if (row == null || j == 0) {
                continue;
            } else {
                boolean isEmptyRowB = isEmptyRow(row);
                if (!isEmptyRowB) {
                    endRowNum = (row.getRowNum() + 1);
                    break;
                }
            }

        }
        // 遍历当前sheet中的所有行
        for (int j = beginRowNum; j < endRowNum; j++) {
            row = sheet.getRow(j);
            if (row == null || j == 0) {
                continue;
            }

            // 遍历列
            List<Object> li = new ArrayList<Object>();
            for (int y = 0; y < columnNum; y++) {
                cell = row.getCell(y);
                if (cell == null) {
                    li.add("");
                } else {
                    li.add(getCellValue(cell));
                }
            }
            workTabMap.put((j + 1), li);// map<k=行号,v=每行结果列表>
        }
        list.add(workTabMap);
        work.close();
        return list;
    }

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     *
     * @param in
     *            ,fileName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> getDataListByExcel(InputStream in,
                                                        String fileName) throws Exception {
        List<List<Object>> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 遍历当前sheet中的所有行
            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                row = sheet.getRow(j);
                if (row == null || j == 0) {
                    continue;
                }
                // 遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = 0; y < row.getPhysicalNumberOfCells(); y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        li.add("");
                    } else {
                        li.add(getCellValue(cell));
                    }
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    /**
     *
     * @param in
     * @param fileName
     * @param headerNum
     *            标题行号，数据行号=标题行号+1
     * @return
     * @throws Exception
     */
    public static List<List<Object>> getDataListByExcelHeaderNum(
            InputStream in, String fileName, int headerNum) throws Exception {
        List<List<Object>> list = null;
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 遍历当前sheet中的所有行
            for (int j = (headerNum + 1); j < sheet.getPhysicalNumberOfRows(); j++) {
                row = sheet.getRow(j);
                if (row == null || j == 0) {
                    continue;
                }
                // 遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = 0; y < row.getPhysicalNumberOfCells(); y++) {
                    cell = row.getCell(y);
                    if (cell == null) {
                        li.add("");
                    } else {
                        li.add(getCellValue(cell));
                    }
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr
     *            ,fileName
     * @return
     * @throws Exception
     */
    private static Workbook getWorkbook(InputStream inStr, String fileName)
            throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003.equals(fileType)) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if (excel2007.equals(fileType)) {
            wb = new XSSFWorkbook(inStr); // 2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        Object value = null;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        short format = cell.getCellStyle().getDataFormat();
                        String formatString = cell.getCellStyle()
                                .getDataFormatString();
                        if (format > 0) {
                            System.out.println("format: " + format
                                    + "　formatString: " + formatString);
                        }
                        if (format == 14 || format == 31 || format == 57
                                || format == 58 || (format >= 176 && format <= 183)) {
                            // 格式化日期
                            value = formatDate(date, "yyyy-MM-dd");
                        } else if (format == 20 || format == 32
                                || (format >= 184 && format <= 187)) {
                            // 格式化时间
                            value = formatDate(date, "HH:mm");
                        }
                    } else {
                        value = "";
                    }
                } else {
                    // 格式化数据
                    double doubleVal = cell.getNumericCellValue();
                    DecimalFormat format = new DecimalFormat();
                    // 单元格设置成常规
                    if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                        format.applyPattern("#");
                        value = format.format(doubleVal);
                    } else {
                        format.applyPattern("0");
                        value = format.format(doubleVal);
                    }
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /*
     * 不带序号导出数据
     */
    public static void exportWithoutSeq(String title, List<String> rowsHeader,
                                        List<Map<String, Object>> dataSet, OutputStream out)
            throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
            HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
            HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowsHeader
                    .size() - 1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowsHeader.size();
            HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                String headJson = rowsHeader.get(n);
                Map<String, Object> head = JSONUtils.jsonToMap(headJson);
                String headName = (String) head.get("name");
                HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(headName);
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataSet.size(); i++) {

                Map<String, Object> data = dataSet.get(i);// 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数

                for (int j = 0; j < rowsHeader.size(); j++) {
                    String headStr = rowsHeader.get(j);
                    Map<String, Object> head = JSONUtils.jsonToMap(headStr);
                    String headKey = (String) head.get("key");
                    Object value = data.get(headKey);
                    HSSFCell cell = null; // 设置单元格的数据类型
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (value != null && !"".equals(value)) {
                        cell.setCellValue(value.toString()); // 设置单元格的值
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }

                /*
                 * Set<String> keys = data.keySet(); Iterator<String> it =
                 * keys.iterator(); int j=0; while(it.hasNext()){ String key =
                 * it.next(); Object value = data.get(key); HSSFCell cell =
                 * null; // 设置单元格的数据类型 if(j==0){ cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); }else{
                 * cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (value != null && !"".equals(value)) {
                 * cell.setCellValue(value.toString()); // 设置单元格的值 }else{
                 * cell.setCellValue(""); } } cell.setCellStyle(style); //
                 * 设置单元格样式 j++; }
                 */

                /*
                 * for (int j = 0; j < obj.size(); j++) { HSSFCell cell = null;
                 * // 设置单元格的数据类型 if (j == 0) { cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); } else
                 * { cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (!"".equals(obj.get(j)) && obj.get(j) != null) {
                 * cell.setCellValue(obj.get(j).toString()); // 设置单元格的值 } }
                 * cell.setCellStyle(style); // 设置单元格样式 }
                 */
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
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

            if (workbook != null) {
                try {
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 导出大量数据
     */
    public static void exportSXSSF(String title, List<String> rowsHeader,
                                   List<Map<String, Object>> dataSet, OutputStream out)
            throws Exception {
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(); // 创建工作簿对象
//            workbook.setForceFormulaRecalculation(true);
            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(title); // 创建工作表

            sheet.setRandomAccessWindowSize(-1);
            // 产生表格标题行
            Row rowm = sheet.createRow(0);
            Cell cellTiltle = rowm.createCell(0);

            // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
//            SXSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
//            XSSFCellStyle style = getStyle(workbook); // 单元格样式对象

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowsHeader
                    .size() - 1)));
//            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowsHeader.size();
            Row rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                String headJson = rowsHeader.get(n);
                Map<String, Object> head = JSONUtils.jsonToMap(headJson);
                String headName = (String) head.get("name");
                Cell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                XSSFRichTextString text = new XSSFRichTextString(headName);
                cellRowName.setCellValue(text); // 设置列头单元格的值
//                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataSet.size(); i++) {

                Map<String, Object> data = dataSet.get(i);// 遍历每个对象
                Row row = sheet.createRow(i + 3);// 创建所需的行数

                for (int j = 0; j < rowsHeader.size(); j++) {
                    String headStr = rowsHeader.get(j);
                    Map<String, Object> head = JSONUtils.jsonToMap(headStr);
                    String headKey = (String) head.get("key");
                    Object value = data.get(headKey);
                    Cell cell = null; // 设置单元格的数据类型
                    if (j == 0) {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (value != null && !"".equals(value)) {
                            cell.setCellValue(value.toString()); // 设置单元格的值
                        } else {
                            cell.setCellValue("");
                        }
                    }
//                    cell.setCellStyle(style); // 设置单元格样式
                }

                /*
                 * Set<String> keys = data.keySet(); Iterator<String> it =
                 * keys.iterator(); int j=0; while(it.hasNext()){ String key =
                 * it.next(); Object value = data.get(key); HSSFCell cell =
                 * null; // 设置单元格的数据类型 if(j==0){ cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); }else{
                 * cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (value != null && !"".equals(value)) {
                 * cell.setCellValue(value.toString()); // 设置单元格的值 }else{
                 * cell.setCellValue(""); } } cell.setCellStyle(style); //
                 * 设置单元格样式 j++; }
                 */

                /*
                 * for (int j = 0; j < obj.size(); j++) { HSSFCell cell = null;
                 * // 设置单元格的数据类型 if (j == 0) { cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); } else
                 * { cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (!"".equals(obj.get(j)) && obj.get(j) != null) {
                 * cell.setCellValue(obj.get(j).toString()); // 设置单元格的值 } }
                 * cell.setCellStyle(style); // 设置单元格样式 }
                 */
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    Row currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        Cell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
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

            if (workbook != null) {
                try {
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 导出数据
     */
    public static void export(String title, List<String> rowsHeader,
                              List<Map<String, Object>> dataSet, OutputStream out)
            throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
            HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
            HSSFCellStyle style = getStyle(workbook); // 单元格样式对象

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowsHeader
                    .size() - 1)));
            cellTiltle.setCellStyle(columnTopStyle);
            cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowsHeader.size();
            HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                String headJson = rowsHeader.get(n);
                Map<String, Object> head = JSONUtils.jsonToMap(headJson);
                String headName = (String) head.get("name");
                HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(headName);
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataSet.size(); i++) {

                Map<String, Object> data = dataSet.get(i);// 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数

                for (int j = 0; j < rowsHeader.size(); j++) {
                    String headStr = rowsHeader.get(j);
                    Map<String, Object> head = JSONUtils.jsonToMap(headStr);
                    String headKey = (String) head.get("key");
                    Object value = data.get(headKey);
                    HSSFCell cell = null; // 设置单元格的数据类型
                    if (j == 0) {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                        if (value != null && !"".equals(value)) {
                            cell.setCellValue(value.toString()); // 设置单元格的值
                        } else {
                            cell.setCellValue("");
                        }
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }

                /*
                 * Set<String> keys = data.keySet(); Iterator<String> it =
                 * keys.iterator(); int j=0; while(it.hasNext()){ String key =
                 * it.next(); Object value = data.get(key); HSSFCell cell =
                 * null; // 设置单元格的数据类型 if(j==0){ cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); }else{
                 * cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (value != null && !"".equals(value)) {
                 * cell.setCellValue(value.toString()); // 设置单元格的值 }else{
                 * cell.setCellValue(""); } } cell.setCellStyle(style); //
                 * 设置单元格样式 j++; }
                 */

                /*
                 * for (int j = 0; j < obj.size(); j++) { HSSFCell cell = null;
                 * // 设置单元格的数据类型 if (j == 0) { cell = row.createCell(j,
                 * HSSFCell.CELL_TYPE_NUMERIC); cell.setCellValue(i + 1); } else
                 * { cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); if
                 * (!"".equals(obj.get(j)) && obj.get(j) != null) {
                 * cell.setCellValue(obj.get(j).toString()); // 设置单元格的值 } }
                 * cell.setCellStyle(style); // 设置单元格样式 }
                 */
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
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

            if (workbook != null) {
                try {
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 列头单元格样式
     */
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗

        //2019.01.17
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        //2019-01-17
        //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        //2019-01-17
        //style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        //2019-01-17
        //style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        //2019-01-17
        //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        //2019-01-17
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        //2019-01-17
        //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    private static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        //2019-01-17
        //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        //2019-01-17
        //style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        //2019-01-17
        //style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        //2019-01-17
        //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        //2019-01-17
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        //2019-01-17
        //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    @SuppressWarnings("deprecation")
    private static String formatDate(Date d, String sdf) {
        String value = null;

        if (d.getSeconds() == 0 && d.getMinutes() == 0 && d.getHours() == 0) {
            value = DateUtils.convertDateToStr(d, "yyyy-MM-dd");
        } else {
            value = DateUtils.convertDateToStr(d, sdf);

        }

        return value;
    }

}
