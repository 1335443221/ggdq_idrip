package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.ExcelExportUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.AlermManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * 报警管理Controller
 * @author lxr
 */
@Controller
@RequestMapping("/export")
@Api(value = "export", description = "导出报警数据")
public class ExportController {

    @Autowired
    private AlermManagerService alermManagerService;


    @ApiOperation(value = "alerm", notes = "导出报警数据", httpMethod = "GET")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "btime", example = "2020-10-10", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "etime", example = "2020-10-10", description = "结束时间"),
            @ApiJsonProperty(type = String.class,key = "isdeal", example = "1", description = "处理类别 0:未处理 1：已处理."),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id,若不填写，默认选择系统第一个厂区"),
            @ApiJsonProperty(type = Integer.class,key = "category", example = "2", description = "报警类型"),
            @ApiJsonProperty(type = String.class,key = "business_name", example = "2", description = "商户名称")
    })
    @RequestMapping(value = "alerm",method = RequestMethod.GET)
    @ResponseBody
    public WebResult alerm(@RequestAttribute Map<String, Object> map,HttpServletResponse response) throws Exception{
        //报警数据
        ArrayList<Map> dataList= alermManagerService.exportData(map);

        String[] rowName = {"报警等级", "报警类别", "所属区域","报警位置","设备/商户","报警描述","报警时间",
                "报警值","正常值","处理状态","处理人","处理时间"};
        String[] rowKey = {"level", "category_name", "factory_name","location", "device_name", "config_desc","log_time",
                "value", "th","dealName", "operater", "confirm_time"};
        //导出列表
        XSSFWorkbook Workbook = ExcelExportUtil.exportTitleAndDataExcel(dataList,rowName,rowKey,22,"报警管理");
        ByteArrayOutputStream out =new  ByteArrayOutputStream();
        Workbook.write(out);
        byte[] bytes = out.toByteArray();
        response.setContentType("application/x-msdownload");
        long time = System.currentTimeMillis();
        String fileName = new String("报警管理".getBytes(),"ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+""+time+".xlsx");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return WebResult.success();
    }

}
