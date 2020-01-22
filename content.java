import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.lang.Math;

public class content extends JPanel{
    private int width = 800;
    private int height = 800;

    private float[] x;
    private float[] y;

    private float[][] point;
    private boolean[] solution;

    private float[] x_line;
    private float[] y_line;

    private float[] x_new_line;
    private float[] y_new_line;

    private int[][] pair;
    private int sol_line;
    private int counter;
    
    private long t1;
    private long t2;

    public content(){
        //Set Dimension and Color
        setBackground(Color.black);
        setPreferredSize(new Dimension(this.width, this.height));
    }

    public void paintComponent (Graphics hal){
        super.paintComponent(hal);
        
        for(int i = 0; i < this.x.length; i++){
            //Convert point into dimension size
            this.x[i] = Math.round(400 + (this.x[i]*10));
            this.y[i] = Math.round(400 - (this.y[i]*10));
        }

        for(int i = 0; i < this.sol_line; i++){
            //Convert point to make line into dimension size
            this.x_line[i] = Math.round(400 + (this.x_line[i] * 10));
            this.y_line[i] = Math.round(400 - (this.y_line[i] * 10));
        }

        for(int i = 0; i < this.x_new_line.length; i++){
            //Convert point to make line into dimension size
            this.x_new_line[i] = Math.round(400 + (this.x_new_line[i] * 10));
            this.y_new_line[i] = Math.round(400 - (this.y_new_line[i] * 10));
        }

        //Make X and Y line
        hal.setColor(Color.gray); 
        hal.drawLine(0, 400, 800, 400);
        hal.setColor(Color.gray);
        hal.drawLine(400,0,400,800);
        
        for(int i = 0; i < this.x.length; i++){
            //Make Circle inside Convex Hull
            hal.setColor(Color.cyan);
            hal.fillOval((int)this.x[i]-2, (int)this.y[i]-2, 4, 4);
        }

        for(int i = 0; i < this.sol_line; i++){
            //Make Circle Convex Hull
            hal.setColor(Color.blue);
            hal.fillOval((int)this.x_line[i]-2, (int)this.y_line[i]-2,4,4);
        }
        
        for(int i = 0; i < this.counter; i++){
            //Make line Convex Hull
            hal.setColor(Color.blue);
            hal.drawLine((int)this.x_new_line[pair[i][0]], (int)this.y_new_line[pair[i][0]], (int)this.x_new_line[pair[i][1]], (int)this.y_new_line[pair[i][1]]);
        }
    }

    public float[] linear_equation(float x1, float y1, float x2, float y2){
        //Make Linear Equation
        float eq[] = new float[4];
        eq[0] = x2-x1;
        eq[1] = -(x2-x1)*y1;
        eq[2] = -(y2-y1);
        eq[3] = (y2-y1)*x1;
        return eq;
    }

    public float value(float[] k, float x, float y){
        //f(x,y) from Linear Equation
        float answer = k[0]*y + k[2]*x + k[1] + k[3];
        return answer;
    }

    public void convex_hull(){
        //Declaration and initialization
        int N;
        int ind = 0;
        int count = 0;
        float line[];

        //Input
        Scanner input = new Scanner(System.in);
        Random random = new Random();
        N = input.nextInt();
        
        //Set Size
        this.point = new float[N][2];
        this.solution = new boolean[N];
        this.x = new float[N];
        this.y = new float[N];
        this.x_line = new float[N+1];
        this.y_line = new float[N+1];
        this.x_new_line = new float[N+1];
        this.y_new_line = new float[N+1];
        this.pair = new int[N+1][N+1];
        
        //INPUT
        for(int i = 0; i < N; i++){
            this.point[i][0] = random.nextFloat()*60 + -30;
            this.point[i][1] = random.nextFloat()*60 + -30;
            this.x[i] = this.point[i][0];
            this.y[i] = this.point[i][1];
            this.solution[i] = false;
        }

        this.t1 = System.nanoTime();
        //First Loop
        for(int i = 0; i < N; i++){
            //Second Loop
            for(int j = 0; j < N && i != j; j++){
                boolean positive = true;; //Mark first value is positive or not
                boolean first = true; //Mark is it first point or not
                boolean mark = true; // Mark stop while loop
                int k = 0; //Index of point
                float val; //value

                //Make Linear Equation from 2 point
                line = linear_equation(point[i][0], point[i][1], point[j][0], point[j][1]);
                while(mark){
                    //If point is equal to i or j, it will not be calculated
                    if(k != i && k != j){
                        val = value(line, point[k][0], point[k][1]);
                        if(first){
                            if(val < 0){
                                positive = false;
                                first = false;
                            }
                            else if(val > 0){
                                positive = true;
                                first = false;
                            }
                        }

                        else{
                            //If one of the val is different from the other (not positive or not negative)
                            if((val < 0 && positive) || (val > 0 && !positive)){
                                mark = false;
                            }
                        }
                    }

                    //Make true for point number i and j and add pair
                    if(k == N-1 && mark){
                        this.solution[i] = true;
                        this.solution[j] = true;
                        this.pair[count][0] = i;
                        this.pair[count][1] = j;
                        count++;
                        mark = false;
                    } 
                    k++;
                }
            }
        }

        //Add line into 
        for(int i = 0; i < N; i++){
            if(this.solution[i]){
                this.x_line[ind] = point[i][0];
                this.y_line[ind] = point[i][1];
                this.x_new_line[i] = point[i][0];
                this.y_new_line[i] = point[i][1];
                ind++;
            }
        }
        this.counter = count;
        this.sol_line = ind;
        this.t2 = System.nanoTime();
    }

    public void show_solution(){
        //Show set of convex hull
        System.out.print("[");
        for(int i = 0; i < this.sol_line; i++){
            if(i == 0){
                System.out.print("(" + this.x_line[i] + "," + this.y_line[i] + ")");
            }
            else{
                System.out.print(",(" + this.x_line[i] + "," + this.y_line[i] + ")");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Convex Hull");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content panel = new content();
        panel.convex_hull();
        panel.show_solution();
        double t1 = (double)panel.t1;
        double t2 = (double)panel.t2;
        double t = ((t2 - t1) / 1000000);
        System.out.println(t + " Milliseconds");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}