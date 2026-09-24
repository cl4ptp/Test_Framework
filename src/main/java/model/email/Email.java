package model.email;

import model.DataModel;
import utilities.EmailClient;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data object class that represents email message.
 * <p></p>
 * Due to the fact that JavaMail provides only on-demand access
 * to all the data in the messages from the mailbox,
 * this class provides more convenient way to work with Email objects.
 *
 * @see EmailClient
 */
public class Email extends DataModel {
    public String subject;
    public LocalDateTime dateReceived;
    public List<String> from;
    public List<String> to;
    public String content;
}
