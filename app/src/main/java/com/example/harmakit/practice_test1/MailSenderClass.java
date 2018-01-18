package com.example.harmakit.practice_test1;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.content.SharedPreferences;

public class MailSenderClass extends javax.mail.Authenticator {

	public static final String APP_PREFERENCES = "mysettings";
	public static final String APP_PREFERENCES_LOGIN = "login";
	public static final String APP_PREFERENCES_FIRSTNAME = "firstname";
	public static final String APP_PREFERENCES_LASTNAME = "lastname";
	public static final String APP_PREFERENCES_PATRONYMIC = "patronymic";
	public static final String APP_PREFERENCES_COMPANY_NAME = "company_name";
	public static final String APP_PREFERENCES_CLIENT = "client";

	private String mailhost = "smtp.gmail.com";
	private String user;
	private String password;
	private javax.mail.Session session;

	private Multipart _multipart; 
	
	static {
		Security.addProvider(new com.example.harmakit.practice_test1.JSSEProvider());
	}

	Context context;

	public MailSenderClass(String user, String password, Context ctx) {

		context = ctx;

		this.user = user;
		this.password = password;

		final String muser = user;
		final String mpassword = password;

		_multipart = new MimeMultipart(); 

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		session = javax.mail.Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(muser, mpassword);
					}
				});
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
	}


	public synchronized void sendMail(String subject, String body, String sender, String recipients, String filename) throws Exception {

			MimeMessage message = new MimeMessage(session);

			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients));

			BodyPart messageBodyPart = new MimeBodyPart();

			String signature = "\n____________\n" + getPrefs(context).getString(APP_PREFERENCES_CLIENT, "") + "\n"
					                              + getPrefs(context).getString(APP_PREFERENCES_LOGIN, "")+ "\n"
												+ getPrefs(context).getString(APP_PREFERENCES_FIRSTNAME, "")+ "\n"
												+ getPrefs(context).getString(APP_PREFERENCES_LASTNAME, "")+ "\n"
												+ getPrefs(context).getString(APP_PREFERENCES_PATRONYMIC, "")+ "\n"
												+ getPrefs(context).getString(APP_PREFERENCES_COMPANY_NAME, "")+ "\n";
			body = body + signature;
			messageBodyPart.setText(body);
			_multipart.addBodyPart(messageBodyPart);

			if (!filename.equalsIgnoreCase("")) {
				BodyPart attachBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(filename);
				attachBodyPart.setDataHandler(new DataHandler(source));
				attachBodyPart.setFileName(filename);

				_multipart.addBodyPart(attachBodyPart);
			}
			
			message.setContent(_multipart);

			Transport.send(message);

	}
}
