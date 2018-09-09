package edu.gatech.cse6242

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._

object Q2 {

	def main(args: Array[String]) {
    	val sc = new SparkContext(new SparkConf().setAppName("Q2"))
		val sqlContext = new SQLContext(sc)
		import sqlContext.implicits._

    	// read the file
    	val file = sc.textFile("hdfs://localhost:8020" + args(0))
		/* TODO: Needs to be implemented */

val src_wt = file.map( _.split("\t")).filter(p=>(p.last).toInt >= 10).map(p=>(p(0).toInt, p(2).toDouble))
val tgt_wt = file.map( _.split("\t")).filter(p=>(p.last).toInt >= 10).map(p=>(p(1).toInt, p(2).toDouble*(-1)))

val outer=src_wt.groupBy(_._1).mapValues(a=>a.map(_._2).sum/a.map(_._2).size)
val inner=tgt_wt.groupBy(_._1).mapValues(a=>a.map(_._2).sum/a.map(_._2).size)

val total= outer.union(inner)
val merged = total.groupBy ( _._1) .map { case (k,v) => k -> v.map(_._2).sum }


 val output =merged.map(line => line._1 + "\t" + line._2)
   println(s"$output")

    	// store output on given HDFS path.
    	// YOU NEED TO CHANGE THIS
//    	output.map(x => x.mkString("\t")).saveAsTextFile("hdfs://localhost:8020" + args(1))
    	output.saveAsTextFile("hdfs://localhost:8020" + args(1))
	//sc.makeRDD(output).saveAsTextFile("hdfs://localhost:8020" + args(1)) 	
	}
}
