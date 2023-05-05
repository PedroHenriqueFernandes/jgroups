package pt.ipb.dsys.peer;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.jgroups.ObjectMessage;

public class Chat implements Receiver {

    private final JChannel channel;
    private Address userAddress;

    public Chat() throws Exception {
        // Cria um novo canal de comunicação JGroups
        channel = new JChannel();

        // Define o receptor do canal para receber mensagens
        channel.setReceiver(this);

        // Conecta o canal ao cluster
        channel.connect("chat");

        // Obtém o endereço do usuário neste canal
        userAddress = channel.getAddress();
    }

    @Override
    public void viewAccepted(View view) {
        // Exibe a lista de membros no cluster sempre que houver uma alteração
        System.out.println("\n-- Membros Conectados --");
        List<Address> members = view.getMembers();
        for (Address member : members) {
            System.out.println(member);
        }
        System.out.println("------------------------");
    }

    @Override
    public void receive(Message msg) {
        // Verifica se a mensagem foi enviada por outra instância do Chat
        if (!msg.getSrc().equals(userAddress)) {
            // Exibe a mensagem recebida
            System.out.println(msg.getSrc() + ": " + msg.getObject().toString());
        }
    }

    public void sendMessage(String message) throws Exception {
        // Cria uma mensagem com o texto da mensagem e o endereço do usuário
        // Message msg = new ObjectMessage(userAddress, message);

        // Envia a mensagem multicast para todos os membros do cluster
        channel.send(new ObjectMessage(null, message));
    }

    public static void main(String[] args) throws Exception {
        // Cria uma nova instância do chat para o usuário atual
        Chat chat = new Chat();

        // Lê a entrada do usuário continuamente e envia as mensagens para o canal
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = in.readLine();
            if (line.startsWith("/quit")) {
                break;
            }
            chat.sendMessage(line);
        }

        // Fecha o canal quando o usuário sair do chat
        chat.channel.close();
    }
}
