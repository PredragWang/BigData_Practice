package gwang.scalabasic;

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.rdd._
import scala.math._

/** Find the minimum temperature by weather station */
object Weather {
  /**
   * Input csv format
   * <Weather Station ID>,<date>,<entry type><temperature>
   */
  def parseLine(line:String)= {
    val fields = line.split(",")
    val stationID = fields(0)
    val entryType = fields(2)
    val temperature = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f
    (stationID, entryType, temperature)
  }
  
  def minTemperaturesByStation(lines: RDD[String]) = {
    val parsedLines = lines.map(parseLine)    
    // Filter out all but TMIN entries
    val minTemps = parsedLines.filter(x => x._2 == "TMIN")    
    // Convert to (stationID, temperature)
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))    
    // Reduce by stationID retaining the minimum temperature found
    val minTempsByStation = stationTemps.reduceByKey( (t1,t2) => min(t1,t2))    
    // Collect, format, and print the results
    val results = minTempsByStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val temp = result._2
       println(s"$station minimum temperature: $temp") 
    }
  }
  
  def maxTemperaturesByStation(lines: RDD[String]) = {
    val parsedLines = lines.map(parseLine)    
    // Filter out all but TMAX entries
    val maxTemps = parsedLines.filter(x => x._2 == "TMAX")    
    // Convert to (stationID, temperature)
    val stationTemps = maxTemps.map(x => (x._1, x._3.toFloat))    
    // Reduce by stationID retaining the maximum temperature found
    val maxTempsByStation = stationTemps.reduceByKey( (t1,t2) => max(t1,t2))    
    // Collect, format, and print the results
    val results = maxTempsByStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val temp = result._2
       println(s"$station maximum temperature: $temp") 
    }
  }
  
  def avgMinTemperaturesByStation(lines: RDD[String]) = {
    val parsedLines = lines.map(parseLine)    
    // Filter out all but TMIN entries
    val minTemps = parsedLines.filter(x => x._2 == "TMIN")    
    // Convert to (stationID, temperature)
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat)).mapValues { t => (t, 1) }
    // sum the min temperatures by station
    val sumMinTempsByStation = stationTemps.reduceByKey( (t1,t2) => (t1._1+t2._1, t1._2 + t2._2) )    
    // Collect, format, and print the results
    val avgMinTempsByStation = sumMinTempsByStation.mapValues(t => t._1/t._2.toFloat)
    val results = avgMinTempsByStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val temp = result._2
       println(s"$station average minimum temperature: $temp") 
    }
  }
  
  def avgMaxTemperaturesByStation(lines: RDD[String]) = {
    val parsedLines = lines.map(parseLine)    
    // Filter out all but TMAX entries
    val maxTemps = parsedLines.filter(x => x._2 == "TMAX")    
    // Convert to (stationID, temperature)
    val stationTemps = maxTemps.map(x => (x._1, x._3.toFloat)).mapValues { t => (t, 1) }
    // sum the max temperatures by station
    val sumMaxTempsByStation = stationTemps.reduceByKey( (t1,t2) => (t1._1+t2._1, t1._2 + t2._2) )    
    // Collect, format, and print the results
    val avgMaxTempsByStation = sumMaxTempsByStation.mapValues(t => t._1/t._2.toFloat)
    val results = avgMaxTempsByStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val temp = result._2
       println(s"$station average maximum temperature: $temp") 
    }
  }
  
    /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MinTemperatures")
    
    // Read each line of input data
    val lines = sc.textFile("data/1800.csv")
    
    minTemperaturesByStation(lines)
    maxTemperaturesByStation(lines)
    avgMinTemperaturesByStation(lines)
    avgMaxTemperaturesByStation(lines)
  }
}