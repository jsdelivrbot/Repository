package monitor;

import java.util.Date;
import java.util.Map;

import monitor.cassandra.SimpleClient;
import entity.Task;

public class ClientMonior implements Runnable {

	private SimpleClient cassandraClient;
	private Map<String, Task> submittedTasks;
	private String clientID;
	private boolean clientShutoff = false;

	public SimpleClient getCassandraClient() {
		return cassandraClient;
	}

	public void setCassandraClient(SimpleClient cassandraClient) {
		this.cassandraClient = cassandraClient;
	}

	public Map<String, Task> getSubmittedTasks() {
		return submittedTasks;
	}

	public void setSubmittedTasks(Map<String, Task> submittedTasks) {
		this.submittedTasks = submittedTasks;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public boolean isClientShutoff() {
		return clientShutoff;
	}

	public void setClientShutoff(boolean clientShutoff) {
		this.clientShutoff = clientShutoff;
	}

	public ClientMonior(String clientID, SimpleClient cassandraClient,
			Map<String, Task> submittedTasks) {
		super();
		this.clientID = clientID;
		this.cassandraClient = cassandraClient;
		this.submittedTasks = submittedTasks;

	}

	public static void main(String[] args) {

	}

	@Override
	public void run() {

		try {
			while (!clientShutoff) {
				String[] values = { clientID,
						WorkerMonitor.getTimestamp(new Date()),
						String.valueOf(submittedTasks.size()) };
				cassandraClient.insertQlength(values);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Shutting Client Moniter");

	}
}
