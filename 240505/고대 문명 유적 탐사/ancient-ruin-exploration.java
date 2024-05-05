import java.io.*;
import java.util.*;

public class Main {
    static int k,m,answer,recoveryIdx;
    static int [] dx = {1,-1,0,0};
    static int [] dy = {0,0,1,-1};
    static int [] recovery; 
    static int [][] board;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        k = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[5][5];
        recovery = new int[m];
        for(int i = 0 ; i < 5 ; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0 ; j < 5 ; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        st = new StringTokenizer(br.readLine());
        for(int i = 0  ; i < m ; i++){
            recovery[i] = Integer.parseInt(st.nextToken());
        }
        for(int i = 0 ; i < k ; i++){
            // 탐사 진행
            int result = 0;
            int [] sr = search();
            int x = sr[0];
            int y = sr[1];
            int rot = sr[2];
            int guess = sr[3];
            if(guess == 0){
                System.out.println(sb.toString());
                return;
            }
            turnR(x,y,rot);
            // 유물 획득
            result = getRelic();
            sb.append(result).append(" ");
        }
        System.out.println(sb);
    }
    static int getRelic(){
        int result = 0;
        boolean [][] v = new boolean[5][5];
        for(int i = 0 ; i < 5 ; i++){
            for(int j = 0 ; j < 5 ; j++){
                if(!v[i][j]){
                    ArrayDeque<int []> toDelete = new ArrayDeque<>();
                    ArrayDeque<int []> queue = new ArrayDeque<>();
                    queue.add(new int []{i,j});
                    v[i][j] = true;
                    int cnt = 1;
                    while(!queue.isEmpty()){
                        int [] now = queue.poll();
                        int x = now[0];
                        int y = now[1];
                        toDelete.add(now);
                        int value = board[x][y];
                        for(int k = 0 ; k < 4 ; k++){
                            int nx = x + dx[k];
                            int ny = y + dy[k];
                            if(nx >=0 && ny >=0 && nx < 5 && ny < 5 && !v[nx][ny] && board[nx][ny] == value){
                                v[nx][ny] = true;
                                cnt++;
                                queue.add(new int []{nx,ny});
                            }
                        }
                    }
                    if(cnt > 2){
                        while(!toDelete.isEmpty()){
                            int [] now = toDelete.poll();
                            board[now[0]][now[1]] = 0;
                        }
                        result+=cnt;
                    }
                }
            }
        }
        fillRelic();
        if(getRelicValue() > 2){
            result+= getRelic();
        }
        return result;
    }
    static void fillRelic(){
        for(int i = 0; i < 5 ; i++){
            for(int j = 4 ; j >= 0 ; j--){
                if(board[j][i] == 0){
                    board[j][i] = recovery[recoveryIdx++];
                    if(recoveryIdx == m){
                        recoveryIdx = 0;
                    }
                }
            }
        }
        return;
    }

    static int [] search(){
        int result=0;
        int x = 0;
        int y = 0;
        int rot = 0;
        for(int i = 1 ; i < 4 ; i++){
            for(int j = 1 ; j < 4 ; j++){
                for(int k = 0 ; k < 4 ; k++){
                    int res = getRelicValue();
                    if(res > result){
                        x = i;
                        y = j;
                        result = res;
                        rot = k;
                    }else if(res == result){
                        if(k < rot){
                            rot = k;
                            x = i;
                            y = j;
                        }else if(k == rot){
                            if(j < y){
                                y = j;
                                x = i;
                            }else if(i < x){
                                x = i;
                            }
                        }
                    }
                    turnR(i,j,1);
                }
            }
        }
        return new int[]{x,y,rot,result};
    }
    static int getRelicValue(){
        int result = 0;
        boolean [][] v = new boolean[5][5];
        for(int i = 0 ; i < 5 ; i++){
            for(int j = 0 ; j < 5 ; j++){
                if(!v[i][j]){
                    ArrayDeque<int []> queue = new ArrayDeque<>();
                    queue.add(new int []{i,j});
                    v[i][j] = true;
                    int cnt = 1;
                    while(!queue.isEmpty()){
                        int [] now = queue.poll();
                        int x = now[0];
                        int y = now[1];
                        for(int k = 0 ; k < 4 ; k++){
                            int nx = x + dx[k];
                            int ny = y + dy[k];
                            if(nx >=0 && ny >=0 && nx < 5 && ny < 5 && !v[nx][ny] && board[nx][ny] == board[x][y]){
                                v[nx][ny] = true;
                                cnt++;
                                queue.add(new int []{nx,ny});
                            }
                        }
                    }
                    if(cnt > 2){
                        result+=cnt;
                    }
                }
            }
        }
        return result;
    }

    static void turnL(int x , int y, int cnt){
        if (cnt== 0){
            return;
        }
        int temp = board[x-1][y-1];
        int temp2 = board[x-1][y];

        board[x-1][y-1] = board[x-1][y+1];
        
        board[x-1][y] = board[x][y+1];
        board[x-1][y+1] = board[x+1][y+1];

        board[x][y+1] = board[x+1][y];
        board[x+1][y+1] = board[x+1][y-1];
        board[x+1][y] = board[x][y-1];

        board[x+1][y-1] = temp;
        board[x][y-1] = temp2;
        turnL(x,y,cnt-1);
    }

    static void turnR(int x, int y, int cnt){
        if(cnt == 0){
            return;
        }
        int temp = board[x-1][y-1];
        int temp2 = board[x][y-1];
        board[x-1][y-1] = board[x+1][y-1];
        board[x][y-1] = board[x+1][y];
        board[x+1][y-1] = board[x+1][y+1];
        
        board[x+1][y] = board[x][y+1];
        board[x+1][y+1] = board[x-1][y+1];
        board[x][y+1] = board[x-1][y];
    
        board[x-1][y+1] = temp;
        board[x-1][y] = temp2;

        turnR(x,y,cnt-1);
    }
}