package com.jumkid.like.controller;

import com.jumkid.like.model.AppInfo;
import com.jumkid.like.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class StatusCheckController {

    public enum ServiceStatus {
        OK("OK"), ERROR("ERROR"), WARNING("WARNING");

        private String value;

        ServiceStatus(String value) { this.value = value; }

        public String value() { return value; }
    }

    @Value("${spring.application.name}")
    String appName;

    @Value("${spring.application.version}")
    String appVersion;

    @Value("${spring.application.api}")
    String appApi;

    @Value("${spring.application.description}")
    String appDesc;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public StatusCheckController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    @ResponseBody
    public Response appInfo(){
        AppInfo appInfo = new AppInfo();
        //set info
        appInfo.setName(appName);
        appInfo.setVersion(appVersion);
        appInfo.setDescription(appDesc);
        appInfo.setApi(appApi);

        return this.buildResponse(true, appInfo);

    }

    /**
     *
     * @param success
     * @param data
     * @return
     */
    private Response buildResponse(boolean success, Object data){
        final Response response = new Response();
        response.setSuccess(success);
        response.setData(data);

        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "status", produces = "application/json")
    @ResponseBody
    public String status() {
        var REDIS_TEST_VALUE = "like:statuscheck";
        redisTemplate.opsForValue().set(REDIS_TEST_VALUE, ServiceStatus.OK.value());
        var testVal = redisTemplate.opsForValue().get(REDIS_TEST_VALUE);

        if(testVal.equals(ServiceStatus.OK.value())){
            redisTemplate.delete(REDIS_TEST_VALUE);
            return ServiceStatus.OK.value();
        }else{
            return ServiceStatus.ERROR.value();
        }
    }

}
