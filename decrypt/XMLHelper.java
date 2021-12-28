package com.wen.wenxiaoxintest;

import android.net.wifi.aware.PublishConfig;

import com.jancar.sdk.utils.Logcat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by will on 2021-03-23.
 */

public class XMLHelper {

    /**
     * 用于自动匹配的结束单位
     */
    public static String pxEnd = "px\"";
    public static String dpEnd = "dp\"";
    public static String spEnd = "sp\"";


    /**
     * 宽度起始String
     */
    public static String widthStart = "android:layout_width=\"";

    /**
     * 高度起始String
     */
    public static String hightStart = "android:layout_height=\"";

    /**
     * 文本起始String
     */
    public static String textStart = "android:textSize=\"";

    /**
     * marginleft起始String
     */
    public static String marginLeftStart = "android:layout_marginLeft=\"";

    /**
     * marginright起始String
     */
    public static String marginRightStart = "android:layout_marginRight=\"";

    /**
     * marginTop起始String
     */
    public static String marginTopStart = "android:layout_marginTop=\"";


    /**
     * marginbottom起始String
     */
    public static String marginBottomStart = "android:layout_marginBottom=\"";

    /**
     * margingStart起始String
     */
    public static String marginStartStart = "android:layout_marginStart=\"";

    /**
     * margingEnd起始String
     */
    public static String marginEndStart = "android:layout_marginEnd=\"";


    public static String marginStart = "android:layout_margin=\"";
    public static String paddingStart = "android:padding=\"";


    /**
     * paddingStart起始String
     */
    public static String paddingLeftStart = "android:paddingLeft=\"";
    /**
     * paddingStart起始String
     */
    public static String paddingRightStart = "android:paddingRight=\"";
    /**
     * paddingStart起始String
     */
    public static String paddingTopStart = "android:paddingTop=\"";
    /**
     * paddingStart起始String
     */
    public static String paddingBottomStart = "android:paddingBottom=\"";


    /**
     * paddingStart起始String
     */
    public static String paddingStartStart = "android:paddingStart=\"";

    /**
     * paddingEnd起始String
     */
    public static String paddingEndStart = "android:paddingEnd=\"";

    /**
     * maxHeight起始String
     */
    public static String maxHeightStart = "android:maxHeight=\"";
    /**
     * minHeight起始String
     */
    public static String minHeightStart = "android:minHeight=\"";


    public static String maxWidthStart = "android:maxWidth=\"";
    public static String minWidthStart = "android:minWidth=\"";

    /**
     * 滑块偏移量起始String
     */
    public static String thumbOffsetStart = "android:thumbOffset=\"";

    public static String dividerHeightStart = "android:dividerHeight=\"";


    //以1024x600的布局文件为基础，适配800x480为例,缩放比例如下，可按需增加:
    public static Float widthScale = 0.75F;//宽度要乘的比例800/1024 0.78125
    public static Float heightScale = 1.7F;//高度要乘的比例480/600 0.8
    //文字大小要乘的比例取中间值，padding/magin等上下左右都生效的也可以用改这个
    public static Float textScal = 1.225F;


    private static final String InputPath = "mnt/sdcard/xml/";

    private static final String outPutPath = "mnt/sdcard/xmlnew/";

    /**
     * 将xml文件放入 InputPath目录，
     * 调用此方法，则会在outPutPath目录下生成按比例调整过后
     * 的XML，需要文件读写权限。这两个目录可自行调整，有exlipse的可以创建java
     * 工程将路径改为电脑的盘符路径。
     */
    public static void getNewXML() {

        File rootFile = new File(InputPath);
        if (rootFile.exists()) {
            //创建输出路径
            File outputDirFile = new File(outPutPath);
            if (!outputDirFile.exists()) {
                outputDirFile.mkdirs();
            }

            String[] file = rootFile.list();
            for (int i = 0; i < file.length; i++) {
                try {
                    //创建新文件
                    String filePath = outPutPath + file[i];
                    File newFile = new File(filePath);
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    } else {
                        newFile.delete();
                        newFile.createNewFile();
                    }

                    BufferedReader bufferedReader = new BufferedReader(
                            new FileReader(InputPath + file[i]));

                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {

                        //*************以下为目前支持的属性，可按需要增加所需的属性

                        //把宽度按比例修改
                        if (line.contains(widthStart)) {
                            line = numScale(line, widthStart, widthScale);
                        } else if (line.contains(hightStart)) {
                            //把高度按比例修改
                            line = numScale(line, hightStart, heightScale);
                        } else if (line.contains(textStart)) {
                            //文本按比例修改
                            line = numScale(line, textStart, textScal);
                        } else if (line.contains(marginStartStart)) {
                            line = numScale(line, marginStartStart, widthScale);
                        } else if (line.contains(marginEndStart)) {
                            //marginEndStart按比例修改
                            line = numScale(line, marginEndStart, widthScale);
                        } else if (line.contains(marginLeftStart)) {
                            line = numScale(line, marginLeftStart, widthScale);
                        } else if (line.contains(marginRightStart)) {
                            line = numScale(line, marginRightStart, widthScale);
                        } else if (line.contains(marginTopStart)) {
                            line = numScale(line, marginTopStart, heightScale);
                        } else if (line.contains(marginBottomStart)) {
                            line = numScale(line, marginBottomStart, heightScale);
                        }  else if (line.contains(marginStart)) {
                            line = numScale(line, marginStart, textScal);
                        } else if (line.contains(paddingStart)) {
                            line = numScale(line, paddingStart, textScal);
                        }
                        else if (line.contains(paddingStartStart)) {
                            //paddingStart按比例修改
                            line = numScale(line, paddingStartStart, widthScale);
                        } else if (line.contains(paddingEndStart)) {
                            //paddingEnd按比例修改
                            line = numScale(line, paddingEndStart, widthScale);
                        } else if (line.contains(paddingTopStart)) {
                            line = numScale(line, paddingTopStart, heightScale);
                        } else if (line.contains(paddingBottomStart)) {
                            line = numScale(line, paddingBottomStart, heightScale);
                        } else if (line.contains(paddingLeftStart)) {
                            line = numScale(line, paddingLeftStart, widthScale);
                        } else if (line.contains(paddingRightStart)) {
                            line = numScale(line, paddingRightStart, widthScale);
                        } else if (line.contains(minHeightStart)) {
                            //minHeight按比例修改
                            line = numScale(line, minHeightStart, heightScale);
                        } else if (line.contains(maxHeightStart)) {
                            //maxHeight按比例修改
                            line = numScale(line, maxHeightStart, heightScale);
                        } else if (line.contains(maxWidthStart)) {
                            line = numScale(line, maxWidthStart, widthScale);
                        } else if (line.contains(minWidthStart)) {
                            line = numScale(line, minWidthStart, widthScale);
                        } else if (line.contains(thumbOffsetStart)) {
                            line = numScale(line, thumbOffsetStart, widthScale);
                        } else if (line.contains(dividerHeightStart)) {
                            line = numScale(line, dividerHeightStart, widthScale);
                        }


                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    bufferedReader.close();
                    bufferedWriter.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


            Logcat.d("get new xml end!!!!!!");
        }
    }


    /**
     * 缩放数字，
     * 将一个字符串oldStr中
     * 从startStr开始到endStr结束
     * 的数字按scale比例缩放。
     * endStr会自动检测是px/dp/sp
     *
     * @param oldStr
     * @param startStr
     * @return
     */
    public static String numScale(String oldStr, String startStr, Float scale) {
        String newStr = null;
        if (oldStr.contains(startStr)) {
            //数值有三种单位
            String endStr = null;
            if (oldStr.contains(pxEnd)) {
                endStr = pxEnd;
            } else if (oldStr.contains(dpEnd)) {
                endStr = dpEnd;
            } else if (oldStr.contains(spEnd)) {
                endStr = spEnd;
            } else {
                //找不到结束单位直接返回原字符串，不作改变
                return oldStr;
            }


            int startIndex = oldStr.indexOf(startStr) + startStr.length();
            int endIndex = oldStr.indexOf(endStr);

            //Logcat.d(startIndex + " endIndex " + endIndex);
            //找出数字
            String numStr = oldStr.substring(startIndex, endIndex);
            // Logcat.d("numStr " + numStr);
            Float widthFloat = Float.valueOf(numStr);

            //乘以比例
            widthFloat = widthFloat * scale;

            //有时候属性后面会跟一个结束符“/>”,因此要加上
            String lastStr = oldStr.substring(endIndex + endStr.length(), oldStr.length());

            //有时候几个标签在一行的，前面也会有东西，暂时没有兼容这种情况，使用之前先把xml自动缩进一下即可。

            //拼接新的整行字符串。
            newStr = startStr + widthFloat + endStr + lastStr;

        }


        if (newStr != null) {
            return newStr;
        } else {
            return oldStr;
        }
    }


    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //以下两个方法用于某些公司电脑有自动加密的情况，
    // 将java文件一键转化成txt文件，也可以转化回来。使用方法如下
    //copyFolder("F:\\src" , "F:\\src1");//一个文件夹中的文件递归转化成txt
    //copyFolderBack("F:\\src1", "F:\\src");//一个文件夹中的文件递归转化成java文件。

    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output =
                            new FileOutputStream(newPath + "/" + (temp.getName()).toString() + ".txt");
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void copyFolderBack(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); // ����ļ��в����� �������ļ���
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);

                    // ȥ��ĩβ��.txt

                    int pos = (temp.getName()).toString().lastIndexOf(".");
                    String fname = (temp.getName()).toString().substring(0, pos);

                    FileOutputStream output = new FileOutputStream(newPath + "/" + fname);
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) { // ��������ļ���
                    copyFolderBack(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
