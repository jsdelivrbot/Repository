import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object WordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("wordCount").setMaster("local")
    val sc = new SparkContext(conf)
    val input = sc.textFile("/home/ram/eclipse/artifacts.xml")
    val words = input.flatMap(line => line.split(' '))
    val count = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
    count.saveAsTextFile("output")
  }
}