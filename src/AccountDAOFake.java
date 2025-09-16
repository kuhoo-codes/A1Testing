import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class AccountDAOFake implements IAccountDAO {
	/*
	 * A full in-memory fake of AccountDAO.
	 * 
	 * IMPORTANT NOTE:
	 * If you take advantage of the Account class for simulating an in-memory database,
	 * make sure that your storage representation relies on fully cloned objects, not just Account references passed.
	 * Tests should not be able to distinguish this object from a real DAO object connected to a real DB. 
	 * In this sense, this class is a full fake that works with all inputs. 
	 */
	private final Map<String, Account> accounts = new HashMap<>();
		
	public boolean isFullFake() {
		return true;
	}
	
	private Account clone(Account a) {
        return (a == null) ? null : a.clone();
    }

	public void save(Account member) {
		// implement this method
		if (member != null) {
			accounts.put(member.getUserName(), clone(member));
		}
	}

	public Account findByUserName(String userName)  {
		Account stored = accounts.get(userName);
		return clone(stored);
	}
	
	public Set<Account> findAll()  {
		Set<Account> out = new HashSet<>();
        for (Account a : accounts.values()) {
            out.add(clone(a));
        }
        return out;
	}

	public void delete(Account member) {
		if (member == null) return;
        accounts.remove(member.getUserName());
	}

	public void update(Account member) {
		if (member == null) return;
        accounts.put(member.getUserName(), clone(member));
	}

}
