package accounts.web;

import accounts.AccountManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rewards.internal.account.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A Spring MVC REST Controller handling requests to retrieve Account information.
 *
 * Note that some of the Account related classes are imported from the
 * rewards-db project:
 *
 * -Domain objects: Account and Beneficiary
 * -Service layer: AccountManager interface
 * -Repository layer: AccountRepository interface
 *
 */
// TO-DO-03: Add an appropriate annotation to make this class a REST controller
@RestController
@AllArgsConstructor
@Getter
public class AccountController {

	@Autowired
	private final AccountManager accountManager;

	/**
	 * Creates a new AccountController with a given account manager.
	 */
//	@Autowired
//	public AccountController(AccountManager accountManager) {
//		this.accountManager = accountManager;
//	}

	/**
	 * Return a list of all accounts
	 */
	// TO-DO-04: Add an appropriate annotation to make this method handle "/accounts"
	@GetMapping(value="/accounts")
	public List<Account> accountList() {

		// TO-DO-05: Implement the logic to find and return all accounts
		// - Use "accountManger" object to get all accounts
		// - Recompile this class if necessary, and wait for the application to restart (via devtools)
		// - From the home page, click the link - this should now work
		// - If you prefer, access http://localhost:8080/accounts using curl or Postman

		return accountManager.getAllAccounts(); // REPLACE THIS LINE to return a list accounts
		
		// TO-DO-06: (If you are using STS) We are about to make lots of
		//          changes, so stop the application otherwise Devtools
		//          will keep restarting it.
	}

	// TO-DO-08: Implement the /accounts/{entityId} request handling method.
	// - Call the method accountDetails().
	// - Annotate to define URL mapping /accounts/{entityId}
	//   this method will respond to.
	// - Use a method parameter to obtain the URI template parameter
	//   needed to retrieve an account.
	// - Use the accountManager to obtain an account. This is the value to return
	// - Save all work.

	@GetMapping(value="/accounts/{entityId}")
	public Account getAccount(@PathVariable Long entityId)
	{
		return accountManager.getAccount(entityId);
	}

	// TO-DO-10b: If AccountControllerTests.testHandleDetailsRequest()
	//  fails, fix errors before moving on

	// TO-DO-11: Run the application
	// - You should now be able to invoke http://localhost:8080/accounts/N
	//   where N is 0-20 and get a response. You can use curl, Postman or
	//   your browser to do this.

}
