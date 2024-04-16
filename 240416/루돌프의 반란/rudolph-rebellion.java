import java.util.*;
import java.io.*;

public class Main {
	static int n, m, p, c, d, rx, ry,answer;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static int [][] santaInfo;
	static int[][] map;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		d = Integer.parseInt(st.nextToken());
		map = new int[n][n];
		santaInfo = new int[p+1][4];
		// idx = 번호  x,y,stunned,score 
		st = new StringTokenizer(br.readLine());
		rx = Integer.parseInt(st.nextToken()) - 1;
		ry = Integer.parseInt(st.nextToken()) - 1;
		for (int i = 0; i < p; i++) {
			st = new StringTokenizer(br.readLine());
			int sn = Integer.parseInt(st.nextToken());
			int sr = Integer.parseInt(st.nextToken()) - 1;
			int sc = Integer.parseInt(st.nextToken()) - 1;
			map[sr][sc] = sn;
			santaInfo[sn] = new int[] {sr,sc,0,0};
		}
		while (m > 0) {
			moveRudolf();
			moveSanta();
			m--;
		}
		for(int i = 1 ; i < p+1; i++) {
			sb.append(santaInfo[i][3]).append(" ");
		}
		System.out.println(sb.toString());
	}

	static int getDist(int x, int y) {
		return (x - rx) * (x - rx) + (y - ry) * (y - ry);
	}

	static void moveSanta() {
		for(int l = 1 ; l < p+1; l++) {
			int num = l;
			int x = santaInfo[num][0];
			int y = santaInfo[num][1];
			if ( santaInfo[num][2] > 0) {
				//nMap[x][y] = num;
			} else if(santaInfo[l][2] == 0){
				// stunned가 0인 산타들만(기절 아니고 살아있음)
				int sx = x;
				int sy = y;
				int sd = -1;
				for (int i = 0; i < 4; i++) {
					int nx = x + dx[i];
					int ny = y + dy[i];
					if (nx >= 0 && ny >= 0 && nx < n && ny < n && map[nx][ny] == 0) {
						// 상하좌우 범위내에 갈 수 있으면
						if (getDist(sx, sy) > getDist(nx, ny)) {
							sx = nx;
							sy = ny;
							sd = i;
						}
						
					
					}
				}
				// 다음 거리가 더 적을 경우
				santaInfo[num][0] = sx;
				santaInfo[num][1] = sy;
				map[x][y] = 0;
				map[sx][sy] = num;
				// 갔는데 루돌프가 있는 경우
				if (rx == sx && ry == sy) {
					santaInfo[num][0] = sx;
					santaInfo[num][1] = sy;
					santaInfo[num][2] = 2;
					santaInfo[num][3] += d;
					push(-dx[sd] * d, -dy[sd] * d, sx, sy);
				}
			
			}
		}
		for(int i = 1 ; i < p+1; i++) {
			if(santaInfo[i][2] > 0) {
				santaInfo[i][2]-- ;
			}
			if(santaInfo[i][2] >= 0) {
				santaInfo[i][3]++;
			}
		}
	}

	static void moveRudolf() {
		int dist = Integer.MAX_VALUE;
		int rr = 0;
		int rc = 0;
		int mx = 0;
		int my = 0;
		for(int i = 1 ; i <p+1; i++) {
			int x = santaInfo[i][0];
			int y = santaInfo[i][1];
			int stunned = santaInfo[i][2];
			int santaScore = santaInfo[i][3];
			if(stunned < 0 ) continue;
			int diff = getDist(x,y);
			if(diff < dist) {
				dist  = diff;
				rr = x;
				rc = y;
			}else if(diff == dist){
                if(rr < x){
                    rr = x;
                    rc = y;
                }else if(rr == x){
                    if(rc < y){
                        rc = y;
                    }
                }
            }
		}
		
		if (rr > rx) {
			rx++;
			mx++;
		} else if (rr < rx) {
			rx--;
			mx--;
		}
		if (rc > ry) {
			ry++;
			my++;
		} else if (rc < ry) {
			ry--;
			my--;
		}
		int sNum = map[rx][ry];
		if (map[rx][ry] > 0) {
			// 움직였는데 산타가 있는 경우
			if(santaInfo[sNum][2] < 0) return;
			santaInfo[sNum][3] +=c;
			santaInfo[sNum][2] = 2;
			push(mx * c, my * c, rx, ry);
		}
	}

	static void push(int mx, int my, int x, int y) {
		int nx = x + mx;
		int ny = y + my;
		if (nx >= 0 && ny >= 0 && nx < n && ny < n) {
			if (mx > 1)
				mx = 1;
			if (mx < 0)
				mx = -1;
			if (my > 0)
				my = 1;
			if (my < 0)
				my = -1;
			if (map[nx][ny] > 0) {
				push(mx, my, nx, ny);
			}
			int num = map[x][y];
			santaInfo[num][0] = nx;
			santaInfo[num][1] = ny;
			map[nx][ny] = num;
			map[x][y] = 0;
		} else {
			// 밀려서 밖으로 나가는 경우
			santaInfo[map[x][y]][2] = -1;
			map[x][y] = 0;
		}

	}
}