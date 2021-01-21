package com.sl.common.service;

import com.sl.common.entity.UIDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/10/9 16:34
 * FileName: MongoDBService
 * Description: MongoDB的业务层
 */
@Service("mongoDBService")
public class MongoDBService {
    @Autowired
    private MongoTemplate mongoTemplate;


}
