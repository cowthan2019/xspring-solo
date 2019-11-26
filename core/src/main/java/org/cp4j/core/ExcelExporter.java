package org.cp4j.core;

import cn.afterturn.easypoi.excel.annotation.Excel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelExporter {

    public static abstract class RowParser<T>{
        public abstract  void parse(T t, int cellIndex, String cellName, CellWrapper cellWrapper, String cellType);
    }

    public static class GeneralCell{
        private int cellIndex;

        private String cellName;

        private String cellValue;

        private String cellType;

        public int getCellIndex() {
            return cellIndex;
        }

        public void setCellIndex(int cellIndex) {
            this.cellIndex = cellIndex;
        }

        public String getCellName() {
            return cellName;
        }

        public void setCellName(String cellName) {
            this.cellName = cellName;
        }

        public String getCellValue() {
            return cellValue;
        }

        public void setCellValue(String cellValue) {
            this.cellValue = cellValue;
        }

        public String getCellType() {
            return cellType;
        }

        public void setCellType(String cellType) {
            this.cellType = cellType;
        }
    }

    public static class GeneralRow{
        private List<GeneralCell> cells;

        public GeneralRow() {
            this.cells = new ArrayList<>();
        }

        public List<GeneralCell> getCells() {
            return cells;
        }

        public void setCells(List<GeneralCell> cells) {
            this.cells = cells;
        }
    }

    public static class GeneralRowParser extends RowParser<GeneralRow>{

        @Override
        public void parse(GeneralRow row, int cellIndex, String cellName, CellWrapper cellWrapper, String cellType) {
            GeneralCell cell = new GeneralCell();
            cell.setCellIndex(cellIndex);
            cell.setCellName(cellName);
            cell.setCellValue(cellWrapper.getStringValue());
            cell.setCellType(cellType);
            row.cells.add(cell);
        }
    }

    public static class CellWrapper{
        private Cell cell;

        public CellWrapper(Cell cell) {
            this.cell = cell;
        }

        public String getStringValue(){
            return getRawValue() + "";
        }

        public String getType(){
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    return "numberic";
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    return "string";
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    return "boolean";
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    return "formula";
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    return "blank";
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    return "error";
                default:
                    return "";
            }
        }

        public Object getRawValue(){
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    double v = cell.getNumericCellValue();
                    if(v - (long)(v) < 0.000001) return (long)v;
                    return cell.getNumericCellValue();
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    return cell.getStringCellValue();
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    return cell.getBooleanCellValue();
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    return cell.getCellFormula();
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    return "";
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    return "";
                default:
                    return "";
            }
        }
    }

    public static <T> List<T> export(File file, Class<T> clazz, int titleRowCount, RowParser<T> parser){
        Field[] fields = clazz.getFields();
        Field f;
        Map<String, Field> fieldMap = new HashMap<>();
        for(Field field: fields){
            Excel annotation = field.getAnnotation(Excel.class);
            if(annotation == null) continue;
            String columnName = annotation.name();
            if(Lang.isEmpty(columnName)) continue;
            fieldMap.put(columnName, field);
        }

        int headerRowIndex = titleRowCount;

        try {
            FileInputStream in = new FileInputStream(file);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int len;
            while((len = in.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }

            baos.flush();

            InputStream userIs = new ByteArrayInputStream(baos.toByteArray());
            Workbook book = WorkbookFactory.create(userIs);
            Sheet sheet = book.getSheetAt(0);

            List<T> list = new ArrayList<>();

//            Map<String, Integer> columnIndexMap = new HashMap<>();

            List<String> headers = new ArrayList<>();

            Iterator<Row> rows = sheet.rowIterator();
            int index = -1;
            while (rows.hasNext()){
                index++;
                Row row = rows.next();
                if(index < headerRowIndex) continue;

                //
                if(index == headerRowIndex){
                    //拿出header，及其下标
                    System.out.println(row.getFirstCellNum() + ", " + row.getLastCellNum());
                    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                        System.out.println(row.getCell(i).getStringCellValue());
//                        if(row.getCell(i) == null) break;
                        headers.add(row.getCell(i).getStringCellValue());
                    }
                } else {
                    //构造对象
                    T result = null;
                    try {
                        result = clazz.newInstance();

                        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                            Cell cell = row.getCell(i);
                            if(cell == null) continue;
                            CellWrapper wrapper =  new CellWrapper(cell);
                            parser.parse(result, i, headers.get(i), wrapper, wrapper.getType());
                        }
                        
                        list.add(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }

            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void test(String[] args){
        String filePath = "/Users/cowthan/Documents/车车数据导入0713.xlsx";
        List<GeneralRow> list = export(new File(filePath), GeneralRow.class, 0, new GeneralRowParser());
        System.out.println(JsonUtils.toJsonPretty(list));
    }
}
