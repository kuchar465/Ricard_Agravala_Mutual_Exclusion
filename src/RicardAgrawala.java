import java.util.*;

public class RicardAgrawala {
    //timestamps table
    private int timestamp[];
    //number of nodes
    private int numVertices;
    //list of nodes
    private List<Node> Nodes;
    //list of requests
    List<Integer> requsts = new ArrayList();
    //flag to check if any node is in CS
    private int somethingInCritical = 0;

    //structure for individual node
    class Node {
        int id;
        Map<String, Integer> requestStatus = new HashMap();
        List<Integer> NodesWaitingForGoAhead = new ArrayList();
        boolean isInCritical = false;
        boolean isRequesting = false;

        //prepare node
        Node(int i){
            id = i;
            for(int j=0;j<numVertices;j++){
                if(j!=id){
                    requestStatus.put(String.valueOf(j), 0);
                }
            }
        }

        //reset list of nodes giving go ahead for node using this method
        public void resetRequestStatusList(){
            this.requestStatus = new HashMap();
            for(int j=0;j<numVertices;j++){
                if(j!=id){
                    requestStatus.put(String.valueOf(j), 0);
                }
            }
        }

        //ask node for go-ahead, if node is in critical, node in cs is saving id of requesting node
        public void askForRequest(int i){
            if(i != id){
                Node askedNode = Nodes.get(i);
                if(askedNode.isInCritical){
                    try {
                        askedNode.NodesWaitingForGoAhead.get(this.id);
                    } catch ( IndexOutOfBoundsException e ) {
                        askedNode.NodesWaitingForGoAhead.add(this.id);
                    }
                    //askedNode.NodesWaitingForGoAhead.add(this.id);
                }
                else if(askedNode.isRequesting){
                    if(timestamp[i]>timestamp[this.id]){
                        this.requestStatus.replace(String.valueOf(i), 1);
                    }
                }
                else{
                    this.requestStatus.replace(String.valueOf(i), 1);
                }
            }
        }

        //check if node can enter CS
        public boolean tryToEnterCriticalSection(){
            for (String id: requestStatus.keySet()) {
                String value = requestStatus.get(id).toString();
                if(value.equals("0")) {
                    return false;
                }
            }
            return true;
        }

        //do when entering critical
        private void onCriticalEnter(){
            somethingInCritical = 1;
            System.out.println("Node " + this.id + " entered critical section");
            this.isInCritical=true;
            printStatus();
            try {
                requsts.remove(this.id);
            }
            catch(IndexOutOfBoundsException e){
                requsts.remove(0);
            }
            this.resetRequestStatusList();
            this.isRequesting=false;
            for (Integer WaitingNode : NodesWaitingForGoAhead){
                Nodes.get(WaitingNode).requestStatus.replace(String.valueOf(this.id), 1);
            }
        }

        //do when exiting critical
        private void onCriticalExit(){
            this.isInCritical=false;
            System.out.println("Node " + this.id + " exited critical section");
            somethingInCritical = 0;
        }

        //printing status of go ahead list for all nodes with request status
        public void printCurrentRequestStatus(){
            System.out.print("Node list of id giving Go-Ahead for Node " + this.id + ": ");
            for (String id: requestStatus.keySet()) {
                String value = requestStatus.get(id).toString();
                if(value.equals("1"))
                    System.out.print(id + " ");
            }
            System.out.print("\n");
        }

    }


    public RicardAgrawala(int numVertices) {
        this.numVertices = numVertices;
        Nodes = new ArrayList<>();
        //adjMatrix = new boolean[numVertices][numVertices];
        timestamp = new int[numVertices];
        for(int i=0;i<numVertices;i++){
            Nodes.add(new Node(i));
        }
    }


    //assign random timestamp
    private void assignTimestamp(int i){
        Random random = new Random();
        int new_timestamp = 0;

        do {
            new_timestamp = random.nextInt(100 - 1) + 1;
        } while (!checkunique(new_timestamp, timestamp));

        timestamp[i] = new_timestamp;
    }

    public static boolean checkunique(int testValue, int[] array) {
        for (Integer value : array) {
            if (testValue == value) {
                return false;
            }
        }

        return true;
    }



    //start algorithm
    public void Start(){
        while(requsts.size()>0 || somethingInCritical==1){
            for(Node x : Nodes){
                if(x.isRequesting){
                    for(Node NodeCurrentlyAsked : Nodes){
                        x.askForRequest(NodeCurrentlyAsked.id);
                    }
                }
                else{
                    for(Node NodeCurrentlyAsked : Nodes) {
                        if (NodeCurrentlyAsked.isInCritical) {
                            NodeCurrentlyAsked.onCriticalExit();
                        }
                        if (NodeCurrentlyAsked.tryToEnterCriticalSection()) {
                            NodeCurrentlyAsked.onCriticalEnter();
                        }
                    }
                }
            }
        }
    }

    //in case of static input timestamps
    private void assignTimestamp(int i, int stamp){
        timestamp[i] = stamp;
    }

    public void addRequest(int i) {
        requsts.add(i);
        Nodes.get(i).isRequesting=true;
        assignTimestamp(i);

    }

    public void printAllRequests(){
        for (Integer x : requsts) {
            System.out.println("request from node: " + x);
        }
    }

    public void printAllTimestamps(){
        for (Integer x : requsts) {
            System.out.println("timestamp for request " + x + ": " + timestamp[x]);
        }
    }

    public void printStatus(){
        for (Node x : Nodes) {
            if(x.isRequesting)
                x.printCurrentRequestStatus();
        }
    }

    public void ask(int i){
        for(Node x : Nodes){
            Nodes.get(i).askForRequest(x.id);
        }
    }



}