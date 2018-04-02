package net.glowstone.io.nosql;

public class MigrationSingleton {
	
		String migration = "";

	    private MigrationSingleton()
	    {}
	     

	    private static MigrationSingleton INSTANCE = null;
	     

	    public static synchronized MigrationSingleton getInstance()
	    {           
	        if (INSTANCE == null)
	        {   INSTANCE = new MigrationSingleton(); 
	        }
	        return INSTANCE;
	    }
	    
	    
	    public String getMigrationValue() {
	        return this.migration;
	    }
	    
	    public void setMigrationValue(String migration) {
	    	this.migration = migration;
	    }
	    
	

}
