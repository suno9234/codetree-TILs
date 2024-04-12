import java.io.*;
import java.util.*;

public class Main {
	static int L, N, Q, answer;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static int[][] knightInfo;
	static int[][] knightBoard;
	static boolean[] isAlive;
	static int[][] board;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		board = new int[L][L];
		knightBoard = new int[L][L];
		knightInfo = new int[N + 1][4];
		isAlive = new boolean[N + 1];
		Arrays.fill(isAlive, true);
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < L; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			for (int j = 0; j < h; j++) {
				for (int l = 0; l < w; l++) {
					knightBoard[r + j][c + l] = i + 1;
				}
			}
			knightInfo[i + 1] = new int[] { h, w, k, 0 };
		}
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int I = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			if (knightInfo[I][2] <= 0)
				continue;
			kingsOrder(I, d);
		}
		for (int i = 1; i < N + 1; i++) {
			if (knightInfo[i][2] > 0) {
				answer += knightInfo[i][3];
			}
		}
		System.out.println(answer);
	}

	static boolean kingsOrder(int num, int d) {
		// num번 기사에게 d방향으로 1 이동 명령
		// 위 오 아래 왼
		int[] info = knightInfo[num];
		int[] xy = findXY(num);
		int x = xy[0];
		int y = xy[1];
		int h = info[0];
		int w = info[1];
		if (d == 0 && x - 1 >= 0) {
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < w; i++) {
				int nx = x - 1;
				int ny = y + i;
				if (board[nx][ny] == 2) {
					return false;
				}
				if (knightBoard[nx][ny] != 0) {
					set.add(knightBoard[nx][ny]);
				}
			}
			for (int next : set) {
				if (!kingsOrder(next, d))
					return false;
			}
			for (int i = 0; i < w; i++) {
				knightBoard[x - 1][y + i] = knightBoard[x][y + i];
				knightBoard[x][y + i] = 0;
			}
			damage(x - 1, y);
			return true;
		} else if (d == 1 && y + w < L) {
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < h; i++) {
				int nx = x + i;
				int ny = y + w;
				if (board[nx][ny] == 2) {
					return false;
				}
				if (knightBoard[nx][ny] != 0) {
					set.add(knightBoard[nx][ny]);
				}
			}
			for (int next : set) {
				if (!kingsOrder(next, d))
					return false;
			}
			for (int i = 0; i < h; i++) {
				knightBoard[x + i][y + w] = knightBoard[x + i][y];
				knightBoard[x + i][y] = 0;
			}
			damage(x, y + 1);
			return true;
		} else if (d == 0 && x + h < L) {
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < w; i++) {
				int nx = x + h;
				int ny = y + i;
				if (board[nx][ny] == 2) {
					return false;
				}
				if (knightBoard[nx][ny] != 0) {
					set.add(knightBoard[nx][ny]);
				}
			}
			for (int next : set) {
				if (!kingsOrder(next, d))
					return false;
			}
			for (int i = 0; i < w; i++) {
				knightBoard[x + h][y + i] = knightBoard[x][y + i];
				knightBoard[x][y + i] = 0;
			}
			damage(x + 1, y);
			return true;
		} else if (d == 1 && y - 1 >= 0) {
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < h; i++) {
				int nx = x + i;
				int ny = y - 1;
				if (board[nx][ny] == 2) {
					return false;
				}
				if (knightBoard[nx][ny] != 0) {
					set.add(knightBoard[nx][ny]);
				}
			}
			for (int next : set) {
				if (!kingsOrder(next, d))
					return false;
			}
			for (int i = 0; i < h; i++) {
				knightBoard[x + i][y - 1] = knightBoard[x + i][y];
				knightBoard[x + i][y] = 0;
			}
			damage(x, y - 1);
			return true;
		} else {
			return false;
		}

	}

	static void damage(int x, int y) {
		int num = knightBoard[x][y];
		int[] info = knightInfo[num];
		int h = info[0];
		int w = info[1];
		int k = info[2];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int nx = x + i;
				int ny = y + j;
				if (nx >= 0 && ny >= 0 && nx < L && ny < L && board[nx][ny] == 1) {
					knightInfo[num][2]--;
					if (knightInfo[num][2] >= 0) {
						knightBoard[x][y] = 0;
					}
					knightInfo[num][3]++;
				}
			}
		}
	}

	static int[] findXY(int num) {
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				if (knightBoard[i][j] == num) {
					return new int[] { i, j };
				}
			}
		}
		return new int[] { -1, -1 };
	}
}