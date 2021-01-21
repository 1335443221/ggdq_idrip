package com.sl.common.txgljdao;
import com.sl.common.entity.UIDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface TxgljBaseDao {
    ArrayList<UIDataVo> getBaseDataList(@Param("table") String table,
                                        @Param("bdate") String bdate,
                                        @Param("edate") String edate,
                                        @Param("tgList") ArrayList<String> tgList,
                                        @Param("deviceList") ArrayList<String> deviceList,
                                        @Param("tagList") ArrayList<String> tagList);


}
