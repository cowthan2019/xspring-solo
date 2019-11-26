package org.cp4j.core.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    public static List<AssocArray> read(File file){
        try {
            CSVParser parser = CSVParser.parse(file, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
//            Map<String, Integer> header = parser.getHeaderMap();
//            System.out.println("ddd---" + JsonUtils.toJsonPretty(header));



            List<AssocArray> result = new ArrayList<>();
            List<CSVRecord> list = parser.getRecords();
            AssocArray header = null;
            for (int i = 0; i < Lang.count(list); i++) {
                AssocArray r = AssocArray.array();
                CSVRecord row = list.get(i);
                for (int j = 0; j < row.size(); j++) {
                    if(i == 0){
                        r.add(j + "", row.get(j));
                        header = r;
                    }else{
                        r.add(header.getString(j + "", j + ""), row.get(j));
                    }
                }

                if(i == 0){

                }else{
                    result.add(r);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final static String NEW_LINE_SEPARATOR="\n";

    public static void write(String file, List<String> headers, List<List<Object>> data) throws IOException {
        CSVFormat formator = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        FileWriter fileWriter=new FileWriter(file);
        CSVPrinter printer=new CSVPrinter(fileWriter,formator);
        if(headers != null && headers.size() > 0){
            printer.printRecord(headers);
        }
        if(data != null && data.size() > 0){
            for (List<Object> r: data){
                printer.printRecord(r);
            }
        }
    }

    public static List<Object> parse(AssocArray row, List<String> headers){
        List<Object> row2 = new ArrayList<>();
        for (String key: headers){
            if(row.containsKey(key)){
                row2.add(row.get(key));
            }
        }
        return row2;
    }

    public static List<List<Object>> parse(List<AssocArray> rows, List<String> headers){
        List<List<Object>> rows2 = new ArrayList<>();
        for (AssocArray row: rows){
            rows2.add(parse(row, headers));
        }
        return rows2;
    }

    public static void write2(String file, List<String> headers, List<AssocArray> data) throws IOException {
        write(file, headers, parse(data, headers));
    }

    public static List<AssocArray> read(File file, int headerCount) throws IOException {
        CSVParser parser = CSVParser.parse(file, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        List<AssocArray> result = new ArrayList<>();
        List<CSVRecord> list = parser.getRecords();
        AssocArray header = AssocArray.array();
        for (int i = 0; i < Lang.count(list); i++) {
            AssocArray r = AssocArray.array();
            CSVRecord row = list.get(i);
            for (int j = 0; j < row.size(); j++) {
                if(i == 0 && headerCount == 1){
                    r.add(j + "", row.get(j));
                    header = r;
                    continue;
                }

                r.add(header.getString(j + "", j + ""), row.get(j));
            }

            if(i == 0){

            }else{
                result.add(r);
            }
        }
        return result;
    }

}
