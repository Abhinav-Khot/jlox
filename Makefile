SRC_DIR := lox
BIN_DIR := bin

all:
	@mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(SRC_DIR)/*.java
clean:
	rm -rf $(BIN_DIR)
run: all
	java -cp $(BIN_DIR) lox.Lox $(FILE)
.PHONY: all clean run