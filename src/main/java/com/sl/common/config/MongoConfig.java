package com.sl.common.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {
	@NotBlank
	@Value("${mongo.database}")
	private String database;
	@NotEmpty
	@Value("${mongo.address}")
	private List<String> address;
	@Value("${mongo.username}")
	private String username;
	@Value("${mongo.password}")
	private String password;
	@Value("${mongo.options.min-connections-per-host}")
	private Integer minConnectionsPerHost = 0;
	@Value("${mongo.options.max-connections-per-host}")
	private Integer maxConnectionsPerHost = 100;
	@Value("${mongo.options.threads-allowed-to-block-for-connection-multiplier}")
	private Integer threadsAllowedToBlockForConnectionMultiplier = 5;
	@Value("${mongo.options.server-selection-timeout}")
	private Integer serverSelectionTimeout = 30000;
	@Value("${mongo.options.max-wait-time}")
	private Integer maxWaitTime = 120000;
	@Value("${mongo.options.max-connection-idel-time}")
	private Integer maxConnectionIdleTime = 0;
	@Value("${mongo.options.max-connection-life-time}")
	private Integer maxConnectionLifeTime = 0;
	@Value("${mongo.options.connect-timeout}")
	private Integer connectTimeout = 10000;
	@Value("${mongo.options.socket-timeout}")
	private Integer socketTimeout = 0;
	@Value("${mongo.options.socket-keep-alive}")
	private Boolean socketKeepAlive = false;
	@Value("${mongo.options.ssl-enabled}")
	private Boolean sslEnabled = false;
	@Value("${mongo.options.ssl-invalid-host-name-allowed}")
	private Boolean sslInvalidHostNameAllowed = false;
	@Value("${mongo.options.always-use-m-beans}")
	private Boolean alwaysUseMBeans = false;
	@Value("${mongo.options.heartbeat-socket-timeout}")
	private Integer heartbeatFrequency = 10000;
	@Value("${mongo.options.min-heartbeat-frequency}")
	private Integer minHeartbeatFrequency = 500;
	@Value("${mongo.options.heartbeat-connect-timeout}")
	private Integer heartbeatConnectTimeout = 20000;
	@Value("${mongo.options.heartbeat-socket-timeout}")
	private Integer heartbeatSocketTimeout = 20000;
	@Value("${mongo.options.local-threshold}")
	private Integer localThreshold = 15;
//	@Value("${mongo.options.min-connections-per-host}")
	private String authenticationDatabase;

	// 覆盖默认的MongoDbFacotry
	@Bean
	@Autowired
	public MongoDbFactory mongoDbFactory(MongoConfig properties) {
		// 客户端配置（连接数，副本集群验证）
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(maxConnectionsPerHost);
		builder.minConnectionsPerHost(minConnectionsPerHost);
		/*if (properties.getReplicaSet() != null) {
			builder.requiredReplicaSetName(properties.getReplicaSet());
		}*/
		builder.threadsAllowedToBlockForConnectionMultiplier(
				threadsAllowedToBlockForConnectionMultiplier);
		builder.serverSelectionTimeout(serverSelectionTimeout);
		builder.maxWaitTime(maxWaitTime);
		builder.maxConnectionIdleTime(maxConnectionIdleTime);
		builder.maxConnectionLifeTime(maxConnectionLifeTime);
		builder.connectTimeout(connectTimeout);
		builder.socketTimeout(socketTimeout);
		builder.socketKeepAlive(socketKeepAlive);
		builder.sslEnabled(sslEnabled);
		builder.sslInvalidHostNameAllowed(sslInvalidHostNameAllowed);
		builder.alwaysUseMBeans(alwaysUseMBeans);
		builder.heartbeatFrequency(heartbeatFrequency);
		builder.minHeartbeatFrequency(minHeartbeatFrequency);
		builder.heartbeatConnectTimeout(heartbeatConnectTimeout);
		builder.heartbeatSocketTimeout(heartbeatSocketTimeout);
		builder.localThreshold(localThreshold);

		MongoClientOptions mongoClientOptions = builder.build();

		// MongoDB地址列表
		List<ServerAddress> serverAddresses = new ArrayList<>();
		for (String address : address) {
			String[] hostAndPort = address.split(":");
			String host = hostAndPort[0];
			Integer port = Integer.parseInt(hostAndPort[1]);

			ServerAddress serverAddress = new ServerAddress(host, port);
			serverAddresses.add(serverAddress);
		}
		// 连接认证(60认证)及(线上认证)
		MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(
				username, authenticationDatabase != null
						? authenticationDatabase : database,
				password.toCharArray());
		List<MongoCredential> list = new ArrayList<MongoCredential>();
		list.add(mongoCredential);
		//(60认证)及(线上认证)
		MongoClient mongoClient = new MongoClient(serverAddresses,list, mongoClientOptions);
		// 创建客户端和Factory(线上认证错误)
		//MongoClient mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, database);
		return mongoDbFactory;
	}
	
}
