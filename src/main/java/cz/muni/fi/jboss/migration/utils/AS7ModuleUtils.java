package cz.muni.fi.jboss.migration.utils;

import cz.muni.fi.jboss.migration.RollbackData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Util class for generation of module for driver and module.xml which is required in migration.
 *
 * @author Roman Jakubco
 */
public class AS7ModuleUtils {

    /**
     * Setting module for different databases.
     *
     * @return string containing generate module name
     */
    public static String createDriverModule(String driverName) {
        String module = "migration.jdbc.drivers.";
        if (driverName.contains("mysql")) {
            module = module + "mysql";
        }
        // Mssql
        if (driverName.contains("microsoft")) {
            module = module + "mssql";
        }
        if (driverName.contains("sybase")) {
            module = module + "sybase";
        }
        if (driverName.contains("postgresql")) {
            module = module + "postgresql";
        }
        if (driverName.contains("oracle")) {
            module = module + "oracle";
        }
        if (driverName.contains("hsqldb")) {
            module = module + "hsqldb";
        }
        if (driverName.contains("db2")) {
            module = module + "db2";
        }
        if (driverName.contains("jtds")) {
            module = module + "jtds";
        }

        return module;
    }

    /**
     * Method for creating module.xml for JDBC drivers, which will be copied to modules in AS7
     *
     * @param data object storing all the necessary information about driver
     * @return Document representing created module.xml for given driver
     * @throws javax.xml.parsers.ParserConfigurationException
     *          if parser cannot be initialized
     */
    public static Document createDriverModuleXML(RollbackData data) throws ParserConfigurationException {

        /**
         * module.xml for JDBC driver module
         *
         * Example of module xml,
         *  <module xmlns="urn:jboss:module:1.1" name="com.h2database.h2">
         *       <resources>
         *          <resource-root path="h2-1.3.168.jar"/>
         *       <!-- Insert resources here -->
         *       </resources>
         *       <dependencies>
         *          <module name="javax.api"/>
         *          <module name="javax.transaction.api"/>
         *          <module name="javax.servlet.api" optional="true"/>
         *       </dependencies>
         *  </module>
         */
        Document doc = createDoc();

        Element root = doc.createElement("module");
        doc.appendChild(root);

        root.setAttribute("xmlns", "urn:jboss:module:1.1");
        root.setAttribute("module", data.getModule());

        Element resources = doc.createElement("resources");
        root.appendChild(resources);

        Element resource = doc.createElement("resource-root");
        resource.setAttribute("path", data.getName());
        resources.appendChild(resource);

        Element dependencies = doc.createElement("dependencies");
        Element module1 = doc.createElement("module");
        module1.setAttribute("name", "javax.api");
        Element module2 = doc.createElement("module");
        module2.setAttribute("name", "javax.transaction.api");
        Element module3 = doc.createElement("module");
        module3.setAttribute("name", "javax.servlet.api");
        module3.setAttribute("optional", "true");

        dependencies.appendChild(module1);
        dependencies.appendChild(module2);
        dependencies.appendChild(module3);

        root.appendChild(dependencies);

        return doc;
    }
    
    
    /**
     * Method for creating module.xml for logging jar file, which will be copied to modules in AS7
     *
     * @param data object storing all the necessary information about logging jar file
     * @return Document representing created module.xml for given logging jar file
     * @throws javax.xml.parsers.ParserConfigurationException
     *          if parser cannot be initialized
     */
    public static Document createLogModuleXML(RollbackData data) throws ParserConfigurationException{
        
        Document doc = createDoc();

        Element root = doc.createElement("module");
        doc.appendChild(root);

        root.setAttribute("xmlns", "urn:jboss:module:1.1");
        root.setAttribute("module", data.getModule());

        Element resources = doc.createElement("resources");
        root.appendChild(resources);

        Element resource = doc.createElement("resource-root");
        resource.setAttribute("path", data.getName());
        resources.appendChild(resource);

        Element dependencies = doc.createElement("dependencies");
        Element module1 = doc.createElement("module");
        module1.setAttribute("name", "javax.api");
        Element module2 = doc.createElement("module");

        // Default dependencies for logging
        module2.setAttribute("name", "org.jboss.logging");
        Element module3 = doc.createElement("module");
        module3.setAttribute("name", "org.apache.log4j");
        module3.setAttribute("optional", "true");

        dependencies.appendChild(module1);
        dependencies.appendChild(module2);
        dependencies.appendChild(module3);

        root.appendChild(dependencies);

        return doc;
    }

    
    private static Document createDoc() throws ParserConfigurationException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        Document doc = builder.getDOMImplementation().createDocument(null, null, null);
        return doc;
    }
    
}
