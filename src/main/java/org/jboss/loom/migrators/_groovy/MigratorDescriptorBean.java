package org.jboss.loom.migrators._groovy;


import java.io.File;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

/**
 *
 *  @author Ondrej Zizka, ozizka at redhat.com
 */
@XmlRootElement( name="migrator" )
@XmlAccessorType( XmlAccessType.NONE )
public class MigratorDescriptorBean extends ContainerOfStackables {
    private static final Logger log = LoggerFactory.getLogger( MigratorDescriptorBean.class );
    
    @XmlAttribute
    String name;

    //@XmlElementWrapper(name = "jaxbBeans")
    @XmlElement(name = "jaxbBean")
    //@XmlPath("jaxbBean/@file")
    List<JaxbClassDef> jaxbBeansClasses;
    
    @XmlElement(name = "xmlQuery")
    List<XmlFileQueryDef> xmlQueries;
    
    @XmlElement(name = "propQuery")
    List<PropFileQueryDef> propQueries;
    
    //@XmlElement(name = "action")
    //List<ActionDef> actions;
    
    File fileOfOrigin;
    

    
    // === Subelement classes === //
    
    
    @XmlRootElement
    @XmlAccessorType( XmlAccessType.NONE )
    public static class JaxbClassDef {
        @XmlAttribute(name = "file")
        //@XmlJavaTypeAdapter( StringToFileAdapter.class )
        public File file;
    }
    

    @XmlRootElement
    public static class ActionDef extends ContainerOfStackables {
        @XmlAttribute
        public String type;
        
        //public List<PropertyBean> properties;
        @XmlAnyAttribute
        public Map<String, String> attribs;
    }
    
    @XmlRootElement
    public static class ForEachDef extends ContainerOfStackables {
    }

    
    @XmlRootElement
    public static class XmlFileQueryDef extends FileQueryBase {
        public Class jaxbBean;
        public String xpath;
    }

    @XmlRootElement
    public static class PropFileQueryDef extends FileQueryBase {
        public String propNameMask;
    }
    
    
    private static class FileQueryBase extends QueryBase {
        public String pathMask;     // Path mask of the files to load.
    }
    
    private static class QueryBase {
        public String id;           // Id under which the result will be stored.
        public String subjectLabel; // What's being loaded - for exceptions and logging.
    }
    
}// class
