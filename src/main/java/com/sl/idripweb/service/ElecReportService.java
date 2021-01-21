package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public interface ElecReportService {

    public WebResult index(HttpServletRequest request, Map<String, Object> map);

    public WebResult indexExport(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws Exception;

}
