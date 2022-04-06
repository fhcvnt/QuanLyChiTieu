package data_function;

import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import sqlite.Database;

public class Excel {

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void export(String path, Context context) {
        Database database = new Database(context, "QuanLyChiTieu", null, 3);
        ArrayList<String> arrayListdata = new ArrayList<>();
        String select = "SELECT GroupName.Id,GroupName.GroupName,GroupName.Type,GroupName.Icon,Note.Id,Note.Money,Note.Note,Note.Ngay,Note.Gio FROM GroupName,Note WHERE GroupName.Id=Note.GroupId";
        Cursor result = database.GetData(select);
        while (result.moveToNext()) {
            arrayListdata.add(result.getString(0));
            arrayListdata.add(result.getString(1));
            arrayListdata.add(result.getString(2));
            arrayListdata.add(result.getString(3));

            arrayListdata.add(result.getString(4));
            arrayListdata.add(result.getString(5));
            arrayListdata.add(result.getString(6));
            arrayListdata.add(result.getString(7));
            arrayListdata.add(result.getString(8));
        }

        String filename = "Quanlychitieu" + LocalDate.now().toString() + "." + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + ".xls";
        File directory = new File(path);
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            //file path
            File file = new File(directory, filename);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheetA first sheetA
            WritableSheet sheetA = workbook.createSheet("sheet A", 0);

            // chèn hàng tiêu đề
            sheetA.addCell(new Label(0, 0, "Mã nhóm"));
            sheetA.addCell(new Label(1, 0, "Tên nhóm"));
            sheetA.addCell(new Label(2, 0, "Loại"));
            sheetA.addCell(new Label(3, 0, "Hình ảnh"));
            sheetA.addCell(new Label(4, 0, "Mã ghi chú"));
            sheetA.addCell(new Label(5, 0, "Tiền"));
            sheetA.addCell(new Label(6, 0, "Ghi chú"));
            sheetA.addCell(new Label(7, 0, "Ngày"));
            sheetA.addCell(new Label(8, 0, "Giờ"));

            int row = 1;
            for (int i = 0; i < arrayListdata.size(); i = i + 9) {
                sheetA.addCell(new Label(0, row, arrayListdata.get(i)));
                sheetA.addCell(new Label(1, row, arrayListdata.get(i + 1)));
                sheetA.addCell(new Label(2, row, arrayListdata.get(i + 2)));
                sheetA.addCell(new Label(3, row, arrayListdata.get(i + 3)));
                sheetA.addCell(new Label(4, row, arrayListdata.get(i + 4)));
                sheetA.addCell(new Label(5, row, arrayListdata.get(i + 5)));
                sheetA.addCell(new Label(6, row, arrayListdata.get(i + 6)));
                sheetA.addCell(new Label(7, row, arrayListdata.get(i + 7)));
                sheetA.addCell(new Label(8, row, arrayListdata.get(i + 8)));
                row++;
            }

            workbook.write();
            workbook.close();

        } catch (Exception e) {

        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public static ArrayList<String> readExcelFileFromAssets(Context context, String pathfilename) {
//        ArrayList<String> arrayListdata = new ArrayList<>();
//        //pathfilename="/.../Quanlychitieu...xls"
//
//        try {
//            InputStream myInput;
//            // initialize asset manager
//            AssetManager assetManager = context.getAssets();
//            //  open excel sheet
//            myInput = assetManager.open(pathfilename);
//            // Create a POI File System object
//            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
//            // Create a workbook using the File System
//            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
//            // Get the first sheet from workbook
//            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
//            // We now need something to iterate through the cells.
//            Iterator<Row> rowIter = mySheet.rowIterator();
//            int rownumber = 0;
//            while (rowIter.hasNext()) {
//                HSSFRow myRow = (HSSFRow) rowIter.next();
//                if (rownumber != 0) {
//                    Iterator<Cell> cellIter = myRow.cellIterator();
//                    int colnumber = 0;
//                    String groupid = "", groupname = "", type = "", icon = "", noteid = "", money = "", note = "", date = "", hour = "";
//                    while (cellIter.hasNext()) {
//                        HSSFCell myCell = (HSSFCell) cellIter.next();
//                        if (colnumber == 0) {
//                            groupid = myCell.toString();
//                        } else if (colnumber == 1) {
//                            groupname = myCell.toString();
//                        } else if (colnumber == 2) {
//                            type = myCell.toString();
//                        } else if (colnumber == 3) {
//                            icon = myCell.toString();
//                        } else if (colnumber == 4) {
//                            noteid = myCell.toString();
//                        } else if (colnumber == 5) {
//                            money = myCell.toString();
//                        } else if (colnumber == 6) {
//                            note = myCell.toString();
//                        } else if (colnumber == 7) {
//                            date = myCell.toString();
//                        } else if (colnumber == 8) {
//                            hour = myCell.toString();
//                        }
//                        colnumber++;
//                    }
//
//                    arrayListdata.add(groupid);
//                    arrayListdata.add(groupname);
//                    arrayListdata.add(type);
//                    arrayListdata.add(icon);
//                    arrayListdata.add(noteid);
//                    arrayListdata.add(money);
//                    arrayListdata.add(note);
//                    arrayListdata.add(date);
//                    arrayListdata.add(hour);
//                }
//                rownumber++;
//            }
//        } catch (Exception e) {
//        }
//        Toast.makeText(context, "Size = " + arrayListdata.size(), Toast.LENGTH_SHORT).show();
//        return arrayListdata;
//    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<String> readExcel( String pathfilename) {
        ArrayList<String> arrayListdata = new ArrayList<>();
        //pathfilename="/.../Quanlychitieu...xls"

        try {
            InputStream is=new FileInputStream(new File(pathfilename));
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            int row = sheet.getRows();
            int col = sheet.getColumns();
            col=9;

            for (int i = 1; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    Cell cell = sheet.getCell(j,i);
                    arrayListdata.add(cell.getContents());
                }
            }
        } catch (Exception e) {
        }

        return arrayListdata;
    }
}
