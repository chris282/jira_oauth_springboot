package oauth;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesClient {
    public static final String CONSUMER_KEY = "consumer_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String REQUEST_TOKEN = "request_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SECRET = "secret";
    public static final String JIRA_HOME = "jira_home"; ///export/home/jira
    public static final String jira_privatekey_pcks8="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMNNp6EK6VLejg5H\n" + 
    		"RHumI2ubUUvOD17gXkRyrs7UJXjUXijvWzbJKDWErJU2jO6scvK9R/FpNdFG1Png\n" + 
    		"C0RG85655nMw8x0TtDTz9Ogd1mK99oZelNQsN/pKLHeE8bUCsW2hPXPmd2Rs5rzw\n" + 
    		"NGNoQmhPFH8l8WpRpUgsf0elIYtTAgMBAAECgYBnluPHpmr1engsmJ55WzjEVaB8\n" + 
    		"LbK3Zxh5A782kw3BFhtPaQ2fdHiwUkvb5RBBdBSQLddf86cI9qH2W2B+eYezho1U\n" + 
    		"KoNftZCQu/+I/obhqECxtlWh4sOijOJTQL8hkMGPbFItRj7OjB2L6VcDTufqA43J\n" + 
    		"4AKwFADeDOKBACONyQJBAOkOkNd4NGCHvTKomJRx1ocyTIXV7PodqGY8LFWIRKuv\n" + 
    		"Aau0f3cEnxLztCBXsyvXo/qLpi+I4cpaKIB4A93Sw28CQQDWh6JtGkyBTHycjeKK\n" + 
    		"X4f0ysQWPqefLPq6EIjoPN+csTBq2wk3DqLkoYfM19hZ+C6cwMc+rhz4nNGM5s9X\n" + 
    		"AjRdAkBTVJLziBZqQX6jw2azBTUoqBJ2dDREhiA1VeSgPJYZMP0O8P32h199fkf0\n" + 
    		"nIcC8Rq/tfkY2/+nw67hqIfxY6/fAkBz18gIvCPFykSb0S4cZFuo/vkFvg2LsaEu\n" + 
    		"ahFTvtNM7I1lliOSvG+Pn/4RyhhE9PROpvawTRrN0PiC6nvZ0xHtAkBNW6hROWus\n" + 
    		"sxA12x5f2pWq4zvPU1+EKBFJh8LuACsmDFSd2fvwKuaJWeC2CzW+S/CRx5EFEUM6\n" + 
    		"AKeWtGQb3dVS";
    public static final String jira_privatekey_pem="MIICWwIBAAKBgQDDTaehCulS3o4OR0R7piNrm1FLzg9e4F5Ecq7O1CV41F4o71s2\n" + 
    		"ySg1hKyVNozurHLyvUfxaTXRRtT54AtERvOeueZzMPMdE7Q08/ToHdZivfaGXpTU\n" + 
    		"LDf6Six3hPG1ArFtoT1z5ndkbOa88DRjaEJoTxR/JfFqUaVILH9HpSGLUwIDAQAB\n" + 
    		"AoGAZ5bjx6Zq9Xp4LJieeVs4xFWgfC2yt2cYeQO/NpMNwRYbT2kNn3R4sFJL2+UQ\n" + 
    		"QXQUkC3XX/OnCPah9ltgfnmHs4aNVCqDX7WQkLv/iP6G4ahAsbZVoeLDooziU0C/\n" + 
    		"IZDBj2xSLUY+zowdi+lXA07n6gONyeACsBQA3gzigQAjjckCQQDpDpDXeDRgh70y\n" + 
    		"qJiUcdaHMkyF1ez6HahmPCxViESrrwGrtH93BJ8S87QgV7Mr16P6i6YviOHKWiiA\n" + 
    		"eAPd0sNvAkEA1oeibRpMgUx8nI3iil+H9MrEFj6nnyz6uhCI6DzfnLEwatsJNw6i\n" + 
    		"5KGHzNfYWfgunMDHPq4c+JzRjObPVwI0XQJAU1SS84gWakF+o8NmswU1KKgSdnQ0\n" + 
    		"RIYgNVXkoDyWGTD9DvD99odffX5H9JyHAvEav7X5GNv/p8Ou4aiH8WOv3wJAc9fI\n" + 
    		"CLwjxcpEm9EuHGRbqP75Bb4Ni7GhLmoRU77TTOyNZZYjkrxvj5/+EcoYRPT0Tqb2\n" + 
    		"sE0azdD4gup72dMR7QJATVuoUTlrrLMQNdseX9qVquM7z1NfhCgRSYfC7gArJgxU\n" + 
    		"ndn78CrmiVngtgs1vkvwkceRBRFDOgCnlrRkG93VUg==";
    // public static final String other_private_key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDFkPMZQaTqsSXI+bSI65rSVaDzic6WFA3WCZMVMi7lYXJAUdkXo4DgdfvEBO21Bno3bXIoxqS411G8S53I39yhSp7z2vcB76uQQifi0LEaklZfbTnFUXcKCyfwgKPp0tQVA+JZei6hnscbSw8qEItdc69ReZ6SK+3LHhvFUUP1nLhJDsgdPHRXSllgZzqvWAXQupGYZVANpBJuK+KAfiaVXCgA71N9xx/5XTSFi5K+e1T4HVnKAzDasAUt7Mmad+1PE+56Gpa73FLk1Ww+xaAEvss6LehjyWHM5iNswoNYzrNS2k6ZYkDnZxUlbrPDELETbz/n3YgBHGUlyrXi2PBjAgMBAAECggEAAtMctqq6meRofuQbEa4Uq5cv0uuQeZLV086VPMNX6k2nXYYODYl36T2mmNndMC5khvBYpn6Ykk/5yjBmlB2nQOMZPLFPwMZVdJ2Nhm+naJLZC0o7fje49PrN2mFsdoZeI+LHVLIrgoILpLdBAz/zTiW+RvLvMnXQU4wdp4eO6i8J/Jwh0AY8rWsAGkk1mdZDwklPZZiwR3z+DDsDwPxFs8z6cE5rWJd2c/fhAQrHwOXyrQPsGyLHTOqS3BkjtEZrKRUlfdgV76VlThwrE5pAWuO0GPyfK/XCklwcNS1a5XxCOq3uUogWRhCsqUX6pYfAVS6xzX56MGDndQVlp7U5uQKBgQDyTDwhsNTWlmr++FyYrc6liSF9NEMBNDubrfLJH1kaOp590bE8fu3BG0UlkVcueUr05e33Kx1DMSFW72lR4dht1jruWsbFp6LlT3SUtyW2kcSet3fC8gySs2r6NncsZ2XFPoxTkalKpQ1atGoBe3XIKeT8RDZtgoLztQy7/7yANQKBgQDQvSHEKS5SttoFFf4YkUh2QmNX5m7XaDlTLB/3xjnlz8NWOweK1aVysb4t2Tct/SR4ZZ/qZDBlaaj4X9h9nlxxIMoXEyX6Ilc4tyCWBXxn6HFMSa/Rrq662Vzz228cPvW2XGOQWdj7IqwKO9cXgJkI5W84YtMtYrTPLDSjhfpxNwKBgGVCoPq/iSOpN0wZhbE1KiCaP8mwlrQhHSxBtS6CkF1a1DPm97g9n6VNfUdnB1Vf0YipsxrSBOe416MaaRyUUzwMBRLqExo1pelJnIIuTG+RWeeu6zkoqUKCAxpQuttu1uRo8IJYZLTSZ9NZhNfbveyKPa2D4G9B1PJ+3rSO+ztlAoGAZNRHQEMILkpHLBfAgsuC7iUJacdUmVauAiAZXQ1yoDDo0Xl4HjcvUSTMkccQIXXbLREh2w4EVqhgR4G8yIk7bCYDmHvWZ2o5KZtD8VO7EVI1kD0z4Zx4qKcggGbp2AINnMYqDetopX7NDbB0KNUklyiEvf72tUCtyDk5QBgSrqcCgYEAnlg3ByRd/qTFz/darZi9ehT68Cq0CS7/B9YvfnF7YKTAv6J2Hd/i9jGKcc27x6IMi0vf7zrqCyTMq56omiLdu941oWfsOnwffWRBInvrUWTj6yGHOYUtg2z4xESUoFYDeWwe/vX6TugL3oXSX3Sy3KWGlJhn/OmsN2fgajHRip0\=";
    
    private final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(JIRA_HOME, "https://test:8443")
            .put(CONSUMER_KEY, "OauthKey")
            .put(PRIVATE_KEY, jira_privatekey_pcks8).build();

    private final String fileUrl;
    private final String propFileName = "config.properties";

    public PropertiesClient() throws Exception {
        fileUrl = "./" + propFileName;
    }

    public Map<String, String> getPropertiesOrDefaults() {
        try {
            Map<String, String> map = toMap(tryGetProperties());
            map.putAll(Maps.difference(map, DEFAULT_PROPERTY_VALUES).entriesOnlyOnRight());
            return map;
        } catch (FileNotFoundException e) {
            tryCreateDefaultFile();
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        } catch (IOException e) {
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        }
    }

    private Map<String, String> toMap(Properties properties) {
        return properties.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(o -> o.getKey().toString(), t -> t.getValue().toString()));
    }

    private Properties toProperties(Map<String, String> propertiesMap) {
        Properties properties = new Properties();
        propertiesMap.entrySet()
                .stream()
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
        return properties;
    }

    private Properties tryGetProperties() throws IOException {
        InputStream inputStream = new FileInputStream(new File(fileUrl));
        Properties prop = new Properties();
        prop.load(inputStream);
        System.out.println("Loading properties...");
        printProperties(prop);
        return prop;
    }

    public void savePropertiesToFile(Map<String, String> properties) {
        OutputStream outputStream = null;
        File file = new File(fileUrl);

        try {
            outputStream = new FileOutputStream(file);
            Properties p = toProperties(properties);
            System.out.println("Saving properties...");
            printProperties(p);
            p.store(outputStream, null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            closeQuietly(outputStream);
        }
    }

    public void tryCreateDefaultFile() {
        System.out.println("Creating default properties file: " + propFileName);
        tryCreateFile().ifPresent(file -> savePropertiesToFile(DEFAULT_PROPERTY_VALUES));
    }

    private Optional<File> tryCreateFile() {
        try {
            File file = new File(fileUrl);
            file.createNewFile();
            return Optional.of(file);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // ignored
        }
    }
    
    private void printProperties(Properties p){
    	p.list(System.out);
    }
}
