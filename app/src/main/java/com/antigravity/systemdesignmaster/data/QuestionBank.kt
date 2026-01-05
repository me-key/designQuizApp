package com.antigravity.systemdesignmaster.data

object QuestionBank {
    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                text = "Which component is primarily used to distribute incoming network traffic across multiple servers?",
                option0 = "Load Balancer",
                option1 = "Reverse Proxy",
                option2 = "Cache",
                option3 = "Database Sharding",
                correctAnswerIndex = 0,
                explanation = "A Load Balancer distributes incoming network traffic across a group of backend servers to ensure no single server bears too much load."
            ),
            Question(
                text = "In the CAP theorem, what does 'P' stand for?",
                option0 = "Performance",
                option1 = "Persistence",
                option2 = "Partition Tolerance",
                option3 = "Protocol",
                correctAnswerIndex = 2,
                explanation = "CAP stands for Consistency, Availability, and Partition Tolerance. Partition Tolerance means the system continues to operate despite an arbitrary number of messages being dropped or delayed by the network."
            ),
            Question(
                text = "Which caching strategy writes data to the cache and the database simultaneously?",
                option0 = "Write-through",
                option1 = "Write-behind",
                option2 = "Write-around",
                option3 = "Cache-aside",
                correctAnswerIndex = 0,
                explanation = "In Write-through caching, data is written to the cache and the corresponding database at the same time."
            ),
            Question(
                text = "Which of the following is a NoSQL database type?",
                option0 = "Relational",
                option1 = "Key-Value Store",
                option2 = "Structured Query Language",
                option3 = "Stored Procedure",
                correctAnswerIndex = 1,
                explanation = "Key-Value stores (like Redis, DynamoDB) are a type of NoSQL database."
            ),
            Question(
                text = "What is the main benefit of Database Sharding?",
                option0 = "Data Replication",
                option1 = "Horizontal Scaling",
                option2 = "Data Consistency",
                option3 = "Improved Security",
                correctAnswerIndex = 1,
                explanation = "Sharding partitions data across multiple machines, enabling horizontal scaling to handle larger datasets and higher throughput."
            ),
            Question(
                text = "What is a characteristic of a Microservices architecture?",
                option0 = "Monolithic codebase",
                option1 = "Shared database for all services",
                option2 = "Loose coupling",
                option3 = "Single point of failure",
                correctAnswerIndex = 2,
                explanation = "Microservices are loosely coupled, meaning they can be developed, deployed, and scaled independently."
            ),
             Question(
                text = "Which protocol is stateless?",
                option0 = "TCP",
                option1 = "FTP",
                option2 = "HTTP",
                option3 = "SMTP",
                correctAnswerIndex = 2,
                explanation = "HTTP is a stateless protocol, meaning each request is independent and server does not keep track of previous requests."
            ),
            Question(
                text = "What is the primary purpose of a Reverse Proxy?",
                option0 = "To cache static content",
                option1 = "To forward client requests to backend servers",
                option2 = "To block all traffic",
                option3 = "To store user sessions",
                correctAnswerIndex = 1,
                explanation = "A reverse proxy sits in front of web servers and forwards client requests to those web servers, often providing security, load balancing, and caching."
            )
        )
    }
}
