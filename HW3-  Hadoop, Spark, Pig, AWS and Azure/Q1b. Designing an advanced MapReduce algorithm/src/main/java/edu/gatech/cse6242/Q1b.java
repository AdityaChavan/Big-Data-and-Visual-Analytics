//referenced from : https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Example:_WordCount_v1.0
//referenced from : http://codingjunkie.net/secondary-sort/
package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;


public class Q1b {
    public static class TokenizerMapper extends Mapper<Object, Text, KeyPair, NullWritable>
    {	int src,tgt,wt;
	private NullWritable nullValue = NullWritable.get();
	private KeyPair key1 = new KeyPair();
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException
        {
		StringTokenizer itr = new StringTokenizer(value.toString(),"\t");
                src = Integer.parseInt(itr.nextToken());
                tgt = Integer.parseInt(itr.nextToken());
                wt = Integer.parseInt(itr.nextToken());
		key1.setSrc(src);
		key1.setTgt(tgt);
		key1.setWt(wt);
		               
	 context.write(key1, nullValue);
            
        }
    }
    
    
public static class KeyPair implements Writable, WritableComparable<KeyPair> {

    private IntWritable src = new IntWritable();
    private IntWritable tgt = new IntWritable();
    private IntWritable wt = new IntWritable();

	public KeyPair() {
    }

    public KeyPair(int s, int t, int w) {
        src.set(s);
        tgt.set(t);
        wt.set(w);
    }
	public IntWritable getSrc(){return src;}
	public IntWritable getTgt(){return tgt;}
	public IntWritable getWt(){return wt;}
	public void setWt(int a){wt.set(a);}
	public void setSrc(int a){src.set(a);}
	public void setTgt(int a){tgt.set(a);}

	 public KeyPair read(DataInput in) throws IOException {
        KeyPair keyPair = new KeyPair();
        keyPair.readFields(in);
        return keyPair;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        src.write(out);
	tgt.write(out);
        wt.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        src.readFields(in);
	tgt.readFields(in);
        wt.readFields(in);
    }

    @Override
    public int compareTo(KeyPair keyPair) {
        int compareValue = this.tgt.compareTo(keyPair.getTgt());
        if (compareValue == 0) {
            compareValue =-1* wt.compareTo(keyPair.getWt());
        }
	if (compareValue==0){
	compareValue = src.compareTo(keyPair.getSrc());
        
    }

	return compareValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyPair that = (KeyPair) o;
//	if((wt!=null)&&(src!=null)&&(tgt!=null)){if((tgt.equals(that.tgt)&&(wt.equals(that.wt))))return true;}
        if (wt != null ? !wt.equals(that.wt) : that.wt != null) return false;
        if (src != null ? !src.equals(that.src) : that.src != null) return false;
	if (tgt != null ? !tgt.equals(that.tgt) : that.tgt != null) return false;

        return true;
    }

  
    @Override
    public String toString() {
        return  tgt + "\t"+ src ;
    }

}
	public static class GroupingComparator extends WritableComparator{
    public GroupingComparator() {
        super(KeyPair.class, true);
    }

    @Override
    public int compare(WritableComparable tp1, WritableComparable tp2) {
        KeyPair keyPair = (KeyPair) tp1;
        KeyPair keyPair2 = (KeyPair) tp2;
  //      int a = keyPair.getSrc().compareTo(keyPair2.getSrc());
	int b = keyPair.getTgt().compareTo(keyPair2.getTgt());  
	return b;  
	}
}


 public static class MaxReducer 
extends Reducer<KeyPair, NullWritable, KeyPair, NullWritable> {
private NullWritable nullValue = NullWritable.get();
	
    @Override
    protected void reduce(KeyPair key, Iterable<NullWritable> values, Context context)
	throws IOException, InterruptedException {
        int value;
//            for(NullWritable val : values)
//            {
        context.write(key, nullValue);       
//            }
	
    }
}
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Q1b");
        

        job.setJarByClass(Q1b.class);

        job.setMapperClass(TokenizerMapper.class);
//        job.setCombinerClass(MaxReducer.class);
	job.setGroupingComparatorClass(GroupingComparator.class);
        job.setReducerClass(MaxReducer.class);

        job.setOutputKeyClass(KeyPair.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
