import java.io.*;
import java.util.*;

public class Main {
    static int R,C,K,maxH;
    static int[] c,d; // 0 1 2 3 = 북동남서
    static int [][] forest;
    static int answer;
    static boolean [][] v;
    static int [] dx = {0,0,1,-1};
    static int [] dy = {1,-1,0,0};
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        c = new int[K];
        d = new int[K];
        forest = new int[R+3][C];
        for(int i = 0 ; i < K ; i++){
            st = new StringTokenizer(br.readLine());
            c[i] = Integer.parseInt(st.nextToken())-1;
            d[i] = Integer.parseInt(st.nextToken());
            generate(c[i],d[i],i+1);
            gravity(1,c[i],0);
            // for(int j = 0 ; j < R+3; j++){
            //     System.out.println(Arrays.toString(forest[j]));
            // }
            // System.out.println(answer);
        }
        System.out.println(answer);
    }
    static void gravity(int x , int y,int cnt){
        if(x+2 < R+3 && forest[x+1][y-1] == 0 && forest[x+2][y] == 0 && forest[x+1][y+1] == 0){
            // 아래로 내려갈 수 있으면
            forest[x+2][y] = forest[x+1][y];
            forest[x+1][y-1] = forest[x][y-1];
            forest[x+1][y] = forest[x][y];
            forest[x+1][y+1] = forest[x][y+1];
            forest[x][y] = forest[x-1][y];
            forest[x-1][y] = 0;
            forest[x][y-1] = 0;
            forest[x][y+1] = 0;
            gravity(x+1,y,cnt+1);
        }else if(
            y-2 >= 0 && forest[x-1][y-1] == 0 && forest[x][y-2] == 0 && forest[x+1][y-1] == 0 &&
            x+2 < R+3 && forest[x+2][y-1] == 0 && forest[x+1][y-2] == 0
        ){
            // 서쪽 방향 회전
            forest[x+1][y-1] = forest[x][y-1];
            forest[x][y-2] = forest[x-1][y];
            forest[x-1][y-1] = forest[x][y+1];
            forest[x][y-1] = forest[x][y];
            forest[x][y] = forest[x+1][y];
            forest[x-1][y] = 0;
            forest[x+1][y] = 0;
            forest[x][y+1] = 0;
            
            // 내려가기
            forest[x+2][y-1] = forest[x+1][y-1];
            
            forest[x+1][y-1] = forest[x][y-1];
            forest[x+1][y-2] = forest[x][y-2];
            forest[x+1][y] = forest[x][y];

            forest[x][y-1] = forest[x-1][y-1];

            forest[x-1][y-1] = 0;
            forest[x][y-2] = 0;
            forest[x][y] = 0;

            gravity(x+1,y-1,cnt+1);
        }else if(
            y+2 < C && forest[x-1][y+1] == 0 && forest[x][y+2] == 0 && forest[x+1][y+1] == 0 &&
            x+2 < R+3 && forest[x+2][y+1] == 0 && forest[x+1][y+2] == 0
            ){
            forest[x+1][y+1] = forest[x][y+1];
            forest[x][y+2] = forest[x-1][y];
            forest[x-1][y+1] = forest[x][y-1];
            forest[x][y+1] = forest[x][y];
            forest[x][y] = forest[x+1][y];
            forest[x-1][y] = 0;
            forest[x+1][y] = 0;
            forest[x][y-1] = 0;
            
            forest[x+2][y+1] = forest[x+1][y+1];

            forest[x+1][y+1] = forest[x][y+1];
            forest[x+1][y] = forest[x][y];
            forest[x+1][y+2] = forest[x][y+2];
            
            forest[x][y+1] = forest[x-1][y+1];

            forest[x][y] = 0;
            forest[x][y+2] = 0;
            forest[x-1][y+1] = 0;

            gravity(x+1,y+1,cnt+1);
        }else if(cnt > 2){
            v = new boolean[R+3][C];
            maxH = 0;
            moveFairy(x,y);
            answer+=maxH-2;
        }else{
            forest = new int[R+3][C];
        }
    }

    static void moveFairy(int x , int y){
        v[x][y] = true;
        maxH = Math.max(maxH,x);
        for(int i = 0 ; i <4 ; i++){
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(nx >=0 && ny >=0 && nx < R+3 && ny < C && !v[nx][ny] && forest[nx][ny] != 0){
                // 아직 방문하지 않은 점을 방문할 때
                if(forest[x][y] == -1 || forest[nx][ny] == forest[x][y]){
                    // 내가 출구이거나 같은 블록내로만 이동 가능
                    moveFairy(nx,ny);
                }else if(forest[nx][ny] == -1){
                    // 같은 블록인데 -1이라 구분 못하는 경우
                    int cnt = 0;
                    for(int j = 1 ; j < 4 ; j++){
                        int nnx = x+dx[(i+j)%4];
                        int nny = y+dy[(i+j)%4];
                        if(nnx >= 0 && nny >= 0 && nnx < R+3 && nny < C){
                            if(forest[nnx][nny] == forest[x][y]){
                                cnt++;
                            }
                        }
                    }
                    if(cnt == 3){
                        moveFairy(nx,ny);
                    }
                }
            }
        }

    }

    static void generate(int y,int d, int cnt){
                            forest[0][y] = cnt;
        forest[1][y-1] = cnt; forest[1][y] = cnt; forest[1][y+1] = cnt;
                            forest[2][y] = cnt;
        if(d == 0){
            forest[0][y] = -1;
        }else if(d == 1){
            forest[1][y+1] = -1;
        }else if(d == 2){
            forest[2][y] = -1;
        }else{
            forest[1][y-1] = -1;
        }
    }
}