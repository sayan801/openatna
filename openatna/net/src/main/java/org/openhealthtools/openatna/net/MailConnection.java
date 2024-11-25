package org.openhealthtools.openatna.net;

import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Properties;

public class MailConnection {

    private final Logger logger = LoggerFactory.getLogger(MailConnection.class);

    IConnectionDescription description;
    PropertySet smtp;
    PropertySet pop3;
    PropertySet senderKeystore;
    Session session;
    Transport transport;
    Store store = null;
    Folder inbox = null;
    String senderKeystoreFile = null;
    String senderKeystorePassword = null;
    String senderKeyAlias = null;

    MailConnection(IConnectionDescription description) {
        this.description = description;
        // Create a connection for sending the message
        Properties props = new Properties();
        // fill props with any information
        session = Session.getInstance(props, null);        // Make the call and catch the output
        smtp = description.getPropertySet("smtp");
        pop3 = description.getPropertySet("pop3");
        senderKeystore = description.getPropertySet("senderKeystore");
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            logger.error("Transport misconfigured, no smtp provider.", e);
            transport = null;
            smtp = null;
        }
        try {
            store = session.getStore("pop3");
        } catch (NoSuchProviderException e) {
            logger.error("Transport misconfigured, no smtp provider.");
            transport = null;
            smtp = null;
        }
    }

    public void sendMessage(Message message) throws MessagingException {
        if (transport != null && smtp != null) {
            transport.connect(smtp.getValue("HOSTNAME"), smtp.getValue("USERNAME"), smtp.getValue("PASSWORD"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } else {
            logger.error("Transport and smtp must be set before sending messages.");
            throw new MessagingException("Attempt to send to invalid smtp connection.");
        }
    }

    public Message[] retrieveAllMessages() throws MessagingException {
        Message[] messages = null;
        if (store != null && pop3 != null) {
            if (inbox == null) {
                store.connect(pop3.getValue("HOSTNAME"), pop3.getValue("USERNAME"), pop3.getValue("PASSWORD"));
                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
            }
            messages = inbox.getMessages();
        } else {
            logger.error("Store and pop3 must be set before retrieving messages.");
            throw new MessagingException("Attempt to retrieve from invalid pop3 connection.");
        }
        return messages;
    }

    // Must call when done reading data from messages.
    public void finishedWithMessages() throws MessagingException {
        inbox.close(false);
        store.close();
    }

    public MimeBodyPart decryptMessage(Message message) throws MessagingException {

        try (FileInputStream stream = new FileInputStream(getSenderKeystoreFile())) {
            /* Add BC */
            Security.addProvider(new BouncyCastleProvider());
            // Open the key store
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(stream, getSenderKeystorePassword().toCharArray());

            // find the certificate for the private key and generate a suitable recipient identifier.
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(getSenderKeyAlias());
            //RecipientId recId = new RecipientId();
//            recId.setSerialNumber(cert.getSerialNumber());
//            recId.setIssuer(cert.getIssuerX500Principal().getEncoded());

            SMIMEEnveloped m = new SMIMEEnveloped((MimeMessage) message);
            RecipientInformationStore recipients = m.getRecipientInfos();
            // TODO figure out why this doesn't work...
            //RecipientInformation        recipient = recipients.get(recId);
            RecipientInformation recipient = recipients.getRecipients().iterator().next();

            Key key = keyStore.getKey(getSenderKeyAlias(), getSenderKeystorePassword().toCharArray());
            //byte[] byteContent = recipient.getContent(key, "BC");
            return SMIMEUtil.toMimeBodyPart(new byte[]{});

        } catch (Exception e) {
            throw new MessagingException(e.getMessage());
        }
    }

    public MimeBodyPart unSignMessage(Message message) throws MessagingException {

        try {
            SMIMESigned s = new SMIMESigned((MimeMultipart) message.getContent());
            return s.getContent();
        } catch (Exception e) {
            throw new MessagingException(e.getMessage());
        }
    }


    public MimeMessage getNewMessage() {
        return new MimeMessage(session);
    }

    public Session getSession() {
        return session;
    }

    public String getSenderKeystoreFile() {
        return senderKeystore.getValue("FILE");
    }

    public String getSenderKeystorePassword() {
        return senderKeystore.getValue("PASSWORD");
    }

    public String getSenderKeyAlias() {
        return senderKeystore.getValue("ALIAS");
    }
}
