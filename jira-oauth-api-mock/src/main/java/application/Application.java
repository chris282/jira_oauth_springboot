package application;


/**
 * 
 * TODO : JSON loader, XML loader => utiliser spring boot
 * => use spring boot offline
 * Using Spring Boot on a Closed Network - Spring Forum
 */

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import oauth.OAuthClientRunner;
import org.springframework.boot.Banner;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
/**
 * Example project based on 
 * https://developer.atlassian.com/cloud/jira/platform/jira-rest-api-oauth-authentication/
 * https://bitbucket.org/atlassianlabs/atlassian-oauth-examples/src/master/java/src/main/java/com/atlassian/oauth/client/example/
 * https://developer.atlassian.com/server/jira/platform/jira-rest-api-example-oauth-authentication-6291692/
 *
 */
//see google javadoc : https://googleapis.dev/java/google-oauth-client/1.25.0/index.html

@SpringBootApplication
@ComponentScan(basePackages = {
		"oauth",
		"rest"})
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
	}

	@PostConstruct
	public void init() {
		System.out.println("Run");
	}
	
	/**
	 * Request Token - wait 5 sec then do it 
	 * @throws MessagingException 
	 * @throws IOException 
	 * @throws AddressException 
	 */
	@Scheduled(fixedDelay = 2*60*1000,initialDelay = 3*1000)
	public void requestToken() {
		try{
			OAuthClientRunner.requestToken();
		}
		catch(Exception e){
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void readyMessage() {
		System.out.println("[jira-oauth-mock] Startup complete, PID="+pid());
	}

	public int pid(){
		String vmName = ManagementFactory.getRuntimeMXBean().getName();
		int p = vmName.indexOf("@");
		return Integer.parseInt(vmName.substring(0, p));
	}
}