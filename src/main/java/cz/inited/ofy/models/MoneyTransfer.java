package cz.inited.ofy.models;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.Key;

@Entity(name = "MoneyTransfer")
public class MoneyTransfer {

	@Id
	Long key;

	long amount;

	Key<MoneyAccount> from_title;

	Key<MoneyAccount> to_title;

	String title;

	Date date;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Key<MoneyAccount> getFrom_title() {
		return from_title;
	}

	public void setFrom_title(Key<MoneyAccount> from_title) {
		this.from_title = from_title;
	}

	public Key<MoneyAccount> getTo_title() {
		return to_title;
	}

	public void setTo_title(Key<MoneyAccount> to_title) {
		this.to_title = to_title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}



}
