package com.sl.common.config;

import com.sl.common.service.SchedulingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class SchedulingWork {
    @Autowired
    private SchedulingService schedulingService;

    private Logger logger = Logger.getLogger(getClass());


}
