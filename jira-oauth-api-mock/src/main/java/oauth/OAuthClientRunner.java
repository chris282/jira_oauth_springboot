package oauth;

import java.util.Arrays;
import java.util.List;

public class OAuthClientRunner {

	public static void requestToken() throws Exception{
		System.out.println("[jira-oauth-mock] running REQUEST_TOKEN command");
		run(new String[]{oauth.Command.REQUEST_TOKEN.getName()});
	}
	
	public static void accessToken(String token) throws Exception{
		System.out.println("[jira-oauth-mock] running ACCESS_TOKEN command");
		run(new String[]{oauth.Command.ACCESS_TOKEN.getName(),token});
	}
	
	public static void request(String url) throws Exception{
		System.out.println("[jira-oauth-mock] running REQUEST command");
		run(new String[]{oauth.Command.REQUEST.getName(), url});
	}
	
	/**
	 * @see main.java.oauth.Command.REQUEST_TOKEN("requestToken")
	 * @see main.java.oauth.Command.ACCESS_TOKEN("accessToken")
	 * @see main.java.oauth.Command.REQUEST("request")
	 * java -jar OAuthTutorialClient-1.0.jar requestToken
	 * @param args
	 * @throws Exception
	 */
    private static void run(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command specified. Use one of " + Command.names());
        }

        PropertiesClient propertiesClient = new PropertiesClient();
        JiraOAuthClient jiraOAuthClient = new JiraOAuthClient(propertiesClient);

        List<String> argumentsWithoutFirst = Arrays.asList(args).subList(1, args.length);

        new OAuthClient(propertiesClient, jiraOAuthClient).execute(Command.fromString(args[0]), argumentsWithoutFirst);
    }
}
