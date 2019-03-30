package com.github.tylerhaigh.tourofheros.cucumber;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/" },
        glue = "com.github.tylerhaigh.tourofheros.cucumber.steps",
        strict = false,
        plugin = {"json:e2e-reports/results.json"}
)
public class CucumberTest {

    private static MuleEmbeddedServer MULE_SERVER_INSTANCE = null;
    public static MuleEmbeddedServer muleServer() {
        if (MULE_SERVER_INSTANCE == null) {
            System.out.println("=== CREATING A NEW MULE SERVER INSTANCE");
            MULE_SERVER_INSTANCE = new MuleEmbeddedServer();
        }

        return MULE_SERVER_INSTANCE;
    }

    @BeforeClass()
    public static void initTestEnvironment() {
        System.out.println("=== Initialize Test Environment");
        //muleServer().startMuleServer(CONFIG_RESOURCES);
        System.out.println("=== Test Environment initialized.");
    }

    @AfterClass()
    public static void destroyTestEnvironment() throws Throwable {
        System.out.println("=== Destroy Test Environment.");
        MuleEmbeddedServer.killMule();
        System.out.println("=== Test Environment destroyed.");
    }

}
