package rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApplicationEndpoint {

	//http://localhost:8080/greeting
	@RequestMapping("/greeting")
    public String greeting() {
		System.out.println("[jira-oauth-mock] Rest request to /greeting received");
        return "Hello";
    }
}
