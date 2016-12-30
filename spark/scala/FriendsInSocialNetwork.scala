package gwang.scalabasic

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.log4j._

/** Compute the average number of friends by age in a social network. */
object FriendsInSocialNetwork {
  
  /**
   * input csv format:
   * <id>,<first name>,<age>,<friends count>
   */
  /** A function that splits a line of input into (key, numFriends) tuples. */
  def parseLine (line: String, keyIdx: Int) = {
      // Split by commas
      val fields = line.split(",")
      // Extract the key field, and convert to integers
      val key = fields(keyIdx)
      val numFriends = fields(3).toInt
      // Create a tuple that is our result.
      (key, numFriends)
  }
  
  def countFriendsByKey (lines: RDD[String], keyIdx: Int) = {
    val rdd = lines.map( x => parseLine(x, keyIdx) )
    // count total number of instances and friends count by age
    val totalsByAge = rdd.mapValues( cFriends => (cFriends, 1) ).reduceByKey( (v1,v2) => (v1._1+v2._1, v1._2+v2._2))
    val avgByAge = totalsByAge.mapValues( x => x._1/x._2 )
    // Collect the results from the RDD
    val results = avgByAge.collect()
    
    // Sort and print the final results.
    results.sorted.foreach(println)
  }

  /** Main function */
  def main(args: Array[String]) = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "FriendsInSocialNetwork")
    val lines = sc.textFile("data/fakefriends.csv")
    countFriendsByKey(lines, 2)
  }
}

  