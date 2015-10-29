package fi.aalto.dmg.frame;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by yangjun.wang on 25/10/15.
 */
public class FlinkOperatorCreater extends OperatorCreater {

    private Properties properties;
    final StreamExecutionEnvironment env;

    public FlinkOperatorCreater(String name) throws IOException {
        super(name);
        properties = new Properties();
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("spark-cluster.properties"));
    }

    /**
     *
     * @param zkConStr
     * @param group
     * @param topics single topic
     * @return
     */
    @Override
    public WorkloadOperator<String> createOperatorFromKafka(String zkConStr, String group, String topics) {
        /*
        * Note that the Kafka source is expecting the following parameters to be set
        *  - "bootstrap.servers" (comma separated list of kafka brokers)
        *  - "zookeeper.connect" (comma separated list of zookeeper servers)
        *  - "group.id" the id of the consumer group
        *  - "topic" the name of the topic to read data from.
        *  "--bootstrap.servers host:port,host1:port1 --zookeeper.connect host:port --topic testTopic"
        */
        Properties properties = new Properties();
        properties.put("group.id", "flink-streaming-demo-a");
        properties.put("auto.offset.reset", "smallest");

        DataStream<String> stream = env
                .addSource(new FlinkKafkaConsumer082<String>(topics, new SimpleStringSchema(), properties));
        return new FlinkWorkloadOperator<String>(stream);
    }

    @Override
    public void Start() {
        try {
            env.execute(this.getAppName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}