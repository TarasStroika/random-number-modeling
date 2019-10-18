package javaapplication2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RandGenerator myrand = new RandGenerator();
        //FirstTask
        double[] d1=new double[1000];
        double s1=0;//для середнього
        double disp1=0;//для дисперсії

        for (int i=0;i<d1.length;i++) {
            d1[i]=myrand.getRand01();
            s1+=d1[i];
        }
        s1/=d1.length;
        for (int i=0;i<d1.length;i++) disp1+=(d1[i]-s1)*(d1[i]-s1);
        disp1/=d1.length;
        System.out.println("The mathematical expectation of a sequence is equal 0,5");
        System.out.printf("The sample mean is equal %.5f%n",s1);
        System.out.println("The mathematical expectation of a sequence is equal 0,08333");
        System.out.printf("The sample mean is equal %.5f%n",disp1);
        int[] interval1 = new int[20];
        for (int i=0;i<d1.length;i++)
            interval1[(int)Math.round(Math.floor(d1[i]*20))]++;
        double[] x1 = new double[20];
        for (int i=0;i<20;i++){
            x1[i]=0.05*i;
            System.out.printf("%4.2f-%4.2f%10d%10.4f%n", (0.05*i),(0.05*(i+1)),interval1[i],(1.0*interval1[i]/d1.length));
        }
        //SecondTask
        ArrayList<Integer> x2 = new ArrayList<Integer>(7);
        ArrayList<Double> p2 = new ArrayList<Double>(7);
        x2.add(1);
        p2.add(0.02);
        x2.add(3);
        p2.add(0.26);
        x2.add(8);
        p2.add(0.18);
        x2.add(11);
        p2.add(0.32);
        x2.add(19);
        p2.add(0.16);
        x2.add(29);
        p2.add(0.02);
        x2.add(33);
        p2.add(0.04);
        int[] d2=new int[7];
      
        for (int i=0;i<1000;i++)
            d2[myrand.getDiscr(p2)]++;
        double s2=0;
        for (int i=0;i<7;i++) s2+=(1.0*(Integer)x2.get(i)*d2[i]/1000);
        System.out.printf("The mathematical expectation of a discrete random variable is %.5f%n",s2);
        double disp2=0;
        for (int i=0;i<7;i++) disp2+=(Math.pow(1.0*(Integer)x2.get(i)-s2,2)*d2[i]/1000);
        System.out.printf("The variance of the discrete random variable is equal %.5f%n",disp2);
        for (int i=0;i<7;i++)
            System.out.printf("%5d%10.4f%n", (Integer)x2.get(i),(1.0*d2[i]/1000));
        //ThirdTask
        MyFunction tmp = new MyFunction();
        double[] d3=new double[1000];
        double s3=0;
        double disp3=0;
        
        for (int i=0;i<d3.length;i++) {
            d3[i]=myrand.getFunc(tmp);
            s3+=d3[i];
        }
        s3/=d3.length;
        for (int i=0;i<d3.length;i++) disp3+=(d3[i]-s3)*(d3[i]-s3);
        disp3/=d3.length;
        System.out.printf("Mathematical expectation of continuous random variable %.5f%n",s3);
        System.out.printf("Variance of continuous random variable %.5f%n",disp3);
        int[] interval3 = new int[60];
        double[] x3 = new double[60];
        for (int i=0;i<d3.length;i++)
            interval3[(int)Math.round(Math.floor(d3[i]*10))]++;
        for (int i=0;i<60;i++){
            x3[i]=0.1*i;
            System.out.printf("%4.2f-%4.2f%10d%10.4f%n", (0.1*i),(0.1*(i+1)),interval3[i],(1.0*interval3[i]/d3.length));//виводимо таблицю
        }

        new Draw(x1,interval1,3,"The sequence is evenly distributed",800,300,"%4.2f",20,20);
        new Draw(new double[]{1,3,8,11,19,29,33},d2,0.6,"Histogram of the distribution of a discrete random variable",400,300,"%2.0f",20,350);
        new Draw(x3,interval3,3,"Histogram of distribution of continuous random variable",1200,300,"%3.1f",20,680);
    }
    
}


interface DoubleFunction{
    public double y(double x);
    public double getA();
    public double getB();
    public double getM();
}


class MyFunction implements DoubleFunction{
    public MyFunction(){}
    
    @Override
    public double y(double x){
        if (x<0) return 0;
        else if (x<2) return 0.25-0.125*x;
        else if (x<4) return 0.125*x-0.25;
        else if (x<6) return 0.25*x-1;
        else return 0;
    }
    
    @Override
    public double getA(){
        return 0;
    }
    
    @Override
    public double getB(){
        return 6;
    }
    
    @Override
    public double getM(){
        return 0.5;
    }
}

class RandGenerator{
    private double seed;
    private static long M=482103291;
    public RandGenerator(){
        seed=0.1;
    }
    
    public RandGenerator(double seed){
        this.seed=seed;
    }
    
    public double getRand01(){
        seed*=M;
        seed=seed-Math.floor(seed);
        return seed;
    }
    
    public double getGauss(double MX, double sigma){
        return MX+sigma*getRand01();
    }
    
    public int getDiscr(List<Double> data){
        double tmp=getRand01();
        double sum=0;
        Double[] ardata=data.toArray(new Double[0]);
        for (int i=0;i<ardata.length;i++){
            sum+=ardata[i];
            if (sum>=tmp) return i;
        }
        return ardata.length;
    }
    
    public double getFunc(DoubleFunction prob){
        double x0,nu;
        do{
            x0=prob.getA()+(prob.getB()-prob.getA())*getRand01();
            nu=prob.getM()*getRand01();
            if (prob.y(x0)>=nu) return x0;
        }while(true);
    }
}

class Draw extends javax.swing.JFrame {
 
    private double[] x;
    private int[] y;
    private int[] yInt;
    private int colwidth;
    private Dimension size;
    private Dimension startPointXoY;
    private double scale;
    String title;
    String form;
    int lx,ly;
 
    public Draw(double[] x,int[] y, double scale, String title,int width, int height,String form, int lx, int ly) {
        this.x = x;
        this.y = y;
        this.scale=scale;
        this.title=title;
        this.form=form;
        this.lx=lx;
        this.ly=ly;
        size=new Dimension(width,height);
        startPointXoY=new Dimension(5,size.height-50);
        yInt = new int[x.length];
        colwidth=(size.width-2*startPointXoY.width)/x.length;
        reBuildArreys();
        initInterface();
    }
 
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,size.width,size.height);
 
        g.setColor(Color.BLUE);
 
        for (int i = 0; i < x.length; i++) {
	            g.drawString(String.format(form, x[i]),startPointXoY.width + colwidth * i+colwidth/2-10,startPointXoY.height + 15);
	            g.drawString(String.valueOf(y[i]),startPointXoY.width + colwidth * i+colwidth/2-10,yInt[i]-5);
	            g.drawRect(startPointXoY.width+colwidth*i, yInt[i], colwidth, startPointXoY.height-yInt[i]);
        }
    }
 
    private void initInterface() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(size);
        setResizable(false);
        setTitle(title);
        setLocation(lx,ly);
        setVisible(true);
    }
 
    private void reBuildArreys() {
        for (int i = 0; i < y.length; i++) {
            yInt[i] = (int)Math.round(y[i]*scale);
            yInt[i] = startPointXoY.height - (yInt[i]);
        }
    }
}
