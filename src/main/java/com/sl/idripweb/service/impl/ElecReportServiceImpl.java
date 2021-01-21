package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.ExcelExportUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.config.MySession;
import com.sl.idripweb.dao.ElecReportDao;
import com.sl.idripweb.service.ElecReportService;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("elecReportServiceImpl")
public class ElecReportServiceImpl implements ElecReportService {

    @Autowired
    private MySession mySession;
    @Autowired
    private ElecReportDao elecReportDao;
    private  String select_category_ids;
    private  List<String> select_category_ids_arr;


    /**
     * 用电报表
     * @param map
     * @return
     */
    @Override
    public WebResult index(HttpServletRequest request, Map<String, Object> map) {
        if (map.get("room_type") == null) {
            return WebResult.error(301);
        }
        if (map.get("select_category_ids")==null||String.valueOf(map.get("select_category_ids")).equals("")){
            select_category_ids ="all";
            select_category_ids_arr=new ArrayList<>();
        }else{
            select_category_ids=map.get("select_category_ids").toString();
            select_category_ids_arr=Arrays.asList(select_category_ids.split(","));
        }

        //获取项目的date_time
        String projectTime = mySession.getProjectTime(request);



        switch (String.valueOf(map.get("room_type"))) {
            case "1":
                map.put("tag","ep");  //数据表后缀
                map.put("categoryRelationTable", "elec_project_category_relation");  // category
                map.put("meterRelationTable", "elec_category_meter_relation");  // category_meter
                map.put("roomIdName", "transformerroom_id");  // 配电室名称id
                map.put("meterTable", "elec_meter");  // 表
                map.put("tagTable", "elec_tag");  // 表
                break;
            case "2":
            case "3":
                map.put("tag","qf");  //数据表后缀
                map.put("categoryRelationTable", "water_project_category_relation");  // category
                map.put("meterRelationTable", "water_category_meter_relation");  // category_meter
                map.put("roomIdName", "water_pump_room_id");  // 配电室名称id
                map.put("meterTable", "water_meter");  // 表
                map.put("tagTable", "water_tag");  // 表
                break;

              /*  map.put("tag","gqf");  //数据表后缀
                map.put("categoryRelationTable", "gas_project_category_relation");  // category
                map.put("meterRelationTable", "gas_category_meter_relation");  // category_meter
                map.put("roomIdName", "gas_pressure_room_id");  // 配电室名称id
                map.put("meterTable", "gas_meter");  // 表
                map.put("tagTable", "gas_tag");  // 表
                break;*/
        }
        List<String> dateTitle = new ArrayList<>();   //时间标题
        String beginTime=String.valueOf(map.get("date_from"));
        String endTime=String.valueOf(map.get("date_to"));
        String date_type=String.valueOf(map.get("date_type"));
        switch (date_type) {
            case "hour_data":
                map.put("dateType", "power_per_hour");
                map.put("dataTable", "day_data_");  //数据表前缀  后缀为标签
                for (int i = 0; i <= 23; i++) {
                    dateTitle.add(i + "时");
                }
                break;
            case "day_data":
                map.put("dateType", "power_per_hour");
                map.put("dataTable", "day_data_");  //数据表前缀  后缀为标签
                dateTitle=DateUtil.getDayListOfDate(beginTime,endTime,0,0,1,0,0,0,0,DateUtil.DATE_FORMAT_YYYY_MM_DD);
                break;
            case "month_data":
                map.put("dateType", "power_per_day");
                map.put("dataTable", "month_data_"); //数据表前缀  后缀为标签
                map.put("date_from",DateUtil.parseStrToStr(beginTime,DateUtil.DATE_FORMAT_YYYY_MM));
                map.put("date_to",DateUtil.parseStrToStr(endTime,DateUtil.DATE_FORMAT_YYYY_MM));
                dateTitle=DateUtil.getMonthListOfDate(beginTime,endTime,DateUtil.DATE_FORMAT_YYYY_MM月);
                break;
            case "year_data":
                map.put("dateType", "power_per_month");
                map.put("dataTable", "year_data_"); //数据表前缀  后缀为标签
                map.put("date_from",DateUtil.parseStrToStr(beginTime,DateUtil.DATE_FORMAT_YYYY));
                map.put("date_to",DateUtil.parseStrToStr(endTime,DateUtil.DATE_FORMAT_YYYY));
                dateTitle=DateUtil.getYearListOfDate(beginTime,endTime,DateUtil.DATE_FORMAT_YYYY年);
                break;
        }
        map.put("dataTable",map.get("dataTable").toString()+map.get("tag").toString());   //数据表

//=============================================================//

        List<Map<String,Object>> category_meter_list=elecReportDao.getCategoryList(map);
        // 获取设备名
        List<String> device_name_list=new ArrayList<>();
        for (Map<String,Object> val:category_meter_list){
            if (val.get("device_name")!=null){
                device_name_list.add(val.get("device_name").toString());
            }
        }
        map.put("deviceNameList",device_name_list);
        /* 获取ep数据 - 开始 */
        List<Map<String,Object>> meter_data=new ArrayList<>();
        if(device_name_list.size() > 0)
            meter_data=elecReportDao.getEpData(map);

        /* 获取ep数据 - 结束 */

        Map<String,Map<String,Object>> elec_meter_ep=new HashMap<>();
        for (Map<String,Object> v5:meter_data){
            if (elec_meter_ep.get(v5.get("device_name").toString())==null){
                Map<String,Object> dateList=new LinkedHashMap<>(dateTitle.size());
                dateList.put(v5.get("date").toString(),v5);
                elec_meter_ep.put(v5.get("device_name").toString(),dateList);
            }else{
                Map<String,Object> dateList=elec_meter_ep.get(v5.get("device_name"));
                dateList.put(v5.get("date").toString(),v5);
                elec_meter_ep.put(v5.get("device_name").toString(),dateList);
            }
        }

        // 构建category-meter-ep树
        // category_id相同的节点合并为1个（水、电的区域用能；水的分类用能）
        Map<String,Object> resultData=new LinkedHashMap<>();
        for (Map<String,Object> v:category_meter_list){
            //初始化数据
            Map<String,Object> tmp=new LinkedHashMap<>(dateTitle.size());
           for (String v3:dateTitle){
            if (date_type.equals("month_data")||date_type.equals("year_data")){
                v3=v3.substring(0,v3.length() - 1);
            }
            tmp.put(v3,"-");
           }
            v.put("detail",tmp);


            if (elec_meter_ep.get(v.get("device_name"))!=null){

            Map<String,Object> ky=elec_meter_ep.get(v.get("device_name"));
            for (Map.Entry<String, Object> entry : ky.entrySet()) {
                Map<String,Object> v3=(Map<String,Object>)entry.getValue();
                String k3=entry.getKey();
                // 分时报表取day_data表power_per_day字段数据
                if (map.get("date_type").toString().equals("hour_data")){
                    Map<String,Object> dateMap=new LinkedHashMap<>();
                   //如果为空
                    if (v3.get("detail")==null||String.valueOf(v3.get("detail")).equals("[]")){
                        for (int i=0;i<=23;i++){
                            dateMap.put(String.valueOf(i),"-");
                        }
                    }else {
                        List<String> detailList = JSONArray.parseArray(v3.get("detail").toString(), String.class);
                        for (int i = 0; i <= 23; i++) {
                            dateMap.put(String.valueOf(i), detailList.get(i));
                        }
                    }
                    v.put("detail",dateMap);
                }else{
                    if (v.get("detail")==null){
                        Map<String,Object> dateMap=new LinkedHashMap<>();
                        dateMap.put(k3,v3.get("power"));
                        v.put("detail",dateMap);
                    }else{
                        Map<String,Object> dateMap=(LinkedHashMap<String,Object>)v.get("detail");
                        dateMap.put(k3,v3.get("power"));
                        v.put("detail",dateMap);
                    }
                }
            }

            }


            // category_id相同的节点合并为1个
            Map<String,Object> v1=v;
            Map<String,Object> categoryIdMap=new HashMap<>();
            if (resultData.get(v1.get("category_id").toString())!=null){
                categoryIdMap=(Map<String,Object>)resultData.get(v1.get("category_id").toString());
            }
            categoryIdMap.put("category_id",v1.get("category_id"));
            categoryIdMap.put("category_name",v1.get("category_name"));
            categoryIdMap.put("parent_category_id",v1.get("parent_category_id"));
            if (v1.get("detail")!=null) {
                LinkedHashMap<String, Object> dateMap = (LinkedHashMap<String, Object>) v1.get("detail");
                for (Map.Entry<String, Object> ky2 : dateMap.entrySet()) {
                    Map<String, Object> d = new LinkedHashMap<>();
                    if (categoryIdMap.get("detail") != null) {
                        d = (LinkedHashMap<String, Object>) categoryIdMap.get("detail");
                    }

                    String k2 = ky2.getKey();
                    String v2 = ky2.getValue().toString();
                        if (!v2.equals("-")){
                             if (d.get(k2) != null&&!d.get(k2).toString().equals("-")) {
                                 d.put(k2, Double.parseDouble(d.get(k2).toString()) + Double.parseDouble(v2));
                             }else {
                                 d.put(k2, v2);
                             }
                        }else {
                            if (d.get(k2) == null) {
                                d.put(k2, v2);
                            }
                        }
                    categoryIdMap.put("detail", d);
                }
            }
            resultData.put(v1.get("category_id").toString(),categoryIdMap);
            }
          // 格式化数据,生成对应树

        map.put("max_level",0);
        List<Map<String,Object>> category_tree=getCategoryData(resultData,0,1,map);
        int max_level=Integer.parseInt(String.valueOf(map.get("max_level")));
         // 生成数据列表

        List<String>  content_tmp=new ArrayList();  //当前行信息
        List<List<String>>  table_content=new ArrayList();  //表格数据

        getTable(category_tree,max_level,0, table_content,content_tmp);
        List<String> amount=new ArrayList<>();
        List<String> title=new ArrayList<>();
        amount.add("总计");
        for (int i = 1; i <= max_level; i++) {
            title.add(i+"级"); // 表头说明
            if (i < max_level){
                amount.add(""); // 总计说明
            }
        }
        // 计算总计行
        int data_count = dateTitle.size();
        for (int i = max_level; i < (max_level + data_count); i++) {
            double m=0;
            String m2="-";
             for (List<String> content:table_content){
                  if (content.size()>max_level&&!content.get(i).equals("")){
                      if (!content.get(i).equals("-")){
                          m+=Double.parseDouble(content.get(i));
                          m2="--";
                      }
                  }
             }
             if (m2.equals("-")){
                 amount.add(m2);
             }else{
                 amount.add(String.format("%.2f",m));
             }

        }
        title.addAll(dateTitle);
        table_content.add(amount);


        //返回标题
        List<Map<String,Object>> tableTitle = new ArrayList<>();
        for (String t:title){
            Map<String,Object> tableTitleMap=new HashMap<>();
            tableTitleMap.put("prop",t);
            tableTitleMap.put("label",t);
            if (t.contains("级")){  //
                tableTitleMap.put("type","fixed");
            }
            tableTitle.add(tableTitleMap);
        }
        //返回数据
        List<Map<String,Object>> tableContent = new ArrayList<>();
        for (List<String> t:table_content){
            Map<String,Object> tableContentMap=new LinkedHashMap<>(t.size());
                for (int i=0;i<t.size();i++){
                    tableContentMap.put(title.get(i),t.get(i));
                }
            tableContent.add(tableContentMap);
        }

        Map<String,Object> result=new LinkedHashMap<>(3);
        result.put("table_title",tableTitle);
        result.put("table_content",tableContent);
        result.put("project_time",projectTime);
        return WebResult.success(result);
    }


    /**
     * 构造树
     * @param category_meter_list
     * @param parent_category_id
     * @param level
     * @return
     */
    public List<Map<String,Object>> getCategoryData(Map<String,Object> category_meter_list,int parent_category_id,int level,Map<String,Object> map){
        int max_level=Integer.parseInt(String.valueOf(map.get("max_level")));
        if (category_meter_list!=null){
            if (level > max_level) {
                map.put("max_level",level);
            }
        }

        List<Map<String,Object>> tree = new ArrayList<>();
        // 区分是否是最末子节点
        List<String> parent_category_ids=getParentCategoryIds(category_meter_list);
        for (Map.Entry<String, Object> ky : category_meter_list.entrySet()) {
            String key=ky.getKey();
            Map<String,Object> value=(Map<String,Object>)ky.getValue();
            if (Integer.parseInt(value.get("parent_category_id").toString())==parent_category_id){
                // 跳过未选中节点
                if (!select_category_ids.equals("all")&&!select_category_ids_arr.contains(value.get("category_id").toString())){
                    continue;
                }
                //如果  父id  有此id  证明  有子节点
                if (parent_category_ids.contains(value.get("category_id").toString())){
                    List<Map<String,Object>> tmp=getCategoryData(category_meter_list,Integer.parseInt(value.get("category_id").toString()),level+1,map);
                    Map<String,Object> node=new HashMap<>();
                    node.put("parent_category_id",value.get("parent_category_id"));
                    node.put("category_id",value.get("category_id"));
                    node.put("text",value.get("category_name"));
                    node.put("data",new HashMap<>());
                    node.put("nodes",tmp);
                    tree.add(node);
                }else{ // 最末子节点
                    Map<String,Object> node=new HashMap<>();
                    node.put("parent_category_id",value.get("parent_category_id"));
                    node.put("category_id",value.get("category_id"));
                    node.put("text",value.get("category_name"));
                    node.put("data",value.get("detail"));
                    node.put("nodes",new ArrayList<>());
                    node.put("count",1);
                    tree.add(node);
                }
            }
        }
        return tree;
    }

    /**
     * 获取所有父节点id
     * @param category_meter_list
     * @return
     */
    public List<String> getParentCategoryIds(Map<String,Object> category_meter_list) {
        List<String> parent_category_ids=new ArrayList<>();
        for (Map.Entry<String, Object> ky : category_meter_list.entrySet()) {
            Map<String,Object> v=(Map<String,Object>)ky.getValue();
            String parent_category_id=v.get("parent_category_id").toString();
            if (!parent_category_ids.contains(parent_category_id)){
                parent_category_ids.add(parent_category_id);
            }
        }
        return parent_category_ids;
    }

    /**
     * 获取表格数据
     * @param data
     * @param max_level
     * @param level
     * @param result
     * @param content_tmp
     */
    public void getTable(Object data,int max_level,int level,List<List<String>> result,List<String> content_tmp) {

        if (data!=null){
            List<Map<String, Object>> data_tmp =new ArrayList<>();
            if (level==0) {  //第一次为list
                data_tmp = (List<Map<String, Object>>)data;
            }else{  //第二次为map
                Map<String, Object> data2=(Map<String, Object>)data;
                data_tmp =(List<Map<String, Object>>)data2.get("nodes");
            }

            for (Map<String,Object> val:data_tmp){
                List<String>  content=new ArrayList();
                List nodes=(List<Map<String,Object>>)val.get("nodes");
                if (nodes.size()>0){
                    // 保存当前行信息，继续遍历
                    content_tmp.add(val.get("text").toString());
                    getTable(val,max_level,level+1,result,content_tmp);
                }

                // 删除当前行最末行信息，为下行做准备
                if (content_tmp.size()>level){
                        for (int j=0;j<(content_tmp.size()-level);j++){
                            content_tmp.remove(content_tmp.size()-1);
                        }
                }
                // 拼接行信息
                if (content_tmp.size()>0){
                    List<String> content_tmp2=new ArrayList<>();
                    for (String kk:content_tmp){
                        content_tmp2.add(kk);
                    }
                    for (String kk:content){
                        content_tmp2.add(kk);
                    }
                    content=content_tmp2;
                    content.add(val.get("text").toString());
                    // 层级小于最大层级时用''填充
                    int count=content.size();
                    for (int i = 0; i < (max_level - count);i++) {
                        content.add("");
                    }
                }

                // 拼接最末级数据
                   List val2=(List<Map<String, Object>>) val.get("nodes");
                   if (val2.size()==0){
                    // 只有一级数据时补充节点信息
                    if (level==0){
                        content.add(val.get("text").toString());
                    }
                    // 拼接数据
                    Map<String,Object> kv4=(Map<String,Object>)val.get("data");
                    for (Map.Entry<String,Object> v4:kv4.entrySet()){
                        if (!v4.getValue().toString().equals("-")||v4.getValue() instanceof Double){
                            content.add(String.format("%.2f",Double.parseDouble(v4.getValue().toString())));
                        }else{
                            content.add(v4.getValue().toString());
                        }
                    }
                 result.add(content);
                }

            }
        }

    }


    /**
     * 导出报表
     * @param request
     * @param map
     * @return
     */
    @Override
    public WebResult indexExport(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws Exception{
       String shellName="";
        switch (String.valueOf(map.get("room_type"))) {
            case "1":
                shellName="用电报表";
                break;
            case "2":
                shellName="用水报表";
                break;
            case "3":
                shellName="用气报表";
                break;
        }

        WebResult webResult=index(request, map);
        //请求成功  解析数据 返回excel文档
        if(webResult.getStatus()==200){
            JSONObject json=JSONObject.parseObject(JSON.toJSONString(webResult.getData()));
            //excel标题
            List<Map> table_title=JSONArray.parseArray(JSON.toJSONString(json.get("table_title")),Map.class);
            String[] title =new String[table_title.size()];
            for (int i=0;i<table_title.size();i++){
                title[i]= table_title.get(i).get("prop").toString();
            }
            //数据
            List<Map> table_content=JSONArray.parseArray(JSON.toJSONString(json.get("table_content")),Map.class);

            //导出列表
            XSSFWorkbook Workbook = ExcelExportUtil.exportTitleAndDataExcel(table_content,title,title,22,shellName);
            ByteArrayOutputStream out =new  ByteArrayOutputStream();
            Workbook.write(out);
            byte[] bytes = out.toByteArray();
            response.setContentType("application/x-msdownload");
            long time = System.currentTimeMillis();
            String fileName = new String(shellName.getBytes(),"ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName+""+time+".xlsx");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }

        return webResult;
    }

}
