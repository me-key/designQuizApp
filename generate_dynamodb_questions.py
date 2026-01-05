import json

new_questions = [
    {
        "question": "What is the maximum size of a DynamoDB partition key value?",
        "options": ["1 KB", "2048 bytes", "400 KB", "1024 bytes"],
        "correctAnswerIndex": 1,
        "explanation": "The maximum length for a partition key value is 2048 bytes.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the maximum size of a DynamoDB sort key value?",
        "options": ["1 KB", "1024 bytes", "400 KB", "2048 bytes"],
        "correctAnswerIndex": 1,
        "explanation": "The maximum length for a sort key value is 1024 bytes.",
        "subject": "DynamoDB"
    },
    {
        "question": "Which DynamoDB write operation creates a new item or replaces an old item with a new item?",
        "options": ["UpdateItem", "PutItem", "BatchWriteItem", "TransactWriteItems"],
        "correctAnswerIndex": 1,
        "explanation": "PutItem creates a new item, or replaces an old item with a new item. If an item with the same primary key already exists, PutItem replaces it completely.",
        "subject": "DynamoDB"
    },
    {
        "question": "In DynamoDB, which consistency model offers the lowest latency?",
        "options": ["Strongly Consistent Reads", "Eventual Consistent Reads", "Transactional Reads", "Linearizable Reads"],
        "correctAnswerIndex": 1,
        "explanation": "Eventual Consistent Reads offer the lowest read latency and highest read throughput, but data might be stale.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the limit on the number of Global Secondary Indexes (GSIs) per table by default?",
        "options": ["5", "10", "20", "50"],
        "correctAnswerIndex": 2,
        "explanation": "By default, you can have up to 20 Global Secondary Indexes per table.",
        "subject": "DynamoDB"
    },
    {
        "question": "Can you add a Local Secondary Index (LSI) to an existing DynamoDB table?",
        "options": ["Yes, anytime", "No, only at table creation", "Yes, but table will be locked", "Yes, if the table is empty"],
        "correctAnswerIndex": 1,
        "explanation": "Local Secondary Indexes must be created when the table is created. You cannot add or delete them later.",
        "subject": "DynamoDB"
    },
    {
        "question": "Which of the following consumes Write Capacity Units (WCUs)?",
        "options": ["GetItem", "Query", "PutItem", "Scan"],
        "correctAnswerIndex": 2,
        "explanation": "PutItem is a write operation and consumes Write Capacity Units. GetItem, Query, and Scan consume Read Capacity Units.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the unit size for a Write Capacity Unit (WCU)?",
        "options": ["1 KB", "4 KB", "8 KB", "16 KB"],
        "correctAnswerIndex": 0,
        "explanation": "One Write Capacity Unit represents one write per second for an item up to 1 KB in size.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the unit size for a Strongly Consistent Read Capacity Unit (RCU)?",
        "options": ["1 KB", "4 KB", "8 KB", "16 KB"],
        "correctAnswerIndex": 1,
        "explanation": "One Read Capacity Unit represents one strongly consistent read per second for an item up to 4 KB in size.",
        "subject": "DynamoDB"
    },
    {
        "question": "Does DynamoDB support foreign keys?",
        "options": ["Yes, fully supported", "No, it is a NoSQL database", "Yes, via GSIs", "Only for LSIs"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB does not support foreign keys or referential integrity constraints like relational databases.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the maximum size of a DynamoDB item attribute name?",
        "options": ["255 chars", "1024 bytes", "64 KB", "65535 bytes"],
        "correctAnswerIndex": 3,
        "explanation": "The maximum length of an attribute name is 65535 bytes.",
        "subject": "DynamoDB"
    },
    {
        "question": "How many items can `BatchGetItem` retrieve in a single operation?",
        "options": ["10", "25", "50", "100"],
        "correctAnswerIndex": 3,
        "explanation": "A single implementation of BatchGetItem can retrieve up to 100 items.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the maximum data size `BatchGetItem` can retrieve?",
        "options": ["1 MB", "16 MB", "4 MB", "Unlimited"],
        "correctAnswerIndex": 1,
        "explanation": "BatchGetItem can retrieve up to 16 MB of data.",
        "subject": "DynamoDB"
    },
    {
        "question": "How many items can `BatchWriteItem` put or delete in a single operation?",
        "options": ["10", "25", "50", "100"],
        "correctAnswerIndex": 1,
        "explanation": "A single BatchWriteItem operation can write up to 25 items.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is DynamoDB TTL (Time To Live) used for?",
        "options": ["Caching data", "Automatically deleting expired items", "Sorting items", "Encrypting items"],
        "correctAnswerIndex": 1,
        "explanation": "TTL enables you to define when items in a table expire so that they can be automatically deleted from the database.",
        "subject": "DynamoDB"
    },
    {
        "question": "Does DynamoDB On-Demand Mode require you to specify capacity units?",
        "options": ["Yes, always", "No, it scales automatically", "Only for writes", "Only for reads"],
        "correctAnswerIndex": 1,
        "explanation": "On-Demand mode automatically manages read and write throughput capacity as traffic scales up and down, charging per request.",
        "subject": "DynamoDB"
    },
    {
        "question": "Which API checks for a condition before writing an item?",
        "options": ["PutItem with ConditionExpression", "CheckItem", "ValidateItem", "TestAndSet"],
        "correctAnswerIndex": 0,
        "explanation": "PutItem, UpdateItem, and DeleteItem operations support ConditionExpressions to verify the state of an item before modifying it.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'Scan' in DynamoDB?",
        "options": ["Reads a single item", "Reads all items in a table or index", "Finds items based on key", "Deletes all items"],
        "correctAnswerIndex": 1,
        "explanation": "A Scan operation reads every item in a table or a secondary index. It is generally less efficient than Query.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'Query' in DynamoDB?",
        "options": ["Reads all items", "Finds items based on primary key values", "Randomly selects items", "Updates items"],
        "correctAnswerIndex": 1,
        "explanation": "A Query operation finds items based on primary key values. It is efficient and recommended over Scan.",
        "subject": "DynamoDB"
    },
    {
        "question": "Can DynamoDB Streams preserve item order?",
        "options": ["No", "Yes, strictly within a shard", "Yes, globally across all shards", "Only for 5 minutes"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB Streams guarantees the order of stream records within a shard.",
        "subject": "DynamoDB"
    },
    {
        "question": "What happens if a ProvisionedThroughputExceededException occurs?",
        "options": ["Data is corrupted", "Client should retry with exponential backoff", "Table is locked", "AWS deletes the table"],
        "correctAnswerIndex": 1,
        "explanation": "The standard recommendation is to implement error retries and exponential backoff.",
        "subject": "DynamoDB"
    },
    {
        "question": "What data type is NOT supported in DynamoDB?",
        "options": ["Number", "String", "Date", "Binary"],
        "correctAnswerIndex": 2,
        "explanation": "DynamoDB does not have a native 'Date' or 'Datetime' type. Dates are typically stored as ISO-8601 strings or Number timestamps.",
        "subject": "DynamoDB"
    },
    {
        "question": "Which allows you to read data from a backup without restoring the whole table?",
        "options": ["Export to S3", "PartiQL", "DAX", "Point-In-Time Recovery"],
        "correctAnswerIndex": 0,
        "explanation": "You can export Amazon DynamoDB table data to an Amazon S3 bucket, allowing you to perform analytics or recover specific data.",
        "subject": "DynamoDB"
    },
    {
        "question": "What acts as the primary key for a GSI?",
        "options": ["Same as the base table", "Partition key and optional sort key, can be different from base", "Only Sort Key", "The Item Hash"],
        "correctAnswerIndex": 1,
        "explanation": "GSIs have their own Partition Key and optional Sort Key, which can differ from those of the base table.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is 'PartiQL' for DynamoDB?",
        "options": ["A partition management tool", "A SQL-compatible query language for DynamoDB", "A simplified API", "A backup format"],
        "correctAnswerIndex": 1,
        "explanation": "PartiQL provides a SQL-compatible query language to select, insert, update, and delete data in DynamoDB.",
        "subject": "DynamoDB"
    },
    {
        "question": "Is DynamoDB schema-less?",
        "options": ["No, it is relational", "Yes, except for the primary key", "Yes, completely", "No, schema is strict"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB is schema-less for non-key attributes, but the primary key schema must be defined at creation.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the maximum nested depth for a DynamoDB document?",
        "options": ["10 levels", "32 levels", "Unlimited", "100 levels"],
        "correctAnswerIndex": 1,
        "explanation": "DynamoDB supports nested attributes up to 32 levels deep.",
        "subject": "DynamoDB"
    },
    {
        "question": "Which encryption type is enabled by default in DynamoDB?",
        "options": ["Encryption at Rest", "Client-side encryption", "None", "SSL only"],
        "correctAnswerIndex": 0,
        "explanation": "Encryption at Rest is enabled by default for all DynamoDB tables using AWS owned keys.",
        "subject": "DynamoDB"
    },
    {
        "question": "Can `Scan` results be sorted?",
        "options": ["Yes, by Primary Key", "Yes, by any attribute", "No, they are returned in no specific order", "Yes, by timestamp"],
        "correctAnswerIndex": 2,
        "explanation": "A Scan operation reads items in no particular order. You cannot request a sorted Scan.",
        "subject": "DynamoDB"
    },
    {
        "question": "What is the maximum throughput of a single DynamoDB partition?",
        "options": ["1000 WCU / 3000 RCU", "3000 WCU / 1000 RCU", "Unlimited", "5000 WCU / 5000 RCU"],
        "correctAnswerIndex": 0,
        "explanation": "A single partition can support a maximum of 3,000 Read Capacity Units (RCUs) or 1,000 Write Capacity Units (WCUs).",
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
        print(f"Added {len(new_questions)} new DynamoDB questions.")
        
except Exception as e:
    print(e)
