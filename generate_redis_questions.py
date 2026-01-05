import json

new_questions = [
    {
        "question": "What is the maximum size of a value in Redis?",
        "options": ["1 GB", "512 MB", "128 MB", "2 GB"],
        "correctAnswerIndex": 1,
        "explanation": "A Redis value can hold up to 512 MB of data, including strings, lists, sets, and hashes.",
        "subject": "Redis"
    },
    {
        "question": "Which Redis data structure is a probabilistic data structure used to count unique items?",
        "options": ["HyperLogLog", "Bitmap", "Bloom Filter", "GeoHash"],
        "correctAnswerIndex": 0,
        "explanation": "HyperLogLog is a probabilistic data structure used in Redis to estimate the cardinality of a set with very little memory.",
        "subject": "Redis"
    },
    {
        "question": "Which command is used to set a key only if it does not already exist?",
        "options": ["SETNX", "SET", "MSET", "GETSET"],
        "correctAnswerIndex": 0,
        "explanation": "SETNX stands for 'SET if Not eXists'. It sets the key to the value only if the key does not already hold a value.",
        "subject": "Redis"
    },
    {
        "question": "What happens to expired keys in Redis?",
        "options": ["They are archived", "They remain until manually deleted", "They are automatically deleted", "They are moved to a separate database"],
        "correctAnswerIndex": 2,
        "explanation": "Redis automatically deletes keys when their Time To Live (TTL) expires.",
        "subject": "Redis"
    },
    {
        "question": "Which eviction policy removes the least recently used keys first?",
        "options": ["allkeys-lru", "allkeys-random", "volatile-ttl", "noeviction"],
        "correctAnswerIndex": 0,
        "explanation": "allkeys-lru evicts the least recently used keys regardless of whether they have an expiration set.",
        "subject": "Redis"
    },
    {
        "question": "Does Redis support nested data structures (e.g., a List inside a Hash)?",
        "options": ["Yes, fully supported", "No, Redis does not support nested types", "Only via JSON module", "Yes, up to 3 levels"],
        "correctAnswerIndex": 1,
        "explanation": "Native Redis data structures are not nested. You cannot store a List inside a Hash directly without using serialization or modules like RedisJSON.",
        "subject": "Redis"
    },
    {
        "question": "What is the complexity of adding an element to a Redis Set (SADD)?",
        "options": ["O(N)", "O(1)", "O(log N)", "O(N*2)"],
        "correctAnswerIndex": 1,
        "explanation": "Adding an element to a Redis Set is O(1) in the average case.",
        "subject": "Redis"
    },
    {
        "question": "Which command allows you to execute a group of commands atomically in Redis?",
        "options": ["MULTI / EXEC", "START / END", "BEGIN / COMMIT", "LOCK / UNLOCK"],
        "correctAnswerIndex": 0,
        "explanation": "The MULTI command starts a transaction, and EXEC executes all commands queued in the transaction atomically.",
        "subject": "Redis"
    },
    {
        "question": "What is key space notification in Redis?",
        "options": ["A way to increase key size", "A mechanism to receive events when keys change", "A backup alert", "A cluster heartbeat"],
        "correctAnswerIndex": 1,
        "explanation": "Keyspace notifications allow clients to subscribe to Pub/Sub channels to receive events affecting the Redis data set.",
        "subject": "Redis"
    },
    {
        "question": "Which Redis module adds support for full-text search and secondary indexing?",
        "options": ["RedisJSON", "RediSearch", "RedisGraph", "RedisBloom"],
        "correctAnswerIndex": 1,
        "explanation": "RediSearch provides full-text search, secondary indexing, and querying capabilities on top of Redis.",
        "subject": "Redis"
    },
    {
        "question": "What is the primary use case for Redis 'Bitmaps'?",
        "options": ["Storing images", "Efficiently storing boolean information (e.g., daily active users)", "Storing large text blobs", "Indexing documents"],
        "correctAnswerIndex": 1,
        "explanation": "Bitmaps allow you to manipulate individual bits in a string, making them extremely space-efficient for storing boolean data like user activity status.",
        "subject": "Redis"
    },
    {
        "question": "How does Redis replication work by default?",
        "options": ["Synchronous", "Asynchronous", "Semi-synchronous", "Manual only"],
        "correctAnswerIndex": 1,
        "explanation": "Redis replication is asynchronous by default, meaning the master acknowledges the write to the client before propagating to replicas.",
        "subject": "Redis"
    },
    {
        "question": "What is the 'SCAN' command used for?",
        "options": ["To incrementally iterate over keys", "To scan for viruses", "To read a file", "To dump the database"],
        "correctAnswerIndex": 0,
        "explanation": "SCAN allows you to iterate over the keys in the database incrementally without blocking the server like 'KEYS *' would.",
        "subject": "Redis"
    },
    {
        "question": "Which data structure would you use to implement a priority queue in Redis?",
        "options": ["List", "Set", "Sorted Set (ZSET)", "Hash"],
        "correctAnswerIndex": 2,
        "explanation": "Sorted Sets are ideal for priority queues because elements are automatically ordered by their score (priority).",
        "subject": "Redis"
    },
    {
        "question": "What does the 'PERSIST' command do?",
        "options": ["Saves data to disk immediately", "Removes the expiration from a key", "Makes a key read-only", "Locks the key"],
        "correctAnswerIndex": 1,
        "explanation": "PERSIST removes the existing timeout on a key, turning a volatile key into a persistent key.",
        "subject": "Redis"
    },
    {
        "question": "What is 'Redis Geo' used for?",
        "options": ["Geometry calculations", "Storing and querying geospatial data", "Geophysical simulations", "Global replication"],
        "correctAnswerIndex": 1,
        "explanation": "Redis Geo commands allow you to store geospatial items (latitude, longitude, member) and query by distance or radius.",
        "subject": "Redis"
    },
    {
        "question": "Which command returns the remaining time to live of a key?",
        "options": ["TTL", "TIME", "EXPIRE", "CHECK"],
        "correctAnswerIndex": 0,
        "explanation": "TTL returns the remaining time to live of a key in seconds.",
        "subject": "Redis"
    },
    {
        "question": "Can Redis be used as a Message Broker?",
        "options": ["No, it is a database", "Yes, using Pub/Sub or Streams", "Only via third-party plugins", "Yes, but only for text"],
        "correctAnswerIndex": 1,
        "explanation": "Redis is widely used as a message broker using its Pub/Sub capabilities or the more durable Redis Streams data structure.",
        "subject": "Redis"
    },
    {
        "question": "What is 'Redis Mass Insertion'?",
        "options": ["Using the SET command repeatedly", "Using the Redis protocol (RESP) to feed data via pipe mode", "Manually typing data", "Importing CSV"],
        "correctAnswerIndex": 1,
        "explanation": "Mass insertion involves generating the Redis protocol formatted data and piping it to the `redis-cli --pipe` command for maximum throughput.",
        "subject": "Redis"
    },
    {
        "question": "Does Redis support ACID transactions fully?",
        "options": ["Yes, always", "No, it lacks rollback on error", "Yes, via Lua scripts", "No, it is eventually consistent"],
        "correctAnswerIndex": 1,
        "explanation": "Redis transactions (MULTI/EXEC) guarantee isolation and atomicity, but it does not support rollback mechanisms if a command fails inside a transaction (except for syntax errors).",
        "subject": "Redis"
    },
    {
        "question": "What is a 'Lua script' in Redis?",
        "options": ["A configuration file", "A script executed on the server side atomically", "A client-side library", "A backup script"],
        "correctAnswerIndex": 1,
        "explanation": "Lua scripts allow you to define complex logic that runs atomically on the Redis server, avoiding race conditions.",
        "subject": "Redis"
    },
    {
        "question": "What is the max length of a Redis List?",
        "options": ["2^32 - 1 elements", "Unlimited", "1 Million", "2048 elements"],
        "correctAnswerIndex": 0,
        "explanation": "A Redis List can hold up to 2^32 - 1 elements (more than 4 billion).",
        "subject": "Redis"
    },
    {
        "question": "Which command adds an element to the head of a list?",
        "options": ["RPUSH", "LPUSH", "LADD", "HSET"],
        "correctAnswerIndex": 1,
        "explanation": "LPUSH inserts one or multiple values at the head (left) of the list.",
        "subject": "Redis"
    },
    {
        "question": "Which Redis String operation increments an integer value?",
        "options": ["ADD", "INCR", "SUM", "COUNT"],
        "correctAnswerIndex": 1,
        "explanation": "INCR increments the number stored at the key by one. It is atomic.",
        "subject": "Redis"
    },
    {
        "question": "What is the difference between RDB and AOF?",
        "options": ["RDB is faster, AOF is safer", "AOF is faster, RDB is safer", "They are the same", "RDB is for Windows, AOF for Linux"],
        "correctAnswerIndex": 0,
        "explanation": "RDB (snapshots) is faster for recovery but can lose recent data. AOF (Append Only File) logs every write, offering better durability but larger file sizes and slower recovery.",
        "subject": "Redis"
    },
    {
        "question": "Can you set an expiration on a specific hash field in Redis?",
        "options": ["Yes", "No, only on the entire key", "Only with a module", "Yes, using HEXPIRE"],
        "correctAnswerIndex": 3,
        "explanation": "As of recent versions (Redis 7.4), Redis supports individual field expiration in Hashes (HEXPIRE), though historically it was only per-key.",
        "subject": "Redis"
    },
    {
        "question": "What is 'Redis Bloom Filter' useful for?",
        "options": ["Storing images", "Checking for existence of an element with high efficiency", "Filtering spam emails", "Sorting numbers"],
        "correctAnswerIndex": 1,
        "explanation": "A Bloom filter tells you if an element 'possibly exists' or 'definitely does not exist' in a set, using very little memory.",
        "subject": "Redis"
    },
    {
        "question": "What is the protocol used by Redis called?",
        "options": ["HTTP", "RESP (Redis Serialization Protocol)", "FTP", "MQTT"],
        "correctAnswerIndex": 1,
        "explanation": "Redis clients communicate with the Redis server using RESP (Redis Serialization Protocol).",
        "subject": "Redis"
    },
    {
        "question": "What is 'Client-side Caching' in Redis?",
        "options": ["Caching executed by the browser", "A feature where Redis helps clients cache keys locally to avoid network trips", "Storing data on the client's disk", "None of the above"],
        "correctAnswerIndex": 1,
        "explanation": "Client-side caching allows clients to cache responses locally, with Redis sending invalidation messages when the data changes.",
        "subject": "Redis"
    },
    {
        "question": "Which command gets all fields and values of a Hash?",
        "options": ["HGETALL", "HGET", "HMGET", "HVALS"],
        "correctAnswerIndex": 0,
        "explanation": "HGETALL returns all fields and values of the hash stored at the key.",
        "subject": "Redis"
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
        print(f"Added {len(new_questions)} new Redis questions.")
        
except Exception as e:
    print(e)
