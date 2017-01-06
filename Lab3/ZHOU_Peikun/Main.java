/**
 * Created by peikun on 2016/11/14.
 */
import java.io.*;
import java.util.Stack;

public class Main {

    public static class Stamp{
        public int path;
        public int node;

        public Stamp(int nodeStamp, int pathStamp){
            node = nodeStamp;
            path = pathStamp;
        }

        public int getPath(){
            return path;
        }

        public int getNode(){
            return node;
        }
    }


    public static class StreamXML {
        private File file;
        private BufferedReader reader;

        public StreamXML(String filePath) {

            file = new File(filePath);
            try{
                FileInputStream fileStream = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fileStream));
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public XMLNode nextNode() throws IOException{
            String line = reader.readLine();

            if(line != null ){
                return new XMLNode(line);
            }else{
                return null;
            }
        }

    }

    public static class XMLNode {
        String name;
        Boolean opened;

        public XMLNode(String line){
            String[] data = line.split(" ");
            name = data[1];
            opened = data[0].equals("0");

        }

        public String getName(){
            return name;
        }

        public Boolean isOpened(){
            return opened;
        }
    }

    public static class XPath {

        String[][] Xpath;

        public XPath(String string){
            String[] paths = string.split("//");
            Xpath = new String[paths.length - 1][];

            for(int i = 1;i < paths.length; i++){
                Xpath[i-1] = paths[i].split("/");
            }
        }

        public String[][] get(){
            return  Xpath;
        }
    }


    private static Stack<Stamp> stack = new Stack<Stamp>();
    private static String[][] XPath;
    private static int[][] queries;
    private static int[] query;
    private static int order = -1;

    public static void main(String[] args) throws IOException{

        long startTime = System.currentTimeMillis();

        String filePath = args[0];
        String xpath = args[1];

        StreamXML streamXML = new StreamXML(filePath);
        XPath = (new XPath(xpath)).get();
        queries = new int[XPath.length][];

        for(int i = 0;i < XPath.length; i++){
            queries[i] = KMP(XPath[i]);
        }

        stack.push(new Stamp(0,0));

        while(!stack.isEmpty()){

            XMLNode currentNode = streamXML.nextNode();

            if(currentNode.isOpened()){
                order++;
                match(currentNode,stack.lastElement().getNode(),stack.lastElement().getPath());
            }else{
                stack.pop();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " milliseconds");

        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
    }

    private static void match(XMLNode xmlNode,int node,int path){

        int pathMatch, nodeMatch;

        if(XPath[path][node].equals(xmlNode.getName())){
            if(XPath[path].length == node + 1){
                if(XPath.length == path + 1){
                    nodeMatch = queries[path][node];
                    pathMatch = path;
                    System.out.println(order);
                }else{
                    nodeMatch = 0;
                    pathMatch = path + 1;
                }

            }else{
                nodeMatch = node +1;
                pathMatch = path;
            }
            stack.push(new Stamp(nodeMatch,pathMatch));
        }else{
            nodeMatch = queries[path][node];
            pathMatch = path;
            if(node !=0){
                match(xmlNode, nodeMatch, pathMatch);
            }else{
                stack.push(new Stamp(nodeMatch, pathMatch));
            }
        }
    }

    private static int[] KMP(String[] path){

        int i = 2;
        int count = 0;

        query = new int[path.length];
        query[0] = -1;
        if(path.length > 1)
            query[1] = 0;

        while(i < path.length){
            if(path[count].equals(path[i - 1])){
                query[i] = count + 1;
                count ++;
                i++;
            }else if(count != 0){
                count = query[count];
            }else{
                query[i] = 0;
                i++;
            }
        }
        query[0] = 0;
        return query;
    }

}
