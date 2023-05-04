# JGroups

Requirements:
- Docker and Docker Compose
- Java 11 <small>(use the embedded Download JDK from IntelliJ if needed...)</small>

## Challenge 1 

1. Star the `Gossip Router` (use run configuration: *Start Gossip Router*)
2. Complete the class `pt.ipb.dsys.peer.PeerMain`:
   - Create a `JChannel` to connect to the `Gossip Router` at `gossip-router:12001`
     - Use `DefaultProtocols.gossipRouter` to generate the list of `org.jgroups.stack.Protocol` needed
3. Configure the channel so that:
   - Each peer `discards` his own messages
   - Connect (`connect`) to a cluster with the name `PeerCluster`
4. Create a class (`extends org.jgroups.Receiver`) to use as the `receiver` responsible for _handling_ all events on the _cluster_

### Receiver

In the receivers of type `org.jgroup.Receiver` you will find 2 _events_ of interest:
- `viewAccepted` - triggered when the membership of the _cluster_ changes
- `receive` - a new message arrives

To _capture_ this events you can simply, in your `Receiver` implementation _override_ the methos with the event name.

The `receiver` should:
1. Keep a local list of all the members in the _cluster_;
2. Anytime a member (except self) arrives at the _cluster_ (event `viewAccepted`) spread the rumour:
    - _Darth Vader is Luke's father..._
    - _Suggestion_: Use the `pt.ipb.dsys.peer.model.Rumour` class to encapsulate the message and keep count of how many times the rumour was spread
3. Anytime a new message arrives (event `receive`) _broadcast_ the message to all the _cluster_ 

## Challenge 2

Build the project using the `gradlew` (`gradlew.bat` if you are in windows) command line (or an IDE with `gradle` support) to run the `build` task.

Example for command line:
```shell
./gradlew clean build
```
Upon completion, a file named `ds-jgroups-demo-1.0-SNAPSHOT.jar` should be created inside the `{project}/build/libs` directory.

### Dockerize

1. Complete the `docker/docker-compose.yml` file and declaring following `services`:
  - `gossip-router` - use the `openjdk:11` image to start the class `org.jgroups.stack.GossipRouter` and pass the `-port 12001` parameter, use `gossip-router` as the `container_name` and expose the `12001` port;
  - `peer-node` - use the `openjdk:11` image to start your class (`PeerMain`?) with 5 replicas (`deploy.replicas`)
2. Start the docker.

### Hints

- Since the `ds-jgroups-demo-1.0-SNAPSHOT.jar` file is outside docker you're going to need a volume on both services to access it (with `java -jar ...`);
- For better isolation try and declare a network (`peer-network`) and include both services on that network.
- Instead of `System.out` try and use the 

## Challenge 3

1. Commmit your modifications to the current branch (`git`);
2. Create a new `git branch` named `peer-chat`;
3. Try to modify the example to create a simple [jgroups based chat](http://www.jgroups.org/tutorial5/) (_outside docker_). 