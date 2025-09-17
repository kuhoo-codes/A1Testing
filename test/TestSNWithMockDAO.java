import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestSNWithMockDAO extends TestSNAbstractGeneric {

	private IAccountDAO mockDAO;
	
	@Override @Before
	public void setUp() throws Exception {
		// whatever you need to do here
		mockDAO = mock(IAccountDAO.class);
		this.accountDAO = mockDAO;
		sn = new SocialNetwork(mockDAO);
		super.setUp();
	}
	
	/* 
	 * Generic tests are automatically inherited from abstract superclass - they should continue to work here! 
	 */
	
	/* 
	 * VERIFICATION TESTS
	 * 
	 * These tests use a mock and verify that persistence operations are called. 
	 * They ONLY ensure that the right persistence operations of the mocked IAccountDAO implementation are called with
	 * the right parameters. They need not and cannot verify that the underlying DB is actually updated. 
	 * They don't verify the state of the SocialNetwork either. 
	 * 
	 */
	
	@Test public void willAttemptToPersistANewAccount() throws UserExistsException {
		// make sure that when a new member account is created, it will be persisted 
		Account newAccount = sn.join("A new Member");
		verify(mockDAO).save(newAccount);
	}
	
	@Test public void willAttemptToPersistSendingAFriendRequest() 
        throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		// make sure that when a logged-in member issues a friend request, any changes to the affected accounts will be persisted
		when(mockDAO.findByUserName(m1.getUserName())).thenReturn(m1);
		when(mockDAO.findByUserName(m2.getUserName())).thenReturn(m2);
		sn.login(m1);
		sn.sendFriendshipTo(m2.getUserName());
		verify(mockDAO).update(m1);
		verify(mockDAO).update(m2);
	}
	
	@Test public void willAttemptToPersistAcceptanceOfFriendRequest() 
        throws UserNotFoundException, UserExistsException, NoUserLoggedInException {

		when(mockDAO.findByUserName(m1.getUserName())).thenReturn(m1);
		when(mockDAO.findByUserName(m2.getUserName())).thenReturn(m2);

		sn.login(m1);
		sn.sendFriendshipTo(m2.getUserName());
		sn.logout();
		sn.login(m2);
		sn.acceptFriendshipFrom(m1.getUserName());
		verify(mockDAO, times(2)).update(m1);
		verify(mockDAO, times(2)).update(m2);

		verify(mockDAO, never()).delete(any(Account.class));
		
		}
	
	@Test public void willAttemptToPersistRejectionOfFriendRequest() 
        throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		when(mockDAO.findByUserName(m1.getUserName())).thenReturn(m1);
		when(mockDAO.findByUserName(m2.getUserName())).thenReturn(m2);

		sn.login(m1);
		sn.sendFriendshipTo(m2.getUserName());
		sn.logout();
		sn.login(m2);
		sn.rejectFriendshipFrom(m1.getUserName());
		verify(mockDAO, times(2)).update(m1);
		verify(mockDAO, times(2)).update(m2);

		verify(mockDAO, never()).delete(any(Account.class));
		
	}
	
	@Test public void willAttemptToPersistBlockingAMember() 
        throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		
		when(mockDAO.findByUserName(m1.getUserName())).thenReturn(m1);
		when(mockDAO.findByUserName(m2.getUserName())).thenReturn(m2);

		sn.login(m1);
		sn.block(m2.getUserName());
		verify(mockDAO, times(1)).update(m1);
		verify(mockDAO, times(1)).update(m2);

		verify(mockDAO, never()).delete(any(Account.class));
	}
		
	@Test public void willAttemptToPersistLeavingSocialNetwork() 
        throws UserExistsException, UserNotFoundException, NoUserLoggedInException {
		
		when(mockDAO.findByUserName(m1.getUserName())).thenReturn(m1);
		when(mockDAO.findByUserName(m2.getUserName())).thenReturn(m2);

		sn.login(m1);
		sn.sendFriendshipTo(m2.getUserName());
		sn.logout();
		sn.login(m2);
		sn.acceptFriendshipFrom(m1.getUserName());
		sn.leave(); // m2 leaves


		verify(mockDAO, times(3)).update(m1);
		verify(mockDAO, times(2)).update(m2);
		verify(mockDAO, times(1)).delete(m2);

		verify(mockDAO).delete(any(Account.class));


		}

}
