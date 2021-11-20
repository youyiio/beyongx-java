package com.beyongx.common.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");
    
    /**
     * 根据文件名获取扩展名
     *
     * @param filename
     *            文件名
     * @return 文件扩展名
     */
    public static String getFileExtensionFromName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    private static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(".");
        int lastSeparator = filename.lastIndexOf("/");
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    public static String getFileExtensionFromUrl(String url) {
        if (url != null && url.length() > 0) {
            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf('/');
            String filename = 0 <= filenamePos ? url.substring(filenamePos + 1)
                    : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (filename.length() > 0
                    && Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    public static byte[] readFile(File targetFile) {
        FileInputStream fin = null;
        ByteArrayOutputStream baos = null;
        try {
            if (targetFile == null || !targetFile.exists()) {
                return null;
            }
            fin = new FileInputStream(targetFile.getAbsolutePath());
            baos = new ByteArrayOutputStream();
            byte[] data = new byte[512];
            int count;
            while ((count = fin.read(data)) > -1) {
                baos.write(data, 0, count);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(file.length());
        raf.write(data);
        raf.close();
    }

    public static void copyFile(File f1, File f2) {
        try {
            if (!f1.exists()) {
                return;
            }
            if (!f2.exists()) {
                f2.createNewFile();
            }
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
        }
    }

    public static void deleteFiles(File path) {
        try {
            if (path == null || !path.isDirectory()) {
                return;
            }
            File[] files = path.listFiles();
            if(null!=files&&files.length>0){
            	for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFiles(file);
                    }
                    file.delete();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩计费相关资源文件，将计费资源文件Zip包解压到指定的输出目录
     */
    public static void releaseResFile(String outPath, File file) {
        File path = new File(outPath);
        if (path.exists()) {
            deleteFiles(path);
        } else {
            path.mkdir();
        }
        ZipFile zf = null;
        FileOutputStream out = null;
        InputStream in = null;
        try {
            zf = new ZipFile(file);
            int size = zf.size();
            Enumeration<? extends ZipEntry> entries = zf.entries();
            // 所有文件与文件夹
            for (int i = 0; i < size; i++) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory())
                    continue;
                in = zf.getInputStream(entry);
                File outFile = new File(outPath + File.separator + entry.getName());
                File parent = new File(outFile.getParent());
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                while (true) {
                    int nRead = in.read(buf, 0, buf.length);
                    if (nRead <= 0) {
                        break;
                    }
                    out.write(buf, 0, nRead);
                }
                out.close();
                Date archiveTime = new Date(entry.getTime());
                outFile.setLastModified(archiveTime.getTime());
            }
            zf.close();
            Thread.sleep(100);
        } catch (Exception e) {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException ioe) {
                    ioe.toString();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    ioe.toString();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.toString();
                }
            }
        }
    }

    /**
     * 文件大小转换
     */
    public static String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }
}
