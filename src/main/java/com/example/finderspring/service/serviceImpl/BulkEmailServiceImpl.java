package com.example.finderspring.service.serviceImpl;

import com.example.finderspring.service.BulkEmailService;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BulkEmailServiceImpl implements BulkEmailService {

    @Override
    public String bulkEmailVerify(MultipartFile file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));
        CSVParser csvRecords = new CSVParser(bufferedReader, CSVFormat.EXCEL.withHeader("email"));
        Iterable<CSVRecord> csvRecords1  =  csvRecords.getRecords();
        List<String> emails = new ArrayList<>();
        for (CSVRecord csvRecord:csvRecords1) {
            emails.add(csvRecord.get("email"));
//            Email email = new Email(null,null,null,csvRecord.get("email"),null);
        }
        List<List<String>> subSets = Lists.partition(emails, 25);
        JSONArray jsonArray_final = new JSONArray();
        subSets.parallelStream().forEach(lst ->{
            JSONArray jsonArray = new JSONArray();
            lst.parallelStream().forEach(email -> {
                String servers[] = {"smtp1.applet.io", "smtp2.applet.io",
                        "smtp3.applet.io", "smtp4.applet.io", "smtp5.applet.io", "smtp6.applet.io", "smtp7.applet.io", "smtp8.applet.io", "smtp9.applet.io", "smtp10.applet.io"};
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://"+servers[new Random().nextInt(10)]+":8080/verify/"+email;
                String result = restTemplate.getForObject(url, String.class);
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("Error") || status.equals("Failed")){
                    String scrap_url = "https://finderio.qa.500apps.io/v2qa/emails201909?fields=email,src&where= email in (\""+email+"\")";
                    String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InZpbmF5LnBvc2luYUA1MDBhcHBzLmNvbSIsInRlbmFudF9pZCI6MSwidXNlcl9pZCI6MzA4LCJleHAiOiIxNjEzNTY4NzA2In0.AgsuAYDq4ATnPZ-DUawpyWc30XKFIiP0E4xzypsVVZI";
                    HttpHeaders headers = new HttpHeaders();
                    System.out.println(scrap_url);

                    headers.set("token",token);
                    HttpEntity entity = new HttpEntity(headers);
                    ResponseEntity<String> response = restTemplate.exchange(scrap_url, HttpMethod.GET,entity,String.class);
                    if (response.getBody().length() > 2){
                        jsonObject.put("confidence","70%");
                        jsonObject.put("email",email);
                        jsonObject.put("message","Email may bounce");
                        jsonObject.put("status","success");
                    }else {
                        jsonObject.put("confidence","0%");
                        jsonObject.put("email",email);
                        jsonObject.put("message","Email does not exist");
                        jsonObject.put("status","Error");
                    }
                } else
                    jsonObject.put("email",email);
                jsonArray.put(jsonObject);
            });
            jsonArray_final.putAll(jsonArray);
        });

        return jsonArray_final.toString();
    }
}
