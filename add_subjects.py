import json

subjects = {
    "Load Balancing": ["load balanc", "consistent hashing", "round robin", "proxy", "sticky session", "gateway", "service mesh", "sidecar"],
    "Databases": ["database", "sql", "nosql", "sharding", "replication", "partition", "columnar", "acid", "cap theorem", "pacelc", "wal", "locking", "mvcc", "index", "b-tree", "bloom", "lsm"],
    "Caching": ["cach", "redis", "memcached", "eviction", "cdn", "content delivery", "static", "thundering herd", "stampede"],
    "Microservices": ["microservice", "monolith", "saga", "event sourc", "cqrs", "circuit breaker", "orchestrat", "rpc", "grpc", "message queue", "kafka", "pub/sub", "idempotenc", "api gateway"],
    "Networking": ["tcp", "udp", "http", "tls", "ssl", "dns", "ip", "protocol", "websocket", "polling", "socket", "quic"],
    "Security": ["security", "auth", "token", "encryption", "tls", "ssl", "attack", "cors", "dos", "ddos", "firewall", "bastion"],
    "Redis": ["redis", "pipelining", "sentinel", "pub/sub"],
    "DynamoDB": ["dynamodb", "dynamo", "dax", "provisioned throughput", "rcu", "wcu", "gsi", "lsi"]
}

def categorize(text):
    text = text.lower()
    for subject, keywords in subjects.items():
        for keyword in keywords:
            if keyword in text:
                return subject
    return "General"

try:
    with open('app/src/main/assets/system_design_questions.json', 'r') as f:
        questions = json.load(f)

    for q in questions:
        # Categorize based on question text and explanation
        combined = q['question'] + " " + q.get('explanation', "")
        q['subject'] = categorize(combined)

    with open('app/src/main/assets/system_design_questions.json', 'w') as f:
        json.dump(questions, f, indent=4)
        
    print(f"Updated {len(questions)} questions with subjects.")

except Exception as e:
    print(f"Error: {e}")
