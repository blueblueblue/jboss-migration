package org.jboss.loom;

import java.net.UnknownHostException;
import org.jboss.loom.conf.AS7Config;
import org.jboss.loom.conf.Configuration;
import org.jboss.loom.utils.AS7CliUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.loom.categories.AS;
import org.jboss.loom.categories.EAP;
import org.jboss.loom.conf.ConfigurationValidator;
import org.jboss.loom.ex.MigrationException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 *
 * @author Ondrej Zizka, ozizka at redhat.com
 */
@RunWith(Arquillian.class)
public class ArqTest {
    
    /**
     * Test of doMigration method, of class MigratorEngine.
     */
    @Test @Category( AS.class )
    @RunAsClient
    @Ignore
    public void test_AS_510_all( /*@ArquillianResource ManagementClient client*/ ) throws Exception {
                
        Configuration conf = TestAppConfig.createTestConfig_AS_510_all();
        AS7Config as7Config = conf.getGlobal().getAS7Config();
        
        // Set the Mgmt host & port from the client;
        //as7Config.setHost( client.getMgmtAddress() );
        //as7Config.setManagementPort( client.getMgmtPort() );
        //ModelControllerClient as7client = client.getControllerClient();
        /*
           This fails for some reason:
            java.lang.RuntimeException: Provider for type class org.jboss.as.arquillian.container.ManagementClient returned a null value: org.jboss.as.arquillian.container.ManagementClientProvider@4aee4b87
                at org.jboss.arquillian.test.impl.enricher.resource.ArquillianResourceTestEnricher.lookup(ArquillianResourceTestEnricher.java:115)
                at org.jboss.arquillian.test.impl.enricher.resource.ArquillianResourceTestEnricher.resolve(ArquillianResourceTestEnricher.java:91)        
                 
           Let's resort to default values.
        */
        
        
        updateAS7ConfAsPerServerMgmtInfo( as7Config );
        
        TestAppConfig.announceMigration( conf );
        ConfigurationValidator.validate( conf );
        MigratorEngine migrator = new MigratorEngine(conf);
        migrator.doMigration();
    }
    
    
    /**
     *   With EAP 5.2.0. config.
     */
    @Test @Category( EAP.class )
    @RunAsClient
    public void test_EAP_520_production( ) throws Exception {

        Configuration conf = TestAppConfig.createTestConfig_EAP_520_production();
        updateAS7ConfAsPerServerMgmtInfo( conf.getGlobal().getAS7Config() );
        
        TestAppConfig.announceMigration( conf );
        ConfigurationValidator.validate( conf );
        MigratorEngine migrator = new MigratorEngine(conf);
        migrator.doMigration();
    }
    
    /**
     *   EAP 5.2.0, dry run.
     */
    @Test @Category( EAP.class )
    @RunAsClient
    public void testDryRun_EAP_520_production( ) throws Exception {

        Configuration conf = TestAppConfig.createTestConfig_EAP_520_production();
        conf.getGlobal().setDryRun( true );
        updateAS7ConfAsPerServerMgmtInfo( conf.getGlobal().getAS7Config() );
        
        TestAppConfig.announceMigration( conf );
        ConfigurationValidator.validate( conf );
        MigratorEngine migrator = new MigratorEngine(conf);
        migrator.doMigration();
    }
    
    /**
     *   EAP 5.2.0, with JBOSS_HOME (which should be ignored) set.
     */
    @Test @Category( EAP.class )
    @RunAsClient
    //@Ignore
    public void testJBOSS_HOME() throws Exception {

        Configuration conf = TestAppConfig.createTestConfig_EAP_520_production();
        System.setProperty("JBOSS_HOME", "/foo/bar");
        updateAS7ConfAsPerServerMgmtInfo( conf.getGlobal().getAS7Config() );
        
        TestAppConfig.announceMigration( conf );
        ConfigurationValidator.validate( conf );
        MigratorEngine migrator = new MigratorEngine(conf);
        migrator.doMigration();
    }
    
    
    
    
    
    // -- Util methods --
    
    private void updateAS7ConfAsPerServerMgmtInfo( AS7Config conf ) throws UnknownHostException, MigrationException {
        ModelControllerClient as7client = ModelControllerClient.Factory.create(conf.getHost(), conf.getManagementPort());
        updateAS7ConfAsPerServerMgmtInfo( conf, as7client );
    }
    
    private void updateAS7ConfAsPerServerMgmtInfo( AS7Config conf, ModelControllerClient as7client ) throws UnknownHostException, MigrationException {
        
        // Query for the server path.
        String as7Dir = AS7CliUtils.queryServerHomeDir( as7client );
        if( as7Dir != null )  // AS 7.1.1 doesn't define it.
            conf.setDir( as7Dir );
    }
    
    
        
}// class
