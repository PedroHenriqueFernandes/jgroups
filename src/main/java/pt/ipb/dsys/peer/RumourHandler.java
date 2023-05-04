package pt.ipb.dsys.peer;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.peer.model.Rumour;
import pt.ipb.dsys.peer.util.Sleeper;

import java.util.HashSet;
import java.util.Set;

public class RumourHandler implements Receiver {

    public static final String RUMOUR = "Darth Vader is Luke's father...";

    private static final Logger logger = LoggerFactory.getLogger(RumourHandler.class);

    private final JChannel channel;

    private Set<Address> told = new HashSet<>();

    public RumourHandler(JChannel channel) {
        this.channel = channel;
    }

    @Override
    public void viewAccepted(View view) {
        logger.debug("[{}] view changed ({} members)", channel.address(), view.getMembers().size());
        for (Address member : view.getMembers()) {
            boolean itsMe = member == channel.address();
            if (itsMe || told.contains(member))
                continue;

            sendRumourTo(member, new Rumour(RUMOUR));
            told.add(member);
        }
    }

    @Override
    public void receive(Message msg) {
        Rumour rumour = msg.getObject();
        logger.info("[{}] {} told me '{}' (told: {} times)", channel.address(), msg.src(), rumour.getRumour(), rumour.told());

        Sleeper.sleep(2000); // wait before relaying
        brodcast(rumour);
    }

    public void brodcast(Rumour rumour) {
        try {
            rumour.increaseRumour();
            channel.send(new ObjectMessage(null, rumour));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendRumourTo(Address member, Rumour rumour) {
        try {
            rumour.increaseRumour();
            channel.send(new ObjectMessage(member, rumour));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
