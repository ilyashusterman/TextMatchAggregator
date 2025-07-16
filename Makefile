run:
	javac -d out src/main/java/org/example/Aggregator.java src/main/java/org/example/Main.java
	java -cp out org.example.Main

test:
	JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$$PATH" ./gradlew test --info

clean:
	rm -rf out 

text_match_java:
	JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$$PATH" java -cp build/classes/java/main org.example.Main $(FILE) 

build_java:
	javac -d out src/main/java/org/example/*.java 

build_run:
	make build_java && JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$$PATH" java -cp out org.example.Main ./big.txt 

test-integration:
	JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$$PATH" ./gradlew test --tests org.example.TestMain --info 

run_cli:
	make build_java && JAVA_HOME= PATH="/opt/homebrew/opt/openjdk@21/bin:$$PATH" java -cp out org.example.Main $(FILE) 

zip_source:
	zip -r text-match-aggregator-source.zip . -x '*.git*' '*out*' '*.DS_Store' 'test.log' '*.zip' 