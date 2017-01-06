
/**
 * Created by peikun on 2016/11/14.
 */
import java.io.*;
import java.util.Stack;

public class Main {

    public static class State{
        public int path;
        public int node;

        public State(int n, int p){
            node = n;
            path = p;
        }

        public int getPath(){
            return path;
        }

        public int getNode(){
            return node;
        }
    }


    public static class XMLObject {
        private File file;
        private BufferedReader reader;

        public XMLObject(String filePath) {

            file = new File(filePath);
            try{
                FileInputStream fileStream = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(fileStream));
            }catch(IOException e){
                System.out.println("The file \""+filePath+"\" is not found");
            }
        }

        // get the next Tag
        public XMLTag nextTag() throws IOException{
            String line = reader.readLine();

            if(line != null ){
                return new XMLTag(line);
            }else{
                return null;
            }
        }

    }

    public static class XMLTag {
        String name;
        Boolean opened;

        public XMLTag(String line){
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

    public static class XPathObject {

        String[][] Xpath;

        public XPathObject(String string){
            String[] paths = string.split("//");
            Xpath = new String[paths.length - 1][];

            for(int i = 1;i<paths.length;i++){

                Xpath[i-1] = paths[i].split("/");
            }
        }

        public String[][] get(){
            return  Xpath;
        }
    }


    private static Stack<State> stack = new Stack<State>();
    private static XMLObject xml;
    private static String[][] XPath;
    private static int[][] betas;
    private static int[] query;
    private static int order = -1;

    public static void main(String[] args) throws IOException{


        String filePath = args[0];
        String xpath = args[1];

        xml = new XMLObject(filePath);
        XPath = (new XPathObject(xpath)).get();
        betas = new int[XPath.length][];

        for(int i=0;i< XPath.length;i++){
            betas[i] = KMP(XPath[i]);
        }

        State firstState = new State(0,0);
        stack.push(firstState);

        //while the stack contains something else than the first state
        do{

            XMLTag currentTag = xml.nextTag();

            if(currentTag.isOpened()){
                order++;
                read(currentTag,stack.lastElement().getNode(),stack.lastElement().getPath());

            }else{
                stack.pop();
            }

        }while(stack.size()>1);


    }

    private static void read(XMLTag tag,int node,int path){
        int newPath;
        int newNode;

        if(XPath[path][node].equals(tag.getName())){
            //current path is complete
            if(XPath[path].length == node +1){

                //all paths are complete; display !
                if(XPath.length == path +1){
                    newNode = betas[path][node];
                    newPath = path;
                    display();
                }else{
                    newNode = 0;
                    newPath = path +1;
                }

            }else{
                newNode = node +1;
                newPath = path;
            }

            stack.push(new State(newNode,newPath));
        }else{
            newNode = betas[path][node];
            newPath = path;
            //if there is a hope
            if(node !=0){
                read(tag,newNode,newPath);
            }else{
                stack.push(new State(newNode,newPath));
            }

        }
    }

    private static void display(){
        System.out.println(order);
    }

    private static int[] KMP(String[] path){

        int i = 2;
        int cnd = 0;
        query = new int[path.length];

        query[0] = -1;
        if(path.length>1)
            query[1] = 0;

        while(i < path.length){
            if(path[cnd].equals(path[i-1])){
                query[i] = cnd +1;
                cnd ++;
                i++;
            }else if(cnd>0){
                cnd = query[cnd];
            }else{
                query[i] = 0;
                i++;
            }
        }

        query[0] = 0;

        return query;
    }


}
