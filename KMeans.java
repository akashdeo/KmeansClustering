package k.means;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Akash Deo
 * net-id: apd160330
 * ML Assignment part-1
 * k-means algorithm implementation
 * it runs 25 times
 */
public class KMeans {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    static String noOfClusters;
    static String InputFile;
    static String OutputFile;
    static String KMeans;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        KMeans=args[0];
        noOfClusters=args[1];
        InputFile=args[2];
        OutputFile=args[3];
        File F=new File(InputFile);
        String outputFileName = OutputFile;
        FileWriter fstream = new FileWriter(outputFileName);
        BufferedWriter out = new BufferedWriter(fstream);
        FileReader fr=new FileReader(F);
        BufferedReader br=new BufferedReader(fr);
        String line;
        int lineCount=0;
        int noOfColumns = 0;
        while((line=br.readLine())!=null)
        {
            if(lineCount==0)
            {
                String split1[]=line.split("\\s+");
                noOfColumns=split1.length;
            }
           lineCount++;           
        }
        line="";
        br.close();
        String arrayOfRecords[][]=new String[lineCount][noOfColumns];
        FileReader fr1=new FileReader(F);
        BufferedReader br1=new BufferedReader(fr1);
        int Counter=0;
        HashMap hmap=new HashMap();
        while((line=br1.readLine())!=null)
        {
            String split[]=line.split("\\s+");
            for(int j=0;j<noOfColumns;j++)
                arrayOfRecords[Counter][j]=split[j]+"";
            if(Counter!=0)
            {
              hmap.put(Counter,new Point(Double.parseDouble(split[1]),Double.parseDouble(split[2])));
            }
            Counter++;
        }
        br1.close();
        Set set = hmap.entrySet();
        Iterator p = set.iterator();
        while(p.hasNext()){
         Map.Entry me = (Map.Entry)p.next();
         Point u=(Point) me.getValue();
        }
        HashMap hmap1=new HashMap();
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < Integer.parseInt(noOfClusters))
        {
           generated.add(ThreadLocalRandom.current().nextInt(1,lineCount-1));
        }
        Object arr1[]=generated.toArray();
        Arrays.sort(arr1);
        int arr2[]=new int[Integer.parseInt(noOfClusters)];
        for(int i=0;i<arr1.length;i++)
        {
            arr2[i]=(int)arr1[i];
            hmap1.put(arr2[i],new Point(Double.parseDouble(arrayOfRecords[arr2[i]+1][1]),Double.parseDouble(arrayOfRecords[arr2[i]+1][2])));
        }
        Set s1=hmap1.entrySet();
        Iterator p3=s1.iterator();
        double distanceMatrix[][]=new double[lineCount][Integer.parseInt(noOfClusters)+2];
        distanceMatrix[0][0]=0;
        Iterator p2=s1.iterator();
        int t=1;
        while(p2.hasNext())
        {
           Map.Entry me=(Map.Entry) p2.next();
           distanceMatrix[0][t]=new Double(me.getKey().toString());
           t++;
        }
        distanceMatrix[0][Integer.parseInt(noOfClusters)+1]=0;
        Counter=1;
        for(int l=1;l<lineCount;l++)
        {
           distanceMatrix[l][0]=Counter;
           Counter++;
        }
        ArrayList<Double> a1=new ArrayList<>();
        ArrayList<Double> a2=new ArrayList<>();
        for(int i=0;i<lineCount-1;i++)
        {
            a2.add(0.0);
        }
        for(int i=0;i<lineCount-1;i++)
        {
            a1.add(0.0);
        }
        DecimalFormat df = new DecimalFormat("#.#");
        double sumOfSquaredError=0.0;
        g:
        {
        for(int w=0;w<25;w++)
        {
        for(int i=1;i<lineCount;i++)
        {
            Point b=new Point(Double.parseDouble(arrayOfRecords[i][1]),Double.parseDouble(arrayOfRecords[i][2]));
            Point a = null;
            double min=Double.MAX_VALUE;
            int ClusterNumber=0;
            Iterator p1=s1.iterator();
            t=1;
            while(p1.hasNext())
            {
                 Map.Entry me = (Map.Entry)p1.next();
                 a=(Point) me.getValue();
                 distanceMatrix[i][t]=(double)Math.sqrt((b.x-a.x)*(b.x-a.x)*1.0+(b.y-a.y)*(b.y-a.y)*1.0);  
                 if(distanceMatrix[i][t]<min)
                 {
                     min=distanceMatrix[i][t];
                     ClusterNumber=(int)distanceMatrix[0][t];
                 }
                 t++;    
            }
            double SquareOfError=SquaredError(i,distanceMatrix,noOfClusters);
            sumOfSquaredError+=SquareOfError;
            distanceMatrix[i][Integer.parseInt(noOfClusters)+1]=ClusterNumber;
            a1.add(distanceMatrix[i][Integer.parseInt(noOfClusters)+1]);
        }
         int SameClusterCounter=0;
        
        a2=new ArrayList<>();
        for(Double d:a1)
        {
            a2.add(d);
        }
        a1=new ArrayList<>();
        p2=s1.iterator();
        int Cluster;
        int CounterOfCluster;
        double Xcor,Ycor;
        while(p2.hasNext())
        {
        Map.Entry me=(Map.Entry) p2.next();
        Cluster=new Integer(me.getKey().toString());

        double XCoordinate=0.0;
        double YCoordinate=0.0;
        CounterOfCluster=0;
        for(int j=1;j<lineCount;j++)
        {
            if(Cluster==distanceMatrix[j][Integer.parseInt(noOfClusters)+1])
            {
                XCoordinate=XCoordinate+Double.parseDouble(arrayOfRecords[j][1]);
                YCoordinate=YCoordinate+Double.parseDouble(arrayOfRecords[j][2]);
                CounterOfCluster++;
            }
        }
        Point c=(Point) me.getValue();
        
        Xcor=(XCoordinate/CounterOfCluster)*1.0;
        Ycor=(YCoordinate/CounterOfCluster)*1.0;
        if(c.x==Xcor&&c.y==Ycor)
        {
           SameClusterCounter++; 
        }
        Point a=new Point(Xcor,Ycor);
        me.setValue(a);
        }
        if(SameClusterCounter==arr1.length)
        {
            out.write("Broken after "+(w+1)+"iterations"+"\r\n");
           
            out.flush();
            break g;
        }
        
        }
        }
        ArrayList<ArrayList<Integer>> arraylist=new ArrayList<>();
        for(int j=1;j<=Integer.parseInt(noOfClusters);j++)
        {
            ArrayList<Integer> a5=new ArrayList<>();
            for(int i=1;i<lineCount;i++)
            {
             if(distanceMatrix[i][Integer.parseInt(noOfClusters)+1]==arr2[j-1])
             {
              a5.add(i);
             }
            }
            arraylist.add(a5);
        }
        t=0;
        for(ArrayList<Integer> a5:arraylist)
        {
            out.write("Cluster id: "+(t+1)+" "+a5+"\r\n");
            out.flush();
            out.write("\r\n");
            t++;
        }
        out.write("Sum of squared error :"+sumOfSquaredError+"\r\n");
        out.flush();
        
    }
    private static double SquaredError(int i, double[][] distanceMatrix, String noOfClusters) {
       double min=Double.MAX_VALUE;
        for(int j=1;j<=Integer.parseInt(noOfClusters);j++)
       {
          if(distanceMatrix[i][j]<min)
              min=distanceMatrix[i][j];
       }
       double squareOfError=min*min;
       return squareOfError;
    }

    private static class Point {

        double x;
        double y;
        public Point(double a,double b) {
          x=a;
          y=b;
        }
    }
    
}
