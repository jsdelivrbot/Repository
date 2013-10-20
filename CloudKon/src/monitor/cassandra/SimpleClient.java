package monitor.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Query;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class SimpleClient {

	private Cluster cluster;
	private Session session;
	final String[] columnsCPU = { "instance_id", "collected_at", "util_percent" };
	final String[] columnsnodestatus = { "instance_id", "collected_at",
			"nStatus" };

	public void connect(String node) {

		Builder cBuilder = Cluster.builder().withPort(9042);
		String[] splits = node.split(":");

		System.out.println("splits.size: " + splits.length);

		for (String asset : splits) {
			cBuilder.addContactPoint(asset);
		}

		cluster = cBuilder.build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n",
				metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
		session = cluster.connect();
	}

	public void close() {
		cluster.shutdown();
	}

	public void insertCPU(String[] values) {
		Query query = QueryBuilder.insertInto("cs554_cloudkon", "cpuUtil")
				.values(columnsCPU, values);
		session.execute(query);
	}

	public void insertNodeStatus(String[] values) {
		Query query = QueryBuilder.insertInto("cs554_cloudkon", "nodestatus")
				.values(columnsnodestatus, values);
		session.execute(query);
	}

	public void getRowsStatus() {
		Query query = QueryBuilder.select().all()
				.from("cs554_cloudkon", "nodestatus");
		ResultSetFuture results = session.executeAsync(query);
		int counter = 0;
		for (Row row : results.getUninterruptibly()) {
			System.out.printf("%s: %s / %s\n", row.getString("instance_id"),
					row.getString("collected_at"), row.getString("nStatus"));
			counter++;
		}
		System.out.println("counter " + counter);
	}

	public void getRowsCpu() {
		Query query = QueryBuilder.select().all()
				.from("cs554_cloudkon", "cpuUtil");
		ResultSetFuture results = session.executeAsync(query);
		for (Row row : results.getUninterruptibly()) {
			System.out.printf("%s: %s / %s\n", row.getString("instance_id"),
					row.getString("collected_at"),
					row.getString("util_percent"));
		}
	}
}