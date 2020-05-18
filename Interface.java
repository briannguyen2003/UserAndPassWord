package UserAndPassWord;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
public class Interface extends UserAccount {
	private static File accountFile = new File("AccountList.txt");
	private static ArrayList<UserAccount> accountList = new ArrayList<UserAccount>();
	private static int amountAccounts = -1;
	
	public static void main(String[] args) {
		try {
			scanAccountFile();
			begin();
		} catch (Exception e) {
			System.out.println("Err");
			System.exit(0);
		}
	}
	public static void scanAccountFile() {
		accountList.clear();
		try {
			Scanner scFile;
			scFile = new Scanner(accountFile);
			scFile.useDelimiter(":|\n");
			ArrayList<String> data = new ArrayList<String>();
			while(scFile.hasNext()) {
				data.add(scFile.next());
			}
			for(int i = 0; i < data.size(); i+=3) {
				UserAccount account = new UserAccount(data.get(i),data.get(i + 1),data.get(i + 2));
				accountList.add(account);
			}
			amountAccounts = data.size()/3;
		} catch (FileNotFoundException e) {
			System.out.println("Error: No account file.");
			System.exit(0);
		}
	}
	public static void begin() {
		boolean cont = true;
		while(cont) {
			System.out.println("Would you like to: ");
			System.out.println("1 : Login");
			System.out.println("2 : Create an Account");
			Scanner input = new Scanner(System.in);
			System.out.print("Enter option: ");
			String iString = input.next();
			try {
				int i = Integer.parseInt(iString);
				if(i == 1) {
					login();
					break;
				}		
				else if(i == 2) {
					createAccount();
					break;
				}
				else {
					System.out.println("Not a valid option, try again.");
					cont = true;
				}
			} catch(Exception ex) {
				System.out.println("Not a valid option, try again.");
				cont = true;
			}
		}
	}
	public static void login() throws FileNotFoundException {
		scanAccountFile();
		Scanner input = new Scanner(System.in);
		System.out.print("Enter your Username / Email: ");
		String user = input.next();
		while(correctUser(user) == false) {
			System.out.print("This is not a valid username / email. Please enter a valid one: ");
			user = input.next();
		}
		int accountIndex = getAccount(user);
		System.out.print("Enter your password: ");
		String pass = input.next();
		UserAccount account = accountList.get(accountIndex);
		while(pass.contentEquals(account.getPassword()) == false) {
			System.out.println("Incorret Password. Try Again, Enter Password: ");
			pass = input.next();
		}
		correctlogin(account, accountIndex);
	}
	public static void createAccount() {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter your email: ");
		String email = input.next();
		while(validEmail(email) == false) {
			System.out.println("Invalid Email Address: Your Email is taken.");
			System.out.print("Use a different email: ");
			email = input.next();
		}
		System.out.print("Enter your username: ");
		String username = input.next();
		while(validUser(username) == false) {
			System.out.println("Invalid User Name: Your username is taken.");
			System.out.print("Use a different username: ");
			username = input.next();
		}
		System.out.print("Enter your password: ");
		String password = input.next();
		while(validPassWord(password) == false) {
			System.out.println("Invalid Pass Word. Your password must have:");
			System.out.println("- At least 1 number");
			System.out.println("- At least 1 special character : !/@/#/$/&/_");
			System.out.println("- At least 1 Uppercase and 1 Lowercase letter");
			System.out.print("Use a different password: ");
			password = input.next();
		}
		UserAccount account = new UserAccount(username, password, email);
		accountList.add(account);
		try {
			addToFile(account);
		} catch (Exception e) {
			System.out.println("Error");
			System.exit(0);
		}
		System.out.println("Thank you for making an account with us!");
		amountAccounts++;
		scanAccountFile();
		begin();
	}
	public static void addToFile(UserAccount account) throws IOException {
		String s = account.getUsername() + ":" + account.getPassword()+ ":" + account.getEmail();
		    Files.write(Paths.get("AccountList.txt"), s.getBytes(), StandardOpenOption.APPEND);
		    Files.write(Paths.get("AccountList.txt"), "\n".getBytes(), StandardOpenOption.APPEND);
	}
	public static boolean validEmail(String email) {
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getEmail();
			if(email.contentEquals(compare)) {
				return false;
			}
		}
		return true;
	}
	public static boolean validUser(String user) {
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getUsername();
			if(user.contentEquals(compare)) {
				return false;
			}
		}
		return true;
	}
	public static boolean validPassWord(String password) {
		boolean isUpper = false;
		boolean isLower = false;
		boolean isNumber = false;
		boolean isSpecial = false;
		for(int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			String b = c + "";
				if(isUpperCase(c)) {
					isUpper = true;
				}
				else if(isLowerCase(c)) {
					isLower = true;
				}
				else if(isNumber(c)) {
					isNumber = true;
				}
				else if(isSpecialChar(c)) {
					isSpecial = true;
				}
		}
		return isUpper && isLower && isNumber && isSpecial;
	}
	public static boolean isUpperCase(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}
	public static boolean isLowerCase(char ch) {
		return ch >= 'a' && ch <= 'z';
	}
	public static boolean isNumber(char ch) {
		return Character.isDigit(ch);
	}
	public static boolean isSpecialChar(char ch) {
		boolean result = false;
		switch(ch) {
		case '!':
			result = true;
			break;
		case '@':
			result = true;
			break;
		case '#':
			result = true;
			break;
		case '$':
			result = true;
			break;
		case '&':
			result = true;
			break;
		case '_':
			result = true;
			break;
		default:
			result = false;
		}
		return result;
	}
	public static boolean correctUser(String user) {
		boolean bol = false;
		boolean bol2 = false;
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getEmail();
			if(user.contentEquals(compare)) {
				bol = true;
			}
		}
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getUsername();
			if(user.contentEquals(compare)) {
				bol2 = true;
			}
		}
		return bol || bol2;
	}
	public static int getAccount(String user) {
		int correctNum = -1;
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getEmail();
			if(user.contentEquals(compare)) {
				correctNum = i;
			}
		}
		for(int i = 0; i < amountAccounts; i++) {
			UserAccount y = accountList.get(i);
			String compare = y.getUsername();
			if(user.contentEquals(compare)) {
				correctNum = i;
			}
		}
		return correctNum;
	}
	public static void correctlogin(UserAccount account, int accountIndex) {
		System.out.println("Hello, " + account.getUsername());
	}
}
