package com.example.finderspring.service.serviceImpl;

import com.example.finderspring.service.SingleEmailService;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SingleEmailServiceImpl implements SingleEmailService {

    @Override
    public String singleEmailVerify(String email) {
        String servers[] = {"smtp1.applet.io", "smtp2.applet.io",
                "smtp3.applet.io", "smtp4.applet.io", "smtp5.applet.io", "smtp6.applet.io", "smtp7.applet.io", "smtp8.applet.io", "smtp9.applet.io", "smtp10.applet.io"};
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://"+servers[0]+":8080/verify/"+email;
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
        }
        return jsonObject.toString();
    }

    @Override
    public String singleEmailFinder(String firstName, String lastName, String domain) {
        String servers[] = {"smtp1.applet.io", "smtp2.applet.io",
                "smtp3.applet.io", "smtp4.applet.io", "smtp5.applet.io", "smtp6.applet.io", "smtp7.applet.io", "smtp8.applet.io", "smtp9.applet.io", "smtp10.applet.io"};
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://"+servers[0]+":8080/find/"+firstName+"/"+lastName+"/"+domain;
        String result = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = new JSONObject(result);
        String status = jsonObject.getString("status");
        if(status.equals("Error") || status.equals("Failed")){
            String email = generateCombinations(firstName,lastName,domain);
            String scrap_url = "https://finderio.qa.500apps.io/v2qa/emails201909?fields=email,src&where= email in "+email;
            String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InZpbmF5LnBvc2luYUA1MDBhcHBzLmNvbSIsInRlbmFudF9pZCI6MSwidXNlcl9pZCI6MzA4LCJleHAiOiIxNjEzNTY4NzA2In0.AgsuAYDq4ATnPZ-DUawpyWc30XKFIiP0E4xzypsVVZI";
            HttpHeaders headers = new HttpHeaders();
            System.out.println(scrap_url);

            headers.set("token",token);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(scrap_url, HttpMethod.GET,entity,String.class);
            System.out.println(response.getBody());
            if (response.getBody().length() > 2){
                jsonObject.put("confidence","70%");
                jsonObject.put("email",email);
                jsonObject.put("message","Email may bounce");
                jsonObject.put("status","success");
            }else {
                jsonObject.put("confidence","0%");
                jsonObject.put("message","Email does not exist");
                jsonObject.put("status","Error");

            }
        }
        return jsonObject.toString();
    }

    public String generateCombinations(String first, String last, String domain){
        StringBuffer emails = new StringBuffer("(");
        emails.append("\""+last + "@" +domain +"\",");
        emails.append("\""+first+"."+last + "@" +domain+"\",");
        emails.append("\""+first + "@" +domain+"\",");
        emails.append("\""+last+"."+first + "@" +domain+"\",");
        emails.append("\""+first.charAt(0)+last + "@" +domain+"\",");
        emails.append("\""+last.charAt(0)+"."+first + "@" +domain+"\",");
        emails.append("\""+first+"_"+last + "@" +domain+"\",");
        emails.append("\""+last+"_"+first + "@" +domain+"\")");
        return new String(emails);
    }


}
