package br.com.ciscience.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.ciscience.model.entity.impl.MyMail;

public class MyMailService {
	private static final String HOST = "smtp.gmail.com";
	private static final int PORT = 465;
	private static final boolean AUTH = true;
	private static final boolean DEBUG = true;

	public static boolean send(MyMail myMail) throws UnsupportedEncodingException {

		Properties properties = new Properties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.socketFactory.port", PORT);
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", AUTH);
		properties.put("mail.smtp.port", PORT);

		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constants.ROBO_MAIL, Constants.ROBO_MAIL_PASS);
			}
		});
		session.setDebug(DEBUG);

		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom(new InternetAddress(Constants.ROBO_MAIL, Constants.APPLICATION_NAME));
			InternetAddress[] addressTo = { new InternetAddress(myMail.getTo()) };
			mimeMessage.setRecipients(Message.RecipientType.TO, addressTo);
			mimeMessage.setSubject(myMail.getSubject());
			mimeMessage.setSentDate(new Date());
			mimeMessage.setText(myMail.getBody(), "utf-8", "html");
			Transport.send(mimeMessage);

			return true;
		} catch (MessagingException me) {
			System.out.println("MessagingException -> " + me.getMessage());
			return false;
		}

	}
}
