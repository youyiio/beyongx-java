package com.beyongx.framework.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件下载处理类
 */
public class FileDownloader {
    
    /**
     * 导出excel
     */
    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        // String tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
        // File file = new File(tempPath);
        // BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        // // 一次性写出内容，使用默认样式，强制输出标题
        // writer.write(list, true);
        // SXSSFSheet sheet = (SXSSFSheet)writer.getSheet();
        // //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
        // sheet.trackAllColumnsForAutoSizing();
        // //列宽自适应
        // writer.autoSizeColumnAll();
        // //response为HttpServletResponse对象
        // response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        // //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        // response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        // ServletOutputStream out = response.getOutputStream();
        // // 终止后删除临时文件
        // file.deleteOnExit();
        // writer.flush(out, true);
        // //此处记得关闭输出Servlet流
        // IoUtil.close(out);
    }
}
