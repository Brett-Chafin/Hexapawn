/**
 * Created by brettchafin on 4/19/17.
 */
public class Move {
    int[] src = new int[2];
    int[] dest = new int[2];

    Move(int sX, int sY, int dX, int dY) {
        src[0] = sX;
        src[1] = sY;
        dest[0] = dX;
        dest[1] = dY;
    }

    public void printMove(){
        System.err.println("src: (" + src[0] + "," + src[1] + ")" + " dest: (" + dest[0] + "," + dest[1] + ")");
        //System.err.println("dest: (" + dest[0] + "," + dest[1] + ")");
    }
}
