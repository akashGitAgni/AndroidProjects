import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.csvreader.CsvReader;

public class SendEmails {

	HashMap<String, String> scoreList = new HashMap<>();

	String message = "\n Following is the score for HW2 ,"
			+ " if you have any questions please contact the GTF \n \n";

	public SendEmails() {
		readScores();
		sendMail();

	}

	public void sendMail() {

		String from = "-------";
		String host = "mail.cs.uoregon.edu";
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // for gmail use smtp.gmail.com
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		try {

			Session mailSession = Session.getInstance(props,
					new javax.mail.Authenticator() {

						protected PasswordAuthentication getPasswordAuthentication() {
							String username = "------";
							String password = "-------";
							return new PasswordAuthentication(username,
									password);
						}
					});

			for (String emails : scoreList.keySet()) {
				System.out.println(emails);
				MimeMessage message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(emails));
				message.setSubject("CIS 315 HW2 GRADE");
				message.setText(scoreList.get(emails));
				Transport.send(message);
				System.out.println(scoreList.get(emails));
				System.out
						.println("Sent-------------------------------------------------------");
			}

		} catch (MessagingException mex) {
			mex.printStackTrace();
			System.out.println("Could not send....");
		}
	}

	public void readScores() {

		try {

			CsvReader scores = new CsvReader("testgrading.csv");

			scores.readHeaders();

			while (scores.readRecord()) {
				String email = parseEmail(scores.get("Email"));
				String points = scores.get("Score");
				String comments = scores.get("Comments");
				String body = message + points + "/100" + "\n";
				if (comments != null && comments.length() > 0)
					body = body + "Comments:" + comments + "\n";
				System.out.println(email + "  " + points);
				scoreList.put(email, body);
			}

			scores.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String parseEmail(String from) {
		StringBuilder s = new StringBuilder();

		int start = from.indexOf('<');
		int end = from.indexOf('>');
		for (int i = start + 1; i < end; i++) {
			s.append(from.charAt(i));
		}

		return s.toString();

	}

	public static void main(String[] args) {
		new SendEmails();
	}

}
