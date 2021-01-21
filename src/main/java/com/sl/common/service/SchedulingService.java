package com.sl.common.service;

import com.sl.common.config.ConstantConfig;
import com.sl.common.dao.CommonDao;
import com.sl.common.dao.SchedulingDao;
import com.sl.common.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SchedulingService")
public class SchedulingService {
    @Autowired
    private SchedulingDao schedulingDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private ConstantConfig constantConfig;
    @Autowired
    private CommonService commonService;
    private Logger logger = Logger.getLogger(getClass());


}
