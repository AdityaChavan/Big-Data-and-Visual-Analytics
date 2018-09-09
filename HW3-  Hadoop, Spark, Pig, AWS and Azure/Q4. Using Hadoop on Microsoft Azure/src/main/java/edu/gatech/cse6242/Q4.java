package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Object;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4 {

 public static class TokenizerMapper1 extends Mapper<Object,Text,IntWritable,IntWritable>{
private IntWritable src = new IntWritable();
private IntWritable tgt = new IntWritable();
private IntWritable a = new IntWritable(1);	//a=1;
private IntWritable b = new IntWritable(-1);	//b=-1;
public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
String line = value.toString();
String[] field = line.split("\t");
if (field.length == 2) {
src.set(Integer.parseInt(field[0]));
context.write(src,b);
tgt.set(Integer.parseInt(field[1]));
context.write(tgt,a);
}
}
}//mapper1

public static class Reducer1 extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
private IntWritable result = new IntWritable();

public void reduce(IntWritable key,Iterable<IntWritable> value, Context context) throws IOException, InterruptedException {
int sum=0;
for(IntWritable val:value)sum+=val.get();
result.set(sum);
context.write(key,result);
}
}//reducer1

  public static class TokenizerMapper2
    extends Mapper<Object, Text, IntWritable, IntWritable>{
private IntWritable src = new IntWritable();
private IntWritable tgt = new IntWritable();
private final static IntWritable a = new IntWritable(1);
public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
String line = value.toString();
String[] field = line.split("\t");
if (field.length == 2) {
src.set(Integer.parseInt(field[0]));
tgt.set(Integer.parseInt(field[1]));
context.write(tgt,a);
}
    }
  }

public static class Reducer2 extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
private IntWritable result = new IntWritable();

public void reduce(IntWritable key,Iterable<IntWritable> value, Context context) throws IOException, InterruptedException {
int sum=0;
for(IntWritable val:value)sum+=val.get();
result.set(sum);
context.write(key,result);
}
}//reducer2


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job1 = Job.getInstance(conf, "Q4");

    job1.setJarByClass(Q4.class);
    job1.setMapperClass(TokenizerMapper1.class);
    job1.setCombinerClass(Reducer1.class);
    job1.setReducerClass(Reducer1.class);
    job1.setOutputKeyClass(IntWritable.class);
    job1.setOutputValueClass(IntWritable.class);
//*/
    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, new Path("first_output"));

    job1.waitForCompletion(true);
    Job job2 = Job.getInstance(conf, "job2");

    job2.setJarByClass(Q4.class);
    job2.setMapperClass(TokenizerMapper2.class);
    job2.setCombinerClass(Reducer2.class);
    job2.setReducerClass(Reducer2.class);
    job2.setOutputKeyClass(IntWritable.class);
    job2.setOutputValueClass(IntWritable.class);
//*/
    FileInputFormat.addInputPath(job2, new Path("first_output"));
    FileOutputFormat.setOutputPath(job2, new Path(args[1]));
    System.exit(job2.waitForCompletion(true) ? 0 : 1);
  }
}
