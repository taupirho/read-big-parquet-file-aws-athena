package tom

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql._
import org.apache.log4j._
import java.util.Calendar

object test {  
  /** Our main function where the action happens */
  def main(args: Array[String]) {
    
    
    println(Calendar.getInstance.getTime)
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    println("Setting up Spark session")
    
    // Use new SparkSession interface in Spark 2.0
    val spark = SparkSession
      .builder
      .appName("SparkSQL")
      .master("spark://127.0.0.1:7077")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
      .getOrCreate()
    
    println("Reading in input file")
    
              
    val df = spark.read.format("com.databricks.spark.csv").option("header", "false").option("inferSchema", "true").option("delimiter", "|").load("file:///d:/tmp/iholding/IssueHolding.txt")
    df.write.parquet("file:///d:/tmp/iholding/newdir")
    // Use the line below if you want just one mega file as output instead of a bunch of smaller ones
    df.coalesce(1).write.parquet("file:///d:/tmp/iholding/newdir")
    spark.stop()
  }
}
