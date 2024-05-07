import java.io.*;
import java.util.*;

public class Main {
    static Node[] nodes;
    static HashSet<Node> tails;
    static StringBuilder sb;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine());
        nodes = new Node[100001];
        tails = new HashSet<>();
        for(int i = 0 ; i < q ; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int oper = Integer.parseInt(st.nextToken());
            switch(oper){
                case 100 :{
                    int mId = Integer.parseInt(st.nextToken());
                    int pId = Integer.parseInt(st.nextToken());
                    int color = Integer.parseInt(st.nextToken());
                    int maxDepth = Integer.parseInt(st.nextToken());
                    add(mId,pId,color,maxDepth,i);
                    break;
                }
                case 200 :{
                    int mId = Integer.parseInt(st.nextToken());
                    int color = Integer.parseInt(st.nextToken());
                    change(mId,color,i);
                    break;
                }
                case 300 :{
                    int mId = Integer.parseInt(st.nextToken());
                    sb.append(search(mId)).append("\n");
                    break;
                }
                case 400 :{
                    sb.append(calc()).append("\n");
                    break;
                }
            }
        }
        System.out.println(sb);
    }
    static void add(int mId, int pId, int color, int maxDepth,int time){
        if(pId == -1){
            // 새로운 루트 노드
            nodes[mId] = new Node(mId,pId,color,maxDepth,time);
            tails.add(nodes[mId]);
        }else{
            // 다른 노드에 붙이기
            Node parent = nodes[pId];
            int pDepth = parent.maxDepth;
            if(pDepth == 1){
                return;
            }else{
                int newDepth = Math.min(maxDepth,pDepth-1);
                nodes[mId] = new Node(mId,pId,color,newDepth,time); 
                parent.childs.add(nodes[mId]);
                tails.remove(nodes[pId]);
                tails.add(nodes[mId]);
            }
        }
    }
    static void change(int mId, int color,int time){
        Node now = nodes[mId];
        now.color = color;
        now.updateTime = time;
    }
    static int search(int mId){
        Node now = nodes[mId];
        int updateTime = now.updateTime;
        int color = now.color;
        while(now.pId != -1){
            now = nodes[now.pId];
            if(now.updateTime > updateTime){
                updateTime = now.updateTime;
                color = now.color;
            }
        }
        return color;
    }
    static int calc(){
        int result = 0;
        int [][] colorArr = new int[100001][2]; 
        ArrayDeque<Object[]> queue = new ArrayDeque<>();
        for(Node node : tails){
            queue.add(new Object[]{node,0});
        }
        
        while(!queue.isEmpty()){
            ArrayDeque<Object []> nq = new ArrayDeque<>();
            while(!queue.isEmpty()){
                Object [] now = queue.poll();
                Node nNode = (Node)now[0];
                int v = (int)now[1];
                int nowV = v  | 1 << search(nNode.id);
                if(nNode.pId != -1){
                    colorArr[nNode.pId][0]++;
                    colorArr[nNode.pId][1] = colorArr[nNode.pId][1] | nowV;
                    if(colorArr[nNode.pId][0] == nodes[nNode.pId].childs.size()){
                        nq.add(new Object[]{nodes[nNode.pId],colorArr[nNode.pId][1]});
                    }
                }

                int cnt = 0;
                while(nowV > 0){
                    if((nowV & 1) == 1){
                        cnt++;
                    }
                    nowV = nowV >> 1;
                }
                result+= cnt*cnt;
            }
            queue = nq;
        }
        return result;
    }  
    
}


class Node{
    int id;
    int maxDepth;
    int updateTime;
    int color;
    int pId;
    List<Node> childs;
    Node(int id, int pId, int color,int maxDepth, int updateTime){
        this.id = id;
        this.pId = pId;
        this.color = color;
        this.maxDepth = maxDepth;
        this.updateTime = updateTime;
        this.childs = new ArrayList<>();
    }
}