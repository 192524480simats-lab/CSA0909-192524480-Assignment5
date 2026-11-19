# CSA09 – Assignment 5: Data Persistence Engineering

**Register number:** 192524480  
**Repository:** CSA0909-192524480-Assignment5  
**Language:** Java (JDK 17 or later recommended)  
**Database:** MySQL 8.x with MySQL Connector/J

This repository follows the assignment's two-part structure. Part A contains design and SIMATS-style pseudocode; Part B contains Java implementation, SQL, sample input and test/measurement instructions.

## Folder guide

| Assignment task | Folder | Contents |
|---|---|---|
| A1 – Design a File Format | [Task01](Task01/Design.md) | Delimited and binary record layouts, add/find/list pseudocode, damaged-data handling, RandomAccessFile comparison |
| A2 – Don’t Read What You Don’t Need | [Task02](Task02/Design.md) | Read-call estimate, buffered redesign, buffering limitations and readAllLines comparison |
| A3 – Data-Access Design Pattern | [Task03](Task03/Design.md) | Common DAO pattern, generic Dao<T,K>, StudentDao and object-table mapping |
| A4 – What Should Be Serialized? | [Task04](Task04/Design.md) | transient fields, object graph, serialVersionUID and CSV/JSON comparison |
| A5 – Correctness and Safety | [Task05](Task05/Design.md) | JDBC transfer transaction, rollback and SQL injection / PreparedStatement |
| B1 – Instrumented File Store | [Task06](Task06) | PassStore.java, LineCounter.java and PassStoreTest.java |
| B2 – Adversarial Inputs | [Task07](Task07) | SQL injection demonstration and local test database schema |
| B3 – Statement vs PreparedStatement vs Batch | [Task08](Task08) | PassLoader.java, schema.sql and BatchExperiment.java |
| B4 – Final Engineering Decision | [Task09](Task09) | Migration program and sample CSV input |

## Part A – Design documents

Open each `Task01/Design.md` through `Task05/Design.md`. These are design/pseudocode deliverables, not substitutes for the required Part B execution evidence.

## Requirements

- JDK 17 or later
- MySQL Server 8.x for Tasks B2–B4
- MySQL Connector/J JAR for JDBC tasks
- A terminal / command prompt

Set database connection values using environment variables where the program supports them:
- `DB_URL` – for example `jdbc:mysql://localhost:3306/bus_pass_db`
- `DB_USER` – your MySQL username
- `DB_PASSWORD` – your MySQL password

Do not commit real passwords to GitHub.

## Compile and run – Task B1

From the repository root:

```bash
cd Task06
javac PassStore.java LineCounter.java PassStoreTest.java
java PassStoreTest
```

The test program checks adding and finding records, a missing ID and a malformed record line. Run it locally and save screenshots of the complete console output.

For line-counter timing, run the provided line counter against files of 1 MB, 10 MB and 50 MB. Record your own timings for both buffered and unbuffered approaches; timing depends on the computer, storage device, Java version and file cache.

## Compile and run – Task B2

Run `Task07/schema.sql` in your local MySQL test database, then add MySQL Connector/J to the classpath.

Windows example:

```bash
javac -cp ".;mysql-connector-j.jar" SqlInjectionDemo.java
java -cp ".;mysql-connector-j.jar" SqlInjectionDemo
```

Linux/macOS: use `:` instead of `; ` in the classpath. The demonstration is intended for a local test database only.

## Compile and run – Task B3

1. Run `Task08/schema.sql` in your test database.
2. Configure `DB_URL`, `DB_USER` and `DB_PASSWORD` (or update the local connection settings as documented in the source).
3. Add MySQL Connector/J to the classpath.
4. Compile and run:

```bash
cd Task08
javac -cp ".;../mysql-connector-j.jar" PassLoader.java BatchExperiment.java JdbcComparison.java
java -cp ".;../mysql-connector-j.jar" BatchExperiment
java -cp ".;../mysql-connector-j.jar" JdbcComparison
```

Run each data size requested in the assignment (10, 1,000, 10,000 and 100,000 as applicable) and record observed elapsed time, inserted row count, auto-commit/transaction behaviour and rollback results. Do not use invented timings.

## Compile and run – Task B4

1. Create the target table using the appropriate schema from Task08.
2. Put the source pass data in the CSV format expected by `MigratePasses.java`; a small example is in `Task09/passes-sample.csv`.
3. Configure database environment variables and Connector/J.
4. Compile and run:

```bash
cd Task09
javac -cp ".;../mysql-connector-j.jar" ../Task08/PassLoader.java MigratePasses.java
java -cp ".;../mysql-connector-j.jar" MigratePasses passes-sample.csv
```

The migration reports invalid records and loads valid records in a transaction/batches. Run the migration against your own database and retain the actual success/failure report.

## Evidence checklist before submission

- [ ] Run every Part B program on your own computer.
- [ ] Capture screenshots showing inputs, relevant console output and database query results (not only a final success message).
- [ ] Test normal, missing and malformed records.
- [ ] Measure 1 MB, 10 MB and 50 MB file scans for buffered and unbuffered versions.
- [ ] Measure database insertion at the assignment's requested sizes and compare Statement, PreparedStatement and batch.
- [ ] Demonstrate transaction rollback after a deliberately invalid row.
- [ ] Record your own measurements, hardware/software details and interpretation.
- [ ] Include the required diagrams, pseudocode and engineering decisions in the report.
- [ ] Commit work progressively, with at least one meaningful commit per task.

**Important:** The source files and sample inputs are starter implementation material. Actual execution screenshots, database results and performance measurements must be produced on your computer. No timing values or successful local database execution are claimed by this README.
