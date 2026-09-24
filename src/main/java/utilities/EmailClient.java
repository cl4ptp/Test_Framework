package utilities;

import model.email.Email;
import org.apache.commons.lang3.ArrayUtils;

import javax.mail.*;
import javax.mail.search.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Email client class that provides helpful methods to work with email:
 * <p> - get new unread messages </p>
 * <p> - delete messages </p>
 * <p> - filter messages by criteria </p>
 * <p> etc... </p>
 */
public class EmailClient {
    //  Credentials
    private static final String SERVER = System.getProperty("email.server", "InvalidEmailServer");
    private static final String USER = System.getProperty("email.username", "InvalidEmailUsername");
    private static final String PASSWORD = System.getProperty("email.password", "InvalidEmailPassword");

    //  Email properties
    private static final String STORE_NAME = "imaps";
    private static final String INBOX_FOLDER = "Inbox";
    private static final String SPAM_FOLDER = "[Gmail]/Spam";

    private static final String EMAIL_ERROR = "Unable to perform operation with email! \nDetails: ";

    /**
     * Fetch messages marked as "Unread" (unseen) from the mailbox's incoming messages
     * that was received on the given date or later.
     * <p></p>
     * <b> Note: method only filters by calendar's day,
     * and does not take the time (hours, minutes, seconds) into account! </b>
     *
     * @param receivedDate the date that new messages were received
     * @return list of unread Email objects
     */
    public static List<Email> getUnreadMessages(LocalDateTime receivedDate) {
        var emails = new ArrayList<Email>();

        var session = Session.getDefaultInstance(new Properties(), null);
        try {
            var store = session.getStore(STORE_NAME);
            store.connect(SERVER, USER, PASSWORD);

            var inbox = store.getFolder(INBOX_FOLDER);
            var spam = store.getFolder(SPAM_FOLDER);

            inbox.open(Folder.READ_ONLY);
            spam.open(Folder.READ_ONLY);

            var unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            var newerThan = new ReceivedDateTerm(ComparisonTerm.GE, Timestamp.valueOf(receivedDate));
            var searchTerm = new AndTerm(unseenFlagTerm, newerThan);

            var messages = ArrayUtils.addAll(inbox.search(searchTerm), spam.search(searchTerm));

            for (var message : messages) {
                var email = new Email();

                var from = new ArrayList<String>();
                if (message.getFrom() != null) {
                    for (var a : message.getFrom())
                        from.add(a.toString());
                }

                var to = new ArrayList<String>();
                if (message.getRecipients(Message.RecipientType.TO) != null) {
                    for (var a : message.getRecipients(Message.RecipientType.TO)) {
                        to.add(a.toString());
                    }
                }

                var content = StringHelper.EMPTY_STRING;
                if (message.isMimeType("text/plain")) {
                    content = message.getContent().toString();
                } else if (message.isMimeType("multipart/*")) {
                    var mp = (Multipart) message.getContent();
                    content = processMultiPart(mp);
                }

                email.subject = message.getSubject();
                email.dateReceived = new Timestamp(message.getReceivedDate().getTime()).toLocalDateTime();
                email.from = from;
                email.to = to;
                email.content = content;

                emails.add(email);
            }

            inbox.close(false);
            spam.close(false);
            store.close();
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(EMAIL_ERROR + e.getMessage(), e);
        }

        return emails;
    }

    /**
     * Process email content with "Multipart" MIME type (that consists of multiple body parts).
     *
     * @param multipart container for email content with multiple parts
     * @throws MessagingException in case of any exceptions while working with Messaging API
     * @throws IOException        in case of any exceptions during getting the content from the individual parts
     */
    private static String processMultiPart(Multipart multipart) throws MessagingException, IOException {
        var sb = new StringBuilder();

        for (int i = 0, n = multipart.getCount(); i < n; i++) {
            var part = multipart.getBodyPart(i);

            if (part.getContent() instanceof Multipart) {
                var multipartContent = (Multipart) part.getContent();
                var processedMultipart = processMultiPart(multipartContent);
                sb.append(processedMultipart);
            } else {
                sb.append(part.getContent().toString());
            }
        }

        return sb.toString();
    }
}