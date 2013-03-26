package cz.muni.fi.jboss.migration;

import org.apache.commons.lang.StringUtils;

/**
 * Helper class to store files which will be copied from AS5 to AS7 so they can be deleted if app fails.
 * Also helps to create modules for drivers.
 *
 * @author Roman Jakubco
 */
public class RollbackData {
    public static enum Type{
        DRIVER, LOG, RESOURCE, SECURITY, LOGMODULE
    }
    private String name;

    private Type type;

    // TODO: Change to File.
    private String targetPath;

    private String homePath;

    // Only if it is driver
    private String module;

    // Different name for jdbc driver. For Sybase and Mssql
    private String altName;

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTargetPath() { return targetPath; }
    public void setTargetPath(String targetPath) { this.targetPath = targetPath; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getHomePath() { return homePath; }
    public void setHomePath(String homePath) { this.homePath = homePath; }
    public String getAltName() { return altName; }
    //</editor-fold>
    
    
    /**
     * Setting name of the Copy Memory for drivers. In special cases altName is set for alternative JDBC driver (JTDS)
     *
     * @param name driver-class from -ds.xml file from AS5
     */
    public void deriveDriverName(String name) {
        this.name = this.altName = null;
        
        if (name.contains("postgres")) {
            this.name = "postgresql";
        }
        else if (name.contains("microsoft")) {
            this.name = "sqljdbc";
            this.altName = "jtds";
        }
        else if (name.contains("db2")) {
            this.name = "db2";
        }
        else if (name.contains("sybase")) {
            this.name = "sybase";
            this.altName = "jtds";
        }
        else if (name.contains("mysql")) {
            this.name = "sqljdbc";
        }
        else if (name.contains("oracle")) {
            this.name = "ojdbc";
        }
        else if (name.contains("hsqldb")) {
            this.name = "hsqldb";
        }
        else {
            // Guesstimate the value from the classname: org.foo.Bar -> "foo".
            String temp = StringUtils.substringAfter(name, ".");
            this.name = StringUtils.substringBefore(temp, ".");
        }
    }

    
    // TBC: Do we need this?
    //<editor-fold defaultstate="collapsed" desc="hash/eq">
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RollbackData)) return false;
        
        RollbackData that = (RollbackData) o;
        
        if(type.equals(Type.LOGMODULE)){
            if (homePath != null ? !homePath.equals(that.homePath) : that.homePath != null) return false;
            if (module != null ? !module.equals(that.module) : that.module != null) return false;
            if (targetPath != null ? !targetPath.equals(that.targetPath) : that.targetPath != null) return false;
            if (type != null ? !type.equals(that.type) : that.type != null) return false;
            
            return true;
        }
        
        if (homePath != null ? !homePath.equals(that.homePath) : that.homePath != null) return false;
        if (module != null ? !module.equals(that.module) : that.module != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (targetPath != null ? !targetPath.equals(that.targetPath) : that.targetPath != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (targetPath != null ? targetPath.hashCode() : 0);
        result = 31 * result + (homePath != null ? homePath.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        return result;
    }
    //</editor-fold>
}
