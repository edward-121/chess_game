package chessboard;
import java.awt.Color;
import java.awt.Graphics2D;

public class chessboard{
    final int Col = 8;
    final int Row = 8;
    public static final int sq_size = 100; //each square is 100 x 100 pixels
    public static final int hf_sq_size = sq_size/2;

    public void draw (Graphics2D g2){
           int c = 0 ;
        for (int row  = 0; row < Row ; row++){
            for (int col  = 0; col < Col ; col++) {
                if (c %2== 0){
                    g2.setColor(Color.WHITE);
                    c =1;
                } else {
                    g2.setColor(Color.GRAY);
                    c = 0;
                }
                g2.fillRect(col*sq_size, row*sq_size, sq_size, sq_size);
            }
            c++;
        }
    }
}