package net.glowstone.io.nosql;

public class MigrationSingleton {

    String migration = "";

    /**
     * Singleton constructor.
     */
    private MigrationSingleton() {}

    private static MigrationSingleton INSTANCE = null;

    /**
     * Singleton.
     *  
     * @return singleton instance
     */
    public static synchronized MigrationSingleton getInstance() {           
        if (INSTANCE == null) {   
            INSTANCE = new MigrationSingleton(); 
        }
        return INSTANCE;
    }

    /**
     * Getter.
     * 
     * @return migration string value
     */
    public String getMigrationValue() {
        return this.migration;
    }

    /**
     * Set migration value.
     * 
     * @param migration string value
     */
    public void setMigrationValue(String migration) {
        this.migration = migration;
    }

}
