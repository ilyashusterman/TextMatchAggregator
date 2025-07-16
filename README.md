# TextMatchAggregator

A Java CLI application that processes large text files by splitting them into chunks and searching for names in parallel, using modern Java concurrency utilities.

---

## Prerequisites

- **Java 21 (OpenJDK 21)**
- **Gradle 8.13** (for running tests)
- **Make** (for convenient build/run commands)

### Install Java 21 (macOS/Homebrew)
```sh
brew install openjdk@21
# Add to your shell profile if needed:
# echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
```

### Install Gradle 8.13 (if not present)
```sh
# Homebrew may install the latest version, but you need 8.13 specifically:
brew install gradle@8.13
# Or download from https://gradle.org/releases/
```

Check your Gradle version:
```sh
gradle --version
# Should output: Gradle 8.13
```

### Install Make (if not present)
- macOS: usually pre-installed / or install bash-completion

---

## Running the App with the Makefile

### 1. Build the Java Sources

This compiles all Java files into the `out/` directory:
```sh
make build_java
```

### 2. Run the CLI App on a Text File

Use the `run_cli` target, specifying your input file (for example, `./big.txt`):

```sh
make run_cli FILE=./big.txt
```
- Replace `./big.txt` with the path to your input file.
- This command will:
  - Build the Java sources (if not already built)
  - Run the CLI app using JDK 21 (as set up in your Makefile)
  - Pass the file path to your `Main` class

### 3. Example Full Workflow

```sh
make build_java
make run_cli FILE=./big.txt
```
Or, in one step (the `run_cli` target will build if needed):

```sh
make run_cli FILE=./big.txt
```

### 4. What Happens Under the Hood

The Makefile command expands to:
```sh
make build_java && JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH" java -cp out org.example.Main ./big.txt
```
- Ensures the correct JDK is used
- Runs your CLI with the specified file

### 5. Troubleshooting

- If you see errors about missing files, check the file path you provided.
- If you see Java version errors, ensure your `PATH` and `JAVA_HOME` are set as described above.

---

## Benchmark Timing

When you run the CLI app, it will print a benchmark line showing the total execution time in milliseconds:

```
[main] BENCHMARK: Total execution time: 1234 ms
```

### Example Benchmark Result

On a recent run with `./big.txt`:
```
[main] BENCHMARK: Total execution time: 179 ms
```

This helps you measure the performance of the application on your input files.

---

## Testing the Project

You can run all tests using either the Makefile or Gradle:

### Using the Makefile

Run all unit and integration tests:
```sh
make test
make test-integration
```
- `make test` runs all tests.
- `make test-integration` runs integration tests (e.g., the CLI on `big.txt`).

### Using Gradle Directly

You can also run all tests with Gradle:
```sh
./gradlew test
```

Test results and reports will be available in the `build/` directory.

---

## Testing
