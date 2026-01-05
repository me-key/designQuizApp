import json
import random

new_questions = [
    # REDIS Questions
    {
        "question": "What is the primary data structure used by Redis for its Key-Value store?",
        "options": ["B-Tree", "Hash Table", "LSM Tree", "Linked List"],
        "correctAnswerIndex": 1,
        "explanation": "Redis primarily uses a global Hash Table to store all keys and values, allowing for O(1) average time complexity for lookups.",
        "subject": "Redis"
    },
    {
        "question": "Which Redis persistence mechanism provides faster recovery but potentially more data loss?",
        "options": ["AOF (Append Only File)", "RDB (Redis Database Backup)", "WAL (Write Ahead Log)", "Snapshot Isolation"],
        "correctAnswerIndex": 1,
        "explanation": "RDB (Snapshotting) creates point-in-time snapshots. It is faster to restore from but you lose data between the last snapshot and the crash.",
        "subject": "Redis"
    },
    {
        "question": "What is a 'Redis Sentinel' used for?",
        "options": ["Data encryption", "Monitoring, Notification, and Automatic Failover", "Compressing data", "Query optimization"],
        "correctAnswerIndex": 1,
        "explanation": "Redis Sentinel provides high availability. It monitors Redis instances, notifies administrators of errors, and handles automatic failover if the master goes down.",
        "subject": "Redis"
    },
    {
        "question": "Which data structure in Redis is most suitable for implementing a Leaderboard?",
        "options": ["Set", "List", "Sorted Set (ZSET)", "Hash"],
        "correctAnswerIndex": 2,
        "explanation": "Sorted Sets (ZSET) store unique elements with a score. They support efficient retrieval of elements by rank or score range, making them perfect for leaderboards.",
        "subject": "Redis"
    },
    {
        "question": "How does Redis handle data expiration?",
        "options": ["It deletes all data at midnight", "Using active (probabilistic) and passive (lazy) expiration", "It requires a separate garbage collector process", "It overwrites old data immediately"],
        "correctAnswerIndex": 1,
        "explanation": "Redis uses two methods: Lazy (remove when accessed) and Active (randomly test keys with expiry and remove expired ones) to ensure expired keys are cleaned up.",
        "subject": "Redis"
    },
    {
        "question": "What is 'Redis Cluster'?",
        "options": ["A single large Redis instance", "A distributed implementation of Redis with automatic sharding", "A backup service", "A GUI for Redis"],
        "correctAnswerIndex": 1,
        "explanation": "Redis Cluster provides a way to run a Redis installation where data is automatically sharded across multiple Redis nodes.",
        "subject": "Redis"
    },
    {
        "question": "What is the main limitation of Redis compared to a disk-based DB?",
        "options": ["It is slower", "It requires dataset to fit in RAM", "It does not support strings", "It cannot be replicated"],
        "correctAnswerIndex": 1,
        "explanation": "Since Redis is an in-memory store, the entire dataset must generally fit within the available RAM, unlike disk-based databases which can store petabytes.",
        "subject": "Redis"
    },
    {
        "question": "In Redis, what is 'Pipelining'?",
        "options": ["Sending multiple commands without waiting for replies", "Compressing network traffic", "Encrypting the connection", "Streaming video"],
        "correctAnswerIndex": 0,
        "explanation": "Pipelining facilitates sending multiple commands to the server without waiting for the replies, reducing the cost of Round Trip Time (RTT).",
        "subject": "Redis"
    },
    {
        "question": "What happens when Redis runs out of memory (maxmemory reached)?",
        "options": ["It crashes", "It always stops accepting writes", "It evicts keys according to the configured eviction policy (e.g., LRU, LFU)", "It writes to swap automatically"],
        "correctAnswerIndex": 2,
        "explanation": "Redis will try to remove keys based on the `maxmemory-policy` (e.g., allkeys-lru, volatile-lru). If it cannot free memory, it returns an error for write commands.",
        "subject": "Redis"
    },
    {
        "question": "Which Redis data structure is a linked list of strings?",
        "options": ["Redis List", "Redis Set", "Redis Hash", "Redis Stream"],
        "correctAnswerIndex": 0,
        "explanation": "Redis Lists are implemented as linked lists. This makes adding elements to the head or tail O(1) but accessing by index O(N).",
        "subject": "Redis"
    },
    {
        "question": "What is the pub/sub feature in Redis?",
        "options": ["Publish/Subscribe messaging pattern", "Public/Subnet configuration", "Publication Submission", "Public Substitute"],
        "correctAnswerIndex": 0,
        "explanation": "Pub/Sub allows senders (publishers) to send messages into channels, while receivers (subscribers) listen to them, decoupling the message senders from receivers.",
        "subject": "Redis"
    },
    {
        "question": "Is Redis single-threaded?",
        "options": ["No, it uses a thread pool for everything", "Yes, the main event loop is single-threaded", "Yes, but only for reads", "No, it uses multiple processes by default"],
        "correctAnswerIndex": 1,
        "explanation": "Redis uses a single-threaded architecture for its main event loop to handle commands, ensuring atomicity and avoiding context-switching overhead. However, it uses background threads for I/O tasks like snapshotting.",
        "subject": "Redis"
    },
    {
        "question": "What is a 'Hash Slot' in Redis Cluster?",
        "options": ["A slot for physical RAM", "A logical unit for data sharding (there are 16384 slots)", "A type of encryption", "A backup slot"],
        "correctAnswerIndex": 1,
        "explanation": "Redis Cluster does not use consistent hashing, but max 16384 hash slots. Every key is mapped to a hash slot, and slots are distributed among nodes.",
        "subject": "Redis"
    },

    # DYNAMODB Questions
    {
        "question": "What type of database is Amazon DynamoDB?",
        "options": ["Relational (SQL)", "Key-Value and Document (NoSQL)", "Graph", "Time Series"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB is a fully managed proprietary NoSQL database service that supports key-value and document data structures.",
        "subject": "DynamoDB"
    },
    {
        "question": "In DynamoDB, what is the 'Partition Key'?",
        "options": ["A key used to encrypt the partition", "The primary key attribute used to distribute data across physical partitions", "The backup key", "The sorting key"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB uses the partition key's value as input to an internal hash function. The output determines the partition where the item is stored.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is a 'Sort Key' (Range Key) in DynamoDB?",
        "options": ["A key used to sort table names", "The second part of a composite primary key that sorts items with the same partition key", "A key for sorting the entire database", "A secondary index"],
        "correctAnswerIndex": 1,
        "explanation": "In a composite primary key (Partition Key + Sort Key), the Sort Key stores items with the same Partition Key physically close together, sorted by value.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'Provisioned Throughput' in DynamoDB?",
        "options": ["The amount of RAM allocated", "Defining Read Capacity Units (RCUs) and Write Capacity Units (WCUs)", "The network bandwidth", "The disk space"],
        "correctAnswerIndex": 1,
        "explanation": "You assume responsibility for defined throughput capacity. You pay for the provisioned RCUs and WCUs, regardless of whether you use them (unless using On-Demand mode).",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'Eventually Consistent Read' in DynamoDB?",
        "options": ["The default read mode that might return stale data but costs half the RCUs", "A read that never returns", "A slow read", "A read that guarantees latest data"],
        "correctAnswerIndex": 0,
        "explanation": "Eventually Consistent Reads maximize read throughput and effectively cost half the RCUs of a Strongly Consistent Read, but might not reflect the results of a recently completed write.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is a 'GSI' (Global Secondary Index) in DynamoDB?",
        "options": ["Grid System Index", "An index with a partition key and sort key that can be different from those on the base table", "Global Server Identifier", "A backup index"],
        "correctAnswerIndex": 1,
        "explanation": "GSI permits queries on attributes other than the table's primary key. It can have a different partition key and sort key from the main table and is stored separately.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'LSI' (Local Secondary Index) in DynamoDB?",
        "options": ["An index with the same partition key as the base table but a different sort key", "Local Server Index", "Light Storage Interface", "Large Scale Index"],
        "correctAnswerIndex": 0,
        "explanation": "LSI allows querying data using the same partition key but a different sort key. It must be created when the table is created.",
        "subject": "DynamoDB"
    },
    {
        "question": "What happens if you exceed your provisioned throughput in DynamoDB?",
        "options": ["The request is throttled (rejected with 400 error)", "The database scales up automatically instantly", "The request is queued forever", "The table is deleted"],
        "correctAnswerIndex": 0,
        "explanation": "DynamoDB throttles requests that exceed the allocated RCU/WCU, returning a 'ProvisionedThroughputExceededException'. The SDK usually handles retries with backoff.",
        "subject": "DynamoDB"
    },
    {
        "question": "Does DynamoDB support ACID transactions?",
        "options": ["No, never", "Yes, for single and multi-item operations within and across tables", "Only for single items", "Only for read operations"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB supports ACID transactions across one or more tables within a single AWS account and region, streamlining workflows that require all-or-nothing changes.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'DynamoDB Streams'?",
        "options": ["A video streaming service", "An ordered flow of information about changes to items in a DynamoDB table", "A query language", "A backup tool"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB Streams captures a time-ordered sequence of item-level modifications in any DynamoDB table and stores this information for up to 24 hours.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the 'Item Size Limit' in DynamoDB?",
        "options": ["1 KB", "400 KB", "1 MB", "1 GB"],
        "correctAnswerIndex": 1,
        "explanation": "The maximum size of a single item in DynamoDB (including attribute names and values) is 400 KB.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'DAX' (DynamoDB Accelerator)?",
        "options": ["A query syntax", "A fully managed, highly available, in-memory cache for DynamoDB", "A simplified version of DynamoDB", "A migration tool"],
        "correctAnswerIndex": 1,
        "explanation": "DAX is an in-memory cache that delivers up to 10x performance improvement from milliseconds to microseconds for read-heavy workloads.",
        "subject": "DynamoDB"
    },
    {
        "question": "In DynamoDB, what is the 'Hot Partition' problem?",
        "options": ["When a partition overheats physically", "When access is unevenly distributed, causing one partition to exceed its throughput limits", "When data is deleted too fast", "When you use too many hotkeys"],
        "correctAnswerIndex": 1,
        "explanation": "If a workload is uneven (e.g., many requests for a single partition key), it creates a 'hot partition' that can handle a limited amount of throughput, potentially causing throttling even if total table throughput is sufficient.",
        "subject": "DynamoDB"
    }
]

# Append to assets
try:
    with open('app/src/main/assets/system_design_questions.json', 'r') as f:
        existing = json.load(f)
    
    # Assign IDs
    start_id = max(q['id'] for q in existing) + 1
    for i, q in enumerate(new_questions):
        q['id'] = start_id + i
        existing.append(q)
        
    with open('app/src/main/assets/system_design_questions.json', 'w') as f:
        json.dump(existing, f, indent=4)
        print(f"Added {len(new_questions)} new questions.")
        
except Exception as e:
    print(e)
