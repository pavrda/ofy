package cz.inited.ofy.controllers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.googlecode.objectify.ObjectifyService;
import cz.inited.ofy.models.MoneyAccount;
import cz.inited.ofy.utils.CustomException;

public class MoneyController {

	// zaregistruji si vsechny entity, ktere budu pouzivat
	static {
		ObjectifyService.register(MoneyAccount.class);
	}

	private static final MoneyController instance = new MoneyController();
	
	private MoneyController() {
	}

	public static MoneyController getInstance() {
		return instance;
	}
	
    public long getBalance(String title) throws CustomException {
    	MoneyAccount ac = ofy().load().type(MoneyAccount.class).id(title).now();
    	if (ac == null) {
    		throw new CustomException("Account '" + title + "' doesn't exist");
    	}
    	return ac.getBalance();
    }
    
    public MoneyAccount createMoneyAccount(String title) {
    	MoneyAccount ac = ofy().load().type(MoneyAccount.class).id(title).now();
    	if (ac != null) {
    		return ac;
    	}
    	ac = new MoneyAccount();
		ac.setTitle(title);
		ac.setBalance(0);
		ac.setLastUpdate(new Date());
		ofy().save().entity(ac).now();
		return ac;
    }
}
