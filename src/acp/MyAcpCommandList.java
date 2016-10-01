package acp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAcpCommandList {

    private static List<String> commandList = new ArrayList<String>();
    private static Map<String,String> paramMap = new HashMap<String,String>();

    public static void add(String command) {
        commandList.add(command);
        System.out.println("发送ACP ：  " +command );
    }

    public static int size() {
        return commandList.size();
    }
    public static void remove() {
    	 commandList.remove(0);
    }
    public static String getItem(int index) {
    	return commandList.get(index);
    }
    
    
    public static void addParam(String param,String value){
    	paramMap.put(param,value);
    }
    
    public static void clearParam(){
    	paramMap.clear();
    }
    public static Map<String,String>  getParamMap(){
    	return paramMap;
    }
}

