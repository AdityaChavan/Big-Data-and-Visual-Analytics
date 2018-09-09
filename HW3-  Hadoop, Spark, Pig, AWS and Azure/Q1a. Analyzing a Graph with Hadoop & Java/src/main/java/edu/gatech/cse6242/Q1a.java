//referenced from : https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Example:_WordCount_v1.0

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



public class Q1a {
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
    {
        private final static IntWritable one = new IntWritable();
        private Text word1 = new Text();
        private Text word2 = new Text();
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException
        {
		StringTokenizer itr = new StringTokenizer(value.toString(),"\t");
                word1.set(itr.nextToken()); //word1=source node
            	word2.set(itr.nextToken()); //word2=target node -not needed
                one.set(Integer.parseInt(itr.nextToken()));
                context.write(word1, one);
            
        }
    }
    
    public static class MaxReducer extends Reducer<Text, IntWritable, Text, IntWritable>
    {
        private IntWritable result = new IntWritable();
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
        {
            int max=0;
            int value;
            for(IntWritable val : values)
            {
                value = val.get();
                if(value > max) max = value; 
            }
            result.set(max);
            context.write(key, result);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Q1a");
        
        job.setJarByClass(Q1a.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(MaxReducer.class);
        job.setReducerClass(MaxReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
