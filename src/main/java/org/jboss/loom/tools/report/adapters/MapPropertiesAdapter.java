package org.jboss.loom.tools.report.adapters;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  @author Ondrej Zizka, ozizka at redhat.com
 */
public class MapPropertiesAdapter extends XmlAdapter<MapPropertiesAdapter.Properties, Map<String,String>> {
    private static final Logger log = LoggerFactory.getLogger( MapPropertiesAdapter.class );

    @Override public Properties marshal( Map<String, String> map ) throws Exception {
        if( map == null )
            return null;
        
        List<Property> ret = new ArrayList( map.size() );
        for( Map.Entry<String, String> entry : map.entrySet() ) {
            ret.add( new Property( entry.getKey(), entry.getValue() ) );
        }
        return new Properties( ret );
    }

    
    // Bean
    // JAXB (at least MOXy) can't deal with Map -> List adapter, needs a special wrapping object.
    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=410160
    @XmlRootElement
    @XmlAccessorType( XmlAccessType.FIELD )
    public static final class Properties {
        
        @XmlElement(name = "property")
        private List<Property> props;

        public Properties() { props = new LinkedList(); }
        public Properties( List<Property> props ) {
            this.props = props;
        }
    }

    
    @XmlRootElement
    @XmlAccessorType( XmlAccessType.NONE )
    public static final class Property {
        @XmlAttribute
        public String name;
        @XmlAttribute
        public String value;

        public Property() {
        }
        
        public Property( String name, String value ) {
            this.name = name;
            this.value = value;
        }
    }
    
    
    @Override public Map<String, String> unmarshal( Properties v ) throws Exception {
        throw new UnsupportedOperationException( "Not supported." );
    }
    
}// class