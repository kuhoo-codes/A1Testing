public class DAOFactory {
	
	/* Creates instances of Account Data Access Objects.
	 * DAOs interface with the persistent store, or DB.
	 */
	
	private static DAOFactory instance = null;
	private static IAccountDAO accountDAOImplementation = new AccountDAO("AccountDatabase");
	
	public static DAOFactory getInstance() {
		// implementing a singleton
		if (instance == null) {
				instance = new DAOFactory();
		}
		return instance;
	}
	
	public IAccountDAO getAccountDAO() {
		return accountDAOImplementation;
	}

	public IAccountDAO getAccountDAOFake() {
		return new AccountDAOFake();
	}
	
	public void setAccountDAOMock(IAccountDAO mockDAO) {
		/* this should inject a Mockito mock can be used as a test double
		 that is custom configured in the tests, 
		 like this: setAccountDAOMock(mock(IAccountDAO.class)); 
		 */
		accountDAOImplementation = mockDAO;
	}
	
	public IAccountDAO getAccountDAOMock() {
		/* use this to get the mock object
		 */
		return accountDAOImplementation;
	}
	
	public static boolean isMock (IAccountDAO dao) {
		/*
		 *  with this method, you can check whether an IAccountDAO object is a mock object
		 */
		if (dao == null) return false;
		return !(dao instanceof AccountDAOFake) && !(dao instanceof AccountDAO);
	}

}
