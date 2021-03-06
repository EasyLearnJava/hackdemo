package hello;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    static List<Integer> recList = new ArrayList<Integer>();

    @CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("/greeting")
    public List<RecognitionResult> greeting(@RequestParam(required=false, defaultValue="World") String name) {
        System.out.println("==== in greeting ====");
        
        String theUrl = "http://35.193.175.155:8080/api/recognitions";
        HttpHeaders headers = createHttpHeaders();
        RestTemplate restTemplate = new RestTemplate();     
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<List<RecognitionResult>> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<List<RecognitionResult>>() {});
        System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.getBody());
        System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.getBody().size());
    
        List<RecognitionResult> rectempList = new ArrayList<RecognitionResult>();
        int listSize = recList.size(); 
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!11 : " + listSize);
        for(RecognitionResult obj : response.getBody()) {
        	if(!recList.contains(obj.getId())) {
        		recList.add(obj.getId());
        		rectempList.add(obj);
        	}
        }        
        return rectempList;        
    }

    @GetMapping("/greeting-javaconfig")
    public Greeting greetingWithJavaconfig(@RequestParam(required=false, defaultValue="World") String name) {
        System.out.println("==== in greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    
    private HttpHeaders createHttpHeaders()
    {
        //String notEncoded = user + ":" + password;
        String encodedAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUyNjg3MTkyM30.rHUT76xMf1rzWoUHVVE8SFeeDXfZ807ihu4mCi93vBOfP8OBnA1VmXXMLDew25CG18qwDrn5TW0yq5i3lj9DSA";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }

}
