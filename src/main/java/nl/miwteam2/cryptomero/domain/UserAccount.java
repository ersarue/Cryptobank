package nl.miwteam2.cryptomero.domain;

/**
 * Model UserAccount
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */
public class UserAccount {
  private int idAccount;
  private String email;
  private String password;

  public UserAccount(int idAccount, String email, String password) {
	this.idAccount = idAccount;
	this.email = email;
	this.password = password;
  }

  public UserAccount(String email, String password) {
	this(0, email, password);
  }

  public UserAccount() {
	this(0, "", "");
  }

  @Override
  public String toString() {
	return "UserAccount{" + "idAccount=" + idAccount + ", email='" + email + '\'' + ", password='"
			+ password + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;

	UserAccount that = (UserAccount) o;

	if (idAccount != that.idAccount) return false;
	if (!email.equals(that.email)) return false;
	return password.equals(that.password);
  }

  @Override
  public int hashCode() {
	int result = idAccount;
	result = 31 * result + email.hashCode();
	result = 31 * result + password.hashCode();
	return result;
  }

  public int getIdAccount() {
	return idAccount;
  }

  public void setIdAccount(int idAccount) {
	this.idAccount = idAccount;
  }

  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
	this.email = email;
  }

  public String getPassword() {
	return password;
  }

  public void setPassword(String password) {
	this.password = password;
  }
}
