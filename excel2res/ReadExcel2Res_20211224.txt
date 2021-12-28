package com.test.myapplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jancar.sdk.utils.Logcat;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 将翻译的excel文档中的翻译提取出来
 *
 * @author C
 */

public class ReadExcel2Res_20211224 extends Activity implements OnClickListener {

    Context mContext;
    Button btn1, btn2, btn3, btn4;
    TextView tv1, tv2;

    TextView btn_xls_path, btn_xml_path;
    EditText btn_sheet_index, btn_row_data, btn_col_data, btn_key_language, btn_col_stringname;

    // 翻译需求 （中翻英文、阿拉伯语、西班牙语）-译文.xlsx
    String xls_trans = "xls2trans.xls";
    // 英语、阿拉伯语、西班牙语
//	String xml_trans = "/storage/emulated/0/test_trans.txt";
//    String xml_trans = "test_trans.txt";
    String xml_trans = "strings.xml";
    private int SHEET_INDEX = 7; // 第几张表，从0开始
    private String KEY_LANGUAGE = "中文";
    private int ROW_DATA = -1; // string数据从哪一行开始
    private int COL_DATA = -1; // 导出哪一列
    private int COL_STRING_NAME = 1; // tag在第几列，从0开始
    private HashMap<String, String> mMapXls = new HashMap<>();

    final int MSG_UPDATE_PROGRESS = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    btn2.setText("己结束，点击开始");
                    break;
            }
        }
    };
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readexcel);
        mContext = this;

        btn_xls_path = findViewById(R.id.btn_xls_path);
        btn_xml_path = findViewById(R.id.btn_xml_path);
        String file_xls;
        File file = new File(Environment.getExternalStorageDirectory(), xls_trans);
        if (file.exists()) {
            file_xls = file.getPath();
            xls_trans = file_xls;
        } else {
            file_xls = "文件不存在：" + Environment.getExternalStorageDirectory() + File.separator + xls_trans;
        }
        xml_trans = Environment.getExternalStorageDirectory() + File.separator + xml_trans;
        Logcat.d("testss xls_trans: " + xls_trans);
        Logcat.d("testss xml_trans: " + xml_trans);
        btn_xls_path.setText(file_xls);
        btn_xml_path.setText(xml_trans);

        btn_sheet_index = findViewById(R.id.btn_sheet_index);
        btn_row_data = findViewById(R.id.btn_row_data);
        btn_col_data = findViewById(R.id.btn_col_data);
        btn_key_language = findViewById(R.id.btn_key_language);
        btn_col_stringname = findViewById(R.id.btn_col_stringname);
        if (SHEET_INDEX >= 0) {
            btn_sheet_index.setText(SHEET_INDEX + "");
        }
        if (ROW_DATA >= 0) {
            btn_row_data.setText(ROW_DATA + "");
        }
        if (COL_DATA >= 0) {
            btn_col_data.setText(COL_DATA + "");
        }
        if (COL_STRING_NAME >= 0) {
            btn_col_stringname.setText(COL_STRING_NAME + "");
        }
        if (!TextUtils.isEmpty(KEY_LANGUAGE)) {
            btn_key_language.setText(KEY_LANGUAGE);
        }

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn1.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
    }

    Runnable writeRunnable = new Runnable() {
        @Override
        public void run() {
            InputStream mInputStreamTxt = null;
            InputStreamReader mInputStreamReaderTxt = null;
            BufferedReader mBufferedReaderTxt = null;
            FileOutputStream mFileOutputStreamTxt = null;
            try {
                // txt
                // 创建或打开txt文件
                File file = new File(xml_trans);
                boolean txtExist = file.exists();
                if (!txtExist) {
                    file.createNewFile();
                }
                Logcat.d("txtExist: " + txtExist);
                boolean hasData = false;
                String strname = "", value, str2 = "";
                // RandomAccessFile raf = new RandomAccessFile(file, "rw");
                mInputStreamTxt = new FileInputStream(file);
                mInputStreamReaderTxt = new InputStreamReader(mInputStreamTxt);
                mBufferedReaderTxt = new BufferedReader(mInputStreamReaderTxt);
                int lineCount = 0;
                String str0 = "<string name=";
                String line = mBufferedReaderTxt.readLine();
                // line格式：    <string name="strname">value</string>
                Logcat.d(lineCount + " line: " + line);
                while (line != null) {
                    lineCount++;
                    if (line.trim().startsWith(str0)) {
                        hasData = true;
                        String[] strings = line.split("\"");
                        strname = strings[1];
                        value = mMapXls.get(strname);
                        str2 += strings[0] + "\"" + strname + "\">" + value + "</string>\n";
                    } else {
                        str2 += line + "\n";
                    }
                    line = mBufferedReaderTxt.readLine();
                    lineCount++;
//                    Logcat.d(lineCount + " line: " + line);
                }
                Logcat.d("hasData: " + hasData + " lineCount: " + lineCount);
                lineCount = 0;
                if (TextUtils.isEmpty(str2)) {
                    for (Map.Entry<String, String> obj : mMapXls.entrySet()) {
                        str2 += str0 + "\"" + obj.getKey() + "\">" + obj.getValue() + "</string>\n";
                        lineCount++;
                    }
                }
                // 有的翻译后面加了空格，这里去掉所有的结尾处的空格
                while (str2.contains(" </string>")
                        || str2.contains("\"> ")) {
                    str2 = str2.replaceAll(" </string>", "</string>");
                    str2 = str2.replaceAll("\"> ", "\">");
                }
                str2.replaceAll("<string name=\"\"></string>", "");
                mFileOutputStreamTxt = new FileOutputStream(file);
                mFileOutputStreamTxt.write(str2.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mInputStreamTxt.close();
                    mInputStreamReaderTxt.close();
                    mBufferedReaderTxt.close();
                    mFileOutputStreamTxt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void readExcel() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // xls
                Workbook book_orig = null;
                InputStream inputStream = null;
                ByteArrayOutputStream outputStream = null;
                File tmpFile = null;
                // 先读入缓存中，后面会用
                try {
                    tmpFile = new File(xls_trans);
                    inputStream = new FileInputStream(tmpFile);
                    outputStream = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // 打开原xls
                    book_orig = Workbook.getWorkbook(tmpFile);
                    book_orig.getNumberOfSheets();
                    // 获得第一个工作表对象
                    Sheet sheet = book_orig.getSheet(SHEET_INDEX);
                    int Rows = sheet.getRows();
                    int Cols = sheet.getColumns();

                    String str1 = "";
                    str1 += "当前工作表的名字:" + sheet.getName() + "\n";
                    str1 += "总行数:" + Rows + "\n";
                    str1 += "总列数:" + Cols + "\n";
                    Log.d("tt", "str1: " + str1);
                    if (!getRowAndCol(sheet)) {
                        Logcat.d("cannot find start position");
                        return;
                    }
                    for (int j = ROW_DATA; j < Rows; j++) {
                        String strname = sheet.getCell(COL_STRING_NAME, j).getContents();
                        String value = sheet.getCell(COL_DATA, j).getContents();
                        mMapXls.put(strname, value);
                    }
                    new Thread(writeRunnable).start();
                } catch (Exception e) {
                    Log.d("tt", "Exception readExcel");
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != book_orig) {
                            book_orig.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
                Logcat.d("------------end------------");
            }
        }).start();

    }

    private boolean getRowAndCol(Sheet sheet) {
        Logcat.d("COL_DATA: " + COL_DATA + " ROW_DATA: " + ROW_DATA + " KEY_LANGUAGE: " + KEY_LANGUAGE);
        int totalRows = sheet.getRows();
        int totalCols = sheet.getColumns();
        if (COL_DATA >= 0 && ROW_DATA >= 0) {
            // 有确切坐标
            if (COL_DATA >= totalCols || ROW_DATA >= totalRows) {
                COL_DATA = -1;
                ROW_DATA = -1;
            }
        } else if (TextUtils.isEmpty(KEY_LANGUAGE)) {
            // 坐标不全，须得有要查找的内容
        } else if (COL_DATA >= 0) {
            for (int j = 0; j < 3; ++j) { // 只看前3行
                String contents = sheet.getCell(COL_DATA, j).getContents();
                if (!TextUtils.isEmpty(contents) && contents.contains(KEY_LANGUAGE)) {
                    ROW_DATA = j + 2;
                    break;
                }
            }
        } else if (ROW_DATA >= 0) {
            for (int i = 0; i < totalCols; i++) {
                String contents = sheet.getCell(i, ROW_DATA).getContents();
                if (!TextUtils.isEmpty(contents) && contents.contains(KEY_LANGUAGE)) {
                    COL_DATA = i;
                    break;
                }
            }
        } else {
            for (int i = 0; i < totalCols; i++) {
                for (int j = 0; j < 3; ++j) { // 只看前3行
                    String contents = sheet.getCell(i, j).getContents();
                    if (!TextUtils.isEmpty(contents) && contents.contains(KEY_LANGUAGE)) {
                        COL_DATA = i;
                        ROW_DATA = j + 2;
                        break;
                    }
                }
                if (COL_DATA >= 0) { // 找到列了
                    break;
                }
            }
        }
        Logcat.d("COL_DATA: " + COL_DATA + " ROW_DATA: " + ROW_DATA + " KEY_LANGUAGE: " + KEY_LANGUAGE);
        return COL_DATA >= 0 && ROW_DATA >= 0;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                btn2.setText("进行中……");
                Logcat.d("------------start------------");
                Logcat.d("getText   SHEET_INDEX: " + btn_sheet_index.getText()
                        + " row: " + btn_row_data.getText()
                        + " col: " + btn_col_data.getText()
                        + " col_stringname: " + btn_col_stringname.getText()
                        + " KEY_LANGUAGE: " + btn_key_language.getText());
                Logcat.d("lastValue SHEET_INDEX: " + SHEET_INDEX + " row: " + ROW_DATA
                        + " col: " + COL_DATA + " col_stringname: " + COL_STRING_NAME
                        + " KEY_LANGUAGE: " + KEY_LANGUAGE);
                String s = String.valueOf(btn_sheet_index.getText());
                if (!TextUtils.isEmpty(s)) {
                    SHEET_INDEX = Integer.parseInt(s);
                }
                s = String.valueOf(btn_row_data.getText());
                if (!TextUtils.isEmpty(s)) {
                    ROW_DATA = Integer.parseInt(s);
                }
                s = String.valueOf(btn_col_data.getText());
                if (!TextUtils.isEmpty(s)) {
                    COL_DATA = Integer.parseInt(s);
                }
                s = String.valueOf(btn_col_stringname.getText());
                if (!TextUtils.isEmpty(s)) {
                    COL_STRING_NAME = Integer.parseInt(s);
                }
                s = String.valueOf(btn_key_language.getText());
                if (!TextUtils.isEmpty(s)) {
                    KEY_LANGUAGE = s;
                }
                Logcat.d("newValue  SHEET_INDEX: " + SHEET_INDEX + " row: " + ROW_DATA
                        + " col: " + COL_DATA + " col_stringname: " + COL_STRING_NAME
                        + " KEY_LANGUAGE: " + KEY_LANGUAGE);
                readExcel();
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
        }

    }
}
